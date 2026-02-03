package com.javarush.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RedisConfigTest {

    @Test
    void shouldReturnPropertyWhenFileExists() {
        RedisConfig config = new RedisConfig(ConfigFile.REDIS);

        assertNotNull(config.getHost());
        assertFalse(config.getHost().isEmpty());

        int port = config.getPort();
        assertTrue(port > 0 && port <= 65535);
    }

    @Test
    void shouldHandleDefaultValues() {
        RedisConfig config = new RedisConfig(ConfigFile.REDIS);
        assertDoesNotThrow(() -> {
            String host = config.getHost();
            int port = config.getPort();
            assertNotNull(host);
        });
    }

    @Test
    void shouldThrowExceptionWhenConfigFileIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new RedisConfig(null);
        });
    }
}