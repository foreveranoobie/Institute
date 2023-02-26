import info.AnomalyInfo;
import info.GeneratorInfo;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AnomalyGenerator {

    private final AnomalyInfo anomalyInfo;
    private final GeneratorInfo generatorInfo;

    public AnomalyGenerator(GeneratorInfo generatorInfo, AnomalyInfo anomalyInfo) {
        this.anomalyInfo = anomalyInfo;
        this.generatorInfo = generatorInfo;
    }

    public void saveAnomalyInfo() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(generatorInfo.getAnomalyDataFilename());
        outputStream.write(String.valueOf(anomalyInfo.getAnomalyScoreLower()).getBytes(StandardCharsets.UTF_8));
        outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
        outputStream.flush();

        outputStream.write(String.valueOf(anomalyInfo.getAnomalyScoreUpper()).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    public void generateAnomalies() throws IOException {
        calculateAnomalyIndexes();

        doAnomaliesGeneration();
    }

    private void calculateAnomalyIndexes() {
        List<Integer> contradictoryRows = new ArrayList<>(generatorInfo.getContradictoryRows());
        int rowsCount = generatorInfo.getRowsCount();
        int anomalyCount = generatorInfo.getTotalAnomaliesCount();
        Set<Integer> anomalyRowIndexes = new HashSet<>(anomalyCount);
        Random random = new Random();
        int randomAnomalyRowValue;
        do {

            do {
                randomAnomalyRowValue = random.nextInt(rowsCount);
            } while (contradictoryRows.contains(randomAnomalyRowValue));

            anomalyRowIndexes.add(randomAnomalyRowValue);
        } while (anomalyRowIndexes.size() < anomalyCount);

        List<Integer> anomalyRows = new ArrayList<>(anomalyRowIndexes);

        anomalyInfo.setAnomaliesRowIndexes(anomalyRows);
        System.out.println("Calculated row indexes for anomalies: " + anomalyRows);
    }

    private void doAnomaliesGeneration() throws IOException {
        //Read file
        FileInputStream inputStream = new FileInputStream(generatorInfo.getInitialDataFilename());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        //Write file
        FileOutputStream outputStream = new FileOutputStream(generatorInfo.getCompletelyGeneratedDataFilename());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        List<Integer> anomalyIndexes = new ArrayList<>(anomalyInfo.getAnomaliesRowIndexes());

        Integer currentRowIndex = 0;

        StringBuilder currentRowData;
        int lastReadSymbol;
        do {
            currentRowData = new StringBuilder();
            System.out.println("Read");
            do {
                lastReadSymbol = bufferedInputStream.read();
                if (lastReadSymbol != -1) {
                    currentRowData.append((char) lastReadSymbol);
                }
            } while (lastReadSymbol != 10 && lastReadSymbol != -1);
            System.out.println("Recalculate");
            if (anomalyIndexes.contains(currentRowIndex)) {
                anomalyIndexes.indexOf(currentRowIndex);
                recalculateAnomalyRowData(currentRowData, currentRowIndex);
            } else {
                recalculateSingleRowData(currentRowData, currentRowIndex);
            }

            currentRowIndex++;
            System.out.println(currentRowIndex);

            System.out.println("Write");
            bufferedOutputStream.write(currentRowData.toString().getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
            if (currentRowIndex % 100 == 0 && currentRowIndex > 0) {
                System.gc();
            }
        } while (lastReadSymbol != -1);

        inputStream.close();
        outputStream.close();
    }

    private void recalculateAnomalyRowData(StringBuilder currentRowData, int rowIndex) {
        int L = anomalyInfo.getL();
        int columnsCount = generatorInfo.getColumnsCount();

        int leftLowerValue = 2;
        int rightLowerValue = 16;

        int leftUpperValue = 80;
        int rightUpperValue = 99;

        int leftNormalValue = 30;
        int rightNormalValue = 75;

        Random random = new Random();

        int currentSymbolIndex = 0;

        Set<Integer> anomalyColumnIndexes = new HashSet<>();
        for (int i = 0; i < L; i++) {
            anomalyColumnIndexes.add(random.nextInt(columnsCount - 2));
        }

        boolean isEvenRow = generatorInfo.getRowEvens().get(rowIndex);
        int sum = 0;

        for (int symIndex = 0; symIndex < currentRowData.length() - 2; symIndex++) {
            if (currentRowData.charAt(symIndex) >= '0' && currentRowData.charAt(symIndex) <= '9') {
                StringBuilder strNumber = new StringBuilder();
                strNumber.append(currentRowData.charAt(symIndex));
                int beginIndex = symIndex + 1;
                while (beginIndex < currentRowData.length() && (currentRowData.charAt(beginIndex) >= '0'
                        && currentRowData.charAt(beginIndex) <= '9')) {
                    strNumber.append(currentRowData.charAt(beginIndex++));
                }

                Integer number = Integer.parseInt(strNumber.toString());

                //If this number should be normal value
                if (!anomalyColumnIndexes.contains(currentSymbolIndex)) {
                    //But it is anomaly and should become as normal one
                    if (number < leftNormalValue || number > rightNormalValue) {
                        strNumber = new StringBuilder().append(
                                random.nextInt(1 + rightNormalValue - leftNormalValue) + leftNormalValue);
                        currentRowData.replace(symIndex, beginIndex, strNumber.toString());
                    }
                    symIndex += strNumber.length() - 1;
                } else {
                    //If this number should be as anomaly but it's normal now
                    if (number >= leftNormalValue && number <= rightNormalValue) {
                        //If random wants to generate value from 2 to 16
                        if (random.nextInt(2) == 0) {
                            strNumber = new StringBuilder().append(
                                    random.nextInt(1 + rightLowerValue - leftLowerValue) + leftLowerValue);
                        } else {
                            strNumber = new StringBuilder().append(
                                    random.nextInt(1 + rightUpperValue - leftUpperValue) + leftUpperValue);
                        }
                        currentRowData.replace(symIndex, beginIndex, strNumber.toString());
                        symIndex += strNumber.length() - 1;
                        sum = (sum + Integer.parseInt(strNumber.toString())) % 2;
                    }
                }

            } else if (currentRowData.charAt(symIndex) == ';') {
                currentSymbolIndex++;
            }
        }
        //If parity is not kept
        if ((sum % 2 == 0) != isEvenRow) {
            int generatedNumber = isEvenRow ? 0 : 1;
            int symbolIndex = currentRowData.length() - 1;
            char symbolCode;
            do {
                symbolCode = currentRowData.charAt(symbolIndex--);
            } while (symbolCode < '0' || symbolCode > '9');
            symbolIndex++;
            currentRowData.replace(symbolIndex, symbolIndex + 1, Character.toString(symbolCode));

/*            do {
                if (currentRowData.charAt(currentSymbolIndex) >= '0'
                        && currentRowData.charAt(currentSymbolIndex) <= '9') {
                    //readWholeNumber
                    StringBuilder strNumber = new StringBuilder();
                    strNumber.append(currentRowData.charAt(currentSymbolIndex));
                    int beginIndex = currentSymbolIndex + 1;
                    while (beginIndex < currentRowData.length() && (currentRowData.charAt(beginIndex) >= '0'
                            && currentRowData.charAt(beginIndex) <= '9')) {
                        strNumber.append(currentRowData.charAt(beginIndex++));
                    }

                    isPresentNumberUneven = Integer.parseInt(strNumber.toString()) % 2 == 1;
                    //If we should regenerate anomaly
                    if (anomalyColumnIndexes.contains(currentColumnIndex)) {
                        //If random wants to generate value from 2 to 16
                        if (random.nextInt(2) == 0) {
                            do {
                                generatedNumber = random.nextInt(1 + rightLowerValue - leftLowerValue) + leftLowerValue;
                            } while ((generatedNumber % 2 == 1) == isPresentNumberUneven);

                            strNumber = new StringBuilder().append(generatedNumber);
                        } else {
                            do {
                                generatedNumber = random.nextInt(1 + rightUpperValue - leftUpperValue) + leftUpperValue;
                            } while ((generatedNumber % 2 == 1) == isPresentNumberUneven);

                            strNumber = new StringBuilder().append(
                                    random.nextInt(1 + rightUpperValue - leftUpperValue) + leftUpperValue);
                        }
                        currentRowData.replace(currentSymbolIndex, beginIndex, strNumber.toString());
                        currentSymbolIndex += strNumber.length() - 1;
                        isSymbolRead = true;
                    } else {
                        do {
                            generatedNumber = random.nextInt(1 + rightNormalValue - leftNormalValue) + leftNormalValue;
                        } while ((generatedNumber % 2 == 1) == isPresentNumberUneven);

                        strNumber = new StringBuilder().append(generatedNumber);
                        currentRowData.replace(currentColumnIndex, beginIndex, strNumber.toString());
                        currentColumnIndex += strNumber.length() - 1;

                        isSymbolRead = true;
                    }
                } else if (currentRowData.charAt(currentSymbolIndex) == ';') {
                    currentColumnIndex++;
                    currentSymbolIndex++;
                } else {
                    currentSymbolIndex++;
                }
            } while (!isSymbolRead);*/
        }
    }

    private void recalculateSingleRowData(StringBuilder currentRowData, int rowIndex) {
        int leftNormalValue = 40;
        int rightNormalValue = 60;

        int generatedNumber;

        Random random = new Random();
        int sum = 0;
        for (int symIndex = 0; symIndex < currentRowData.length() - 2; symIndex++) {
            if (currentRowData.charAt(symIndex) >= '0' && currentRowData.charAt(symIndex) <= '9') {
                StringBuilder strNumber = new StringBuilder();
                strNumber.append(currentRowData.charAt(symIndex));
                int beginIndex = symIndex + 1;
                while (beginIndex < currentRowData.length() && (currentRowData.charAt(beginIndex) >= '0'
                        && currentRowData.charAt(beginIndex) <= '9')) {
                    strNumber.append(currentRowData.charAt(beginIndex++));
                }
                generatedNumber = random.nextInt(1 + rightNormalValue - leftNormalValue) + leftNormalValue;
                strNumber = new StringBuilder().append(generatedNumber);
                currentRowData.replace(symIndex, beginIndex, strNumber.toString());
                symIndex += strNumber.length() - 1;
                sum = (sum + generatedNumber) % 2;
            }
        }

        boolean isEvenRow = generatorInfo.getRowEvens().get(rowIndex);

        if ((sum % 2 == 0) != isEvenRow) {
            boolean isSymbolRead = false;
            int currentSymbolIndex = 0;
            int currentColumnIndex = 0;
            boolean isPresentNumberUneven;
            do {
                if (currentRowData.charAt(currentSymbolIndex) >= '0'
                        && currentRowData.charAt(currentSymbolIndex) <= '9') {
                    StringBuilder strNumber = new StringBuilder();
                    strNumber.append(currentRowData.charAt(currentSymbolIndex));
                    int beginIndex = currentSymbolIndex + 1;
                    while (beginIndex < currentRowData.length() && (currentRowData.charAt(beginIndex) >= '0'
                            && currentRowData.charAt(beginIndex) <= '9')) {
                        strNumber.append(currentRowData.charAt(beginIndex++));
                    }
                    isPresentNumberUneven = Integer.parseInt(strNumber.toString()) % 2 == 1;
                    do {
                        generatedNumber = random.nextInt(1 + rightNormalValue - leftNormalValue) + leftNormalValue;
                    } while ((generatedNumber % 2 == 1) == isPresentNumberUneven);

                    strNumber = new StringBuilder().append(generatedNumber);
                    currentRowData.replace(currentSymbolIndex, beginIndex, strNumber.toString());
                    currentSymbolIndex += strNumber.length() - 1;
                    sum = (sum + generatedNumber) % 2;
                    isSymbolRead = true;
                } else if (currentRowData.charAt(currentSymbolIndex) == ';') {
                    currentColumnIndex++;
                    currentSymbolIndex++;
                } else {
                    currentSymbolIndex++;
                }
            } while (!isSymbolRead);
        }
    }

    private String readValue(InputStream inputStream, boolean parseNewLines) throws IOException {
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
}
