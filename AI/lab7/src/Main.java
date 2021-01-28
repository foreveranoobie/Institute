import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("System is learning the ethalon images");
        List<Image> ethalonImages = initEthalonImages();
        System.out.println(ethalonImages);
        Hebbian hebbian = new Hebbian();
        hebbian.teachNetwork(ethalonImages);
        System.out.println("Learning has been done. Now program is doing the test stages.");
        Image testImage = new Image();
        testImage.setBits(new int[]{-1, -1, 1, -1, -1,
                -1, 1, -1, 1, -1,
                1, 1, 1, 1, 1,
                1, -1, -1, -1, 1,
                1, -1, -1, 1, 1
        });
        testImage(ethalonImages, testImage, hebbian);
        testImage.setBits(new int[]{1, -1, -1, 1, 1,
                1, -1, 1, -1, -1,
                1, 1, -1, -1, -1,
                1, -1, 1, -1, -1,
                1, 1, -1, 1, -1
        });
        testImage(ethalonImages, testImage, hebbian);
        testImage.setBits(new int[]{
                1, 1, 1, 1, 1,
                1, -1, -1, 1, -1,
                1, 1, 1, 1, 1,
                1, 1, -1, -1, -1,
                1, 1, 1, 1, 1
        });
        testImage(ethalonImages, testImage, hebbian);
        testImage.setBits(new int[]{
                -1, 1, 1, 1, -1,
                -1, 1, -1, 1, 1,
                -1, 1, -1, 1, -1,
                -1, 1, -1, 1, -1,
                1, 1, -1, 1, 1
        });
        testImage(ethalonImages, testImage, hebbian);
        testImage.setBits(new int[]{
                -1, 1, 1, 1, 1,
                1, 1, -1, -1, 1,
                -1, 1, -1, 1, -1,
                -1, -1, -1, 1, -1,
                1, 1, -1, 1, 1
        });
        testImage(ethalonImages, testImage, hebbian);
    }

    private static void testImage(List<Image> ethalons, Image unknown, Hebbian hebbian) {
        int bits[] = unknown.getBits();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 25; i++) {
            text.append(String.format("%3s", bits[i]));
            if ((i + 1) % 5 == 0) {
                text.append("\n");
            }
        }
        System.out.println("Trying to recognize image with matrix:\n" + text.toString());
        int[] vector = hebbian.predict(unknown);
        recognizeImageByVector(ethalons, vector);
    }

    private static void recognizeImageByVector(List<Image> ethalons, int[] vector) {
        for (Image image : ethalons) {
            if (Arrays.equals(image.getVector(), vector)) {
                System.out.println("The current image is probably symbol: " + image.getSymbol());
                return;
            }
        }
        System.out.println("Can't recognize symbol");
    }

    //А, Л, Е, К (5х5 матрица)
    private static List<Image> initEthalonImages() {
        List<Image> ethalons = new ArrayList<>(4);
        ethalons.add(new Image(new int[]{-1, -1, 1, -1, -1,
                -1, 1, -1, 1, -1,
                1, 1, 1, 1, 1,
                1, -1, -1, -1, 1,
                1, -1, -1, -1, 1
        }, new int[]{-1, 1, 1, 1}, 'А'));
        ethalons.add(new Image(new int[]{-1, 1, 1, 1, -1,
                -1, 1, -1, 1, -1,
                -1, 1, -1, 1, -1,
                -1, 1, -1, 1, -1,
                1, 1, -1, 1, -1
        }, new int[]{1, -1, 1, 1}, 'Л'));
        ethalons.add(new Image(new int[]{1, 1, 1, 1, 1,
                1, -1, -1, -1, -1,
                1, 1, 1, 1, 1,
                1, -1, -1, -1, -1,
                1, 1, 1, 1, 1
        }, new int[]{1, 1, -1, 1}, 'Е'));
        ethalons.add(new Image(new int[]{1, -1, -1, 1, -1,
                1, -1, 1, -1, -1,
                1, 1, -1, -1, -1,
                1, -1, 1, -1, -1,
                1, -1, -1, 1, -1
        }, new int[]{1, 1, 1, -1}, 'К'));
        return ethalons;
    }
}
