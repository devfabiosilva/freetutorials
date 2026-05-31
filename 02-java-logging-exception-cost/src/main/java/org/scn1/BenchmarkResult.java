package org.scn1;

public class BenchmarkResult {
    private final String name;
    private final long avgNanos;
    private final long totalNanos;
    private final int errors;
    private final int iterations;

    public BenchmarkResult(String name, long avgNanos, long totalNanos, int errors, int iterations) {
        this.name = name;
        this.avgNanos = avgNanos;
        this.totalNanos = totalNanos;
        this.errors = errors;
        this.iterations = iterations;
    }

    public void print() {
        System.out.println("\n=== " + name + " ===");
        System.out.println("Iterations  : " + iterations);
        System.out.println("Errors      : " + errors);
        System.out.println("Avg (ns)    : " + avgNanos);
        System.out.println("Avg (µs)    : " + avgNanos / 1_000);
        System.out.println("Total (ms)  : " + totalNanos / 1_000_000);
    }
}