package com.javarush.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public class PerformanceTestService {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceTestService.class);
    public void runPerformanceTest(List<Integer> ids, Consumer<List<Integer>> redisTest, Consumer<List<Integer>> mysqlTest) {

        long startRedis = System.currentTimeMillis();
        redisTest.accept(ids);
        long stopRedis = System.currentTimeMillis();

        long startMysql = System.currentTimeMillis();
        mysqlTest.accept(ids);
        long stopMysql = System.currentTimeMillis();

        logger.info("Performance results: Redis: {} ms, MySQL: {} ms", (stopRedis - startRedis), (stopMysql - startMysql));
    }
}