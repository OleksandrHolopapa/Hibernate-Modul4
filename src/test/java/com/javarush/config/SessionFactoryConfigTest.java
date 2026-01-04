package com.javarush.config;

import com.javarush.domain.City;
import com.javarush.domain.Country;
import com.javarush.domain.CountryLanguage;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SessionFactoryConfigTest {

    @Test
    void shouldCreateSessionFactoryWithValidConfig() {
        SessionFactoryConfig config = new SessionFactoryConfig(ConfigFile.TEST_HIBERNATE);

        try (SessionFactory sessionFactory = config.buildSessionFactory()) {
            assertNotNull(sessionFactory);
            assertTrue(sessionFactory.isOpen());

            assertDoesNotThrow(() -> {
                sessionFactory.getMetamodel().entity(City.class);
                sessionFactory.getMetamodel().entity(Country.class);
                sessionFactory.getMetamodel().entity(CountryLanguage.class);
            });
        }
    }

    @Test
    void shouldThrowExceptionWhenConfigFileIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new SessionFactoryConfig(null);
        });
    }
}