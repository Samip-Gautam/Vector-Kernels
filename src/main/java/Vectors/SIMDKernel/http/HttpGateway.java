package Vectors.SIMDKernel.http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class HttpGateway {
    private HttpGateway() {
    }
    static void main(String[] args) {

    }

    public static void start(int i) throws IOException {
        HttpServer server = HttpServer.create(
                            new InetSocketAddress(i),        0
        );
        Routes.register(server);
        server.start();
        System.out.println("SIMD Kernel HTTP Gateway started on port: " + server.getAddress().getPort());

    }
}
