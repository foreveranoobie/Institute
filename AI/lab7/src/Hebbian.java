import java.util.Arrays;
import java.util.List;

public class Hebbian {
    private int[][] weights;

    public Hebbian() {
        weights = new int[4][25];
    }

    public void teachNetwork(List<Image> ethalonImages) {
        boolean isDone = false;
        int[] bits;
        int[] vector;
        while (!isDone) {
            for (Image image : ethalonImages) {
                isDone = isImageComplete(image);
                if (!isDone) {
                    bits = image.getBits();
                    vector = image.getVector();
                    for (int j = 0; j < 4; j++) {
                        for (int i = 0; i < 25; i++) {
                            weights[j][i] += bits[i] * vector[j];
                        }
                    }
                }
            }
            isDone = networkTeachCompleted(ethalonImages);
        }
    }

    private boolean networkTeachCompleted(List<Image> ethalonImages) {
        for (Image image : ethalonImages) {
            if (!isImageComplete(image)) {
                return false;
            }
        }
        return true;
    }

    public int[] predict(Image image) {
        int[] vector = new int[4];
        int index = 0;
        int[] sum = new int[25];
        int[] binaryImage = image.getBits();
        for (int sym = 0; sym < 4; sym++) {
            for (int i = 0; i < 25; i++) {
                sum[sym] += binaryImage[i] * weights[sym][i];
            }
            if (sum[sym] > 0) {
                vector[index++] = 1;
            } else {
                vector[index++] = -1;
            }
        }
        return vector;
    }

    private boolean isImageComplete(Image image) {
        return Arrays.equals(image.getVector(), predict(image));
    }

    public void setWeights(int[][] weights) {
        this.weights = weights;
    }

    public int[][] getWeights() {
        return weights;
    }
}
