package preprocessor;

import static util.ReaderUtil.readDummyRow;
import static util.ReaderUtil.readValue;

import info.DataInfo;
import info.FileInfo;
import info.InputsInfo;
import info.NormalizerInfo;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import thread.NormalizerThread;
import thread.SimpleWriterProcessorThread;

public class DataPreprocessor {

    private final DataInfo dataInfo;
    private final FileInfo fileInfo;

    public DataPreprocessor(DataInfo dataInfo, FileInfo fileInfo) {
        this.dataInfo = dataInfo;
        this.fileInfo = fileInfo;
    }

    public void preprocessAnomaliesData(String fileName) throws IOException {
        FileInputStream inputStream = new FileInputStream(fileName);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        FileOutputStream outputStream = new FileOutputStream(fileInfo.getAfterAnomaliesProcessedDataFilename());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        List<Integer> anomalyRows = dataInfo.getAnomalyRowIndexes();

        int lastReadSymbol;
        Integer currentRowIndex = 0;
        int totalRows = dataInfo.getRowsCount();

        do {
            if (anomalyRows.contains(currentRowIndex)) {
                lastReadSymbol = readDummyRow(bufferedInputStream);
                anomalyRows.remove(currentRowIndex);
            } else {
                do {
                    lastReadSymbol = bufferedInputStream.read();
                    if (lastReadSymbol == 10) {
                        if (!(currentRowIndex == totalRows - 1 && anomalyRows.contains(currentRowIndex + 1))) {
                            bufferedOutputStream.write(lastReadSymbol);
                        }
                    } else if (lastReadSymbol != -1) {
                        bufferedOutputStream.write(lastReadSymbol);
                    }

                } while (lastReadSymbol != 10 && lastReadSymbol != -1);
                bufferedOutputStream.flush();
            }

            currentRowIndex++;

        } while (lastReadSymbol != -1);

        inputStream.close();
        outputStream.close();
    }

    public void processEmptyAndErrorInputs(String fileName) throws IOException, InterruptedException {
        FileInputStream inputStream = new FileInputStream(fileName);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

/*        FileOutputStream outputStream = new FileOutputStream(
                fileInfo.getAfterEmptyAndErrorInputsProcessedDataFilename());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);*/

        InputsInfo inputsInfo = new InputsInfo();

        Long meanValue = dataInfo.getGeometricMeanValue();
        String meanValueStr = meanValue.toString();

        int lastReadSymbol = 0;
        int currentReadSymbol;

        int bufferCount = 0;
        int bufferLimit = 3000;
        StringBuilder row = new StringBuilder();
        StringBuilder alreadyGivenRow;
        char comaSeparator = ';';

        Thread processorThread = new Thread(new SimpleWriterProcessorThread(inputsInfo,
                fileInfo.getAfterEmptyAndErrorInputsProcessedDataFilename()));
        processorThread.start();
        do {
            currentReadSymbol = bufferedInputStream.read();
            if (currentReadSymbol == '_') {
                row.append(meanValueStr);
                //bufferedOutputStream.write(meanValueBytes);
            } else if (currentReadSymbol == comaSeparator && (lastReadSymbol == 0
                    || lastReadSymbol == comaSeparator || lastReadSymbol == 10)) {
                row.append(meanValueStr);
                row.append(';');
                //bufferedOutputStream.write(meanValueBytes);
                //bufferedOutputStream.write(';');
            } else {
                if (currentReadSymbol > -1) {
                    row.append((char) currentReadSymbol);
                    //bufferedOutputStream.write(currentReadSymbol);
                }
            }
            if (bufferCount > bufferLimit || currentReadSymbol == -1) {
                //bufferedOutputStream.flush();
                do {
                    synchronized (inputsInfo) {
                        alreadyGivenRow = inputsInfo.getRow();
                    }
                } while (alreadyGivenRow != null);

                synchronized (inputsInfo) {
                    inputsInfo.setRow(row);
                    inputsInfo.setEof(currentReadSymbol == -1);
                }
                row = new StringBuilder();
                bufferCount = 0;
            }
            bufferCount++;
            lastReadSymbol = currentReadSymbol;
        } while (currentReadSymbol != -1);

        inputStream.close();
        processorThread.join();
        //outputStream.close();
    }

