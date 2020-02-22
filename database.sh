#!/bin/bash

SQL_FILE=$(pwd)/target/randjfiudsjfijsd.sql

docker pull mariadb/server:10.4
{ echo "DROP DATABASE IF EXISTS APPLICATION;" ;
  echo "DROP USER IF EXISTS 'devops'@'%';" ;
  echo "FLUSH PRIVILEGES;" ;
  echo "CREATE DATABASE APPLICATION;" ;
  echo "CREATE USER 'devops'@'%' IDENTIFIED BY 'devops';" ;
  echo "GRANT ALL PRIVILEGES ON APPLICATION.* TO 'devops'@'%';" ; } > "$SQL_FILE"
docker run --name database --rm  -p 3306:3306 \
  -v $SQL_FILE:/docker-entrypoint-initdb.d/init.sql \
  -e MARIADB_ROOT_PASSWORD=root \
  -e MARIADB_DATABASE=APPLICATION \
  -e MARIADB_USER=devops \
  -e MARIADB_PASSWORD=devops \
  -d mariadb/server:10.4

# -v $HOME/.data/mariadb:/var/lib/mysql