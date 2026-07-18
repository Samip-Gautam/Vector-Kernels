package Vectors.SIMDKernel.karnel;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;

public class VectorMathImpl implements VectorMath {
    @Override
    public int[] add(int[] arrayOne, int[] arrayTwo) {
        int[] out = new int[arrayOne.length];
        int upper = SPECIES.loopBound(arrayOne.length);
        for (int i = 0; i < upper; i += SPECIES.length()) {
            IntVector vectorOne = IntVector.fromArray(SPECIES, arrayOne, i);
            IntVector vectorTwo = IntVector.fromArray(SPECIES, arrayTwo, i);
            IntVector result = vectorOne.add(vectorTwo);
            result.intoArray(out, i);
        }
        for (int i = upper; i < arrayOne.length; i++) {
            out[i] = arrayOne[i] + arrayTwo[i];
        }
        return out;
    }

    @Override
    public int[] subtract(int[] arrayOne, int[] arrayTwo) {
        int[] out = new int[arrayOne.length];
        int upper = SPECIES.loopBound(arrayOne.length);
        for (int i = 0; i < upper; i += SPECIES.length()) {
            IntVector vectorOne = IntVector.fromArray(SPECIES, arrayOne, i);
            IntVector vectorTwo = IntVector.fromArray(SPECIES, arrayTwo, i);
            IntVector result = vectorOne.sub(vectorTwo);
            result.intoArray(out, i);
        }
        for (int i = upper; i < arrayOne.length; i++) {
            out[i] = arrayOne[i] - arrayTwo[i];
        }
        return out;
    }

    @Override
    public int[] multiply(int[] arrayOne, int[] arrayTwo) {
        int[] out = new int[arrayOne.length];
        int upper = SPECIES.loopBound(arrayOne.length);
        for (int i = 0; i < upper; i += SPECIES.length()) {
            IntVector vectorOne = IntVector.fromArray(SPECIES, arrayOne, i);
            IntVector vectorTwo = IntVector.fromArray(SPECIES, arrayTwo, i);
            IntVector result = vectorOne.mul(vectorTwo);
            result.intoArray(out, i);
        }
        for (int i = upper; i < arrayOne.length; i++) {
            out[i] = arrayOne[i] * arrayTwo[i];
        }
        return out;
    }

    @Override
    public int[] divide(int[] arrayOne, int[] arrayTwo) {
        int[] out = new int[arrayOne.length];
        int upper = SPECIES.loopBound(arrayOne.length);
        for (int i = 0; i < upper; i += SPECIES.length()) {
            IntVector vectorOne = IntVector.fromArray(SPECIES, arrayOne, i);
            IntVector vectorTwo = IntVector.fromArray(SPECIES, arrayTwo, i);
            IntVector result = vectorOne.div(vectorTwo);
            result.intoArray(out, i);
        }
        for (int i = upper; i < arrayOne.length; i++) {
            out[i] = arrayOne[i] / arrayTwo[i];
        }
        return out;
    }

    @Override
    public int dotProduct(int[] arrayOne, int[] arrayTwo) {
        IntVector sum = IntVector.zero(SPECIES);
        int upper = SPECIES.loopBound(arrayOne.length);
        for (int i = 0; i < upper; i += SPECIES.length()) {
            IntVector vectorOne = IntVector.fromArray(SPECIES, arrayOne, i);
            IntVector vectorTwo = IntVector.fromArray(SPECIES, arrayTwo, i);
            IntVector product = vectorOne.mul(vectorTwo);
            sum = sum.add(product);
        }
        int result = sum.reduceLanes(VectorOperators.ADD);
        for (int i = upper; i < arrayOne.length; i++) {
            result += arrayOne[i] * arrayTwo[i];
        }
        return result;
    }

    @Override
    public int[] relu(int[] array) {
        int[] out = new int[array.length];
        int upper = SPECIES.loopBound(array.length);
        for (int i = 0; i < upper; i += SPECIES.length()) {
            IntVector vector = IntVector.fromArray(SPECIES, array, i);
            VectorMask<Integer> mask = vector.compare(VectorOperators.LT, 0);
            IntVector result = vector.blend(0, mask);
            result.intoArray(out, i);
        }
        for (int i = upper; i < array.length; i++) {
            out[i] = Math.max(0, array[i]);
        }
        return out;
    }

    @Override
    public int[] threshold(int[] array, int threshold) {
        int[] out = new int[array.length];
        int upper = SPECIES.loopBound(array.length);
        for (int i = 0; i < upper; i += SPECIES.length()) {
            IntVector vector = IntVector.fromArray(SPECIES, array, i);
            VectorMask<Integer> mask = vector.compare(VectorOperators.LT, threshold);
            IntVector result = vector.blend(0, mask);
            result.intoArray(out, i);
        }
        for (int i = upper; i < array.length; i++) {
            out[i] = array[i] < threshold ? 0 : array[i];
        }
        return out;
    }

    @Override
    public int[] clamp(int[] array, int low, int high) {
        int[] out = new int[array.length];
        int upper = SPECIES.loopBound(array.length);
        for (int i = 0; i < upper; i += SPECIES.length()) {
            IntVector vector = IntVector.fromArray(SPECIES, array, i);
            IntVector result = vector.max(low).min(high);
            result.intoArray(out, i);
        }
        for (int i = upper; i < array.length; i++) {
            out[i] = Math.clamp(array[i], low, high);
        }
        return out;
    }

}
