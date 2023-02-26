package util;

import java.io.IOException;
import java.io.InputStream;

public class ReaderUtil {

    public static String readValue(InputStream inputStream, boolean parseNewLines) throws IOException {
        StringBuilder input = new StringBuilder();
        int readValue;
        do {
            readValue = inputStream.read();
            if ((readValue > 47 && readValue < 58)) {
                input.append(Character.valueOf((char) readValue));
            } else if (readValue == -1) {
                input.append("f");
            } else if (parseNewLines && readValue == 10) {
                input.append("n");
            }

        } while (readValue > 47 && readValue < 58);
        return input.toString();
    }

    public static StringBuilder readValue(InputStream inputStream) throws IOException {
        StringBuilder input = new StringBuilder();
        int readValue;
        do {
            readValue = inputStream.read();
            if ((readValue > 47 && readValue < 58)) {
                input.append(Character.valueOf((char) readValue));
            } else if (readValue == -1) {
                input.append("f");
            } else if (readValue == 10) {
                input.append("n");
            }

        } while (readValue > 47 && readValue < 58);
        return input;
    }

    public static int readDummyRow(InputStream inputStream) throws IOException {
        int readSymbol;
        do {
            readSymbol = inputStream.read();
        } while (readSymbol != 10 && readSymbol != -1);
        return readSymbol;
    }
}
