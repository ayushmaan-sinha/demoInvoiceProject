package invoiddemoproj.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class OrderRecord {
    private static final BigDecimal DISCOUNT_THRESHOLD = new BigDecimal("500.00");
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10");

    private final String orderId;
    private final String customerName;
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final LocalDate orderDate;

    public OrderRecord(String orderId, String customerName, String productName,
                       int quantity, BigDecimal unitPrice, LocalDate orderDate) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
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

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal discount() {
        BigDecimal total = lineTotal();
        if (total.compareTo(DISCOUNT_THRESHOLD) > 0) {
            return total.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal netTotal() {
        return lineTotal().subtract(discount()).setScale(2, RoundingMode.HALF_UP);
    }
}
