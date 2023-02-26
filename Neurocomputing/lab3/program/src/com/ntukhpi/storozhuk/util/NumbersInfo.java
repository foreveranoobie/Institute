package com.ntukhpi.storozhuk.util;

import static com.ntukhpi.storozhuk.util.MathUtil.DEFAULT_BIG_DECIMAL_ROUND;
import static com.ntukhpi.storozhuk.util.MathUtil.THREE_SYMBOLS_BIG_DECIMAL_ROUND;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class NumbersInfo {

    private int symbolsColumnsCount;
    private int symbolsRowsCount;
    private int sNeuronsCount;
    private int totalSymbolsCount;
    private BigDecimal m;
    private int zNeuronsCount;
    private List<List<Integer>> sBipolarInputs;
    private List<List<BigDecimal>> sWeights;
    private List<BigDecimal> uInputValuesForZNeurons;
    private List<BigDecimal> uOutputValuesForZNeurons;
    private List<List<BigDecimal>> resultValuesIterations;
    private BigDecimal k;
    private BigDecimal epsilon;
    private BigDecimal valueUn;

    public NumbersInfo(int symbolsColumnsCount, int symbolsRowsCount, int totalSymbolsCount,
            BigDecimal epsilon, BigDecimal valueUn) {
        this.symbolsColumnsCount = symbolsColumnsCount;
        this.symbolsRowsCount = symbolsRowsCount;
        this.sNeuronsCount = symbolsRowsCount * symbolsColumnsCount;
        this.totalSymbolsCount = totalSymbolsCount;
        this.zNeuronsCount = totalSymbolsCount;
        this.epsilon = epsilon;
        initM();
    }

    private void initM() {
        float value = sNeuronsCount;
        m = BigDecimal.valueOf(value / 2);
    }

    public int getSymbolsColumnsCount() {
        return symbolsColumnsCount;
    }

    public void setSymbolsColumnsCount(int symbolsColumnsCount) {
        this.symbolsColumnsCount = symbolsColumnsCount;
    }

    public int getSymbolsRowsCount() {
        return symbolsRowsCount;
    }

    public void setSymbolsRowsCount(int symbolsRowsCount) {
        this.symbolsRowsCount = symbolsRowsCount;
    }

    public List<List<BigDecimal>> getWeightsForSNeurons() {
        return sWeights;
    }

    public void setWeightsForSNeurons(List<List<BigDecimal>> sWeights) {
        this.sWeights = new ArrayList<>(sWeights);
    }

    public int getsNeuronsCount() {
        return sNeuronsCount;
    }

    public void setsNeuronsCount(int sNeuronsCount) {
        this.sNeuronsCount = sNeuronsCount;
    }

    public int getTotalSymbolsCount() {
        return totalSymbolsCount;
    }

    public void setTotalSymbolsCount(int totalSymbolsCount) {
        this.totalSymbolsCount = totalSymbolsCount;
    }

    public BigDecimal getM() {
        return m;
    }

    public void setM(BigDecimal m) {
        this.m = m;
    }

    public List<BigDecimal> getuInputValuesForZNeurons() {
        return uInputValuesForZNeurons;
    }

    public void setuInputValuesForZNeurons(List<BigDecimal> uInputValuesForZNeurons) {
        this.uInputValuesForZNeurons = new ArrayList<>(uInputValuesForZNeurons);
    }

    public int getzNeuronsCount() {
        return zNeuronsCount;
    }

    public void setzNeuronsCount(int zNeuronsCount) {
        this.zNeuronsCount = zNeuronsCount;
    }

    public List<List<Integer>> getsBipolarInputs() {
        return sBipolarInputs;
    }

    public void setsBipolarInputs(List<List<Integer>> sBipolarInputs) {
        this.sBipolarInputs = sBipolarInputs;
    }

    public BigDecimal getK() {
        return k;
    }

    public void setK(BigDecimal k) {
        this.k = k.round(DEFAULT_BIG_DECIMAL_ROUND);
        valueUn = BigDecimal.ONE.divide(k, 2, RoundingMode.HALF_DOWN);
    }

    public List<BigDecimal> getuOutputValuesForZNeurons() {
        return uOutputValuesForZNeurons;
    }

    public void setuOutputValuesForZNeurons(List<BigDecimal> uOutputValuesForZNeurons) {
        this.uOutputValuesForZNeurons = uOutputValuesForZNeurons;
    }

    public BigDecimal getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(BigDecimal epsilon) {
        this.epsilon = epsilon;
    }

    public BigDecimal getValueUn() {
        return valueUn;
    }

    public void setValueUn(BigDecimal valueUn) {
        this.valueUn = valueUn;
    }

    public List<List<BigDecimal>> getResultValuesIterations() {
        return resultValuesIterations;
    }

    public void setResultValuesIterations(List<List<BigDecimal>> resultValuesIterations) {
        this.resultValuesIterations = new ArrayList<>(resultValuesIterations);
    }
}