    public void processContradictoryInfo() throws IOException {
        FileInputStream inputStream = new FileInputStream(fileInfo.getAfterEmptyAndErrorInputsProcessedDataFilename());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        Integer currentRowIndex = 0;
        String currentValue;
        Long rowValuesSum = 0L;
        boolean containsNewRow;
        boolean isEof;

        List<Integer> contradictoryRowIndexes = new ArrayList<>(dataInfo.getRowsCount() / 5);

        do {
            do {
                currentValue = readValue(bufferedInputStream, true);
                containsNewRow = currentValue.contains("n");
                isEof = currentValue.contains("f");
                if (isEof || containsNewRow) {
                    currentValue = currentValue.replace("f", "").replace("n", "");
                    if (rowValuesSum % 2 != Long.parseLong(currentValue)) {
                        contradictoryRowIndexes.add(currentRowIndex);
                    }
                } else {
                    rowValuesSum += Long.parseLong(currentValue);
                }
            } while (!containsNewRow && !isEof);

            currentRowIndex++;
            rowValuesSum = 0L;
        } while (!isEof);
        currentRowIndex = 0;

        inputStream.close();

        inputStream = new FileInputStream(fileInfo.getAfterEmptyAndErrorInputsProcessedDataFilename());
        bufferedInputStream = new BufferedInputStream(inputStream);

        FileOutputStream outputStream = new FileOutputStream(fileInfo.getAfterContradictoriesProcessedDataFilename());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        int lastReadSymbol;
        int removedRows = 0;

        do {
            if (contradictoryRowIndexes.contains(currentRowIndex)) {
                lastReadSymbol = readDummyRow(bufferedInputStream);
                contradictoryRowIndexes.remove(currentRowIndex);
                removedRows++;
            } else {
                do {
                    lastReadSymbol = bufferedInputStream.read();
                    if (lastReadSymbol != -1) {
                        bufferedOutputStream.write(lastReadSymbol);
                    }
                } while (lastReadSymbol != 10 && lastReadSymbol != -1);
                bufferedOutputStream.flush();
            }
            currentRowIndex++;
        } while (lastReadSymbol != -1);

        outputStream.close();
        inputStream.close();

        dataInfo.setRowsCount(dataInfo.getRowsCount() - removedRows);
    }

