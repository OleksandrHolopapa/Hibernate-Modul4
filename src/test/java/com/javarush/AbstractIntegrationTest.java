package com.javarush;

import com.javarush.config.ConfigFile;
import com.javarush.config.SessionFactoryConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractIntegrationTest {
    protected static SessionFactory sessionFactory;

    @BeforeAll
    static void init() {
        SessionFactoryConfig config = new SessionFactoryConfig(ConfigFile.TEST_HIBERNATE);
        sessionFactory = config.buildSessionFactory();
    }

    @AfterAll
    static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}