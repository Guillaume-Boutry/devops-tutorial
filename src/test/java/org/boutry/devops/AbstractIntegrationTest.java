package org.boutry.devops;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;

public abstract class AbstractIntegrationTest {

    public static GenericContainer db = new MariaDBContainer("mariadb/server:10.4")
            .withExposedPorts(3306, 3306);

    static {
        db.start();
    }
}