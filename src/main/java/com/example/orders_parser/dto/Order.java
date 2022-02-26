
package com.example.orders_parser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;

@JsonPropertyOrder({"orderId", "amount", "currency", "comment"})
public class Order {
    @JsonProperty
    @NotNull(message = "orderId is required field")
    protected Integer orderId;

    @JsonProperty
    @NotNull(message = "amount is required field")
    protected Float amount;

    @JsonProperty
    @NotNull(message = "currency is required field")
    protected String currency;

    @JsonProperty
    @NotNull(message = "comment is required field")
    protected String comment;

    public void setField(String fieldName, Object value) {
        switch (fieldName) {
            case "orderId":
                orderId = (Integer) value;
                break;
            case "amount":
                amount = (Float) value;
                break;
            case "currency":
                currency = (String) value;
                break;
            case "comment":
                comment = (String) value;
                break;
        }
    }

    public Object getField(String fieldName) {
        switch (fieldName) {
            case "orderId":
                return orderId;
            case "amount":
                return amount;
            case "currency":
                return currency;
            case "comment":
                return comment;
            default:
                return null;
        }
    }
}
