package com.javarush.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.redis.CityCountry;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {
    @Mock
    private RedisClient redisClient;
    @Mock
    private StatefulRedisConnection<String, String> connection;
    @Mock
    private RedisCommands<String, String> syncCommands;
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        when(redisClient.connect()).thenReturn(connection);
        when(connection.sync()).thenReturn(syncCommands);
        redisService = new RedisService(redisClient, new ObjectMapper());
    }

    @Test
    void shouldMethodPushToRedisWorkCorrect() {
        CityCountry cityCountry = new CityCountry();
        cityCountry.setId(1);
        cityCountry.setName("Kyiv");

        redisService.pushToRedis(List.of(cityCountry));

        verify(syncCommands, times(1)).set(eq("1"), anyString());
    }

    @Test
    void shouldMethodTestRedisDataWorkCorrect() {
        when(syncCommands.get("1")).thenReturn("{\"id\":1,\"name\":\"Kyiv\"}");

        redisService.testRedisData(List.of(1));

        verify(syncCommands).get("1");
    }
}