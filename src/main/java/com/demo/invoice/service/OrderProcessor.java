package com.demo.invoice.service;

import com.demo.invoice.model.CustomerSummary;
import com.demo.invoice.model.OrderRecord;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrderProcessor {
    private static final BigDecimal DISCOUNT_THRESHOLD = new BigDecimal("500.00");
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10");

    public SummaryResult processOrders(List<OrderRecord> orders) {
        Map<String, CustomerSummary> summaries = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        BigDecimal grossTotal = BigDecimal.ZERO;
        BigDecimal discountTotal = BigDecimal.ZERO;

        for (OrderRecord order : orders) {
            BigDecimal lineTotal = order.getLineTotal().setScale(2, RoundingMode.HALF_UP);
            BigDecimal discount = lineTotal.compareTo(DISCOUNT_THRESHOLD) > 0
                    ? lineTotal.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            CustomerSummary summary = summaries.computeIfAbsent(order.getCustomerName(), CustomerSummary::new);
            summary.addOrder(order.getQuantity(), lineTotal, discount);

            grossTotal = grossTotal.add(lineTotal);
            discountTotal = discountTotal.add(discount);
        }

        BigDecimal netTotal = grossTotal.subtract(discountTotal);
        return new SummaryResult(summaries, grossTotal, discountTotal, netTotal);
    }

    public record SummaryResult(Map<String, CustomerSummary> customerSummaries,
                                 BigDecimal grossTotal,
                                 BigDecimal discountTotal,
                                 BigDecimal netTotal) {
    }
}
