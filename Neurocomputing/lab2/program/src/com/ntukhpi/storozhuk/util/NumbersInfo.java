package com.ntukhpi.storozhuk.util;

import com.ntukhpi.storozhuk.panel.AssociativeToReactivePanel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NumbersInfo {

    private AssociativeToReactivePanel associativeToReactivePanel;

    private int sensoryNeuronsNumber;
    private int associativeNeuronsNumber;
    private int reactiveNeuronsNumber;
    private BigDecimal theta;
    private String negativeReactiveSymbol = "";
    private String positiveReactiveSymbol = "";
    private boolean isAlphaConfirmationSelected;
    private BigDecimal averageReactiveTheta;
    private BigDecimal correctionStep;
    private List<BigDecimal> reactiveWeights;
    private boolean isLastRecognizedSymbolPositive;

    public NumbersInfo() {
    }

    public NumbersInfo(int sensoryNeuronsNumber, int associativeNeuronsNumber, int reactiveNeuronsNumber) {
        this.sensoryNeuronsNumber = sensoryNeuronsNumber;
        this.associativeNeuronsNumber = associativeNeuronsNumber;
        this.reactiveNeuronsNumber = reactiveNeuronsNumber;
    }

    public void updateAssociativeOutputWeightsPanel(List<BigDecimal> updatedWeights) {
        associativeToReactivePanel.updateWeightsTable(updatedWeights);
    }

    public int getSensoryNeuronsNumber() {
        return sensoryNeuronsNumber;
    }

    public void setSensoryNeuronsNumber(int sensoryNeuronsNumber) {
        this.sensoryNeuronsNumber = sensoryNeuronsNumber;
    }

    public int getAssociativeNeuronsNumber() {
        return associativeNeuronsNumber;
    }

    public void setAssociativeNeuronsNumber(int associativeNeuronsNumber) {
        this.associativeNeuronsNumber = associativeNeuronsNumber;
    }

    public int getReactiveNeuronsNumber() {
        return reactiveNeuronsNumber;
    }

    public void setReactiveNeuronsNumber(int reactiveNeuronsNumber) {
        this.reactiveNeuronsNumber = reactiveNeuronsNumber;
    }

    public BigDecimal getTheta() {
        return theta;
    }

    public void setTheta(BigDecimal theta) {
        this.theta = theta;
    }

    public String getNegativeReactiveSymbol() {
        return negativeReactiveSymbol;
    }

    public void setNegativeReactiveSymbol(String negativeReactiveSymbol) {
        this.negativeReactiveSymbol = negativeReactiveSymbol;
    }

    public String getPositiveReactiveSymbol() {
        return positiveReactiveSymbol;
    }

    public void setPositiveReactiveSymbol(String positiveReactiveSymbol) {
        this.positiveReactiveSymbol = positiveReactiveSymbol;
    }

    public boolean isAlphaConfirmationSelected() {
        return isAlphaConfirmationSelected;
    }

    public void setAlphaConfirmationSelected(boolean alphaConfirmationSelected) {
        isAlphaConfirmationSelected = alphaConfirmationSelected;
    }

    public BigDecimal getAverageReactiveTheta() {
        return averageReactiveTheta;
    }

    public void setAverageReactiveTheta(BigDecimal averageReactiveTheta) {
        this.averageReactiveTheta = averageReactiveTheta;
    }

    public BigDecimal getCorrectionStep() {
        return correctionStep;
    }

    public void setCorrectionStep(BigDecimal correctionStep) {
        this.correctionStep = correctionStep;
    }

    public List<BigDecimal> getReactiveWeights() {
        return reactiveWeights;
    }

    public void setReactiveWeights(List<BigDecimal> reactiveWeights) {
        this.reactiveWeights = new ArrayList<>(reactiveWeights);
    }

    public boolean isLastRecognizedSymbolPositive() {
        return isLastRecognizedSymbolPositive;
    }

    public void setLastRecognizedSymbolPositive(boolean lastRecognizedSymbolPositive) {
        isLastRecognizedSymbolPositive = lastRecognizedSymbolPositive;
    }

    public void setAssociativeToReactivePanel(AssociativeToReactivePanel associativeToReactivePanel) {
        this.associativeToReactivePanel = associativeToReactivePanel;
    }
}
