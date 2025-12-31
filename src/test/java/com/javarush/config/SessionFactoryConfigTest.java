package com.javarush.config;

import com.javarush.exceptions.ConfigException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SessionFactoryConfigTest {

    @Test
    void shouldThrowConfigExceptionWhenFileNotFound() {
        assertThrows(ConfigException.class, () -> {
            new SessionFactoryConfig("invalid-hibernate.properties");
        });
    }
}