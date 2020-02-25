#!/bin/bash
# To be ran on projet root
docker volume create --name maven-repo
docker run --rm -it -v /var/run/docker.sock:/var/run/docker.sock -v "$(pwd)":/tmp/project -v maven-repo:/root/.m2 -w /tmp/project maven:3.6.3-jdk-11 mvn clean package
docker build -f src/main/docker/Dockerfile.jvm -t boutry/devops-tutorial-jvm "$(pwd)"
