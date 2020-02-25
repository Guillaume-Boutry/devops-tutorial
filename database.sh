#!/bin/bash


docker pull mariadb/server:10.4
docker run --name database --rm  -p 3306:3306 \
  -v ./init_database.sql:/docker-entrypoint-initdb.d/init.sql \
  -e MARIADB_ROOT_PASSWORD=root \
  -e MARIADB_DATABASE=APPLICATION \
  -e MARIADB_USER=devops \
  -e MARIADB_PASSWORD=devops \
  -d mariadb/server:10.4