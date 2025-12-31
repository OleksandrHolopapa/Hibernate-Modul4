package com.javarush;

import com.javarush.config.ConfigFile;
import com.javarush.config.RedisConfig;
import com.javarush.config.SessionFactoryConfig;
import com.javarush.runner.Runner;
import com.javarush.runner.RunnerFactory;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        RedisConfig redisConfig = new RedisConfig(ConfigFile.REDIS.getFileName());
        SessionFactoryConfig sfConfig = new SessionFactoryConfig(ConfigFile.HIBERNATE.getFileName());

        try (SessionFactory sessionFactory = sfConfig.buildSessionFactory();
             RedisClient redisClient = RedisClient.create(RedisURI.create(redisConfig.getHost(), redisConfig.getPort()))) {
            Runner runner = RunnerFactory.createRunner(sessionFactory, redisClient);
            runner.run();
        } catch (Exception e) {
            logger.error("Critical application error: {}", e.getMessage(), e);
        }
    }
}