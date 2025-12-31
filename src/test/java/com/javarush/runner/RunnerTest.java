package com.javarush.runner;

import com.javarush.services.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunnerTest {

    @Mock
    private DatabaseService databaseService;
    @Mock
    private RedisService redisService;
    @Mock
    private ConsoleInputService consoleInputService;
    @Mock
    private PerformanceTestService performanceTestService;
    @Mock
    private DBPerformanceService dbPerformanceService;

    @Test
    void shouldPerformFullWorkflowWhenIdsPresent() {
        when(databaseService.getAllCities(anyInt())).thenReturn(List.of());
        when(consoleInputService.readCityIds()).thenReturn(List.of(1, 2));

        Runner runner = new Runner(null, redisService, new TransformDataService(), null,
                performanceTestService, consoleInputService, dbPerformanceService, databaseService);

        runner.run();

        verify(performanceTestService).runPerformanceTest(any(), any(), any());
    }

    @Test
    void shouldSkipPerformanceTestWhenIdsEmpty() {
        when(databaseService.getAllCities(anyInt())).thenReturn(List.of());
        when(consoleInputService.readCityIds()).thenReturn(List.of());

        Runner runner = new Runner(null, redisService, new TransformDataService(), null,
                performanceTestService, consoleInputService, dbPerformanceService, databaseService);

        runner.run();

        verify(performanceTestService, never()).runPerformanceTest(any(), any(), any());
    }
}