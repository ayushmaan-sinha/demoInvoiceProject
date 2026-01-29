# Invoice Summary Generator (Assignment 2)

Java 17 CLI app that reads pipe-delimited order records, skips malformed rows with an error log, applies a 10% discount on line totals over $500, aggregates by customer, and writes a formatted invoice summary with grand totals.

## Project Layout
- `src/main/java/com/demo/invoice/App.java` – CLI entry point.
- `src/main/java/com/demo/invoice/service` – parsing, processing, and reporting services.
- `src/main/java/com/demo/invoice/model` – data classes for orders and per-customer summaries.
- `src/main/resources/sample_orders.txt` – sample input (15 orders, 5 customers).
- `sample_output.txt` / `sample_errors.log` – sample output and error log produced from the sample input.
- `src/test/java/com/demo/invoice` – JUnit 5 tests for parsing, processing, and formatting.

## Build & Test
Requirements: Java 17, Maven.

```bash
# run tests
mvn test

# build the jar
mvn package
```

If you prefer to keep Maven artifacts inside the project directory, run with `-Dmaven.repo.local=.m2/repository`.

## Running the App
The application expects three arguments: `<inputFile> <outputFile> <errorFile>`.

```bash
# using the compiled classes after `mvn package`
java -cp target/classes com.demo.invoice.App src/main/resources/sample_orders.txt output/report.txt output/errors.log

# or using the jar
java -jar target/demo-1.0-SNAPSHOT.jar src/main/resources/sample_orders.txt output/report.txt output/errors.log
```

After running, check the output and error files you specified. A message is printed with their absolute locations.

## Input Format
Pipe-delimited records:
```
OrderID|CustomerName|ProductName|Quantity|UnitPrice|OrderDate
```
Example: `ORD001|John Smith|Laptop|2|999.99|2024-03-15`

Rules & validation:
- `Quantity` must be a positive integer.
- `UnitPrice` must be a non-negative decimal.
- `OrderDate` must be ISO-8601 (`yyyy-MM-dd`).
- Empty lines or records with missing fields are treated as malformed and logged.

## Business Rules
- Line total = `Quantity × UnitPrice`.
- 10% discount applied to any line total strictly greater than $500.
- Per-customer summary shows: customer name, number of orders, total items, gross total, discount amount, net total.
- A grand total row sums orders, items, gross, discount, and net across all customers.

## Sample Output
Generated from `src/main/resources/sample_orders.txt`:
```
Customer                 Orders        Items          Gross       Discount            Net
------------------------------------------------------------------------------------------
Alice Brown                   3            3        $989.98         $55.00        $934.98
Carlos Diaz                   3           11       $1389.95         $64.00       $1325.95
John Smith                    3            6       $2196.48        $200.00       $1996.48
Mei Chen                      3            5        $898.96         $79.90        $819.06
Priya Patel                   3            7       $1559.97        $135.00       $1424.97
------------------------------------------------------------------------------------------
GRAND TOTAL                  15           32       $7035.34        $533.90       $6501.44
```
`sample_errors.log` will read `No malformed records found.` for the provided sample.

## Edge Cases & Assumptions
- Empty input file → output contains `No valid orders to summarize.` and error log still writes (no malformed records).
- Malformed rows (bad field count, non-numeric quantity/price, negative quantity, negative price, invalid date, or missing required fields) are skipped and appended to the error file with the line number and reason.
- Discounts are calculated per line item (not per customer total) and rounded to two decimals using HALF_UP.
- Currency values are rendered with a leading `$` and two decimal places.
