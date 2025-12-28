package com.javarush.repositories;

import com.javarush.AbstractHibernateTest;
import com.javarush.domain.*;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityRepositoryTest extends AbstractHibernateTest {
    private CityRepository cityRepository;

    @BeforeEach
    void setUp() {
        cityRepository = new CityRepository(sessionFactory);
    }

    @Test
    void shouldReturnCorrectCitiesListWithPaginationInMethodGetItems() {
        Session session = sessionFactory.getCurrentSession();

        for (int i = 1; i <= 3; i++) {
            City city = new City();
            city.setName("City " + i);
            session.persist(city);
        }

        session.flush();
        session.clear();

        List<City> firstPage = cityRepository.getItems(0, 2);
        assertEquals(2, firstPage.size());
        assertEquals("City 1", firstPage.get(0).getName());
        assertEquals("City 2", firstPage.get(1).getName());

        List<City> secondPage = cityRepository.getItems(2, 2);
        assertEquals(1, secondPage.size());
        assertEquals("City 3", secondPage.get(0).getName());

        List<City> outOfRange = cityRepository.getItems(10, 2);
        assertTrue(outOfRange.isEmpty());
    }

    @Test
    void shouldReturnCorrectCountInMethodGetTotalCount() {
        Session session = sessionFactory.getCurrentSession();
        City city = new City();
        city.setName("Kyiv");
        session.persist(city);

        assertEquals(1, cityRepository.getTotalCount());
    }

    @Test
    void shouldReturnCityWithFetchedCountryInMethodGetById() {
        Session session = sessionFactory.getCurrentSession();
        Country country = new Country();
        country.setId(1);
        country.setName("Ukraine");
        country.setCode("UKR");
        session.persist(country);

        City city = new City();
        city.setName("Kyiv");
        city.setCountry(country);
        session.persist(city);

        session.flush();
        session.clear();

        City foundCity = cityRepository.getById(city.getId());
        assertNotNull(foundCity);
        assertEquals("Ukraine", foundCity.getCountry().getName());
    }
}