#!/bin/bash

# Tells bash to stop executing at first error encountered
set -e

AWS_EC2_IP="51.20.37.115" # from terraform output after deploying
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)

IMAGE_TAG="$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$AWS_ECR_NAME:latest" 

# TO DO : compact all commands below in only one ssh connection command

# Secure copy/paste docker-compose.prod.yml from our machine to EC2 instance 
scp -i ./myKey.pem docker-compose.prod.yml ubuntu@$AWS_EC2_IP:~/

# Instance gets auth token to pull image from ECR
ssh -i ./myKey.pem ubuntu@$AWS_EC2_IP "aws ecr get-login-password --region $AWS_REGION | sudo docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com" 

# Instance pulls image
ssh -i ./myKey.pem ubuntu@$AWS_EC2_IP "sudo docker pull $IMAGE_TAG"

# Finally launch the docker containers (-d to run in background)
ssh -i ./myKey.pem ubuntu@$AWS_EC2_IP "sudo docker compose -f docker-compose.prod.yml up -d"


## IMPORTANT : docker commands need "sudo" as long as the Ubuntu user is not given root rights (in user_data script in aws_instance main.tf)
# TO DO : add in user_data : sudo usermod -aG docker ubuntu


## cat /var/log/cloud-init-output.log --> to see instance logs
## sudo docker compose -f docker-compose.prod.yml logs -f --> normal docker logs 
## sudo docker logs <containername> --> logs of the springboot container
## docker logs springboot --> springboot logs 