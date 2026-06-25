# Spring Boot starter

This repository can be used to bootstrap your Spring Boot application. 
It is a monorepo with 2 workspaces (core-lib and web-server)

🌟 Highlights

The main stacks used in this project are : 
- Spring Boot latest version (snapshot) :
    - Flyway for data migration
    - Hibernate / Jakarta / SpringData for ORM
    - JJWT 
- Docker (postgres for database)
- Terraform for deployment

## Environment variables required
- AWS_REGION
- AWS_ECR_NAME
- AWS_ACCOUNT_ID (for app image tag in docker-compose.prod.yml)
- POSTGRES_USER
- POSTGRES_PASSWORD
- POSTGRES_DB
- JWT_KEY

## To do (short to mid term) : 

- Clean migration files
- Reorganize dependencies (common dependencies between core-lib and web-server in root pom.xml)
- Proper logger for web-server
- Reorganize launch-ec2.bash 
- Reorganize deploy (split main.tf to EC2.tf, ECR.tf)
- Add a compilation stage in web-server Dockerfile to compile to JAR with docker with a maven image

## To do (long term) :

- AWS Secrets manager for env variables
- Spring Security if app gets bigger
- Java Tests

## CLI : 

### MAVEN COMMANDS
- To install all dependencies accross workspaces (will also compile) 
```bash
mvn install -DskipTests
```

- To compile 
```bash
mvn compile
```

- To launch spring boot server locally (flyway migration runs before every launch) :
```bash
mvn spring-boot:run -pl web-server
```

- If compilation or install don't work, clean the JARS and recompile with : 
```bash
mvn clean install
```

### DOCKER COMMANDS
- TO BE RUN BEFORE BUILDING SPRING BOOT IMAGE (clean /target + compile into one .JAR)
```bash
mvn clean package -DskipTests
```

- To run only postgres (if springboot doesn't run in the docker container)
```bash
docker compose up db
```

- To launch postgres container + springboot container (and rebuild image, useful if code has changed)
```bash
docker compose up --build 
```

- To delete volume to start over db container
```bash
docker compose down -v
```

### RUN THE EC2 INSTANCE (AFTER DEPLOYMENT TO AWS)

- Push Spring Boot image to ECR
```bash
bash push-image-to-ecr.bash
```

- Launch Spring Boot app on EC2 Instance 
```bash
bash launch-ec2.bash
```

- Stop Spring boot app
```bash
bash stop-ec2.bash
```