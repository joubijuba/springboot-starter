### SSH KEYS 

# The block below is for ssh key generation + storing private key in "myKey.pem" in root of the repo

resource "tls_private_key" "pk" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "aws_key_pair" "kp" {
  key_name   = "myKey"
  public_key = tls_private_key.pk.public_key_openssh

  # Provisioner + "local-exec" -> execute command / script on our machine after deployment
  provisioner "local-exec" { # Put generated key (private key) into myKey.pem
    command = "echo '${tls_private_key.pk.private_key_pem}' > ../myKey.pem"
  }
}