provider "aws" {
  region = "eu-north-1"
}



//    EC2 ROLE BLOCK   //

// Trust policy
data "aws_iam_policy_document" "assume_role" {
  statement {
    effect = "Allow"

    principals {
      type        = "Service"
      identifiers = ["ec2.amazonaws.com"]
    }

    actions = ["sts:AssumeRole"]
  }
}

// IAM Role for EC2 
resource "aws_iam_role" "ec2_role" {
  name               = "ec2_role"
  assume_role_policy = data.aws_iam_policy_document.assume_role.json
}

// Policy for right to pull images from ECR 
resource "aws_iam_role_policy" "ec2_policy_springboot" {
  name = "ec2_policy"
  role = aws_iam_role.ec2_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect   = "Allow"
        Action   = ["ecr:GetAuthorizationToken"]
        Resource = "*"
      },
      {
        Effect = "Allow"
        Action = [
          "ecr:GetAuthorizationToken",
          "ecr:BatchGetImage",
          "ecr:GetDownloadUrlForLayer"
        ]
        Resource = aws_ecr_repository.ecr_for_ec2.arn
      }
    ]
  })
}

// Policy for right to log in cloudwatch
resource "aws_iam_role_policy_attachment" "lambda_logs" {
  role       = aws_iam_role.ec2_role.id
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchAgentServerPolicy"
}

// IMPORTANT : as we cannot pass iam_roles to EC2 instance, we use an iam_instance_profile
resource "aws_iam_instance_profile" "ec2_profile" {
  name = "ec2_profile"
  role = aws_iam_role.ec2_role.name
}



//   EC2 SECURITY GROUPS BLOCK   //
// Allow SSH to send commands to EC2 instance from our machine
// Allow HTTP to send HTTP requests to the Spring Boot server
// Allow outgoing requests to any web resource (needed to pull image from ECR, to install libraries with apt-get etc)

resource "aws_security_group" "allow_http_ssh" {
  name        = "Security group for ec2"
  description = "Allow HTTP inbound traffic and all outbound traffic"
  vpc_id      = data.aws_vpc.default.id

  tags = {
    Name = "Allow_http_ssh"
  }
}

resource "aws_vpc_security_group_ingress_rule" "allow_http" {
  security_group_id = aws_security_group.allow_http_ssh.id
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 80
  ip_protocol       = "tcp"
  to_port           = 80
}

resource "aws_vpc_security_group_ingress_rule" "allow_ssh" {
  security_group_id = aws_security_group.allow_http_ssh.id
  cidr_ipv4         = "0.0.0.0/0" # Replace with your IP or any IP you want
  from_port         = 22
  ip_protocol       = "tcp"
  to_port           = 22
}

resource "aws_vpc_security_group_egress_rule" "allow_all_traffic" {
  security_group_id = aws_security_group.allow_http_ssh.id
  cidr_ipv4         = "0.0.0.0/0"
  ip_protocol       = "-1" # semantically equivalent to all ports
}



//   EC2 block   // 

resource "aws_instance" "springboot_server" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = "t3.micro"

  key_name = aws_key_pair.kp.key_name

  root_block_device {
    volume_size = 8
  }

  # Move the EC2 into the VPC's securiy group defined above
  vpc_security_group_ids = [aws_security_group.allow_http_ssh.id]

  iam_instance_profile = aws_iam_instance_profile.ec2_profile.name

  tags = {
    Name = "springboot server"
  }

  # user_data : script that is run inside instance after first bootstrap 
  # 1) set env variables
  # 2) install aws-cli and docker 
  # 3) start docker daemon
  user_data = templatefile("${path.module}/user-data.bash", {
    postgres_user     = var.postgres_user
    postgres_password = var.postgres_password
    postgres_db       = var.postgres_db
    jwt_key           = var.jwt_key
    aws_region        = var.aws_region
    aws_account_id    = var.aws_account_id
  })

  monitoring              = true
  disable_api_termination = false
  ebs_optimized           = true
}



///   ECR   ///

resource "aws_ecr_repository" "ecr_for_ec2" {
  name                 = "for_ec2"
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true // AWS will scan at each push if there are vulnerabilities within the images
  }

  force_delete = true
}

// By default if we push an image with the same tag again, the previous one will be left untagged,
// Then we need to add this policy in order to have untagged images deleted from ECR repo
resource "aws_ecr_lifecycle_policy" "delete_untagged_images" {
  repository = aws_ecr_repository.ecr_for_ec2.name

  policy = <<EOF
{
  "rules": [
    {
      "rulePriority": 1,
      "description": "Delete untagged images",
      "selection": {
        "tagStatus": "untagged",
        "countType": "imageCountMoreThan",
        "countNumber": 1
      },
      "action": {
        "type": "expire"
      }
    }
  ]
}
EOF
}