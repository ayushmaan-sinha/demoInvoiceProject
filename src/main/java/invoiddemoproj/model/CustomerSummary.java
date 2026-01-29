package invoiddemoproj.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CustomerSummary {
    private final String customerName;
    private int orderCount;
    private int totalItems;
    private BigDecimal grossTotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    private BigDecimal discountTotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    public CustomerSummary(String customerName) {
        this.customerName = customerName;
    }

    public void addOrder(OrderRecord order) {
        orderCount++;
        totalItems += order.getQuantity();
        grossTotal = grossTotal.add(order.lineTotal()).setScale(2, RoundingMode.HALF_UP);
        discountTotal = discountTotal.add(order.discount()).setScale(2, RoundingMode.HALF_UP);
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
        return grossTotal.subtract(discountTotal).setScale(2, RoundingMode.HALF_UP);
    }
}
