package com.example.orders_parser.service.parsers;

import com.example.orders_parser.dto.Order;
import com.example.orders_parser.service.ResultPrinterService;
import com.fasterxml.jackson.databind.MappingIterator;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;

@Service
abstract public class TypedParserService {
    public abstract HashMap<Integer, Order> parse(InputStream inputStream, String fileName);

    public HashMap<Integer, Order> getOrders(MappingIterator<Order> orderMappingIterator, String fileName) {
        HashMap<Integer, Order> orders = new HashMap<>();
        int line = 1;

        try {
            while (orderMappingIterator.hasNext()) {
                Order value = orderMappingIterator.nextValue();

                if (value != null) {
                    orders.put(line, value);
                }

                line++;
            }
        } catch (Throwable $e) {
            ResultPrinterService.printFailResult(
                    new Order(),
                    "Order format err",
                    fileName,
                    line
            );
        }

        return orders;
    }
}