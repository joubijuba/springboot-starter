#!/bin/bash
# shellcheck disable=SC2086

set -e 

# fetch aws account id, needed for ECR tag
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)

# Build image tag
IMAGE_TAG="$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$AWS_ECR_NAME:latest" 

# Recompile before building spring boot image
mvn clean package -DskipTests 

# Login to ECR first then pass the token to docker so that Docker can login to ECR
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com 

# Build image (long task), platform set to amd64 for the image to be builtable on EC2 Ubuntu instances (arm64 is for mac chips)
docker build --platform linux/amd64 -t $IMAGE_TAG -f web-server/Dockerfile .

# Push built image to ECR (Image tag is both destination URL and name)
docker push $IMAGE_TAG 