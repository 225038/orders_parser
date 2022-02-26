package com.example.orders_parser.service.parsers;

import com.example.orders_parser.dto.Order;
import com.example.orders_parser.service.ResultPrinterService;
import com.example.orders_parser.service.deserializer.OrderDeserializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;

@Service
public class JsonService extends TypedParserService {
    @Override
    public HashMap<Integer, Order> parse(InputStream inputStream, String fileName) {
        try {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Order.class, new OrderDeserializer(fileName));

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(module);
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            MappingIterator<Order> orderMappingIterator = mapper.readerFor(Order.class).readValues(inputStream);

            return getOrders(orderMappingIterator, fileName);
        } catch (Throwable e) {
            ResultPrinterService.printFailResult(
                    new Order(),
                    "ReadingErr",
                    fileName,
                    null
            );

            return null;
        }
    }
}
