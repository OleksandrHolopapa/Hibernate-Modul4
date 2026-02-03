package com.javarush.services;

import com.javarush.AbstractIntegrationTest;
import com.javarush.domain.City;
import com.javarush.repositories.CityRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseServiceTest extends AbstractIntegrationTest {

    @Test
    void shouldReturnAllCitiesFromDB() {
        CityRepository cityRepository = new CityRepository(sessionFactory);
        DatabaseService databaseService = new DatabaseService(sessionFactory, cityRepository);

        List<City> cities = databaseService.getAllCities(1);

        assertNotNull(cities);
        assertEquals(1, cities.size());
        assertEquals("Kyiv", cities.get(0).getName());
    }
}