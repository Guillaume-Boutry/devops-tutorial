#!/bin/bash
set -x
# To be ran on project root
if [ -z "$WORKSPACE" ]
then
  WORKSPACE=$(pwd)
fi

docker volume create --name maven-repo
if [ -z "$DOCKER_HOST" ]
then
  docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v "$WORKSPACE":/tmp/project -v maven-repo:/root/.m2 -w /tmp/project maven:3.6.3-jdk-11 mvn clean install
else
  docker run --rm --network=host -e DOCKER_HOST="$DOCKER_HOST" -v "$WORKSPACE":/tmp/project -v maven-repo:/root/.m2 -w /tmp/project maven:3.6.3-jdk-11 mvn clean install
fi
docker build -f src/main/docker/Dockerfile.jvm -t boutry/devops-tutorial-jvm "$WORKSPACE"
