package com.hamza_ok.config;

import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlConfig {
    private Map<String, Object> config;

    public YamlConfig() {
        Yaml yaml = new Yaml();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.yaml")) {
            this.config = yaml.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
    }

    // Get a configuration string using a path, e.g., "mongodb.uri"
    public String getString(String path) {
        String[] keys = path.split("\\.");
        Map<String, Object> current = config;

        try {
            for (int i = 0; i < keys.length - 1; i++) {
                current = (Map<String, Object>) current.get(keys[i]);
            }

            return (String) current.get(keys[keys.length - 1]);
        } catch (ClassCastException e) {
            throw new RuntimeException("Configuration path is incorrect or not a string", e);
        }
    }
}
