package com.javarush.services;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleInputServiceTest {

    @Test
    void shouldFilterNegativeAndInvalidIds() {
        String input = "5, abc, -10, 42\n";
        ConsoleInputService service = new ConsoleInputService(new ByteArrayInputStream(input.getBytes()));

        List<Integer> result = service.readCityIds();

        assertEquals(2, result.size());
        assertTrue(result.contains(5));
        assertTrue(result.contains(42));
        assertFalse(result.contains(-10));
    }
}