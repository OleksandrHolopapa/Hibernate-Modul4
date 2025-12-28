package com.javarush;

import com.javarush.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractHibernateTest {
    protected static SessionFactory sessionFactory;

    @BeforeAll
    static void init() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(CountryLanguage.class)
                .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:test_db;MODE=MySQL;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS world")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.current_session_context_class", "managed")
                .buildSessionFactory();
    }

    @AfterAll
    static void close() {
        if (sessionFactory != null) sessionFactory.close();
    }

    @BeforeEach
    void openSession() {
        Session session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);
        session.beginTransaction();
    }

    @AfterEach
    void closeSession() {
        Session session = ManagedSessionContext.unbind(sessionFactory);
        if (session != null) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            session.close();
        }
    }
}