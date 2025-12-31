package com.javarush.repositories;

import com.javarush.AbstractIntegrationTest;
import com.javarush.domain.City;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ThreadLocalSessionContext;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class CityRepositoryTest extends AbstractIntegrationTest {

    @Test
    void shouldReturnCityWithFetchedCountryAndLanguages() {
        try (Session session = sessionFactory.openSession()) {
            ThreadLocalSessionContext.bind(session);
            Transaction transaction = session.beginTransaction();
            try {
                CityRepository cityRepository = new CityRepository(sessionFactory);
                Optional<City> cityOpt = cityRepository.getById(1);

                assertTrue(cityOpt.isPresent());
                assertEquals("Kyiv", cityOpt.get().getName());

                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            } finally {
                ThreadLocalSessionContext.unbind(sessionFactory);
            }
        }
    }

    @Test
    void shouldMethodsGetItemsAndTotalCountWorkCorrectly() {
        try (Session session = sessionFactory.openSession()) {
            ThreadLocalSessionContext.bind(session);
            session.beginTransaction();

            CityRepository repository = new CityRepository(sessionFactory);

            assertEquals(1, repository.getTotalCount());
            assertEquals(1, repository.getItems(0, 10).size());

            session.getTransaction().commit();
            ThreadLocalSessionContext.unbind(sessionFactory);
        }
    }
}