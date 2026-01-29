package invoiddemoproj.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

public class InvoiceReport {
    private final List<CustomerSummary> customerSummaries;
    private final int totalOrders;
    private final int totalItems;
    private final BigDecimal grossTotal;
    private final BigDecimal discountTotal;
    private final BigDecimal netTotal;
    private final List<String> errors;

    public InvoiceReport(List<CustomerSummary> customerSummaries,
                         int totalOrders,
                         int totalItems,
                         BigDecimal grossTotal,
                         BigDecimal discountTotal,
                         List<String> errors) {
        this.customerSummaries = customerSummaries;
        this.totalOrders = totalOrders;
        this.totalItems = totalItems;
        this.grossTotal = grossTotal.setScale(2, RoundingMode.HALF_UP);
        this.discountTotal = discountTotal.setScale(2, RoundingMode.HALF_UP);
        this.netTotal = this.grossTotal.subtract(this.discountTotal).setScale(2, RoundingMode.HALF_UP);
        this.errors = errors;
    }

    public List<CustomerSummary> getCustomerSummaries() {
        return customerSummaries;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public BigDecimal getGrossTotal() {
        return grossTotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public BigDecimal getNetTotal() {
        return netTotal;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
