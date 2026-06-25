#!/bin/bash

touch /home/ubuntu/.env
cat > /home/ubuntu/.env <<EOF
POSTGRES_USER=${postgres_user}
POSTGRES_PASSWORD=${postgres_password}
POSTGRES_DB=${postgres_db}
JWT_KEY=${jwt_key}
AWS_REGION=${aws_region}
AWS_ACCOUNT_ID=${aws_account_id}
EOF

# Update system
apt-get update -y

# Basic dependencies
apt-get install -y ca-certificates curl gnupg lsb-release unzip

# AWS CLI (snap version)
snap install aws-cli --classic

# Add docker's official GPG key :
install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
chmod a+r /etc/apt/keyrings/docker.asc

# Add Docker official repository. DOCKEREOF tells Bash to keep $VAL as plain strings
# so that they will be set in the EC2 instance and not here on our machine
cat > /etc/apt/sources.list.d/docker.sources <<DOCKEREOF
Types: deb
URIs: https://download.docker.com/linux/ubuntu
Suites: $(. /etc/os-release && echo "$${UBUNTU_CODENAME:-$VERSION_CODENAME}")
Components: stable
Architectures: $(dpkg --print-architecture)
Signed-By: /etc/apt/keyrings/docker.asc
DOCKEREOF

# Install Docker Engine + Compose v2
apt-get update -y
apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Enable Docker service
systemctl enable docker
systemctl start docker