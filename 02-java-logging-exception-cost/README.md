# java-logging-exception-cost

> **How much does a stack trace really cost? Benchmarking Java exception overhead in hot paths.**

A real-world benchmark demonstrating the hidden performance cost of exceptions, stack traces, and logging in Java JVM hot paths — with measurable evidence using `System.nanoTime()` and `perf stat` across 6 scenarios.

📄 **Article (English):** *(coming soon)*
📄 **Artigo (Português):** *(em breve)*
👤 **Author:** [Fábio Silva](https://github.com/devfabiosilva) | [LinkedIn](https://www.linkedin.com/in/devfabiosilva/)

---

## Motivation

Most Java codebases treat every error as an exception — including expected conditions like "file not found", "resource unavailable", or "invalid input". This is a common pattern, but it carries an invisible cost that only appears under load.

This benchmark answers a simple question:

> *"How much does a stack trace really cost when thrown 100,000 times in a hot path?"*

The results are surprising — even for experienced developers.

---

## The Core Insight

```java
// This looks harmless...
throw new ResourceNotFoundException("File not found");

// But the JVM is silently doing this:
// → walking the entire call stack
// → capturing class, method, file, and line for every frame
// → storing it in a StackTraceElement[] array
// → every single time, regardless of whether you log it
```

And if you log it:

```java
log.error("Error reading file", e); // serializes stack trace to String + file I/O
```

You pay twice — once to capture, once to serialize.

---

## Scenarios

| Scenario | Description |
|----------|-------------|
| **SCN0** | Pure baseline — `"hello".toUpperCase()`, no I/O, no exception |
| **SCN1** | Happy path — reads an existing file with `Files.readString` |
| **SCN2** | Exception with full stack trace — default Java behavior |
| **SCN3** | Exception without stack trace — `writableStackTrace = false` |
| **SCN4** | Worst case — exception + `log.error` with full stack trace to file |
| **SCN5** | Expected condition — exception + `log.warn` with message only |

---

## Key Implementation

**Lightweight exception — `writableStackTrace = false`:**

```java
public class ResourceNotFoundLightException extends RuntimeException {
    public ResourceNotFoundLightException(String message) {
        super(
            message,
            null,   // cause
            true,   // enableSuppression
            false   // writableStackTrace — JVM does NOT walk the call stack
        );
    }
}
```

**Benchmark structure — warmup + measurement:**

```java
private static final int WARMUP_ITERATIONS   = 10_000;
private static final int MEASURE_ITERATIONS  = 100_000;

// Phase 1: warmup — JIT compiles, cache warms up
for (int i = 0; i < WARMUP_ITERATIONS; i++) { ... }

// Phase 2: measurement
long start = System.nanoTime();
// scenario runs here
totalNanos += System.nanoTime() - start;
```

---

## Results

**Test environment:**

| Property | Value |
|----------|-------|
| CPU | 4x Intel Core i5-6200U @ 2.30 GHz |
| RAM | 7.6 GB |
| OS | Linux Mint 21.1 (Kernel 5.15.0-179-generic 64-bit) |
| Java | OpenJDK 17 |
| Logging | Logback 1.4.14 with `immediateFlush=true` (file appender) |
| Iterations | 100,000 (after 10,000 warmup) |
| Run mode | `java -jar` outside IDE — no instrumentation agents |

**Benchmark results:**

| Scenario | Avg (ns) | Avg (µs) | Total (ms) | vs Baseline |
|----------|----------|----------|------------|-------------|
| SCN0 — Pure baseline | 209 | 0 | 20 | 1x |
| SCN3 — Exception without stack trace | 252 | 0 | 25 | **1.2x** ✅ |
| SCN2 — Exception with stack trace | 1,329 | 1 | 132 | **6.4x** |
| SCN1 — Happy path with I/O | 8,850 | 8 | 885 | **42x** |
| SCN5 — Expected condition (message only) | 23,467 | 23 | 2,346 | **112x** |
| SCN4 — Exception + full log with stack trace | 46,292 | 46 | 4,629 | **221x** ❌ |

---

> *"With `immediateFlush=true`, each `log.error` with stack trace forces a synchronous flush to disk —
> 100,000 flushes generate many context switches, repeatedly yielding the CPU to the kernel
> to write data that nobody will ever read."*

---

## What the Numbers Reveal

**1. Stack trace capture costs 6.4x — even if you never log it**

```
SCN2 (with stack trace):    1,329 ns
SCN3 (without stack trace):   252 ns
──────────────────────────────────────
Cost of capture alone:      1,077 ns  →  6.4x baseline
```

The JVM walks the call stack at `throw` time — not at log time. You pay this cost regardless of whether you ever read the stack trace.

**2. Logging the stack trace costs 221x the baseline**

```
SCN4 (exception + log.error with stack trace):  46,292 ns  →  221x
SCN5 (exception + log.warn message only):       23,467 ns  →  112x
──────────────────────────────────────────────────────────────────
Cost of serializing stack trace to file:        22,825 ns  →    2x on top of SCN5
```

`log.error("msg", e)` serializes the full stack trace to a String and flushes it to disk — 100,000 times in a hot path, that is **4,629 ms** of pure overhead.

**3. You don't need to log the stack trace to pay for it**

```
SCN5 (log.warn with e.getMessage() only):  23,467 ns  →  112x baseline
```

Even though SCN5 only logs the message — not the stack trace — the `NoSuchFileException` was already created with a full stack trace by `Files.readString`. The capture cost was paid before your `catch` block even ran.

> *"You don't need to log the stack trace to pay the cost of it."*

**4. Exception without stack trace is virtually free**

```
SCN3 (ResourceNotFoundLightException):  252 ns  →  1.2x baseline
SCN0 (pure baseline):                   209 ns  →  1x
```

A lightweight exception with `writableStackTrace = false` costs almost nothing after JIT optimization. For expected business conditions, this is the correct approach.

---

## The Key Distinction

```
Bug / unexpected system error     → stack trace is justified
                                  → you need to know where it happened

Expected business condition       → stack trace is waste
  (file not found, invalid input, → you already know what happened
   resource unavailable)          → log the condition, not the trace
```

> *"A missing file is not a bug — it is an expected condition.*
> *Treating an expected condition as an exception means paying the price of a bug without having one."*

---

## How to Run

```bash
git clone https://github.com/devfabiosilva/freetutorials.git
cd freetutorials/java-logging-exception-cost
mvn package
mv target/java-exception-cost-scenario1-1.0-SNAPSHOT.jar $(pwd) -v
java -Djava.library.path=. -cp java-exception-cost-scenario1-1.0-SNAPSHOT.jar org.scn1.Main
```

Requirements:
- Java 17+
- Maven 3.8+

---

## Project Structure

```
java-exception-cost/
├── src/main/java/org/scn1/
│   ├── Main.java                               # Benchmark runner
│   ├── Benchmark.java                          # Warmup + measurement engine
│   ├── BenchmarkResult.java                    # Result printer
│   ├── exception/
│   │   ├── ResourceNotFoundException.java      # Standard exception (with stack trace)
│   │   └── ResourceNotFoundLightException.java # Lightweight exception (no stack trace)
│   ├── res/
│   │   └── TextFileResource.java               # File resource abstraction
│   └── filereader/
│       └── TextFileReader.java                 # File reader
├── src/main/resources/
│   └── logback.xml                             # Logback with immediateFlush=true
├── test/
│   └── textfile.txt                            # Sample file for happy path
├── logs/                                       # Log output directory
└── pom.xml
```

---

## License

MIT

---

*Benchmarks were run on a real machine under normal desktop load, outside any IDE.*
*Results may vary depending on hardware, JVM version, and system state.*
*Run multiple times to observe variance — especially SCN4 under system load.*

