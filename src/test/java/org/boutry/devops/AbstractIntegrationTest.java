package org.boutry.devops;

import org.testcontainers.containers.MariaDBContainer;

public abstract class AbstractIntegrationTest {

    public static MariaDBContainer db = new MariaDBContainer();

    static {
        db.start();
    }
}