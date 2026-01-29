package invoiddemoproj.service;

import invoiddemoproj.model.InvoiceReport;
import invoiddemoproj.model.CustomerSummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceProcessorTest {

    @TempDir
    Path tempDir;

    @Test
    void processorAggregatesOrdersAndAppliesDiscounts() throws IOException {
        Path input = tempDir.resolve("orders.txt");
        List<String> lines = List.of(
                "ORD001|John Smith|Laptop|2|300.00|2024-03-15",
                "ORD002|John Smith|Mouse|1|25.00|2024-03-16",
                "ORD003|Alice|Phone|1|700.00|2024-03-15",
                "BAD|Alice|Phone|-1|700.00|2024-03-15"
        );
        Files.write(input, lines, StandardCharsets.UTF_8);

        InvoiceProcessor processor = new InvoiceProcessor();
        InvoiceReport report = processor.process(input);

        assertEquals(3, report.getTotalOrders());
        assertEquals(4, report.getTotalItems());
        assertEquals("1325.00", report.getGrossTotal().toPlainString());
        assertEquals("130.00", report.getDiscountTotal().toPlainString());
        assertEquals("1195.00", report.getNetTotal().toPlainString());
        assertEquals(1, report.getErrors().size());

        Optional<CustomerSummary> john = report.getCustomerSummaries().stream()
                .filter(c -> c.getCustomerName().equals("John Smith")).findFirst();
        assertTrue(john.isPresent());
        assertEquals(2, john.get().getOrderCount());
        assertEquals(3, john.get().getTotalItems());
        assertEquals("625.00", john.get().getGrossTotal().toPlainString());
        assertEquals("60.00", john.get().getDiscountTotal().toPlainString());
    }
}
