package com.javarush;

import com.javarush.domain.City;
import com.javarush.domain.Country;
import com.javarush.domain.CountryLanguage;
import com.javarush.redis.CityCountry;
import com.javarush.repositories.CityRepository;
import com.javarush.services.RedisService;
import com.javarush.services.TransformDataService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunnerTest extends AbstractHibernateTest {

    @Mock
    private RedisService redisService;
    @Mock
    private TransformDataService dataTransformService;
    @Mock
    private CityRepository cityRepository;

    private Runner runner;
    private SessionFactory sessionFactorySpy;

    @BeforeEach
    void setUp() {
        sessionFactorySpy = Mockito.spy(sessionFactory);

        lenient().doAnswer(invocation -> {
            Session session = sessionFactory.openSession();
            ManagedSessionContext.bind(session);
            return session;
        }).when(sessionFactorySpy).getCurrentSession();

        runner = new Runner(sessionFactorySpy, redisService, dataTransformService, cityRepository);
    }

    @AfterEach
    void tearDown() {
        ManagedSessionContext.unbind(sessionFactorySpy);
    }

    @Test
    void shouldMethodRunExecuteCorrectly() {
        City mockCity = new City();
        Country mockCountry = new Country();
        mockCountry.setLanguages(new HashSet<>());
        mockCity.setCountry(mockCountry);
        List<CityCountry> transformedData = List.of(new CityCountry());

        when(cityRepository.getTotalCount()).thenReturn(1);
        when(cityRepository.getItems(0, 500)).thenReturn(List.of(mockCity));
        when(cityRepository.getById(anyInt())).thenReturn(mockCity);
        when(dataTransformService.transformData(anyList())).thenReturn(transformedData);

        runner.run();

        verify(cityRepository).getTotalCount();
        verify(cityRepository).getItems(0, 500);
        verify(dataTransformService).transformData(anyList());
        verify(redisService).pushToRedis(transformedData);
        verify(redisService).testRedisData(anyList()); // перевірка виклику тесту продуктивності Redis
        verify(redisService).shutdown();
    }

    @Test
    void shouldMethodFetchDataExecuteCorrectly() {
        when(cityRepository.getTotalCount()).thenReturn(1200);
        when(cityRepository.getItems(0, 500)).thenReturn(Collections.nCopies(500, new City()));
        when(cityRepository.getItems(500, 500)).thenReturn(Collections.nCopies(500, new City()));
        when(cityRepository.getItems(1000, 500)).thenReturn(Collections.nCopies(200, new City()));
        when(dataTransformService.transformData(anyList())).thenReturn(Collections.emptyList());
        City city = mock(City.class);
        Country country = mock(Country.class);
        when(city.getCountry()).thenReturn(country);
        when(cityRepository.getById(anyInt())).thenReturn(city);

        runner.run();

        verify(cityRepository, times(3)).getItems(anyInt(), eq(500));
    }

    @Test
    void shouldMethodTestMysqlDataExecuteCorrectly() {
        City city = mock(City.class);
        Country country = mock(Country.class);
        Set<CountryLanguage> languages = new HashSet<>();

        when(cityRepository.getTotalCount()).thenReturn(0);
        when(cityRepository.getById(anyInt())).thenReturn(city);
        when(city.getCountry()).thenReturn(country);
        when(country.getLanguages()).thenReturn(languages);
        when(dataTransformService.transformData(anyList())).thenReturn(Collections.emptyList());

        runner.run();

        verify(country, times(10)).getLanguages();
    }
}