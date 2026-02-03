package com.javarush.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.repositories.CityRepository;
import com.javarush.services.*;
import io.lettuce.core.RedisClient;
import org.hibernate.SessionFactory;

public class RunnerFactory {

    public static Runner createRunner(SessionFactory sessionFactory, RedisClient redisClient) {
        ObjectMapper mapper = new ObjectMapper();
        CityRepository cityRepository = new CityRepository(sessionFactory);
        RedisService redisService = new RedisService(redisClient, mapper);
        TransformDataService dataTransformService = new TransformDataService();
        PerformanceTestService performanceTestService = new PerformanceTestService();
        ConsoleInputService consoleInputService = new ConsoleInputService(System.in);
        DBPerformanceService dbPerformanceService = new DBPerformanceService(sessionFactory, cityRepository);
        DatabaseService databaseService = new DatabaseService(sessionFactory, cityRepository);

        return new Runner(
                sessionFactory,
                redisService,
                dataTransformService,
                cityRepository,
                performanceTestService,
                consoleInputService,
                dbPerformanceService,
                databaseService
        );
    }
}