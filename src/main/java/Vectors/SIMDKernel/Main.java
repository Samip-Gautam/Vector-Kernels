package Vectors.SIMDKernel;

import Vectors.SIMDKernel.http.HttpGateway;

public class Main {

    public static void main(String[] args) throws Exception {
        HttpGateway.start(8080);
    }
}