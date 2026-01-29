package invoiddemoproj;

import invoiddemoproj.model.InvoiceReport;
import invoiddemoproj.service.InvoiceProcessor;
import invoiddemoproj.service.ReportFormatter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Path inputPath = args.length > 0 ? Path.of(args[0]) : Path.of("sample-data/orders.txt");
        Path outputPath = args.length > 1 ? Path.of(args[1]) : Path.of("sample-data/summary_report.txt");
        Path errorPath = args.length > 2 ? Path.of(args[2]) : Path.of("sample-data/error_log.txt");

        InvoiceProcessor processor = new InvoiceProcessor();
        ReportFormatter formatter = new ReportFormatter();

        try {
            InvoiceReport report = processor.process(inputPath);
            writeString(outputPath, formatter.format(report));
            writeErrors(errorPath, report.getErrors());
            System.out.printf("Processed %d valid orders from %s%n", report.getTotalOrders(), inputPath);
            System.out.printf("Summary written to %s, errors to %s%n", outputPath, errorPath);
        } catch (IOException e) {
            System.err.println("Failed to process orders: " + e.getMessage());
        }
    }

    private static void writeString(Path path, String content) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.writeString(path, content, StandardCharsets.UTF_8);
    }

    private static void writeErrors(Path path, List<String> errors) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        if (errors.isEmpty()) {
            Files.writeString(path, "No errors." + System.lineSeparator(), StandardCharsets.UTF_8);
        } else {
            Files.write(path, errors, StandardCharsets.UTF_8);
        }
    }
}
