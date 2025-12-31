package com.javarush.repositories;

import com.javarush.domain.Country;
import org.hibernate.SessionFactory;
import java.util.List;

public class CountryRepository {
    private final SessionFactory sessionFactory;

    public CountryRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Country> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("select c from Country c join fetch c.languages", Country.class)
                .list();
    }
}