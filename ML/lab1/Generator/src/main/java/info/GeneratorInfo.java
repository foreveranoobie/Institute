package info;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GeneratorInfo {

    private final int rowsCount;
    private final int columnsCount;
    private final int lowestValue;
    private final int highestValue;

    private final int contradictoryInfoPercentage;
    private final int spacesInfoPercentage;
    private final int anomaliesInfoPercantage;
    private final int errorInputInfoPercentage;

    private final String initialDataFilename;
    private final String completelyGeneratedDataFilename;
    private final String anomalyDataFilename;

    private int currentRowIndex = 0;
    private int totalSpacesCount = 0;
    private int totalErrorsCount = 0;
    private int totalContradictoriesCount;
    private int totalAnomaliesCount;

    private List<Integer> contradictoryRows;
    private List<Boolean> rowEvens;


    public GeneratorInfo(int rowsCount, int columnsCount, int lowestValue, int highestValue,
            int contradictoryInfoPercentage, int spacesInfoPercentage, int anomaliesInfoPercantage,
            int errorInputInfoPercentage, String initialDataFilename, String completelyGeneratedDataFilename,
            String anomalyDataFilename) {
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;
        this.lowestValue = lowestValue;
        this.highestValue = highestValue;
        this.contradictoryInfoPercentage = contradictoryInfoPercentage;
        this.spacesInfoPercentage = spacesInfoPercentage;
        this.anomaliesInfoPercantage = anomaliesInfoPercantage;
        this.errorInputInfoPercentage = errorInputInfoPercentage;
        this.initialDataFilename = initialDataFilename;
        this.completelyGeneratedDataFilename = completelyGeneratedDataFilename;
        this.anomalyDataFilename = anomalyDataFilename;
        calculateTotalSpacesCount();
        calculateErrorInputsCount();
        calculateContradictoryInputPercentage();
        calculateAnomaliesPercentage();
    }

    private void calculateAnomaliesPercentage() {
        BigDecimal totalInputs = BigDecimal.valueOf(rowsCount);
        totalInputs = totalInputs.divide(BigDecimal.valueOf(100))
                .multiply(BigDecimal.valueOf(anomaliesInfoPercantage));
        totalAnomaliesCount = totalInputs.intValue();
    }

    private void calculateContradictoryInputPercentage() {
        BigDecimal totalContradictoryInputs = BigDecimal.valueOf(rowsCount);
        totalContradictoryInputs = totalContradictoryInputs.divide(BigDecimal.valueOf(100))
                .multiply(BigDecimal.valueOf(contradictoryInfoPercentage));
        totalContradictoriesCount = totalContradictoryInputs.intValue();
    }

    private void calculateErrorInputsCount() {
        BigDecimal totalErrorInputs = BigDecimal.valueOf(columnsCount * rowsCount);
        totalErrorInputs = totalErrorInputs.divide(BigDecimal.valueOf(100))
                .multiply(BigDecimal.valueOf(errorInputInfoPercentage));
        totalErrorsCount = totalErrorInputs.intValue();
    }

    private void calculateTotalSpacesCount() {
        BigDecimal totalSpaces = BigDecimal.valueOf(columnsCount * rowsCount);
        totalSpaces = totalSpaces.divide(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(spacesInfoPercentage));
        totalSpacesCount = totalSpaces.intValue();
    }


    public int getRowsCount() {
        return rowsCount;
    }

    public int getColumnsCount() {
        return columnsCount;
    }

    public int getLowestValue() {
        return lowestValue;
    }

    public int getHighestValue() {
        return highestValue;
    }

    public int getContradictoryInfoPercentage() {
        return contradictoryInfoPercentage;
    }

    public int getSpacesInfoPercentage() {
        return spacesInfoPercentage;
    }

    public int getAnomaliesInfoPercantage() {
        return anomaliesInfoPercantage;
    }

    public int getErrorInputInfoPercentage() {
        return errorInputInfoPercentage;
    }

    public String getInitialDataFilename() {
        return initialDataFilename;
    }

    public int getCurrentRowIndex() {
        return currentRowIndex;
    }

    public void setCurrentRowIndex(int currentRowIndex) {
        this.currentRowIndex = currentRowIndex;
    }

    public int getTotalSpacesCount() {
        return totalSpacesCount;
    }

    public void decrementTotalSpacesCount() {
        totalSpacesCount = totalSpacesCount - 1;
    }

    public int getTotalErrorsCount() {
        return totalErrorsCount;
    }

    public void decrementTotalErrorsCount() {
        totalErrorsCount = totalErrorsCount - 1;
    }

    public int getTotalContradictoriesCount() {
        return totalContradictoriesCount;
    }

    public void decrementTotalContradictoriesCount() {
        totalContradictoriesCount = totalContradictoriesCount - 1;
    }

    public int getTotalAnomaliesCount() {
        return totalAnomaliesCount;
    }

    public String getCompletelyGeneratedDataFilename() {
        return completelyGeneratedDataFilename;
    }

    public String getAnomalyDataFilename() {
        return anomalyDataFilename;
    }

    public List<Integer> getContradictoryRows() {
        return contradictoryRows;
    }

    public void setContradictoryRows(List<Integer> contradictoryRows) {
        this.contradictoryRows = contradictoryRows;
    }

    public List<Boolean> getRowEvens() {
        return rowEvens;
    }

    public void setRowEvens(List<Boolean> rowEvens) {
        this.rowEvens = new ArrayList<>(rowEvens);
    }
}