    public void optimizeCriteria() {
        int featureColumnsCount = dataInfo.getColumnCount() - 1;
        List<Long> averageColumnValues = new ArrayList<>(featureColumnsCount);
        for (int i = 0; i < featureColumnsCount; i++) {
            averageColumnValues.add(0L);
        }
        int rowIndex = 0;
        int columnIndex = 0;

        //Calculate average for each column
        try (InputStream inputStream = new FileInputStream(fileInfo.getAfterContradictoriesProcessedDataFilename())) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            String currentValue;

            boolean isEof;
            boolean containsNewRow;

            do {
                do {
                    currentValue = readValue(bufferedInputStream, true);
                    containsNewRow = currentValue.contains("n");
                    isEof = currentValue.contains("f");
                    if (!isEof && !containsNewRow) {
                        averageColumnValues.set(columnIndex,
                                averageColumnValues.get(columnIndex) + Long.parseLong(currentValue));
                    }
                    columnIndex++;
                } while (!containsNewRow && !isEof);

                rowIndex++;
                columnIndex = 0;
            } while (!isEof);

            dataInfo.setRowsCount(rowIndex);

            for (int i = 0; i < averageColumnValues.size(); i++) {
                Long sum = averageColumnValues.get(i) / rowIndex;
                averageColumnValues.set(i, sum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        columnIndex = 0;
        rowIndex = 0;
        List<Long> sumOfMeanDifferenceList = new ArrayList<>(featureColumnsCount);
        for (int i = 0; i < featureColumnsCount; i++) {
            sumOfMeanDifferenceList.add(0L);
        }
        //calculate sum of differences from average for each column
        try (InputStream inputStream = new FileInputStream(fileInfo.getAfterContradictoriesProcessedDataFilename())) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            boolean isEof;
            boolean containsNewRow;
            String currentValue;
            Long value;
            do {
                do {
                    currentValue = readValue(bufferedInputStream, true);
                    containsNewRow = currentValue.contains("n");
                    isEof = currentValue.contains("f");
                    if (!isEof && !containsNewRow) {
                        value = averageColumnValues.get(columnIndex) - Long.parseLong(currentValue);
                        value = value * value;
                        value = sumOfMeanDifferenceList.get(columnIndex) + value;
                        sumOfMeanDifferenceList.set(columnIndex, value);
                    }
                    columnIndex++;
                } while (!containsNewRow && !isEof);

                rowIndex++;
                columnIndex = 0;
            } while (!isEof);

            int optimizationThreshold = dataInfo.getOptimizationThreshold();
            List<Integer> optimizationIndexes = new ArrayList<>();
            for (int i = 0; i < featureColumnsCount; i++) {
                value = sumOfMeanDifferenceList.get(i);
                value = value / (rowIndex - 1);
                sumOfMeanDifferenceList.set(i, value);
                if (value < optimizationThreshold) {
                    optimizationIndexes.add(i);
                }
            }

            dataInfo.setToRemoveOptimizationColumnIndexes(optimizationIndexes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        doOptimizationOnDataSet();
    }

    private void doOptimizationOnDataSet() {
        try (InputStream inputStream = new FileInputStream(fileInfo.getAfterContradictoriesProcessedDataFilename());
                OutputStream outputStream = new FileOutputStream(fileInfo.getAfterOptimizationDataFilename())) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

            List<Integer> toRemoveColumns = dataInfo.getToRemoveOptimizationColumnIndexes();
            int columnIndex = 0;
            String stringValue;
            boolean containsNewLine;
            boolean isEof;
            do {
                stringValue = readValue(bufferedInputStream, true);
                containsNewLine = stringValue.contains("n");
                isEof = stringValue.contains("f");
                if (isEof) {
                    stringValue = stringValue.replace("f", "");
                } else if (containsNewLine) {
                    stringValue = stringValue.replace("n", "");
                }
                if (!toRemoveColumns.contains(columnIndex)) {
                    bufferedOutputStream.write(stringValue.getBytes(StandardCharsets.UTF_8));
                    if (containsNewLine) {
                        bufferedOutputStream.write("\n".getBytes(StandardCharsets.UTF_8));
                    } else if (!isEof) {
                        bufferedOutputStream.write(";".getBytes(StandardCharsets.UTF_8));
                    }
                    bufferedOutputStream.flush();
                }
                if (containsNewLine) {
                    columnIndex = 0;
                } else {
                    columnIndex++;
                }
            } while (!isEof);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void normalizeData(String fileName) {
        NormalizerInfo normalizerInfo = new NormalizerInfo();
        NormalizerThread normalizerThread = new NormalizerThread(normalizerInfo, dataInfo);
        Thread thread = new Thread(normalizerThread);

        thread.start();

        boolean isEof;
        boolean containsNewLine;
        try (InputStream inputStream = new FileInputStream(fileName)) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            int numCount = 0;
            StringBuilder numbers;
            StringBuilder number;
            do {
                numbers = new StringBuilder();
                do {
                    number = readValue(bufferedInputStream);
                    containsNewLine = number.indexOf("n") != -1;
                    isEof = number.indexOf("f") != -1;

                    numbers.append(number);

                    if (!containsNewLine && !isEof) {
                        numbers.append(";");
                    }

                    numCount++;
                } while (numCount < 200 && !containsNewLine && !isEof);
                numCount = 0;

                StringBuilder previousRow;

                do {
                    synchronized (normalizerInfo) {
                        previousRow = normalizerInfo.getInputRow();
                    }
                } while (previousRow != null);

                synchronized (normalizerInfo) {
                    normalizerInfo.setInputRow(numbers);
                }
            } while (!isEof);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getMinAndMaxValues(String dataFileName) {
        try (InputStream inputStream = new FileInputStream(dataFileName)) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            boolean isEof;
            boolean hasNewLine;
            String currentValue;
            int numValue;

            currentValue = readValue(bufferedInputStream, false);
            numValue = Integer.parseInt(currentValue);

            int minValue = numValue;
            int maxValue = numValue;

            do {
                currentValue = readValue(bufferedInputStream, true);
                isEof = currentValue.contains("f");
                hasNewLine = currentValue.contains("n");
                if (hasNewLine) {
                    currentValue = currentValue.replace("n", "");
                } else if (isEof) {
                    currentValue = currentValue.replace("f", "");
                }
                if (!currentValue.isEmpty()) {
                    numValue = Integer.parseInt(currentValue);
                    if (!hasNewLine && !isEof) {
                        if (numValue < minValue) {
                            minValue = numValue;
                        } else if (numValue > maxValue) {
                            maxValue = numValue;
                        }
                    }
                }
            } while (!isEof);

            dataInfo.setMaxValue(maxValue);
            dataInfo.setMinValue(minValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
