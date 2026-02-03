package com.javarush.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConfigFile {
    REDIS("redis.properties"),
    HIBERNATE("hibernate.properties"),
    TEST_HIBERNATE("hibernate-test.properties");

    private final String fileName;
}