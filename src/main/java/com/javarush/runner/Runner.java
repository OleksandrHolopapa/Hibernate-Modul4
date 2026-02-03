package com.javarush.runner;

import com.javarush.domain.City;
import com.javarush.redis.CityCountry;
import com.javarush.repositories.CityRepository;
import com.javarush.services.*;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Runner {
    private final SessionFactory sessionFactory;
    private final RedisService redisService;
    private final TransformDataService dataTransformService;
    private final CityRepository cityRepository;
    private final PerformanceTestService performanceTestService;
    private final ConsoleInputService consoleInputService;
    private final DBPerformanceService dbPerformanceService;
    private final DatabaseService databaseService;
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    public Runner(SessionFactory sessionFactory, RedisService redisService, TransformDataService dataTransformService,
                  CityRepository cityRepository, PerformanceTestService performanceTestService,
                  ConsoleInputService consoleInputService, DBPerformanceService dbPerformanceService,
                  DatabaseService databaseService) {
        this.sessionFactory = sessionFactory;
        this.redisService = redisService;
        this.dataTransformService = dataTransformService;
        this.cityRepository = cityRepository;
        this.performanceTestService = performanceTestService;
        this.consoleInputService = consoleInputService;
        this.dbPerformanceService = dbPerformanceService;
        this.databaseService = databaseService;
    }

    public void run() {
        List<City> allCities = databaseService.getAllCities(500);
        List<CityCountry> preparedData = dataTransformService.transformData(allCities);
        redisService.pushToRedis(preparedData);

        List<Integer> ids = consoleInputService.readCityIds();
        if (!ids.isEmpty()) {
            performanceTestService.runPerformanceTest(
                    ids,
                    redisService::testRedisData,
                    dbPerformanceService::testMysqlData
            );
        } else {
            logger.warn("ID list is empty. Performance testing skipped");
        }
    }
}