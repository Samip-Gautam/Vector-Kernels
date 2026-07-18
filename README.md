# SIMD Kernel HTTP Gateway

A small Java HTTP service that exposes SIMD-accelerated vector math
(add, subtract, multiply, divide, dot product, relu, threshold, clamp)
over a JSON API, built on the JDK's built-in `com.sun.net.httpserver`
and the incubating Java Vector API (`jdk.incubator.vector`).

## What was fixed

Your assistant had left a stray, out-of-date copy of the compiled
`.class` files sitting directly inside `src/` (outside the real Maven
source root), which is why it looked like the `.java` sources had been
replaced. The real sources — under `src/main/java/Vectors/SIMDKernel`
— were intact the whole time. This package contains only that clean
source tree, plus a Maven `pom.xml` configured to build and run it.

## Requirements

- **JDK 21** (the Vector API is incubating through JDK 21; the compiler
  plugin already passes `--add-modules jdk.incubator.vector`)
- Maven (or just use IntelliJ's bundled Maven)

## Opening in IntelliJ

1. **File → Open** and select this folder (the one containing `pom.xml`).
2. Let IntelliJ import it as a Maven project (it will download `gson`
   automatically).
3. Open `Main.java` and click **Run**. A `Main` run configuration is
   already included under `.idea/runConfigurations/`, so the required
   `--add-modules jdk.incubator.vector` VM option is set for you — no
   manual setup needed.
4. Once running, you should see:
   ```
   SIMD Kernel HTTP Gateway started on port: 8080
   ```

## Running from the command line instead

```bash
mvn compile exec:exec
```

or build a runnable jar:

```bash
mvn package
java --add-modules jdk.incubator.vector -jar target/SIMDKernel.jar
```

## Trying the API

```bash
curl -X POST http://localhost:8080/api/hello \
  -H "Content-Type: application/json" \
  -d '{"operation":"ADD","arrayOne":[1,2,3],"arrayTwo":[4,5,6]}'
```

Expected response:
```json
{"operation":"ADD","success":true,"resultArray":[5,7,9],"resultValue":0}
```

See the accompanying **handbook PDF** for a full explanation of how the
networking layer works, mapped onto the OSI model.
