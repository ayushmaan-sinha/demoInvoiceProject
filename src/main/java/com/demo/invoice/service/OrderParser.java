package com.demo.invoice.service;

import com.demo.invoice.model.OrderRecord;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderParser {
    private static final String DELIMITER_REGEX = "\\|";

    public List<OrderRecord> parseFile(Path inputFile, Path errorFile) throws IOException {
        if (!Files.exists(inputFile)) {
            throw new IllegalArgumentException("Input file does not exist: " + inputFile);
        }

        List<OrderRecord> validOrders = new ArrayList<>();
        List<String> errorLines = new ArrayList<>();

        AtomicInteger lineNumber = new AtomicInteger(0);
        try (var lines = Files.lines(inputFile)) {
            lines.forEachOrdered(line -> {
                int currentLine = lineNumber.incrementAndGet();
                ParseResult result = parseLine(line);
                if (result.errorMessage != null) {
                    errorLines.add("Line " + currentLine + ": " + result.errorMessage + " | Content: " + line);
                } else {
                    validOrders.add(result.orderRecord);
                }
            });
        }

        if (errorFile != null) {
            Path parent = errorFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (errorLines.isEmpty()) {
                Files.writeString(errorFile, "No malformed records found.\n");
            } else {
                Files.write(errorFile, errorLines);
            }
        }

        return validOrders;
    }

    private ParseResult parseLine(String line) {
        String trimmed = line == null ? "" : line.trim();
        if (trimmed.isEmpty()) {
            return ParseResult.error("Empty line");
        }

        String[] parts = trimmed.split(DELIMITER_REGEX, -1);
        if (parts.length != 6) {
            return ParseResult.error("Expected 6 pipe-delimited fields");
        }

        String orderId = parts[0].trim();
        String customerName = parts[1].trim();
        String productName = parts[2].trim();
        String quantityStr = parts[3].trim();
        String unitPriceStr = parts[4].trim();
        String orderDateStr = parts[5].trim();

        if (orderId.isEmpty() || customerName.isEmpty() || productName.isEmpty()) {
            return ParseResult.error("OrderId, CustomerName, and ProductName are required");
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException ex) {
            return ParseResult.error("Quantity is not an integer");
        }
        if (quantity <= 0) {
            return ParseResult.error("Quantity must be positive");
        }

        BigDecimal unitPrice;
        try {
            unitPrice = new BigDecimal(unitPriceStr);
        } catch (NumberFormatException ex) {
            return ParseResult.error("UnitPrice is not a valid decimal");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            return ParseResult.error("UnitPrice must not be negative");
        }

        LocalDate orderDate;
        try {
            orderDate = LocalDate.parse(orderDateStr);
        } catch (DateTimeParseException ex) {
            return ParseResult.error("OrderDate is not ISO-8601 (yyyy-MM-dd)");
        }

        OrderRecord record = new OrderRecord(orderId, customerName, productName, quantity, unitPrice, orderDate);
        return ParseResult.success(record);
    }

    private static final class ParseResult {
        private final OrderRecord orderRecord;
        private final String errorMessage;

        private ParseResult(OrderRecord orderRecord, String errorMessage) {
            this.orderRecord = orderRecord;
            this.errorMessage = errorMessage;
        }

        static ParseResult success(OrderRecord orderRecord) {
            return new ParseResult(orderRecord, null);
        }

        static ParseResult error(String message) {
            return new ParseResult(null, message);
        }
    }
}
