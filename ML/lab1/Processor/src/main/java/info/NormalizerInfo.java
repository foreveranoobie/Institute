package info;

public class NormalizerInfo {

    private StringBuilder inputRow;
    private StringBuilder outputRow;
    private boolean isProcessed;

    public StringBuilder getInputRow() {
        return inputRow;
    }

    public void setInputRow(StringBuilder inputRow) {
        this.inputRow = inputRow;
    }

    public StringBuilder getOutputRow() {
        return outputRow;
    }

    public void setOutputRow(StringBuilder outputRow) {
        this.outputRow = outputRow;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }
}
