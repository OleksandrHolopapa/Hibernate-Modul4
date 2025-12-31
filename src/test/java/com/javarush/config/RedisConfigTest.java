package com.javarush.config;

import com.javarush.exceptions.ConfigException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RedisConfigTest {

    @Test
    void shouldThrowConfigExceptionWhenFileNotFound() {
        assertThrows(ConfigException.class, () -> {
            new RedisConfig("non-existent.properties");
        });
    }

    @Test
    void shouldReturnPropertyWhenFileExists() {
        RedisConfig config = new RedisConfig(ConfigFile.REDIS.getFileName());
        assertNotNull(config.getHost());
        int port = config.getPort();
        assertTrue(port > 0 && port <= 65535);
    }
}