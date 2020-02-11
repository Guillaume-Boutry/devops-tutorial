#!/bin/zsh

mvn package -DskipTests
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/devops-tutorial-jvm $HOME/IdeaProjects/devops-tutorial 
