package com.demo.invoice;

import com.demo.invoice.model.OrderRecord;
import com.demo.invoice.service.OrderParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderParserTest {

    @TempDir
    Path tempDir;

    @Test
    void parsesValidLine() throws IOException {
        Path input = tempDir.resolve("orders.txt");
        Path errors = tempDir.resolve("errors.log");
        Files.write(input, List.of("ORD01|Jane Doe|Widget|2|10.00|2024-01-01"));

        List<OrderRecord> records = new OrderParser().parseFile(input, errors);

        assertEquals(1, records.size());
        OrderRecord record = records.get(0);
        assertEquals("ORD01", record.getOrderId());
        assertEquals("Jane Doe", record.getCustomerName());
        assertEquals(2, record.getQuantity());
        assertEquals("10.00", record.getUnitPrice().toPlainString());
        assertTrue(Files.readString(errors).contains("No malformed records"));
    }

    @Test
    void logsMalformedLine() throws IOException {
        Path input = tempDir.resolve("orders_bad.txt");
        Path errors = tempDir.resolve("errors_bad.log");
        Files.write(input, List.of("ORD02|Jane Doe|Widget|-1|10.00|2024-01-01"));

        List<OrderRecord> records = new OrderParser().parseFile(input, errors);

        assertTrue(records.isEmpty(), "Malformed record should be skipped");
        String errorLog = Files.readString(errors);
        assertTrue(errorLog.contains("Quantity must be positive"));
    }
}
