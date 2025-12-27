package com.javarush;

import com.javarush.config.SessionFactoryConfig;
import com.javarush.repositories.CityRepository;
import com.javarush.domain.City;
import com.javarush.domain.CountryLanguage;
import com.javarush.redis.CityCountry;
import com.javarush.services.TransformDataService;
import com.javarush.services.RedisService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Runner {
    private final SessionFactory sessionFactory;
    private final RedisService redisService;
    private final TransformDataService dataTransformService;
    private final CityRepository cityRepository;

    public Runner() {
        sessionFactory = SessionFactoryConfig.prepareRelationalDb();
        redisService = new RedisService();
        dataTransformService = new TransformDataService();
        cityRepository = new CityRepository(sessionFactory);
    }

    public void run() {
        List<City> allCities = fetchData();
        List<CityCountry> preparedData = dataTransformService.transformData(allCities);
        redisService.pushToRedis(preparedData);

        sessionFactory.getCurrentSession().close();

        List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

        testPerformance(ids);
        shutdown();
    }

    private List<City> fetchData() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            List<City> allCities = new ArrayList<>();
            int totalCount = cityRepository.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(cityRepository.getItems(i, step));
            }
            session.getTransaction().commit();
            return allCities;
        }
    }

    private void testPerformance(List<Integer> ids) {
        long startRedis = System.currentTimeMillis();
        redisService.testRedisData(ids);
        long stopRedis = System.currentTimeMillis();

        long startMysql = System.currentTimeMillis();
        testMysqlData(ids);
        long stopMysql = System.currentTimeMillis();

        System.out.printf("%s:\t%d мс\n", "Redis", (stopRedis - startRedis));
        System.out.printf("%s:\t%d мс\n", "MySQL", (stopMysql - startMysql));
    }

    private void testMysqlData(List<Integer> ids) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            for (Integer id : ids) {
                City city = cityRepository.getById(id);
                Set<CountryLanguage> languages = city.getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }

    private void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        redisService.shutdown();
    }
}