#!/bin/bash

AWS_EC2_IP="51.20.31.13" # from terraform output after deploying

ssh -i ./myKey.pem ubuntu@$AWS_EC2_IP "sudo docker compose -f docker-compose.prod.yml down -v"