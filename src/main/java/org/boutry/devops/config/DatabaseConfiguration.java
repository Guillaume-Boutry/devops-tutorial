package org.boutry.devops.config;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "quarkus.datasource")
public interface DatabaseConfiguration {
    String username();

    String password();
}