package org.boutry.devops;

import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractIntegrationTest {

    public static MySQLContainer db = new MySQLContainer("mysql/mysql-server:8.0.19-1.1.15");

    static {
        db.start();
    }
}