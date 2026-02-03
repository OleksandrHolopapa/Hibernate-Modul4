package com.javarush.services;

import com.javarush.AbstractIntegrationTest;
import com.javarush.repositories.CityRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DBPerformanceServiceTest extends AbstractIntegrationTest {

    @Test
    void shouldMethodTestMysqlDataExecuteWithoutExceptions() {
        CityRepository cityRepository = new CityRepository(sessionFactory);
        DBPerformanceService service = new DBPerformanceService(sessionFactory, cityRepository);

        assertDoesNotThrow(() -> service.testMysqlData(List.of(1, 999)));
    }
}