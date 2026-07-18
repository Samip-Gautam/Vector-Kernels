package Vectors.SIMDKernel.benchmark;

import Vectors.SIMDKernel.karnel.VectorMath;
import Vectors.SIMDKernel.karnel.VectorMathImpl;

import java.util.Random;

public class Benchmark {

    private static final int SIZE = 10000000;
    private static final int WARMUP_RUNS = 10;
    private static final int BENCHMARK_RUNS = 10;

    public static void main(String[] args) {

        VectorMath math = new VectorMathImpl();

        int[] arrayOne = new int[SIZE];
        int[] arrayTwo = new int[SIZE];

        Random random = new Random(42);

        for (int i = 0; i < SIZE; i++) {
            arrayOne[i] = random.nextInt(1000);
            arrayTwo[i] = random.nextInt(999) + 1;
        }

        for (int i = 0; i < WARMUP_RUNS; i++) {
            math.add(arrayOne, arrayTwo);
        }

        long totalTime = 0;

        for (int i = 1; i <= BENCHMARK_RUNS; i++) {

            long start = System.nanoTime();

            int[] result = math.add(arrayOne, arrayTwo);

            long end = System.nanoTime();

            long elapsed = end - start;
            totalTime += elapsed;

            System.out.printf(
                    "Run %2d : %.3f ms%n",
                    i,
                    elapsed / 1_000_000.0
            );

            if (i == BENCHMARK_RUNS) {
                System.out.println();
                System.out.println("First  : " + result[0]);
                System.out.println("Middle : " + result[SIZE / 2]);
                System.out.println("Last   : " + result[SIZE - 1]);
            }
        }

        double average = totalTime / (double) BENCHMARK_RUNS;

        System.out.println();
        System.out.printf("Average : %.3f ms%n", average / 1000000.00);
    }
}