#!/bin/bash

if [ "$CI" == "true" ]
then
  docker build -f src/main/docker/Dockerfile.jvm -t registry.zouzland.com/boutry/devops-tutorial-jvm .
fi