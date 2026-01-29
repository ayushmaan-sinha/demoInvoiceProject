package com.demo.invoice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderRecord {
    private final String orderId;
    private final String customerName;
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final LocalDate orderDate;

    public OrderRecord(String orderId,
                       String customerName,
                       String productName,
                       int quantity,
                       BigDecimal unitPrice,
                       LocalDate orderDate) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.orderDate = orderDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
