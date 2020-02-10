package org.boutry.devops.config;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "database")
public interface DatabaseConfiguration {
    String path();
}
