package Vectors.SIMDKernel.http;

public class Routes {
    private Routes() {
    }
    public static void register(com.sun.net.httpserver.HttpServer server) {
        server.createContext("/api/hello", new VectorHandler());
    }
}
