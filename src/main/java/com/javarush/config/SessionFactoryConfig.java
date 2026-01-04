package com.javarush.config;

import com.javarush.domain.City;
import com.javarush.domain.Country;
import com.javarush.domain.CountryLanguage;
import com.javarush.exceptions.ConfigException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SessionFactoryConfig {
    private final Configuration configuration;
    private static final Logger logger = LoggerFactory.getLogger(SessionFactoryConfig.class);

    public SessionFactoryConfig(ConfigFile configFile) {
        String propertiesFileName = configFile.getFileName();
        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (inputStream == null) {
                logger.error("Property file {} not found", propertiesFileName);
                throw new ConfigException("Property file not found: " + propertiesFileName);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to load properties from {}", propertiesFileName);
            throw new ConfigException("Error loading " + propertiesFileName);
        }

        this.configuration = new Configuration()
                .addProperties(properties)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(CountryLanguage.class);
    }

    public SessionFactory buildSessionFactory() {
        return configuration.buildSessionFactory();
    }
}