package com.demo.invoice;

import com.demo.invoice.model.CustomerSummary;
import com.demo.invoice.model.OrderRecord;
import com.demo.invoice.service.OrderProcessor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderProcessorTest {

    @Test
    void calculatesDiscountsAndTotals() {
        OrderRecord highValue = new OrderRecord("A1", "John", "Laptop", 1, new BigDecimal("600"), LocalDate.now());
        OrderRecord lowValue = new OrderRecord("A2", "John", "Mouse", 2, new BigDecimal("50"), LocalDate.now());
        OrderRecord otherCustomer = new OrderRecord("B1", "Alice", "Tablet", 3, new BigDecimal("200"), LocalDate.now());

        OrderProcessor processor = new OrderProcessor();
        OrderProcessor.SummaryResult result = processor.processOrders(List.of(highValue, lowValue, otherCustomer));

        Map<String, CustomerSummary> summaries = result.customerSummaries();
        assertEquals(2, summaries.size());

        CustomerSummary john = summaries.get("John");
        assertNotNull(john);
        assertEquals(2, john.getOrderCount());
        assertEquals(3, john.getTotalItems());
        assertEquals(new BigDecimal("700.00"), john.getGrossTotal());
        assertEquals(new BigDecimal("60.00"), john.getDiscountTotal());
        assertEquals(new BigDecimal("640.00"), john.getNetTotal());

        CustomerSummary alice = summaries.get("Alice");
        assertNotNull(alice);
        assertEquals(new BigDecimal("600.00"), alice.getGrossTotal());
        assertEquals(new BigDecimal("60.00"), alice.getDiscountTotal());
        assertEquals(new BigDecimal("540.00"), alice.getNetTotal());

        assertEquals(new BigDecimal("1300.00"), result.grossTotal());
        assertEquals(new BigDecimal("120.00"), result.discountTotal());
        assertEquals(new BigDecimal("1180.00"), result.netTotal());
    }
}
