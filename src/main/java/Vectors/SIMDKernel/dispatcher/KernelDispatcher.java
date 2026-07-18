package Vectors.SIMDKernel.dispatcher;

import Vectors.SIMDKernel.karnel.VectorMath;
import Vectors.SIMDKernel.karnel.VectorMathImpl;
import Vectors.SIMDKernel.protocol.Request;
import Vectors.SIMDKernel.protocol.Response;

public class KernelDispatcher {
    private final VectorMath math = new VectorMathImpl();
    public Response dispatch( Request request, int[] arrayOne, int[] arrayTwo) {
        return switch (request.getOperation()) {
            case ADD -> new Response(request.getOperation(), true, math.add(arrayOne, arrayTwo), 0);
            case SUBTRACT -> new Response(request.getOperation(), true, math.subtract(arrayOne, arrayTwo), 0);
            case MULTIPLY -> new Response(request.getOperation(), true, math.multiply(arrayOne, arrayTwo), 0);
            case DIVIDE -> new Response(request.getOperation(), true, math.divide(arrayOne, arrayTwo), 0);
            case DOT_PRODUCT -> new Response(request.getOperation(), true, null, math.dotProduct(arrayOne, arrayTwo));
            case RELU -> new Response(request.getOperation(), true, math.relu(arrayOne), 0);
            case THRESHOLD ->
                    new Response(request.getOperation(), true, math.threshold(arrayOne, request.getThreshold()), 0);
            case CLAMP ->
                    new Response(request.getOperation(), true, math.clamp(arrayOne, request.getClampLow(), request.getClampHigh()), 0);
            default -> throw new IllegalArgumentException("Unknown operation: " + request.getOperation());
        };
    }
}
