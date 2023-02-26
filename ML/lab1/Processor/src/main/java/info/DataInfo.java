package info;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataInfo {

    private BigDecimal K;
    private int L;
    private int optimizationThreshold;

    private BigDecimal averageElementValue = BigDecimal.ZERO;
    private BigDecimal delta;
    private Long geometricMeanValue = 1L;

    private List<Integer> anomalyScoreLowers = new ArrayList<>();
    private List<Integer> anomalyScoreUppers = new ArrayList<>();

    private Long totalSumValues = 0L;
    private Long nonNullValuesCount = 0L;
    private Long contradictoriesCount = 0L;
    private Long anomaliesCount = 0L;
    private Long emptyInputCount = 0L;
    private Long errorInputCount = 0L;

    private List<Integer> contradictoryRowIndexes = new ArrayList<>();
    private List<Integer> anomalyRowIndexes;
    private List<Integer> toRemoveOptimizationColumnIndexes = new ArrayList<>();
    private int columnCount;
    private int rowsCount;

    private int minValue;
    private int maxValue;
    private List<Integer> anomaliesRowIndexes;


    public DataInfo(BigDecimal K, int L, int optimizationThreshold) {
        this.K = K;
        this.L = L;
        this.optimizationThreshold = optimizationThreshold;
    }

    public Long getTotalSumValues() {
        return totalSumValues;
    }

    public void setTotalSumValues(Long totalSumValues) {
        this.totalSumValues = totalSumValues;
    }

    public Long getNonNullValuesCount() {
        return nonNullValuesCount;
    }

    public void setNonNullValuesCount(Long nonNullValuesCount) {
        this.nonNullValuesCount = nonNullValuesCount;
    }

    public BigDecimal getAverageElementValue() {
        return averageElementValue;
    }

    public void setAverageElementValue(BigDecimal averageElementValue) {
        this.averageElementValue = averageElementValue;
    }

    public BigDecimal getK() {
        return K;
    }

    public int getL() {
        return L;
    }

    public Long getContradictoriesCount() {
        return contradictoriesCount;
    }

    public void setContradictoriesCount(Long contradictoriesCount) {
        this.contradictoriesCount = contradictoriesCount;
    }

    public void incrementContradictories() {
        contradictoriesCount++;
    }

    public Long getAnomaliesCount() {
        return anomaliesCount;
    }

    public void setAnomaliesCount(Long anomaliesCount) {
        this.anomaliesCount = anomaliesCount;
    }

    public Long getEmptyInputCount() {
        return emptyInputCount;
    }

    public void setEmptyInputCount(Long emptyInputCount) {
        this.emptyInputCount = emptyInputCount;
    }

    public Long getErrorInputCount() {
        return errorInputCount;
    }

    public void setErrorInputCount(Long errorInputCount) {
        this.errorInputCount = errorInputCount;
    }

    public BigDecimal getDelta() {
        return delta;
    }

    public void setDelta(BigDecimal delta) {
        this.delta = delta;
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(int rowsCount) {
        this.rowsCount = rowsCount;
    }

    public List<Integer> getAnomalyRowIndexes() {
        return anomalyRowIndexes;
    }

    public void setAnomalyRowIndexes(List<Integer> anomalyRowIndexes) {
        this.anomalyRowIndexes = new ArrayList<>(anomalyRowIndexes);
    }

    public Long getGeometricMeanValue() {
        return geometricMeanValue;
    }

    public void setGeometricMeanValue(Long geometricMeanValue) {
        this.geometricMeanValue = geometricMeanValue;
    }

    public List<Integer> getContradictoryRowIndexes() {
        return contradictoryRowIndexes;
    }

    public void setContradictoryRowIndexes(List<Integer> contradictoryRowIndexes) {
        this.contradictoryRowIndexes = new ArrayList<>(contradictoryRowIndexes);
    }

    public void addContradictoryIndex(Integer index) {
        if (contradictoryRowIndexes != null) {
            contradictoryRowIndexes.add(index);
        }
    }

    public void setColumnCount(int columnCont) {
        this.columnCount = columnCont;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getOptimizationThreshold() {
        return optimizationThreshold;
    }

    public List<Integer> getToRemoveOptimizationColumnIndexes() {
        return toRemoveOptimizationColumnIndexes;
    }

    public void setToRemoveOptimizationColumnIndexes(List<Integer> toRemoveOptimizationColumnIndexes) {
        this.toRemoveOptimizationColumnIndexes = new ArrayList<>(toRemoveOptimizationColumnIndexes);
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public List<Integer> getAnomaliesRowIndexes() {
        return anomaliesRowIndexes;
    }

    public void setAnomaliesRowIndexes(List<Integer> anomaliesRowIndexes) {
        this.anomaliesRowIndexes = anomaliesRowIndexes;
    }

    public List<Integer> getAnomalyScoreUppers() {
        return anomalyScoreUppers;
    }

    public void setAnomalyScoreUppers(List<Integer> anomalyScoreUppers) {
        this.anomalyScoreUppers = new ArrayList<>(anomalyScoreUppers);
    }

    public List<Integer> getAnomalyScoreLowers() {
        return anomalyScoreLowers;
    }

    public void setAnomalyScoreLowers(List<Integer> anomalyScoreLowers) {
        this.anomalyScoreLowers = new ArrayList<>(anomalyScoreLowers);
    }
}
