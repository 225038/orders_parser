package com.example.orders_parser.service.parsers;

import com.example.orders_parser.dto.Order;
import com.example.orders_parser.service.ResultPrinterService;
import com.example.orders_parser.service.deserializer.OrderDeserializer;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;

@Service
public class CsvService extends TypedParserService {
    @Override
    public HashMap<Integer, Order> parse(InputStream inputStream, String fileName) {
        try {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Order.class, new OrderDeserializer(fileName));

            CsvMapper mapper = new CsvMapper();
            mapper.registerModule(module);
            mapper.configure(CsvParser.Feature.SKIP_EMPTY_LINES, true);
            mapper.configure(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE, true);
            mapper.configure(CsvParser.Feature.INSERT_NULLS_FOR_MISSING_COLUMNS, true);

            MappingIterator<Order> orderMappingIterator = mapper.readerWithSchemaFor(Order.class)
                    .readValues(inputStream);

            return getOrders(orderMappingIterator, fileName);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
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
