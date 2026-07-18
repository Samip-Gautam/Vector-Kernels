package Vectors.SIMDKernel.http;

import Vectors.SIMDKernel.dispatcher.KernelDispatcher;
import Vectors.SIMDKernel.protocol.Request;
import Vectors.SIMDKernel.protocol.Response;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class VectorHandler implements HttpHandler {

    private static final Gson gson = new Gson();

    private final KernelDispatcher dispatcher = new KernelDispatcher();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(
                    exchange,
                    405,
                    gson.toJson(Map.of(
                            "success", false,
                            "error", "Only POST requests are supported."
                    ))
            );
            return;
        }

        try {

            String body = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            Request request = gson.fromJson(body, Request.class);

            if (request == null)
                throw new IllegalArgumentException("Empty request.");

            int[] arrayOne = request.getArrayOne();
            int[] arrayTwo = request.getArrayTwo();

            if (arrayOne == null)
                throw new IllegalArgumentException("arrayOne is required.");

            Response response =
                    dispatcher.dispatch(request, arrayOne, arrayTwo);

            sendResponse(
                    exchange,
                    200,
                    gson.toJson(response)
            );

        }
        catch (IllegalArgumentException e) {

            sendResponse(
                    exchange,
                    400,
                    gson.toJson(Map.of(
                            "success", false,
                            "error", e.getMessage()
                    ))
            );

        }
        catch (Exception e) {

            sendResponse(
                    exchange,
                    500,
                    gson.toJson(Map.of(
                            "success", false,
                            "error", e.getMessage()
                    ))
            );

        }
    }

    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String json
    ) throws IOException {

        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json"
        );

        exchange.sendResponseHeaders(statusCode, bytes.length);

        exchange.getResponseBody().write(bytes);

        exchange.close();
    }
}