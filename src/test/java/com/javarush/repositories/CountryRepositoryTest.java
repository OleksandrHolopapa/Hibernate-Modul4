package com.javarush.repositories;

import com.javarush.AbstractIntegrationTest;
import com.javarush.domain.Country;
import org.hibernate.Session;
import org.hibernate.context.internal.ThreadLocalSessionContext;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CountryRepositoryTest extends AbstractIntegrationTest {

    @Test
    void getAll_ShouldReturnCountriesWithLanguages() {
        try (Session session = sessionFactory.openSession()) {
            ThreadLocalSessionContext.bind(session);
            session.beginTransaction();

            CountryRepository repository = new CountryRepository(sessionFactory);
            List<Country> countries = repository.getAll();

            assertFalse(countries.isEmpty());
            assertNotNull(countries.get(0).getLanguages());

            session.getTransaction().commit();
            ThreadLocalSessionContext.unbind(sessionFactory);
        }
    }
}