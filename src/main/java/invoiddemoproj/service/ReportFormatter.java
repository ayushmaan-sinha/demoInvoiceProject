package invoiddemoproj.service;

import invoiddemoproj.model.CustomerSummary;
import invoiddemoproj.model.InvoiceReport;

import java.text.DecimalFormat;
import java.util.List;

public class ReportFormatter {
    private static final String HEADER_FORMAT = "%-22s %8s %8s %14s %12s %12s";
    private static final String ROW_FORMAT = "%-22s %8d %8d %14s %12s %12s";
    private static final String SEPARATOR = "----------------------------------------------------------------------";
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");

    public String format(InvoiceReport report) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(HEADER_FORMAT, "Customer", "Orders", "Items", "Gross Total", "Discount", "Net Total"))
                .append(System.lineSeparator());
        sb.append(SEPARATOR).append(System.lineSeparator());

        List<CustomerSummary> summaries = report.getCustomerSummaries();
        if (summaries.isEmpty()) {
            sb.append("No valid orders found.").append(System.lineSeparator());
        } else {
            for (CustomerSummary summary : summaries) {
                sb.append(String.format(ROW_FORMAT,
                        summary.getCustomerName(),
                        summary.getOrderCount(),
                        summary.getTotalItems(),
                        formatMoney(summary.getGrossTotal()),
                        formatMoney(summary.getDiscountTotal()),
                        formatMoney(summary.getNetTotal())))
                        .append(System.lineSeparator());
            }
        }

        sb.append(SEPARATOR).append(System.lineSeparator());
        sb.append(String.format(ROW_FORMAT,
                "GRAND TOTAL",
                report.getTotalOrders(),
                report.getTotalItems(),
                formatMoney(report.getGrossTotal()),
                formatMoney(report.getDiscountTotal()),
                formatMoney(report.getNetTotal())))
                .append(System.lineSeparator());

        return sb.toString();
    }

    private String formatMoney(java.math.BigDecimal amount) {
        return currencyFormat.format(amount);
    }
}
