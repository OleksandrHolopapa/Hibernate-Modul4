package com.javarush.services;

import com.javarush.domain.*;
import com.javarush.redis.CityCountry;
import com.javarush.redis.Language;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransformDataServiceTest {

    private final TransformDataService transformDataService = new TransformDataService();

    @Test
    void shouldCorrectlyMapAllFieldsIncludingLanguagesInMethodTransformData() {
        Country country = getCountry();

        City city = new City();
        city.setId(1);
        city.setName("Kyiv");
        city.setPopulation(3000000);
        city.setDistrict("Kyiv City");
        city.setCountry(country);

        List<CityCountry> result = transformDataService.transformData(List.of(city));

        assertNotNull(result);
        assertEquals(1, result.size());
        CityCountry cityCountry = result.get(0);

        assertEquals(1, cityCountry.getId());
        assertEquals("Kyiv", cityCountry.getName());
        assertEquals(3000000, cityCountry.getPopulation());
        assertEquals("Kyiv City", cityCountry.getDistrict());
        assertEquals("Ukraine", cityCountry.getCountryName());
        assertEquals("UKR", cityCountry.getCountryCode());
        assertEquals("UA", cityCountry.getAlternativeCountryCode());
        assertEquals(Continent.EUROPE, cityCountry.getContinent());
        assertEquals(30000000, cityCountry.getCountryPopulation());
        assertEquals(BigDecimal.valueOf(603628), cityCountry.getCountrySurfaceArea());

        assertNotNull(cityCountry.getLanguages());
        assertEquals(1, cityCountry.getLanguages().size());
        Language cityCountryLanguage = cityCountry.getLanguages().iterator().next();
        assertEquals("Ukrainian", cityCountryLanguage.getLanguage());
        assertTrue(cityCountryLanguage.getIsOfficial());
        assertEquals(BigDecimal.valueOf(100), cityCountryLanguage.getPercentage());
    }

    private static Country getCountry() {
        CountryLanguage countryLanguage = new CountryLanguage();
        countryLanguage.setLanguage("Ukrainian");
        countryLanguage.setIsOfficial(true);
        countryLanguage.setPercentage(BigDecimal.valueOf(100));

        Country country = new Country();
        country.setName("Ukraine");
        country.setCode("UKR");
        country.setAlternativeCode("UA");
        country.setContinent(Continent.EUROPE);
        country.setPopulation(30000000);
        country.setRegion("Eastern Europe");
        country.setSurfaceArea(BigDecimal.valueOf(603628));
        country.setLanguages(Set.of(countryLanguage));
        return country;
    }

    @Test
    void shouldHandleEmptyListInMethodTransformData() {
        List<CityCountry> result = transformDataService.transformData(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}