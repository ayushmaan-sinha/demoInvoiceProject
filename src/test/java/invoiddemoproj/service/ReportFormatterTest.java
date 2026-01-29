package invoiddemoproj.service;

import invoiddemoproj.model.CustomerSummary;
import invoiddemoproj.model.InvoiceReport;
import invoiddemoproj.model.OrderRecord;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportFormatterTest {

    @Test
    void formatterBuildsAlignedReportWithGrandTotal() {
        CustomerSummary john = new CustomerSummary("John");
        john.addOrder(new OrderRecord("1", "John", "Laptop", 2, new BigDecimal("300.00"), LocalDate.of(2024, 3, 15)));
        john.addOrder(new OrderRecord("2", "John", "Mouse", 1, new BigDecimal("25.00"), LocalDate.of(2024, 3, 16)));

        InvoiceReport report = new InvoiceReport(
                List.of(john),
                2,
                3,
                new BigDecimal("625.00"),
                new BigDecimal("60.00"),
                List.of()
        );

        ReportFormatter formatter = new ReportFormatter();
        String output = formatter.format(report);

        assertTrue(output.contains("Customer"));
        assertTrue(output.contains("John"));
        assertTrue(output.contains("GRAND TOTAL"));
        assertTrue(output.contains("625.00"));
        assertTrue(output.contains("565.00")); // net total
    }
}
