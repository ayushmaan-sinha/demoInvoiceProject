package com.demo.invoice.service;

import com.demo.invoice.model.CustomerSummary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportFormatter {
    private static final int NAME_WIDTH = 20;
    private static final String HEADER_FORMAT = "%-" + NAME_WIDTH + "s %10s %12s %14s %14s %14s";
    private static final String ROW_FORMAT = "%-" + NAME_WIDTH + "s %10d %12d %14s %14s %14s";

    public List<String> format(OrderProcessor.SummaryResult summaryResult) {
        Map<String, CustomerSummary> summaries = summaryResult.customerSummaries();
        if (summaries.isEmpty()) {
            return List.of("No valid orders to summarize.");
        }

        List<String> lines = new ArrayList<>();
        lines.add(String.format(HEADER_FORMAT, "Customer", "Orders", "Items", "Gross", "Discount", "Net"));
        lines.add("-".repeat(90));

        for (CustomerSummary summary : summaries.values()) {
            lines.add(String.format(
                    ROW_FORMAT,
                    summary.getCustomerName(),
                    summary.getOrderCount(),
                    summary.getTotalItems(),
                    money(summary.getGrossTotal()),
                    money(summary.getDiscountTotal()),
                    money(summary.getNetTotal())
            ));
        }

        lines.add("-".repeat(90));
        lines.add(String.format(
                ROW_FORMAT,
                "GRAND TOTAL",
                summaries.values().stream().mapToInt(CustomerSummary::getOrderCount).sum(),
                summaries.values().stream().mapToInt(CustomerSummary::getTotalItems).sum(),
                money(summaryResult.grossTotal()),
                money(summaryResult.discountTotal()),
                money(summaryResult.netTotal())
        ));

        return lines;
    }

    private String money(BigDecimal value) {
        return "$" + value.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }
}
