package com.javarush.services;

import com.javarush.repositories.CityRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class DBPerformanceService {
    private final SessionFactory sessionFactory;
    private final CityRepository cityRepository;
    private final Logger logger = LoggerFactory.getLogger(DBPerformanceService.class);

    public DBPerformanceService(SessionFactory sessionFactory, CityRepository cityRepository) {
        this.sessionFactory = sessionFactory;
        this.cityRepository = cityRepository;
    }

    public void testMysqlData(List<Integer> ids) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            for (Integer id : ids) {
                cityRepository.getById(id).ifPresentOrElse(
                        city -> city.getCountry().getLanguages().size(),
                        () -> logger.warn("No city found in DB for ID: {}", id)
                );
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error during MySQL test", e);
        }
    }
}