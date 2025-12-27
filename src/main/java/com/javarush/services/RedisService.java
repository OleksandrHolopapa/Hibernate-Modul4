package com.javarush.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.redis.CityCountry;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RedisService {
    private final RedisClient redisClient;
    private final ObjectMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(RedisService.class);

    public RedisService() {
        redisClient = RedisClient.create(RedisURI.create("localhost", 6379));
        mapper = new ObjectMapper();
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            System.out.println("\nConnected to Redis\n");
        }
    }

    public void pushToRedis(List<CityCountry> data) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : data) {
                try {
                    sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
                } catch (JsonProcessingException e) {
                    logger.error("Error serializing city with ID {}: {}",
                            cityCountry.getId(), e.getMessage());
                }
            }
        }
    }

    public void testRedisData(List<Integer> ids) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (Integer id : ids) {
                String value = sync.get(String.valueOf(id));
                try {
                    mapper.readValue(value, CityCountry.class);
                } catch (JsonProcessingException e) {
                    logger.error("Error processing JSON for ID {}: {}", id, e.getMessage(), e);
                }
            }
        }
    }

    public void shutdown() {
        if (redisClient != null) {
            redisClient.shutdown();
        }
    }
}