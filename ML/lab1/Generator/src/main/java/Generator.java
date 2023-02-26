import info.AnomalyInfo;
import info.GeneratorInfo;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {

    private final GeneratorInfo generatorInfo;
    private final AnomalyInfo anomalyInfo;
    private Random random;

    public Generator(GeneratorInfo generatorInfo, AnomalyInfo anomalyInfo) {
        this.generatorInfo = generatorInfo;
        this.anomalyInfo = anomalyInfo;
        random = new Random();
    }

    public void generateOutput() throws IOException {
        generatorInfo.setCurrentRowIndex(0);
        OutputStream outputStream = new FileOutputStream(generatorInfo.getInitialDataFilename());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        int rowsCount = generatorInfo.getRowsCount();
        int colsCount = generatorInfo.getColumnsCount();
        StringBuilder rowStringBuilder;
        int currentValue;
        int prevValue = 0;

        Long columnSum;
        Long totalSum = 0L;
        int nonNullValuesCount = 0;
        int resultValue;
        List<Integer> contradictoryRowsIndexes = new ArrayList<>();
        List<Boolean> rowEvens = new ArrayList<>(rowsCount);
        List<Integer> lastGeneratedRandomValues = new ArrayList<>(List.of(0, 1));

        for (int rowIndex = 0; rowIndex < rowsCount; rowIndex++) {
            rowStringBuilder = new StringBuilder();
            columnSum = 0L;
            for (int colIndex = 0; colIndex < colsCount - 1; colIndex++) {
                currentValue = getOrdinaryColumnValue(lastGeneratedRandomValues);
                prevValue = currentValue;
                //if value is generated number
                if (currentValue > -1) {
                    columnSum += currentValue;
                    rowStringBuilder.append(currentValue).append(";");
                    totalSum += currentValue;
                    nonNullValuesCount++;
                } else {
                    //if wrong input
                    if (currentValue == -2) {
                        rowStringBuilder.append("_;");
                    } else {
                        //if empty input
                        rowStringBuilder.append(";");
                    }
                }
                //Append to file if rowStringBuilder size > 200
                if (rowStringBuilder.length() > 200) {
                    bufferedOutputStream.write(rowStringBuilder.toString().getBytes(StandardCharsets.UTF_8));
                    bufferedOutputStream.flush();
                    rowStringBuilder = new StringBuilder();
                }
            }

            resultValue = getOutputColumn((int) (columnSum % 2));
            rowStringBuilder.append(resultValue);
            if (resultValue != columnSum % 2) {
                contradictoryRowsIndexes.add(rowIndex);
            }

            rowEvens.add(resultValue == 0);

            if (rowIndex < rowsCount - 1) {
                rowStringBuilder.append("\n");
            }
            bufferedOutputStream.write(rowStringBuilder.toString().getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
        }
        generatorInfo.setRowEvens(rowEvens);
        anomalyInfo.setTotalSumValues(totalSum);
        anomalyInfo.setNonNullValuesCount(nonNullValuesCount);
        generatorInfo.setContradictoryRows(contradictoryRowsIndexes);
        System.out.println("Contradictory row indexes: " + contradictoryRowsIndexes);
        outputStream.close();
    }

    private int getOutputColumn(int calculatedOutput) {
        int var = random.nextInt(2);
        int val = calculatedOutput;
        switch (var) {
            //correct output
            case 0:
                break;
            //contradictory output
            case 1:
                int totalContradictories = generatorInfo.getTotalContradictoriesCount();
                if (totalContradictories > 0) {
                    if (val == 1) {
                        val = 0;
                    } else {
                        val = 1;
                    }
                    generatorInfo.decrementTotalContradictoriesCount();
                }
                break;
        }
        return val;
    }

    private int getOrdinaryColumnValue(List<Integer> lastGeneratedValues) {
        int lowestValue = generatorInfo.getLowestValue();
        int highestValue = generatorInfo.getHighestValue();
        int var;
        do {
            var = random.nextInt(3);
        } while (lastGeneratedValues.contains(var));

        int value = -2;
        switch (var) {
            case 0:
                value = random.nextInt((highestValue - lowestValue + 1)
                        + lowestValue);
                break;
            case 1:
                int totalSpaces = generatorInfo.getTotalSpacesCount();
                if (totalSpaces == 0) {
                    value = random.nextInt((highestValue - lowestValue + 1)
                            + lowestValue);
                } else {
                    value = -1;
                    generatorInfo.decrementTotalSpacesCount();
                }
                break;
            case 2:
                int totalErrors = generatorInfo.getTotalErrorsCount();
                if (totalErrors == 0) {
                    value = random.nextInt((highestValue - lowestValue + 1)
                            + lowestValue);
                } else {
                    value = -2;
                    generatorInfo.decrementTotalErrorsCount();
                }
                break;
        }
        lastGeneratedValues.set(random.nextInt(2), value);
        return value;
    }

    public static void main(String[] args) throws IOException {
        GeneratorInfo generatorInfo = new GeneratorInfo(19000, 33000, 0, 100, 8, 27, 28, 29,
                "data.csv", "completeData.csv", "anomaly.txt");

        AnomalyInfo anomalyInfo = new AnomalyInfo();
        anomalyInfo.setL(1);
        anomalyInfo.setK(new BigDecimal(1.5f));
        Generator generator = new Generator(generatorInfo, anomalyInfo);
        generator.generateOutput();
        System.gc();
        System.out.println("Done generating .csv. Generating anomalies!");
    }
}
