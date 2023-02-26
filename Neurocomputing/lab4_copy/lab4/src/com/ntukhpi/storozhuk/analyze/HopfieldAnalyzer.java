package com.ntukhpi.storozhuk.analyze;

import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class HopfieldAnalyzer {

    private final NumbersInfo numbersInfo;

    public HopfieldAnalyzer(NumbersInfo numbersInfo) {
        this.numbersInfo = numbersInfo;
    }

    public void calculateRelationWeights() {
        int inputNeuronsCount = numbersInfo.getRowsCount() * numbersInfo.getColumnsCount();
        int weightsCount = (int) Math.pow(numbersInfo.getRowsCount() * numbersInfo.getColumnsCount(), 2);
        List<BigDecimal> outputWeights = new ArrayList<>(weightsCount);
        for (int weight = 0; weight < weightsCount; weight++) {
            outputWeights.add(BigDecimal.ZERO);
        }
        BigDecimal symbolsCount = BigDecimal.valueOf(numbersInfo.getTotalSymbolsCount());
        for (int rowIndex = 0; rowIndex < inputNeuronsCount - 1; rowIndex++) {
            int startRowElementIndex = rowIndex * inputNeuronsCount;
            for (int columnIndex = rowIndex; columnIndex < inputNeuronsCount; columnIndex++) {
                int currentElementIndex = startRowElementIndex + columnIndex;
                outputWeights.set(currentElementIndex,
                        calculateWeightForRowAndColumn(rowIndex, columnIndex).divide(symbolsCount, 4,
                                RoundingMode.HALF_DOWN));
            }
            int copyColumnIndex = rowIndex;
            for (int copyRowIndex = rowIndex + 1; copyRowIndex < inputNeuronsCount; copyRowIndex++) {
                BigDecimal symmetricValue = outputWeights.get(rowIndex * inputNeuronsCount + copyRowIndex);
                outputWeights.set(inputNeuronsCount * copyRowIndex + copyColumnIndex, symmetricValue);
            }
        }
        numbersInfo.setRelationWeights(outputWeights);
    }

    public List<BigDecimal> calculateNeuronInputs(List<Integer> bipolarValues) {
        List<BigDecimal> weights = numbersInfo.getRelationWeights();
        List<BigDecimal> neuronInputs = new ArrayList<>(bipolarValues.size());

        int totalWeightsNumber = numbersInfo.getRowsCount() * numbersInfo.getColumnsCount();

        for (int weightRow = 0; weightRow < totalWeightsNumber; weightRow++) {
            BigDecimal currentSum = BigDecimal.ZERO;
            for (int columnIndex = 0; columnIndex < totalWeightsNumber; columnIndex++) {
                currentSum = currentSum.add(weights.get((weightRow * totalWeightsNumber) + columnIndex));
                //currentSum += weights.get((weightRow * totalWeightsNumber) + columnIndex);
            }
            currentSum = currentSum.multiply(BigDecimal.valueOf(bipolarValues.get(weightRow)));
            neuronInputs.add(currentSum);
        }
        return neuronInputs;
    }

    public List<Integer> calculateNeuronOutputs(List<Integer> bipolarValues, List<BigDecimal> neuronInputs) {
        List<Integer> neuronOutputs = new ArrayList<>(neuronInputs.size());
        int neuronIndex = 0;
        BigDecimal theta = BigDecimal.ZERO;
        for (BigDecimal input : neuronInputs) {
            int comparisonResult = input.compareTo(theta);
            if (comparisonResult >= 0) {
                neuronOutputs.add(1);
            } else {
                neuronOutputs.add(-1);
            } /*else {
                neuronOutputs.add(bipolarValues.get(neuronIndex));
            }*/
            neuronIndex++;
        }

/*        for (int input : neuronInputs) {
            //int theta = numbersInfo.getThetas().get(neuronIndex);
            if (input > theta) {
                neuronOutputs.add(1);
            } else if (input < theta) {
                neuronOutputs.add(-1);
            } else {
                neuronOutputs.add(bipolarValues.get(neuronIndex));
            }
            neuronIndex++;
        }*/
        return neuronOutputs;
    }

    public void calculateTheta() {
        int sum = 0;

/*        int totalWeights = numbersInfo.getRowsCount() * numbersInfo.getColumnsCount();
        List<Integer> weightsMatrix = numbersInfo.getRelationWeights();
        List<Integer> neuronWeights = new ArrayList<>();*/
/*        for (int row = 0; row < totalWeights; row++) {

            for (int column = 0; column < totalWeights; column++) {
                sum += weightsMatrix.get((row * totalWeights) + column);
            }
            neuronWeights.add(sum / numbersInfo.getTotalSymbolsCount());
        }

        numbersInfo.setThetas(neuronWeights);
        numbersInfo.setTheta(new BigDecimal(0));*/
        for (BigDecimal weightValue : numbersInfo.getRelationWeights()) {
            //sum += weightValue;
        }
        sum /= numbersInfo.getTotalSymbolsCount();
        //numbersInfo.setTheta(BigDecimal.valueOf(sum));
        numbersInfo.setTheta(BigDecimal.ZERO);
    }

    private BigDecimal calculateWeightForRowAndColumn(int rowNum, int colNum) {
        if (rowNum == colNum) {
            return BigDecimal.ZERO;
        }
        List<List<Integer>> bipolarInputValues = numbersInfo.getIdealSymbolsBipolarValues();
        BigDecimal sum = BigDecimal.ZERO;
        for (List<Integer> currentBipolarValues : bipolarInputValues) {
            sum = BigDecimal.valueOf(currentBipolarValues.get(rowNum) * currentBipolarValues.get(colNum));
        }
        return sum;
    }
}
