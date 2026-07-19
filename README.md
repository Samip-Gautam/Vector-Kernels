# SIMD Kernel HTTP Gateway

A lightweight Java compute service that exposes SIMD-accelerated vector operations over an HTTP JSON API using the Java Vector API (`jdk.incubator.vector`).

## Features

- SIMD-accelerated mathematical computation using the Java Vector API
- HTTP JSON API built without external web frameworks
- Modular architecture separating HTTP, dispatching, and computation
- Built with Java 21, Maven, and the Java Standard Library
- Supports vector addition, subtraction, multiplication, division, dot product, ReLU, threshold, and clamp operations
- Designed as a systems programming project to explore vectorization, networking, and software architecture
- Can be exposed publicly using Cloudflare Tunnel for remote API access

## Tech Stack

- Java 21
- Java Vector API (`jdk.incubator.vector`)
- `com.sun.net.httpserver`
- Maven

## Purpose

This project explores how SIMD-accelerated computation can be exposed as a lightweight network service while maintaining a clean separation between the networking layer and the computation engine.