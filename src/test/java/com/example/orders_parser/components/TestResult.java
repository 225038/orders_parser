package com.example.orders_parser.components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestResult {
    @JsonProperty
    public String fileName;
    @JsonProperty
    public String[] results;
}
