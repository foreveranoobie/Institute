import java.util.Arrays;

public class Image {
    private int[] bits;
    private int[] vector;
    private char symbol;

    public Image() {
        bits = new int[25];
        vector = new int[4];
    }

    public Image(int[] bits, int[] vector, char symbol) {
        this.bits = bits;
        this.vector = vector;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder("Symbol: " + symbol + "\nBinary matrix is:\n");
        for (int i = 0; i < 25; i++) {
            text.append(String.format("%3s", bits[i]));
            if ((i + 1) % 5 == 0) {
                text.append("\n");
            }
        }
        text.append("The vector is: ");
        for (int vecNum : vector) {
            text.append(vecNum + " ");
        }
        return text.append("\n").toString();
    }

    public void setBits(int[] bits) {
        this.bits = bits;
    }

    public int[] getBits() {
        return bits;
    }

    public void setVector(int[] vector) {
        this.vector = vector;
    }

    public int[] getVector() {
        return vector;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
