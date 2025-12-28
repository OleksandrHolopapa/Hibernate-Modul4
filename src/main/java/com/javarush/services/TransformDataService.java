package com.javarush.services;

import com.javarush.domain.City;
import com.javarush.domain.Country;
import com.javarush.domain.CountryLanguage;
import com.javarush.redis.CityCountry;
import com.javarush.redis.Language;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TransformDataService {
    public List<CityCountry> transformData(List<City> cities) {
        return cities.stream().map(this::convertToCityCountry).collect(Collectors.toList());
    }

    private CityCountry convertToCityCountry(City city) {
        CityCountry cityCountry = new CityCountry();
        cityCountry.setId(city.getId());
        cityCountry.setName(city.getName());
        cityCountry.setPopulation(city.getPopulation());
        cityCountry.setDistrict(city.getDistrict());

        Country country = city.getCountry();
        cityCountry.setAlternativeCountryCode(country.getAlternativeCode());
        cityCountry.setContinent(country.getContinent());
        cityCountry.setCountryCode(country.getCode());
        cityCountry.setCountryName(country.getName());
        cityCountry.setCountryPopulation(country.getPopulation());
        cityCountry.setCountryRegion(country.getRegion());
        cityCountry.setCountrySurfaceArea(country.getSurfaceArea());

        Set<Language> languages = country.getLanguages().stream()
                .map(this::convertToLanguage)
                .collect(Collectors.toSet());
        cityCountry.setLanguages(languages);

        return cityCountry;
    }

    private Language convertToLanguage(CountryLanguage cl) {
        Language language = new Language();
        language.setLanguage(cl.getLanguage());
        language.setIsOfficial(cl.getIsOfficial());
        language.setPercentage(cl.getPercentage());
        return language;
    }
}