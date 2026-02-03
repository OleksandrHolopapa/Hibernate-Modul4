package com.javarush.services;

import com.javarush.domain.City;
import com.javarush.repositories.CityRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private final SessionFactory sessionFactory;
    private final CityRepository cityRepository;

    public DatabaseService(SessionFactory sessionFactory, CityRepository cityRepository) {
        this.sessionFactory = sessionFactory;
        this.cityRepository = cityRepository;
    }

    public List<City> getAllCities(int batchSize) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            List<City> allCities = new ArrayList<>();
            int totalCount = cityRepository.getTotalCount();

            for (int i = 0; i < totalCount; i += batchSize) {
                allCities.addAll(cityRepository.getItems(i, batchSize));
                session.flush();
                session.clear();
            }

            transaction.commit();
            return allCities;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}