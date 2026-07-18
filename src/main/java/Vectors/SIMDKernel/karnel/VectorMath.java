package Vectors.SIMDKernel.karnel;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;

public interface VectorMath {
    VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

    int[] add(int[] arrayOne, int[] arrayTwo);

    int[] subtract(int[] arrayOne, int[] arrayTwo);

    int[] multiply(int[] arrayOne, int[] arrayTwo);

    int[] divide(int[] arrayOne, int[] arrayTwo);

    int dotProduct(int[] arrayOne, int[] arrayTwo);

    int[] relu(int[] array);

    int[] threshold(int[] array, int threshold);

    int[] clamp(int[] array, int low, int high);
}
