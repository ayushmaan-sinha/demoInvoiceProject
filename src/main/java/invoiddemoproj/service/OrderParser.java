package invoiddemoproj.service;

import invoiddemoproj.model.OrderRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class OrderParser {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    public ParseOutcome parseLine(String line, int lineNumber) {
        if (line == null || line.trim().isEmpty()) {
            return ParseOutcome.error("Line " + lineNumber + ": blank line");
        }

        String[] parts = line.split("\\|");
        if (parts.length != 6) {
            return ParseOutcome.error("Line " + lineNumber + ": expected 6 fields, found " + parts.length + " -> " + line);
        }

        String orderId = parts[0].trim();
        String customerName = parts[1].trim();
        String productName = parts[2].trim();
        String quantityStr = parts[3].trim();
        String unitPriceStr = parts[4].trim();
        String dateStr = parts[5].trim();

        if (orderId.isEmpty() || customerName.isEmpty() || productName.isEmpty()) {
            return ParseOutcome.error("Line " + lineNumber + ": orderId, customerName, and productName must not be empty");
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException ex) {
            return ParseOutcome.error("Line " + lineNumber + ": invalid quantity '" + quantityStr + "'");
        }
        if (quantity <= 0) {
            return ParseOutcome.error("Line " + lineNumber + ": quantity must be positive");
        }

        BigDecimal unitPrice;
        try {
            unitPrice = new BigDecimal(unitPriceStr);
        } catch (NumberFormatException ex) {
            return ParseOutcome.error("Line " + lineNumber + ": invalid unit price '" + unitPriceStr + "'");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            return ParseOutcome.error("Line " + lineNumber + ": unit price cannot be negative");
        }

        LocalDate orderDate;
        try {
            orderDate = LocalDate.parse(dateStr, DATE_FORMAT);
        } catch (DateTimeParseException ex) {
            return ParseOutcome.error("Line " + lineNumber + ": invalid date '" + dateStr + "', expected yyyy-MM-dd");
        }

        OrderRecord order = new OrderRecord(orderId, customerName, productName, quantity, unitPrice, orderDate);
        return ParseOutcome.success(order);
    }

    public static class ParseOutcome {
        private final OrderRecord order;
        private final String errorMessage;

        private ParseOutcome(OrderRecord order, String errorMessage) {
            this.order = order;
            this.errorMessage = errorMessage;
        }

        public static ParseOutcome success(OrderRecord order) {
            return new ParseOutcome(order, null);
        }

        public static ParseOutcome error(String message) {
            return new ParseOutcome(null, message);
        }

        public boolean isSuccess() {
            return order != null;
        }

        public OrderRecord getOrder() {
            return order;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
