# Vector-Kernels

A small Java project that exposes SIMD-accelerated math over HTTP. No Spring, no frameworks doing the heavy lifting. Just the JDK standard library and the Vector API.

## Why I built this

I wanted to actually understand what happens between a request hitting a socket and a CPU running a vectorized instruction, instead of trusting a framework to handle it for me. So I skipped Spring Boot and wrote the HTTP layer by hand using `com.sun.net.httpserver`, and used Java's Vector API (`jdk.incubator.vector`) to do the math directly on CPU registers instead of relying on the JIT compiler to maybe auto-vectorize a loop.

The idea started when I connected two things I'd learned separately. One was the OSI model from my networking class, which had always felt like a diagram I memorized for exams. The other was that I'd just finished a concurrency project and wanted to try something with the Vector API next. I figured the best way to actually understand the OSI layers was to build them, not just draw them. So this project is a raw TCP based HTTP server sitting on top of a SIMD compute engine.

## What it does

You send a JSON request describing a vector operation. The server parses it, hands it to the right kernel, and the kernel does the actual math using SIMD lanes instead of a normal scalar loop.

Operations it supports right now:

- Addition, subtraction, multiplication, division (elementwise)
- Dot product
- ReLU, threshold, clamp

I picked these on purpose. Dot product shows up everywhere in linear algebra and embeddings. ReLU, threshold, and clamp are the exact elementwise operations that show up constantly in neural network inference. So this is a tiny version of the same kind of kernel work that libraries like BLAS or SimSIMD do at a much bigger scale.

## How it's structured

There are three layers and they don't know about each other.

1. HTTP layer. Reads the request, writes the response. Doesn't know anything about vector math.
2. Dispatcher. Looks at the operation name in the request and routes it to the right kernel function.
3. SIMD kernel layer. Takes the numbers, packs them into vector lanes, runs the actual vectorized instructions, and returns the result.

Keeping these separate was the whole point. It meant I could learn and rebuild each piece on its own instead of dealing with one tangled file.

```
client sends HTTP request with JSON
        |
HTTP layer parses it (no framework, just the JDK)
        |
dispatcher picks the right kernel by operation name
        |
SIMD kernel layer runs the vectorized math
        |
JSON response goes back to the client
```

## Why SIMD actually matters here

A regular loop processes one number per instruction. SIMD lets one instruction work on several numbers at once, packed side by side in a CPU register. So if you're computing a dot product over 1024 numbers the normal way, that's 1024 multiply and add steps. With SIMD lanes of width 8, it's closer to 128. Same math, far fewer instructions.

The Vector API in Java lets you do this explicitly instead of hoping the compiler figures it out for you, which it usually doesn't for anything beyond trivial loops.

## Tech stack

- Java 21
- Java Vector API (`jdk.incubator.vector`), still incubating as of this JDK version
- `com.sun.net.httpserver` for the HTTP layer
- Maven for building
- Cloudflare Tunnel to expose it publicly through my own domain without opening ports myself

## Running it locally

```bash
git clone https://github.com/Samip-Gautam/Vector-Kernels.git
cd Vector-Kernels
mvn clean package
java --add-modules jdk.incubator.vector -jar target/vector-kernels.jar
```

You need the `--add-modules jdk.incubator.vector` flag. The Vector API isn't exposed by default on Java 21 since it's still incubating.

### Example request

```bash
curl -X POST http://localhost:8080/vector/dot \
  -H "Content-Type: application/json" \
  -d '{"a": [1.0, 2.0, 3.0, 4.0], "b": [5.0, 6.0, 7.0, 8.0]}'
```

Double check this route and payload shape against your actual dispatcher code before pushing. I filled this in as an example, not a verified endpoint, so make sure it actually runs before anyone tries it.

## Making it public

The server just runs locally on a port. I used Cloudflare Tunnel to route a real domain to that port without exposing my machine's IP or setting up a reverse proxy and certificates myself.

## What this is and isn't

This is a learning project built to understand HTTP, TCP, and SIMD vectorization from the ground up, not to hide behind frameworks that normally do this for you.

It is not production ready. There's no auth, no rate limiting, no real input validation, and no load testing yet. Those aren't things I forgot. They're just not done.

## What's next

- Proper request validation and error responses
- Benchmark of scalar math against SIMD math at different vector sizes
- Basic auth before leaving the tunnel open long term
- More operations, for example matrix multiplication

## License
MIT licensed. Do whatever you want with it.