package info;

public class InputsInfo {

    private StringBuilder row;
    private boolean isEof;

    public boolean isEof() {
        return isEof;
    }

    public void setEof(boolean eof) {
        isEof = eof;
    }

    public StringBuilder getRow() {
        return row;
    }

    public void setRow(StringBuilder row) {
        this.row = row;
    }
}
