package preprocessor;

import static util.ReaderUtil.readDummyRow;
import static util.ReaderUtil.readValue;

import info.DataInfo;
import info.FileInfo;
import info.InputsInfo;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import thread.SimpleWriterProcessorThread;

public class AnomalyPreprocessor {

    private final DataInfo dataInfo;
    private final FileInfo fileInfo;

    public AnomalyPreprocessor(DataInfo dataInfo, FileInfo fileInfo) {
        this.dataInfo = dataInfo;
        this.fileInfo = fileInfo;
    }

    public void preprocessAnomalies(String fileName) {
        removeAnomalies(fileName);
    }

    public void removeAnomalies(String fileName) {
        int featureColumnsCount = dataInfo.getColumnCount() - 1;
        List<Long> averageColumnValues = new ArrayList<>(featureColumnsCount);
        for (int i = 0; i < featureColumnsCount; i++) {
            averageColumnValues.add(0L);
        }
        int rowIndex = 0;
        int columnIndex = 0;

        //Calculate average for each column
        try (InputStream inputStream = new FileInputStream(fileName)) {
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
        List<BigDecimal> sumOfMeanDifferenceList = new ArrayList<>(featureColumnsCount);
        for (int i = 0; i < featureColumnsCount; i++) {
            sumOfMeanDifferenceList.add(BigDecimal.ZERO);
        }
        //calculate sum of differences from average for each column
        try (InputStream inputStream = new FileInputStream(fileName)) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            boolean isEof;
            boolean containsNewRow;
            String currentValue;
            BigDecimal value;
            do {
                do {
                    currentValue = readValue(bufferedInputStream, true);
                    containsNewRow = currentValue.contains("n");
                    isEof = currentValue.contains("f");
                    if (!isEof && !containsNewRow) {
                        value = BigDecimal.valueOf(Long.parseLong(currentValue) - averageColumnValues.get(columnIndex));
                        value = value.pow(2);
                        value = sumOfMeanDifferenceList.get(columnIndex).add(value);
                        sumOfMeanDifferenceList.set(columnIndex, value);
                    }
                    columnIndex++;
                } while (!containsNewRow && !isEof);

                rowIndex++;
                columnIndex = 0;
            } while (!isEof);

            List<Integer> anomalyScoreLowers = new ArrayList<>(featureColumnsCount + 1);
            List<Integer> anomalyScoreUppers = new ArrayList<>(featureColumnsCount + 1);

            for (int i = 0; i < featureColumnsCount + 1; i++) {
                anomalyScoreLowers.add(0);
                anomalyScoreUppers.add(0);
            }

            Long averageColumnValue;
            for (int i = 0; i < sumOfMeanDifferenceList.size(); i++) {
                value = sumOfMeanDifferenceList.get(i).divide(BigDecimal.valueOf(rowIndex), 1, RoundingMode.HALF_DOWN)
                        .sqrt(MathContext.DECIMAL32);

                averageColumnValue = averageColumnValues.get(i);

                anomalyScoreLowers.set(i, (int) (averageColumnValue - value.intValue()));
                anomalyScoreUppers.set(i, (int) (averageColumnValue + value.intValue()));
            }

            dataInfo.setAnomalyScoreLowers(anomalyScoreLowers);
            dataInfo.setAnomalyScoreUppers(anomalyScoreUppers);
        } catch (IOException e) {
            e.printStackTrace();
        }

        removeAnomaliesFromDataSet(fileName);
    }

    private void removeAnomaliesFromDataSet(String fileName) {
        try (InputStream inputStream = new FileInputStream(fileName)) {

            InputsInfo inputsInfo = new InputsInfo();
            Thread writerThread = new Thread(
                    new SimpleWriterProcessorThread(inputsInfo, fileInfo.getAfterAnomaliesProcessedDataFilename()));

            int columnIndex = 0;
            String stringValue;
            boolean containsNewLine;
            boolean isEof;

            List<Integer> lowerScores = dataInfo.getAnomalyScoreLowers();
            List<Integer> upperScores = dataInfo.getAnomalyScoreUppers();

            int anomalyRowCount = 0;
            int L = dataInfo.getL();
            Integer number;
            int rowIndex = 0;

            StringBuilder row = new StringBuilder();
            StringBuilder alreadyGivenRow;
            boolean skipRow = false;

            writerThread.start();

            do {
                stringValue = readValue(inputStream, true);
                containsNewLine = stringValue.contains("n");
                isEof = stringValue.contains("f");

                if (containsNewLine) {
                    stringValue = stringValue.replace("n", "");
                } else if (isEof) {
                    stringValue = stringValue.replace("f", "");
                }

                //if not last column
                if (!isEof && !containsNewLine) {
                    number = Integer.parseInt(stringValue);
                    //check for anomaly
                    if (number < lowerScores.get(columnIndex) || number > upperScores.get(columnIndex)) {
                        anomalyRowCount++;
                        //if threshold overfilled
                        if (anomalyRowCount >= L) {
                            anomalyRowCount = 0;
                            skipRow = true;
                        }
                    }
                }

                //If we have anomaly object
                if (skipRow) {
                    readDummyRow(inputStream);
                    skipRow = false;
                    row = new StringBuilder();
                    columnIndex = 0;
                    rowIndex++;
                } else if (isEof) {
                    do {
                        synchronized (inputsInfo) {
                            alreadyGivenRow = inputsInfo.getRow();
                        }
                    } while (alreadyGivenRow != null);
                    row.append(stringValue);
                    synchronized (inputsInfo) {
                        inputsInfo.setRow(row);
                        inputsInfo.setEof(isEof);
                    }
                } else if (containsNewLine) {
                    do {
                        synchronized (inputsInfo) {
                            alreadyGivenRow = inputsInfo.getRow();
                        }
                    } while (alreadyGivenRow != null);
                    row.append(stringValue);
                    row.append("\n");
                    synchronized (inputsInfo) {
                        inputsInfo.setRow(row);
                        inputsInfo.setEof(isEof);
                    }
                    rowIndex++;
                    row = new StringBuilder();
                    columnIndex = 0;
                    anomalyRowCount = 0;
                } else {
                    row.append(stringValue).append(";");
                    columnIndex++;
                }
            } while (!isEof);

            writerThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
