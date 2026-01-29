package invoiddemoproj.service;

import invoiddemoproj.model.OrderRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderParserTest {

    private final OrderParser parser = new OrderParser();

    @Test
    void parseValidLineReturnsOrder() {
        String line = "ORD001|John Smith|Laptop|2|999.99|2024-03-15";
        OrderParser.ParseOutcome outcome = parser.parseLine(line, 1);

        assertTrue(outcome.isSuccess());
        OrderRecord order = outcome.getOrder();
        assertEquals("ORD001", order.getOrderId());
        assertEquals("John Smith", order.getCustomerName());
        assertEquals(2, order.getQuantity());
        assertEquals("999.99", order.getUnitPrice().toPlainString());
        assertEquals(LocalDate.of(2024, 3, 15), order.getOrderDate());
    }

    @Test
    void parseNegativeQuantityReturnsError() {
        String line = "ORD002|Jane Doe|Mouse|-5|25.00|2024-03-16";
        OrderParser.ParseOutcome outcome = parser.parseLine(line, 2);

        assertFalse(outcome.isSuccess());
        assertTrue(outcome.getErrorMessage().contains("quantity"));
    }

    @Test
    void parseInvalidDateReturnsError() {
        String line = "ORD003|Jane Doe|Keyboard|1|45.00|03-20-2024";
        OrderParser.ParseOutcome outcome = parser.parseLine(line, 3);

        assertFalse(outcome.isSuccess());
        assertTrue(outcome.getErrorMessage().contains("invalid date"));
    }
}
