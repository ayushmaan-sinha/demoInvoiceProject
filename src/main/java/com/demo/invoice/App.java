package com.demo.invoice;

import com.demo.invoice.model.OrderRecord;
import com.demo.invoice.service.OrderParser;
import com.demo.invoice.service.OrderProcessor;
import com.demo.invoice.service.ReportFormatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class App {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: java -jar demo.jar <inputFile> <outputFile> <errorFile>");
            System.exit(1);
        }

        Path inputPath = Path.of(args[0]);
        Path outputPath = Path.of(args[1]);
        Path errorPath = Path.of(args[2]);

        OrderParser parser = new OrderParser();
        List<OrderRecord> orders;
        try {
            orders = parser.parseFile(inputPath, errorPath);
        } catch (IOException | IllegalArgumentException ex) {
            System.err.println("Failed to read input file: " + ex.getMessage());
            System.exit(1);
            return;
        }

        OrderProcessor processor = new OrderProcessor();
        OrderProcessor.SummaryResult summaryResult = processor.processOrders(orders);

        ReportFormatter formatter = new ReportFormatter();
        List<String> reportLines = formatter.format(summaryResult);

        try {
            Path parent = outputPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(outputPath, reportLines);
            System.out.println("Report written to: " + outputPath.toAbsolutePath());
            System.out.println("Error log written to: " + errorPath.toAbsolutePath());
        } catch (IOException ex) {
            System.err.println("Failed to write output: " + ex.getMessage());
            System.exit(1);
        }
    }
}
