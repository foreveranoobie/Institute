package thread;

import info.InputsInfo;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SimpleWriterProcessorThread implements Runnable {

    private InputsInfo inputsInfo;
    private String fileName;

    public SimpleWriterProcessorThread(InputsInfo inputsInfo, String fileName) {
        this.inputsInfo = inputsInfo;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try (OutputStream outputStream = new FileOutputStream(fileName);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            StringBuilder row;
            boolean isEof;
            do {
                do {
                    synchronized (inputsInfo) {
                        row = inputsInfo.getRow();
                        inputsInfo.setRow(null);
                        isEof = inputsInfo.isEof();
                    }
                } while (row == null);
                if (row.length() != 0) {
                    if (isEof && row.indexOf("\n") != -1) {
                        row.delete(row.length() - 2, row.length());
                    }
                    bufferedOutputStream.write(row.toString().getBytes(StandardCharsets.UTF_8));
                    bufferedOutputStream.flush();
                }
            } while (!isEof);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
