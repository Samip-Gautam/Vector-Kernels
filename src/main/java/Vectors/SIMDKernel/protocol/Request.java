package Vectors.SIMDKernel.protocol;

public class Request {

    private Operation operation;
//    private int arrayLength;
    private int[] arrayOne;
    private int[] arrayTwo;
    private int threshold;
    private int clampLow;
    private int clampHigh;

    public Request() {}

    public Request(Operation operation) {
        this.operation = operation;
//        this.arrayLength = arrayLength;
    }

    public Operation getOperation() {
        return operation;
    }
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

//    public int getArrayLength() { return arrayLength; }
//    public void setArrayLength(int arrayLength) { this.arrayLength = arrayLength; }

    public int[] getArrayOne() { return arrayOne; }
    public void setArrayOne(int[] arrayOne) { this.arrayOne = arrayOne; }

    public int[] getArrayTwo() { return arrayTwo; }
    public void setArrayTwo(int[] arrayTwo) { this.arrayTwo = arrayTwo; }

    public int getThreshold() { return threshold; }
    public void setThreshold(int threshold) { this.threshold = threshold; }

    public int getClampLow() { return clampLow; }
    public void setClampLow(int clampLow) { this.clampLow = clampLow; }

    public int getClampHigh() { return clampHigh; }
    public void setClampHigh(int clampHigh) { this.clampHigh = clampHigh; }
}