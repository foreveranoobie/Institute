package info;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AnomalyInfo {

    private BigDecimal averageElementValue;
    private BigDecimal delta;
    private int anomalyScoreLower;
    private int anomalyScoreUpper;
    private BigDecimal K;
    private int L;
    private Long totalSumValues;
    private int nonNullValuesCount;

    private List<Integer> anomaliesRowIndexes;
    private List<Integer> calculatedAnomaliesRowIndexes;

    public BigDecimal getAverageElementValue() {
        return averageElementValue;
    }

    public void setAverageElementValue(BigDecimal averageElementValue) {
        this.averageElementValue = averageElementValue;
    }

    public BigDecimal getDelta() {
        return delta;
    }

    public void setDelta(BigDecimal delta) {
        this.delta = delta;
    }

    public int getAnomalyScoreLower() {
        return anomalyScoreLower;
    }

    public void setAnomalyScoreLower(int anomalyScoreLower) {
        this.anomalyScoreLower = anomalyScoreLower;
    }

    public int getAnomalyScoreUpper() {
        return anomalyScoreUpper;
    }

    public void setAnomalyScoreUpper(int anomalyScoreUpper) {
        this.anomalyScoreUpper = anomalyScoreUpper;
    }

    public BigDecimal getK() {
        return K;
    }

    public void setK(BigDecimal k) {
        K = k;
    }

    public int getL() {
        return L;
    }

    public void setL(int l) {
        L = l;
    }

    public Long getTotalSumValues() {
        return totalSumValues;
    }

    public void setTotalSumValues(Long totalSumValues) {
        this.totalSumValues = totalSumValues;
    }

    public int getNonNullValuesCount() {
        return nonNullValuesCount;
    }

    public void setNonNullValuesCount(int nonNullValuesCount) {
        this.nonNullValuesCount = nonNullValuesCount;
    }

    public List<Integer> getAnomaliesRowIndexes() {
        return anomaliesRowIndexes;
    }

    public void setAnomaliesRowIndexes(List<Integer> anomaliesRowIndexes) {
        this.anomaliesRowIndexes = new ArrayList<>(anomaliesRowIndexes);
    }

    public List<Integer> getCalculatedAnomaliesRowIndexes() {
        return calculatedAnomaliesRowIndexes;
    }

    public void setCalculatedAnomaliesRowIndexes(List<Integer> calculatedAnomaliesRowIndexes) {
        this.calculatedAnomaliesRowIndexes = new ArrayList<>(calculatedAnomaliesRowIndexes);
    }
}
