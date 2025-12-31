package com.javarush.exceptions;

public class ConfigException extends RuntimeException {
    public ConfigException(String message) {
        super(message);
    }
}