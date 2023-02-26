package stats;

import static util.ReaderUtil.readValue;

import info.DataInfo;
import info.FileInfo;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataStatsCollector {

    private final DataInfo dataInfo;
    private final FileInfo fileInfo;

    private Long initialFileSize = 0L;
    private Long totalNumbersCount = 0L;
    private Long contradictoriesPercentage = 0L;
    private Long anomaliesPercentage = 0L;
    private Long emptyInputsPercentage = 0L;
    private Long errorInputsPercentage = 0L;

    private Long afterProcessingFileSize = 0L;

    public DataStatsCollector(DataInfo dataInfo, FileInfo fileInfo) {
        this.dataInfo = dataInfo;
        this.fileInfo = fileInfo;
    }

    public void printData() {
        System.out.printf("Initial size = %d bytes\n", initialFileSize);
        System.out.printf("Rows count = %d\n", dataInfo.getRowsCount());
        System.out.printf("Columns count = %d\n", dataInfo.getColumnCount());
        System.out.printf("Total numbers count = %d\n", totalNumbersCount);
        System.out.printf("Non-null values count = %d\n", dataInfo.getNonNullValuesCount());
        System.out.printf("Empty symbols count = %d\n", dataInfo.getEmptyInputCount());
        System.out.printf("Error inputs count = %d\n", dataInfo.getErrorInputCount());
        System.out.printf("Anomaly rows count = %d\n", dataInfo.getAnomaliesCount());
        System.out.printf("Contradictory rows count = %d\n", dataInfo.getContradictoriesCount());
        System.out.printf("Anomaly score lowers = %s\n", dataInfo.getAnomalyScoreLowers());
        System.out.printf("Anomaly score uppers = %s\n", dataInfo.getAnomalyScoreUppers());
        System.out.printf("Geometric mean = %d\n", dataInfo.getGeometricMeanValue());
        System.out.printf("Anomaly row indexes: %s\n", dataInfo.getAnomalyRowIndexes());
    }

    public void analyzeDataFile(String fileName, boolean isOutput) throws IOException {
        initialFileSize = Files.size(Paths.get(fileName));

        FileInputStream inputStream = new FileInputStream(fileName);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        StringBuilder currentRowInputData;
        int currentRowIndex = 0;

        int lastReadSymbol;

        do {
            currentRowInputData = new StringBuilder();

            do {
                lastReadSymbol = bufferedInputStream.read();
                if (lastReadSymbol != -1) {
                    currentRowInputData.append((char) lastReadSymbol);
                }
            } while (lastReadSymbol != 10 && lastReadSymbol != -1);

            if (isOutput) {
                analyzeCurrentRowNumbersCount(currentRowInputData, currentRowIndex);
            } else {
                analyzeCurrentRow(currentRowInputData, currentRowIndex);
            }

            currentRowIndex++;
            if (currentRowIndex % 100 == 0 && currentRowIndex > 0) {
                System.gc();
            }
        } while (lastReadSymbol != -1);
        inputStream.close();
        dataInfo.setRowsCount(currentRowIndex - 1);
    }

    private void analyzeCurrentRow(StringBuilder currentRowInputData, int rowIndex) {
        if (currentRowInputData.length() > 1) {
            double meanValueResult = 1d;

            Long nonNullValues = 0L;
            int meanIterationValuesCount = 0;
            Long errorInputsCount = dataInfo.getErrorInputCount();
            Long emptyInputSymbolsCount = dataInfo.getEmptyInputCount();
            Long rowValuesSum = 0L;
            Long meanValue = 1L;

            int currentCharCode;
            char charSymbol;
            for (int symbolIndex = 0; symbolIndex < currentRowInputData.length() - 2; symbolIndex++) {
                currentCharCode = currentRowInputData.charAt(symbolIndex);
                if (currentCharCode >= '0' && currentCharCode <= '9') {
                    StringBuilder strNumber = new StringBuilder();
                    strNumber.append(Character.valueOf((char) currentCharCode));
                    int beginIndex = symbolIndex + 1;
                    charSymbol = currentRowInputData.charAt(beginIndex);
                    while (charSymbol >= '0'
                            && charSymbol <= '9') {
                        strNumber.append(charSymbol);
                        charSymbol = currentRowInputData.charAt(++beginIndex);
                    }
                    symbolIndex += strNumber.length() - 1;
                    Integer number = Integer.parseInt(strNumber.toString());
                    if (number > 0) {
                        meanValue *= number;
                    }
                    rowValuesSum += number;
                    nonNullValues++;
                    meanIterationValuesCount++;
                    totalNumbersCount++;
                    if ((meanIterationValuesCount % 5 == 0 && meanIterationValuesCount > 0)
                            || symbolIndex >= currentRowInputData.length() - 2) {
                        meanValueResult = Math.pow(meanValueResult * Math.pow(Double.valueOf(meanValue),
                                1d / (double) meanIterationValuesCount), 1d / 2d);

                        meanValue = 1L;
                        meanIterationValuesCount = 0;
                    }
                } else if (currentCharCode == '_') {
                    errorInputsCount++;
                    totalNumbersCount++;
                } else {
                    if (currentCharCode == ';' && symbolIndex == 0
                            || currentRowInputData.charAt(symbolIndex - 1) == ';') {
                        emptyInputSymbolsCount++;
                        totalNumbersCount++;
                    }
                }
            }

            Integer outputNum;
            if (currentRowInputData.charAt(currentRowInputData.length() - 1) == 10) {
                outputNum = Integer.parseInt(
                        String.valueOf(currentRowInputData.charAt(currentRowInputData.length() - 2)));
            } else {
                outputNum = Integer.parseInt(
                        String.valueOf(currentRowInputData.charAt(currentRowInputData.length() - 1)));
            }

            if (rowValuesSum % 2 != outputNum) {
                dataInfo.incrementContradictories();
                dataInfo.addContradictoryIndex(rowIndex);
            }

            BigDecimal meanValueBigDecimal = new BigDecimal(
                    Math.pow(dataInfo.getGeometricMeanValue() * meanValueResult, 1d / 2d));

            dataInfo.setGeometricMeanValue(
                    meanValueBigDecimal.longValue());
            dataInfo.setNonNullValuesCount(dataInfo.getNonNullValuesCount() + nonNullValues);
            dataInfo.setEmptyInputCount(emptyInputSymbolsCount);
            dataInfo.setErrorInputCount(errorInputsCount);
            dataInfo.setTotalSumValues(dataInfo.getTotalSumValues() + rowValuesSum);
        }
    }

    private void analyzeCurrentRowNumbersCount(StringBuilder currentRowInputData, int rowIndex) {
        if (currentRowInputData.length() > 1) {

            int currentCharCode;
            char charSymbol;
            for (int symbolIndex = 0; symbolIndex < currentRowInputData.length() - 2; symbolIndex++) {
                currentCharCode = currentRowInputData.charAt(symbolIndex);
                if (currentCharCode >= '0' && currentCharCode <= '9' || currentCharCode == '.') {
                    StringBuilder strNumber = new StringBuilder();
                    strNumber.append(Character.valueOf((char) currentCharCode));
                    int beginIndex = symbolIndex + 1;
                    charSymbol = currentRowInputData.charAt(beginIndex);
                    while (charSymbol >= '0'
                            && charSymbol <= '9'
                            || charSymbol == '.') {
                        strNumber.append(charSymbol);
                        charSymbol = currentRowInputData.charAt(++beginIndex);
                    }
                    symbolIndex += strNumber.length() - 1;
                    totalNumbersCount++;
                } else if (currentCharCode == '_') {
                    totalNumbersCount++;
                } else {
                    if (currentCharCode == ';' && symbolIndex == 0
                            || currentRowInputData.charAt(symbolIndex - 1) == ';') {
                        totalNumbersCount++;
                    }
                }
            }
        }
    }

    /*public void calculateCurrentAnomalies() throws IOException {
        FileInputStream inputStream = new FileInputStream(fileInfo.getInitialInputDataFilename());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        int lowerAnomalyScore = dataInfo.getAnomalyScoreLower();
        int upperAnomalyScore = dataInfo.getAnomalyScoreUpper();

        String inputStringValue;
        boolean isLastElement;
        boolean containsNewLine;
        int currentRowIndex = 0;
        int anomaliesPerCurrentRowCount = 0;
        int anomaliesThresholdLevel = dataInfo.getL();
        List<Integer> anomalyRowsList = new ArrayList<>(dataInfo.getRowsCount() / 10);

        do {
            inputStringValue = readValue(bufferedInputStream, true);
            isLastElement = inputStringValue.contains("f");
            containsNewLine = inputStringValue.contains("n");
            if (inputStringValue.matches("[0-9]+(n)?")) {
                if (containsNewLine) {
                    inputStringValue = inputStringValue.replace("n", "");
                }
                int value = Integer.parseInt(inputStringValue);
                if (value < lowerAnomalyScore || value > upperAnomalyScore) {
                    anomaliesPerCurrentRowCount++;
                }
            }
            if (containsNewLine || isLastElement) {
                if (anomaliesPerCurrentRowCount > anomaliesThresholdLevel) {
                    anomalyRowsList.add(currentRowIndex);
                }
                currentRowIndex++;
                if (currentRowIndex % 500 == 0) {
                    System.gc();
                }
                anomaliesPerCurrentRowCount = 0;
            }
        } while (!isLastElement);

        dataInfo.setAnomalyRowIndexes(anomalyRowsList);
        dataInfo.setAnomaliesCount((long) anomalyRowsList.size());
        inputStream.close();
    }*/

/*
    public void readAnomalyData() throws IOException {
        FileInputStream inputStream = new FileInputStream(fileInfo.getAnomaliesDataFilename());
        int readSymbol;

        StringBuilder lowerValueStr = new StringBuilder();
        do {
            readSymbol = inputStream.read();
            lowerValueStr.append((char) readSymbol);
        } while (readSymbol != 10);

        lowerValueStr.deleteCharAt(lowerValueStr.length() - 1);

        StringBuilder upperValueStr = new StringBuilder();
        readSymbol = inputStream.read();
        while (readSymbol != -1) {
            upperValueStr.append((char) readSymbol);
            readSymbol = inputStream.read();
        }
        inputStream.close();

        dataInfo.setAnomalyScoreLower(Integer.parseInt(lowerValueStr.toString()));
        dataInfo.setAnomalyScoreUpper(Integer.parseInt(upperValueStr.toString()));
    }
*/

    public void calculateColumns(String fileName) {
        try (InputStream inputStream = new FileInputStream(fileName)) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            int lastReadSymbol;
            int columnsCount = 0;
            do {
                lastReadSymbol = bufferedInputStream.read();
                if (lastReadSymbol == ';') {
                    columnsCount++;
                }
            } while (lastReadSymbol != 10);
            columnsCount++;
            dataInfo.setColumnCount(columnsCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetFileInfo() {
        dataInfo.setRowsCount(0);
        totalNumbersCount = 0L;
        dataInfo.setErrorInputCount(0L);
        dataInfo.setEmptyInputCount(0L);
        dataInfo.setAnomaliesCount(0L);
        dataInfo.setContradictoriesCount(0L);
        dataInfo.setNonNullValuesCount(0L);
    }

    public Long getTotalNumbersCount() {
        return totalNumbersCount;
    }

    public Long getFileSize(String fileName) throws IOException {
        return Files.size(Paths.get(fileName));
    }
}
