package com.javarush;

import com.javarush.config.ConfigFile;
import com.javarush.config.RedisConfig;
import com.javarush.config.SessionFactoryConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MainTest {

    @Test
    void shouldLoadWithoutExceptions() {
        assertDoesNotThrow(() -> {
            new RedisConfig(ConfigFile.REDIS);
            new SessionFactoryConfig(ConfigFile.HIBERNATE);
        });
    }
}