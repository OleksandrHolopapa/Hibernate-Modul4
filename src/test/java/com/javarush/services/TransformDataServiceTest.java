package com.javarush.services;

import com.javarush.domain.City;
import com.javarush.domain.Country;
import com.javarush.domain.CountryLanguage;
import com.javarush.redis.CityCountry;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransformDataServiceTest {

    @Test
    void shouldMapAllFieldsCorrectlyInTransformDataMethod() {
        TransformDataService service = new TransformDataService();

        Country country = new Country();
        country.setName("TestCountry");
        country.setCode("TST");

        CountryLanguage lang = new CountryLanguage();
        lang.setLanguage("TestLang");
        lang.setIsOfficial(true);
        country.setLanguages(Set.of(lang));

        City city = new City();
        city.setId(100);
        city.setName("TestCity");
        city.setCountry(country);

        List<CityCountry> result = service.transformData(Collections.singletonList(city));

        assertEquals(1, result.size());
        CityCountry cityCountry = result.get(0);
        assertEquals("TestCity", cityCountry.getName());
        assertEquals("TestCountry", cityCountry.getCountryName());
        assertEquals(1, cityCountry.getLanguages().size());
    }
}