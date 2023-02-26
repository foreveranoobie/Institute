package thread;

import info.DataInfo;
import info.NormalizerInfo;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;

public class NormalizerThread implements Runnable {

    private NormalizerInfo normalizerInfo;
    private DataInfo dataInfo;

    public NormalizerThread(NormalizerInfo normalizerInfo, DataInfo dataInfo) {
        this.normalizerInfo = normalizerInfo;
        this.dataInfo = dataInfo;
    }

    @Override
    public void run() {
        try (OutputStream outputStream = new FileOutputStream("result.csv")) {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            boolean isEof;
            StringBuilder row;
            int minValue = dataInfo.getMinValue();
            int maxValue = dataInfo.getMaxValue();
            BigDecimal minMaxDiff = new BigDecimal(maxValue - minValue);

            do {

                do {
                    synchronized (normalizerInfo) {
                        row = normalizerInfo.getInputRow();
                    }
                } while (row == null);

                synchronized (normalizerInfo) {
                    normalizerInfo.setInputRow(null);
                }
                isEof = row.length() == 0;
                String numValueStr;
                BigDecimal valueBigDecimal;

                if (!isEof) {
                    StringBuilder number;
                    int value;
                    int numBeginIndex;
                    isEof = row.indexOf("f") != -1;
                    char symbolAtIndex;
                    int valueLengthDiff;
                    for (int numIndex = 0; numIndex < row.length(); numIndex++) {
                        number = new StringBuilder();
                        numBeginIndex = numIndex;
                        symbolAtIndex = row.charAt(numIndex++);
                        while (symbolAtIndex != ';' && symbolAtIndex != 'n'
                                && symbolAtIndex != 'f' && numIndex < row.length()) {
                            number.append(symbolAtIndex);
                            if (numIndex < row.length()) {
                                symbolAtIndex = row.charAt(numIndex++);
                            }
                        }
                        numIndex--;
                        if (numBeginIndex < numIndex && symbolAtIndex != 'n' && symbolAtIndex != 'f') {
                            valueBigDecimal = new BigDecimal(Integer.parseInt(number.toString()));
                            valueBigDecimal = valueBigDecimal.subtract(BigDecimal.valueOf(Double.valueOf(minValue)))
                                    .divide(minMaxDiff, 3, RoundingMode.HALF_DOWN);
                            numValueStr = valueBigDecimal.toString();
                            row.replace(numBeginIndex, numIndex, numValueStr);
                            valueLengthDiff = numIndex - numBeginIndex;
                            valueLengthDiff = numValueStr.length() - valueLengthDiff;
                            numIndex += valueLengthDiff;
                        }
                        if (symbolAtIndex == 'n') {
                            row.replace(numIndex, numIndex + 1, "\n");
                        } else if (symbolAtIndex == 'f') {
                            row.replace(numIndex, numIndex + 1, "");
                        }
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
