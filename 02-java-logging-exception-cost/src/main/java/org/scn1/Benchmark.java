package org.scn1;

public class Benchmark {

    private static final int WARMUP_ITERATIONS = 10_000;
    private static final int MEASURE_ITERATIONS = 100_000;

    public static BenchmarkResult run(String name, Runnable scenario) {

        // Phase 1: warmup — JIT compiling code, cache warmup
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            try {
                scenario.run();
            } catch (Exception ignored) {}
        }

        // Phase 2: measure
        long totalNanos = 0;
        int errors = 0;

        for (int i = 0; i < MEASURE_ITERATIONS; i++) {
            long start = System.nanoTime();
            try {
                scenario.run();
            } catch (Exception e) {
                errors++;
            } finally {
                totalNanos += System.nanoTime() - start;
            }
        }

        // Phase 3: result
        long avgNanos = totalNanos / MEASURE_ITERATIONS;
        return new BenchmarkResult(name, avgNanos, totalNanos, errors, MEASURE_ITERATIONS);
    }
}
