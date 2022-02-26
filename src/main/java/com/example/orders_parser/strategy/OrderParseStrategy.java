package com.example.orders_parser.strategy;

import com.example.orders_parser.dto.Order;
import com.example.orders_parser.service.parsers.CsvService;
import com.example.orders_parser.service.parsers.JsonService;
import com.example.orders_parser.service.parsers.TypedParserService;
import com.example.orders_parser.service.ResultPrinterService;
import org.apache.commons.io.FilenameUtils;

import java.io.InputStream;
import java.util.HashMap;

public enum OrderParseStrategy {
    CSV(new CsvService()),
    JSON(new JsonService()),
    DEFAULT() {
        @Override
        public HashMap<Integer, Order> parseByType(InputStream inputStream, String fileName) {
            ResultPrinterService.printFailResult(
                    new Order(),
                    "Unsupported filetype",
                    fileName,
                    null
            );
            return null;
        }
    };

    protected TypedParserService parserService;

    OrderParseStrategy() {
    }

    OrderParseStrategy(TypedParserService parserService) {
        this.parserService = parserService;
    }

    public static HashMap<Integer, Order> parseFile(InputStream inputStream, String fileName) {
        String type = FilenameUtils.getExtension(fileName);

        switch (type) {
            case "csv": {
                return OrderParseStrategy.CSV.parseByType(inputStream, fileName);
            }
            case "json": {
                return OrderParseStrategy.JSON.parseByType(inputStream, fileName);
            }
            default: {
                return OrderParseStrategy.DEFAULT.parseByType(inputStream, fileName);
            }
        }
    }

    public HashMap<Integer, Order> parseByType(InputStream inputStream, String fileName) {
        return this.parserService.parse(inputStream, fileName);
    }
}
