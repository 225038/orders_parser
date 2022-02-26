package com.example.orders_parser.service.deserializer;

import com.example.orders_parser.dto.Order;
import com.example.orders_parser.exceptions.RequiredFieldEmptyException;
import com.example.orders_parser.service.ResultPrinterService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderDeserializer extends StdDeserializer<Order> {
    private JsonNode node;
    private Order order;

    private List<String> errorsList = new ArrayList<>(4);

    private final String fileName;
    private Integer lineNumber = 1;

    enum FieldType {INTEGER, TEXT, FLOAT}

    public OrderDeserializer(String fileName) {
        super(Order.class);

        this.fileName = fileName;
    }

    @Override
    public Order deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        node = jp.getCodec().readTree(jp);
        order = new Order();

        setField("orderId", FieldType.INTEGER);
        setField("amount", FieldType.FLOAT);
        setField("currency", FieldType.TEXT);
        setField("comment", FieldType.TEXT);

        if (!errorsList.isEmpty()) {
            ResultPrinterService.printFailResult(
                    order,
                    "Order errs: " + errorsList,
                    fileName,
                    lineNumber
            );

            lineNumber++;
            errorsList.clear();

            return null;
        }

        lineNumber++;

        return order;
    }

    private void setField(String fieldName, FieldType type) {
        try {
            switch (type) {
                case TEXT: {
                    order.setField(fieldName, getTextValue(fieldName));

                    break;
                }
                case INTEGER: {
                    order.setField(fieldName, Integer.parseInt(getTextValue(fieldName)));

                    break;
                }
                case FLOAT: {
                    order.setField(fieldName, Float.parseFloat(getTextValue(fieldName)));

                    break;
                }
            }
        } catch (RequiredFieldEmptyException | NullPointerException e) {
            errorsList.add(fieldName + " is required field");
        } catch (Throwable e) {
            errorsList.add("Incorrect value of field " + fieldName);
        }
    }

    private String getTextValue(String fieldName) throws RequiredFieldEmptyException {
        String textValue = node.get(fieldName).asText();

        if (textValue.equals("null") || textValue.isEmpty()) {
            throw new RequiredFieldEmptyException();
        }

        return textValue;
    }
}
