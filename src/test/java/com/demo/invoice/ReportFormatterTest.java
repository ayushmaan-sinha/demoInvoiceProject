package com.demo.invoice;

import com.demo.invoice.model.CustomerSummary;
import com.demo.invoice.service.OrderProcessor;
import com.demo.invoice.service.ReportFormatter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class ReportFormatterTest {

    @Test
    void formatsSummaryWithGrandTotal() {
        Map<String, CustomerSummary> summaries = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        CustomerSummary alice = new CustomerSummary("Alice");
        alice.addOrder(2, new BigDecimal("600.00"), new BigDecimal("60.00"));
        summaries.put("Alice", alice);

        OrderProcessor.SummaryResult summaryResult = new OrderProcessor.SummaryResult(
                summaries,
                new BigDecimal("600.00"),
                new BigDecimal("60.00"),
                new BigDecimal("540.00")
        );

        ReportFormatter formatter = new ReportFormatter();
        List<String> lines = formatter.format(summaryResult);

        assertFalse(lines.isEmpty());
        assertTrue(lines.get(0).contains("Customer"));
        assertTrue(lines.stream().anyMatch(line -> line.contains("GRAND TOTAL")));
        assertTrue(lines.stream().anyMatch(line -> line.contains("$540.00")));
    }

    @Test
    void showsMessageWhenNoOrders() {
        ReportFormatter formatter = new ReportFormatter();
        OrderProcessor.SummaryResult empty = new OrderProcessor.SummaryResult(
                Map.of(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        List<String> lines = formatter.format(empty);
        assertEquals(1, lines.size());
        assertEquals("No valid orders to summarize.", lines.get(0));
    }
}
