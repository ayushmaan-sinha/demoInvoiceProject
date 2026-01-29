package com.demo.invoice.model;

import java.math.BigDecimal;

public class CustomerSummary {
    private final String customerName;
    private int orderCount;
    private int totalItems;
    private BigDecimal grossTotal = BigDecimal.ZERO;
    private BigDecimal discountTotal = BigDecimal.ZERO;

    public CustomerSummary(String customerName) {
        this.customerName = customerName;
    }

    public void addOrder(int quantity, BigDecimal lineTotal, BigDecimal discount) {
        orderCount++;
        totalItems += quantity;
        grossTotal = grossTotal.add(lineTotal);
        discountTotal = discountTotal.add(discount);
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getOrderCount() {
        return orderCount;
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
        return grossTotal.subtract(discountTotal);
    }
}
