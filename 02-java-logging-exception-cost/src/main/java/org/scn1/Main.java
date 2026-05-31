package org.scn1;

import org.scn1.exception.ResourceNotFoundException;
import org.scn1.exception.ResourceNotFoundLightException;
import org.scn1.res.TextFileResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // SCN0: pure baseline — no I/O, no exception
        BenchmarkResult scn0 = Benchmark.run("SCN0 - Pure baseline", () -> {
            String s = "hello".toUpperCase();
        });

        // SCN1: happy path — file exists, (no error)
        TextFileResource exists = new TextFileResource("test", "textfile.txt");
        BenchmarkResult scn1 = Benchmark.run("SCN1 - Happy path", () -> {
            try {
                exists.readText();
            } catch (Exception ignored) {}
        });

        // SCN2: complete exception with stack trace — Java default
        BenchmarkResult scn2 = Benchmark.run("SCN2 - Exception with stack trace", () -> {
            throw new ResourceNotFoundException("File not found");
        });

        // SCN3: exception (no stack trace) — writableStackTrace = false
        BenchmarkResult scn3 = Benchmark.run("SCN3 - Exception (no stack trace)", () -> {
            throw new ResourceNotFoundLightException("File not found");
        });

        TextFileResource notExists = new TextFileResource("test", "notfound.txt");

        // SCN4: worst case — exception with stack trace + logging
        BenchmarkResult scn4 = Benchmark.run("SCN4 - Exception + log", () -> {
            try {
                notExists.readText();
            } catch (Exception e) {
                log.error("Error reading file", e); // stack trace + I/O
            }
        });

        // SCN5: expected error — simple log (no exception)
        BenchmarkResult scn5 = Benchmark.run("SCN5 - Simple log (no exception)", () -> {
            try {
                notExists.readText();
            } catch (Exception e) {
                log.warn("File not found: {}", e.getMessage());
            }
        });

        // Final Report
        System.out.println("\n========== BENCHMARK RESULTS ==========");
        scn0.print();
        scn1.print();
        scn2.print();
        scn3.print();
        scn4.print();
        scn5.print();

    }
}
