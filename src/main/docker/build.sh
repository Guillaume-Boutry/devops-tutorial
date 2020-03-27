#!/bin/bash
set -x
# To be ran on project root
if [ -z "$WORKSPACE" ]
then
  WORKSPACE=$(pwd)
fi

docker volume create --name maven-repo
docker run --rm -it -v /var/run/docker.sock:/var/run/docker.sock -v "$WORKSPACE":/tmp/project -v maven-repo:/root/.m2 -w /tmp/project maven:3.6.3-jdk-11 mvn clean package
docker build -f src/main/docker/Dockerfile.jvm -t boutry/devops-tutorial-jvm "$WORKSPACE"
