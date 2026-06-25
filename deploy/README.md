# Be careful to set up all env variables before deploying : 
# See root README.md (same env variables as there)

## Commands :
- to set env variables 
```bash
source set-env.bash
```

- to deploy :
```bash
terraform apply
```

- after deployment, run from root to grant read permission to owner only to read private ssh key file, 
- SSH will not read private key files with rights too permissive: 
```bash
chmod 400 myKey.pem
```

- to show outputs (do a refresh if outputs.tf has been updated after last deploy) :
```bash
terraform output
```

- to destroy :
```bash
terraform destroy
```

- to destroy targeted resource :
```bash
terraform destroy -target resource_address
```
