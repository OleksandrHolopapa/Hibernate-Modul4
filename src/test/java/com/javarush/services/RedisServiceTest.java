package com.javarush.services;

import com.javarush.redis.CityCountry;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
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
        redisService = new RedisService(redisClient);
        clearInvocations(redisClient, connection);
    }

    @Test
    void shouldExecuteMethodPushToRedis() {
        when(redisClient.connect()).thenReturn(connection);
        when(connection.sync()).thenReturn(syncCommands);

        CityCountry city = new CityCountry();
        city.setId(1);
        city.setName("Kyiv");

        redisService.pushToRedis(Collections.singletonList(city));

        verify(syncCommands, times(1)).set(eq("1"), anyString());
        verify(connection, times(1)).close();
    }

    @Test
    void shouldFetchValuesFromRedisInMethodTestRedisData() {
        when(redisClient.connect()).thenReturn(connection);
        when(connection.sync()).thenReturn(syncCommands);
        when(syncCommands.get("1")).thenReturn("{\"id\":1,\"name\":\"Kyiv\"}");

        redisService.testRedisData(List.of(1));

        verify(syncCommands, times(1)).get("1");
        verify(connection, times(1)).close();
    }

    @Test
    void shouldHandleJsonParsingErrorInMethodTestRedisDataWhenWrongJSON() {
        when(redisClient.connect()).thenReturn(connection);
        when(connection.sync()).thenReturn(syncCommands);
        when(syncCommands.get("1")).thenReturn("not-a-json");

        redisService.testRedisData(List.of(1));

        verify(syncCommands).get("1");
        verify(connection).close();
    }

    @Test
    void shouldCallClientShutdownInMethodShutdown() {
        redisService.shutdown();
        verify(redisClient, times(1)).shutdown();
    }
}