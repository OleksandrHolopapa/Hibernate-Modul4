package com.javarush.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleInputService {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleInputService.class);
    private final Scanner scanner;

    public ConsoleInputService(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
    }

    public List<Integer> readCityIds() {
        List<Integer> ids = new ArrayList<>();
        System.out.println("Enter the IDs of the cities(separated by commas or spaces) that will participate in the testing:");
        if (!scanner.hasNextLine()) return ids;

        String[] inputs = scanner.nextLine().split("[,\\s]+");

        for (String input : inputs) {
            try {
                int id = Integer.parseInt(input.trim());
                if(id>0) ids.add(id);
            } catch (NumberFormatException ignored) {
                logger.error("City with ID = {} do not exist.", input);
            }
        }
        return ids;
    }
}