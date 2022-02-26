
package com.example.orders_parser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderResult extends Order {
    @JsonProperty("filename")
    private String filename;

    @JsonProperty("result")
    private String result;

    @JsonProperty("line")
    private Integer line;

    public OrderResult(Order order, String filename, String result, Integer line) {
        this.orderId = order.orderId;
        this.amount = order.amount;
        this.currency = order.currency;
        this.comment = order.comment;

        this.filename = filename;
        this.result = result;
        this.line = line;
    }
}
