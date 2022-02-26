package com.example.orders_parser;

import com.example.orders_parser.components.TestResult;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrdersParserApplicationTests {
    @Autowired
    private ResourceLoader resourceLoader;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void mainTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        MappingIterator<TestResult> it = mapper.readerFor(TestResult.class)
                .readValues(resourceLoader.getResource("/test/testResults.json").getInputStream());
        List<TestResult> results = it.readAll();

        String[] args = results.stream().map(e -> e.fileName).toArray(String[]::new);

        OrdersParserApplication.main(args);

        List<String> expectedList = Arrays.asList(results.stream().map(e -> String.join("\n", e.results))
                .collect(Collectors.joining("\n")).split("\n"));
        List<String> actualList = Arrays.asList(outputStream.toString().trim().split("\n"));

        assertThat(actualList).hasSameElementsAs(expectedList);
    }
}
