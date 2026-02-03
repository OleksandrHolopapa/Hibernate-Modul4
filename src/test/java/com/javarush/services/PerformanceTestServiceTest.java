package com.javarush.services;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

class PerformanceTestServiceTest {

    @Test
    void shouldExecuteBothTestsMethods() {
        PerformanceTestService service = new PerformanceTestService();
        Consumer<List<Integer>> redisMock = mock(Consumer.class);
        Consumer<List<Integer>> mysqlMock = mock(Consumer.class);
        List<Integer> ids = List.of(1, 2, 3);

        service.runPerformanceTest(ids, redisMock, mysqlMock);

        verify(redisMock).accept(ids);
        verify(mysqlMock).accept(ids);
    }
}