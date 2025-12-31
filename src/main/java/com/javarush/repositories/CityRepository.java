package com.javarush.repositories;

import com.javarush.domain.City;
import org.hibernate.SessionFactory;
import java.util.List;
import java.util.Optional;

public class CityRepository {
    private final SessionFactory sessionFactory;

    public CityRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<City> getItems(int offset, int limit) {
        return sessionFactory.getCurrentSession()
                .createQuery("from City", City.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();
    }

    public int getTotalCount() {
        return Math.toIntExact(sessionFactory.getCurrentSession()
                .createQuery("select count(c) from City c", Long.class)
                .uniqueResult());
    }

    public Optional<City> getById(Integer id) {
        return sessionFactory.getCurrentSession()
                .createQuery("select c from City c join fetch c.country where c.id = :ID", City.class)
                .setParameter("ID", id)
                .uniqueResultOptional();
    }
}