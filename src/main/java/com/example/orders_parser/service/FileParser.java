package com.example.orders_parser.service;

import com.example.orders_parser.dto.Order;
import com.example.orders_parser.strategy.OrderParseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@Service
public class FileParser {
    @Autowired
    private ResourceLoader resourceLoader;

    public HashMap<Integer, Order> handleFileParsing(String fileName) {
        try {
            InputStream inputStream = resourceLoader.getResource(fileName).getInputStream();

            return OrderParseStrategy.parseFile(inputStream, fileName);
        } catch (FileNotFoundException e) {
            ResultPrinterService.printFailResult(
                    new Order(),
                    String.format("File %s wasn't found ⊙︿⊙", fileName),
                    fileName,
                    null
            );
        } catch (IOException e) {
            ResultPrinterService.printFailResult(
                    new Order(),
                    e.getMessage(),
                    fileName,
                    null
            );
        }

        return null;
    }
}
