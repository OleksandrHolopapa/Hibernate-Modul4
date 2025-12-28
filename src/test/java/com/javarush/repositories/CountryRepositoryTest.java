package com.javarush.repositories;

import com.javarush.AbstractHibernateTest;
import com.javarush.domain.*;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CountryRepositoryTest extends AbstractHibernateTest {
    private CountryRepository countryRepository;

    @BeforeEach
    void setUp() {
        countryRepository = new CountryRepository(sessionFactory);
    }

    @Test
    void shouldReturnAllCountriesInMethodGetAll() {
        Session session = sessionFactory.getCurrentSession();
        Country country = new Country();
        country.setId(100);
        country.setName("Ukraine");
        country.setCode("UKR");
        country.setContinent(Continent.EUROPE);
        session.persist(country);

        CountryLanguage lang = new CountryLanguage();
        lang.setCountry(country);
        lang.setLanguage("Ukrainian");
        lang.setIsOfficial(true);
        lang.setPercentage(BigDecimal.valueOf(100));
        session.persist(lang);

        session.flush();
        session.clear();

        List<Country> countries = countryRepository.getAll();
        assertFalse(countries.isEmpty());
        assertEquals(1, countries.get(0).getLanguages().size());
    }
}