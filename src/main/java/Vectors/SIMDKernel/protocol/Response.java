package Vectors.SIMDKernel.protocol;

public class Response {
    private Operation operation;
    private Boolean success;
    private int[] resultArray;
    private int resultValue;

    void setOperation(Operation operation) {
        this.operation = operation;
    }

    public void setResultArray(int[] resultArray) {
        this.resultArray = resultArray;
    }

    public void setResultValue(int resultValue) {
        this.resultValue = resultValue;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public int getResultValue() {
        return resultValue;
    }

    public int[] getResultArray() {
        return resultArray;
    }

    public Operation getOperation() {
        return operation;
    }

    public Response(Operation operation, boolean b, int[] result, int i) {
        this.operation = operation;
        this.success = b;
        this.resultArray = result;
        this.resultValue = i;
    }
}
