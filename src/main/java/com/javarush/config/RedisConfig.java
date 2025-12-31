package com.javarush.config;

import com.javarush.exceptions.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RedisConfig {
    private final Properties properties = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    public RedisConfig(String propertiesFileName) {
        try (InputStream inputStream = RedisConfig.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (inputStream == null) {
                logger.error("Property file {} not found", propertiesFileName);
                throw new ConfigException("Property file not found: " + propertiesFileName);
            }
            properties.load(inputStream);
        } catch (IOException ex) {
            logger.error("Failed to load properties from {}", propertiesFileName);
            throw new ConfigException("Error loading " + propertiesFileName);
        }
    }

    public String getHost() {
        return properties.getProperty("redis.host", "localhost");
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty("redis.port", "6379"));
    }
}