#!/bin/bash
docker pull mariadb
docker run --name database --rm -v $HOME/.data/mariadb:/var/lib/mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mariadb:10.5
