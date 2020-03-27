package org.boutry.devops;

import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractIntegrationTest {

    public static PostgreSQLContainer db = new PostgreSQLContainer();

    static {
        db.start();
    }
}