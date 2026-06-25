#!/bin/bash

# Compile 
mvn clean package -DskipTests

docker compose up --build 