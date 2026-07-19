# Vector-Kernels

**A SIMD-accelerated compute kernel exposed as a raw HTTP service — no web framework, no ORM, no magic.**

Vector-Kernels is a small systems project built to answer one question honestly: *what actually happens between a JSON request landing on a socket and a CPU executing a vectorized instruction?* Every layer here — HTTP parsing, request dispatch, and the math itself — is written against the JDK standard library and the incubating Vector API, with nothing hidden behind a framework.

---

## Why this exists

Most "build an API" tutorials start and end at Spring Boot or Express — you get routing, serialization, and business logic, but the actual wire protocol and the machine underneath stay invisible. This project deliberately strips that away. The goal wasn't to ship something production-ready; it was to build every layer by hand once, so the abstractions frameworks normally hide stop being a black box.

Two ideas collided to produce this:

1. **The OSI model, made physical.** Instead of treating the transport and application layers as diagram boxes from a networking course, this project implements them — a raw socket server (`com.sun.net.httpserver`) parsing HTTP by hand, sitting directly on top of TCP.
2. **The Java Vector API.** Rather than trusting the JIT to auto-vectorize a loop and hoping for the best, `jdk.incubator.vector` lets you *explicitly* pack data into SIMD lanes and issue vector instructions — the same category of hardware acceleration BLAS and NumPy lean on, just written by hand in Java.

## What it actually does

At its core, the service accepts a JSON payload describing a vector operation over HTTP, dispatches it to a SIMD kernel, and returns the result — with the entire numeric computation happening in CPU vector registers rather than a scalar loop.

**Supported operations:**

| Category | Operations |
|---|---|
| Elementwise arithmetic | addition, subtraction, multiplication, division |
| Linear algebra | dot product |
| ML primitives | ReLU, threshold, clamp |

These aren't arbitrary — dot product is the core operation behind matrix multiplication and embedding similarity, and ReLU/threshold/clamp are exactly the elementwise ops that dominate neural network inference. This is, in miniature, the same kind of kernel work that libraries like BLAS or SimSIMD do at scale.

## Architecture

```
 Client (curl / browser / app)
        │  HTTP + JSON
        ▼
 ┌─────────────────────────┐
 │   HTTP Layer              │  com.sun.net.httpserver — raw request/response
 │   (no Spring, no Netty)   │  handling, manual routing
 └────────────┬─────────────┘
              │ parsed operation + operands
              ▼
 ┌─────────────────────────┐
 │   Dispatcher               │  maps operation name → kernel implementation
 └────────────┬─────────────┘
              │
              ▼
 ┌─────────────────────────┐
 │   SIMD Kernel Layer        │  jdk.incubator.vector
 │   (Vectors/SIMDKernel)     │  packs data into vector lanes, executes
 └────────────┬─────────────┘   vectorized instructions on CPU registers
              │
              ▼
      JSON response
```

The three layers are kept deliberately separate: the HTTP layer knows nothing about vector math, and the kernel layer knows nothing about HTTP. That separation is what let each piece be understood — and rebuilt from scratch, if needed — independently.

## Why SIMD, and why it matters here

A normal loop processes one number per CPU instruction. SIMD (Single Instruction, Multiple Data) lets a single instruction operate on a whole *lane* of numbers at once — 4, 8, or more `float`s packed side by side in a register, processed in one cycle instead of several. The Java Vector API exposes this directly instead of hoping the JIT compiler auto-vectorizes your scalar code, which it often won't for anything beyond trivial loops.

Concretely: computing a dot product of two 1024-element vectors the naive way is 1024 multiply-and-accumulate instructions. With SIMD lanes of width 8, it's ~128 — the CPU does 8x the arithmetic per instruction it issues.

## Tech stack

- **Java 21** — LTS release with `jdk.incubator.vector` available
- **Java Vector API** (`jdk.incubator.vector`) — explicit SIMD vectorization
- **`com.sun.net.httpserver`** — HTTP server from the JDK standard library, no external web framework
- **Maven** — build and dependency management
- **Cloudflare Tunnel** — exposes the local server publicly through a custom domain without opening ports or managing TLS certificates directly

## Running it locally

```bash
git clone https://github.com/Samip-Gautam/Vector-Kernels.git
cd Vector-Kernels
mvn clean package
java --add-modules jdk.incubator.vector -jar target/vector-kernels.jar
```

> The `--add-modules jdk.incubator.vector` flag is required — the Vector API is still incubating in Java 21 and isn't exposed by default.

### Example request

```bash
curl -X POST http://localhost:8080/vector/dot \
  -H "Content-Type: application/json" \
  -d '{"a": [1.0, 2.0, 3.0, 4.0], "b": [5.0, 6.0, 7.0, 8.0]}'
```

## Exposing it publicly

The service binds locally and is tunneled out via [Cloudflare Tunnel](https://developers.cloudflare.com/cloudflare-one/connections/connect-networks/), which routes traffic from a real domain to the local port without exposing the machine's IP or manually configuring a reverse proxy and certificates.

## What this project is / isn't

**Is:** a from-scratch exploration of how HTTP, TCP, and SIMD vectorization actually work underneath the frameworks that normally abstract them away.

**Isn't:** a production-hardened API. There's no auth, no rate limiting, no input validation hardening, and no load testing — those are natural next steps, not oversights being hidden.
