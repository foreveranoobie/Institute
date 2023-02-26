package com.ntukhpi.storozhuk.util;

import com.ntukhpi.storozhuk.panel.InputSymbolPanel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NumbersInfo {

    private final int rowsCount;
    private final int columnsCount;
    private final int totalSymbolsCount;

    private List<Integer> lastSymbolBipolarValues;
    private List<Integer> lastSymbolOutputValues;

    private List<Integer> thetas;
    private BigDecimal theta;
    private BigDecimal selectedTheta;
    private List<List<Integer>> idealSymbolsBipolarValues;
    private List<BigDecimal> relationWeights;
    private List<String> idealSymbolNames;

    public NumbersInfo(int rowsCount, int columnsCount, int totalSymbolsCount) {
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;
        this.totalSymbolsCount = totalSymbolsCount;
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public int getColumnsCount() {
        return columnsCount;
    }

    public int getTotalSymbolsCount() {
        return totalSymbolsCount;
    }

    public List<List<Integer>> getIdealSymbolsBipolarValues() {
        return idealSymbolsBipolarValues;
    }

    public void setIdealSymbolsBipolarValues(List<List<Integer>> idealSymbolsBipolarValues) {
        this.idealSymbolsBipolarValues = new ArrayList<>(idealSymbolsBipolarValues);
    }

    public List<BigDecimal> getRelationWeights() {
        return relationWeights;
    }

    public void setRelationWeights(List<BigDecimal> relationWeights) {
        this.relationWeights = new ArrayList<>(relationWeights);
    }

    public BigDecimal getTheta() {
        return theta;
    }

    public void setTheta(BigDecimal theta) {
        this.theta = theta;
    }

    public BigDecimal getSelectedTheta() {
        return selectedTheta;
    }

    public void setSelectedTheta(BigDecimal selectedTheta) {
        this.selectedTheta = selectedTheta;
    }

    public List<String> getIdealSymbolNames() {
        return idealSymbolNames;
    }

    public void setIdealSymbolNames(List<String> idealSymbolNames) {
        this.idealSymbolNames = new ArrayList<>(idealSymbolNames);
    }

    public List<Integer> getLastSymbolBipolarValues() {
        return lastSymbolBipolarValues;
    }

    public void setLastSymbolBipolarValues(List<Integer> lastSymbolBipolarValues) {
        this.lastSymbolBipolarValues = new ArrayList<>(lastSymbolBipolarValues);
    }

    public List<Integer> getLastSymbolOutputValues() {
        return lastSymbolOutputValues;
    }

    public void setLastSymbolOutputValues(List<Integer> lastSymbolOutputValues) {
        this.lastSymbolOutputValues = new ArrayList<>(lastSymbolOutputValues);
    }

    public List<Integer> getThetas() {
        return thetas;
    }

    public void setThetas(List<Integer> thetas) {
        this.thetas = new ArrayList<>(thetas);
    }
}
