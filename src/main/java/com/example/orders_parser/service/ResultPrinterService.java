package com.example.orders_parser.service;

import com.example.orders_parser.dto.Order;
import com.example.orders_parser.dto.OrderResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ResultPrinterService {
    public static synchronized void printOrderResult(Order order, String fileName, Integer lineNumber) {
        printResult(new OrderResult(order, fileName, "OK", lineNumber));
    }

    public static synchronized void printFailResult(Order order, String msg, String fileName, Integer lineNumber) {
        printResult(new OrderResult(order, fileName, msg, lineNumber));
    }

    public static synchronized void printResult(OrderResult orderResult) {
        try {
            ObjectWriter ow = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS)
                    .writer()
                    .with(new MinimalPrettyPrinter());

            System.out.println(ow.writeValueAsString(orderResult));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
