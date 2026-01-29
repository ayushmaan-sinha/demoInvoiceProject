package invoiddemoproj.service;

import invoiddemoproj.model.CustomerSummary;
import invoiddemoproj.model.InvoiceReport;
import invoiddemoproj.model.OrderRecord;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InvoiceProcessor {
    private final OrderParser parser;

    public InvoiceProcessor() {
        this.parser = new OrderParser();
    }

    public InvoiceReport process(Path inputPath) throws IOException {
        List<String> errors = new ArrayList<>();
        List<OrderRecord> orders = new ArrayList<>();

        if (!Files.exists(inputPath)) {
            errors.add("Input file not found: " + inputPath.toAbsolutePath());
            return new InvoiceReport(List.of(), 0, 0,
                    java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, errors);
        }

        List<String> lines = Files.readAllLines(inputPath, StandardCharsets.UTF_8);
        int lineNumber = 0;
        for (String line : lines) {
            lineNumber++;
            OrderParser.ParseOutcome outcome = parser.parseLine(line, lineNumber);
            if (outcome.isSuccess()) {
                orders.add(outcome.getOrder());
            } else {
                errors.add(outcome.getErrorMessage());
            }
        }

        Map<String, CustomerSummary> summaryMap = new LinkedHashMap<>();
        int totalItems = 0;
        int totalOrders = 0;
        java.math.BigDecimal grossTotal = java.math.BigDecimal.ZERO;
        java.math.BigDecimal discountTotal = java.math.BigDecimal.ZERO;

        for (OrderRecord order : orders) {
            CustomerSummary summary = summaryMap.computeIfAbsent(order.getCustomerName(),
                    CustomerSummary::new);
            summary.addOrder(order);
            totalItems += order.getQuantity();
            totalOrders++;
            grossTotal = grossTotal.add(order.lineTotal());
            discountTotal = discountTotal.add(order.discount());
        }

        List<CustomerSummary> sortedSummaries = summaryMap.values().stream()
                .sorted(Comparator.comparing(CustomerSummary::getCustomerName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        return new InvoiceReport(sortedSummaries, totalOrders, totalItems, grossTotal, discountTotal, errors);
    }
}
