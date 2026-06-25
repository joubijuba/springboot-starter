output "ec2_public_ip" {
  value = aws_instance.springboot_server.public_ip
}