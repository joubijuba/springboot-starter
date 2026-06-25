#!/bin/bash

export TF_VAR_postgres_user=$POSTGRES_USER
export TF_VAR_postgres_password=$POSTGRES_PASSWORD
export TF_VAR_postgres_db=$POSTGRES_DB
export TF_VAR_jwt_key=$JWT_KEY

export TF_VAR_aws_ecr_name=$AWS_ECR_NAME
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
export TF_VAR_aws_account_id=$AWS_ACCOUNT_ID
export TF_VAR_aws_region=$AWS_REGION