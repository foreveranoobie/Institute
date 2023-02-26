package com.ntukhpi.storozhuk.analyzer;

import static com.ntukhpi.storozhuk.util.MathUtil.DEFAULT_BIG_DECIMAL_ROUND;

import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LearnAnalyzer {

    private List<BigDecimal> sensoryToAssociativeWeights;
    private List<BigDecimal> associativeToReactiveWeights;
    private NumbersInfo numbersInfo;

    public LearnAnalyzer(NumbersInfo numbersInfo) {
        this.numbersInfo = numbersInfo;
    }

    public void analyzeInputSymbol(List<Integer> inputSymbolSignals) {
        calculateAssociativeInputs(inputSymbolSignals);
    }

    public List<BigDecimal> calculateAssociativeInputs(List<Integer> inputSymbolSignals) {
        List<BigDecimal> associativeInputs = new ArrayList<>(numbersInfo.getAssociativeNeuronsNumber());
        for (int associativeNeuronNum = 0; associativeNeuronNum < numbersInfo.getAssociativeNeuronsNumber();
                associativeNeuronNum++) {
            int sensoryCellIndex = 0;
            BigDecimal currentAssociativeNeuronInputValue = BigDecimal.ZERO;
            for (int inputSymbolValue : inputSymbolSignals) {
                BigDecimal currentCalculated = sensoryToAssociativeWeights.get(
                                associativeNeuronNum * (numbersInfo.getSensoryNeuronsNumber()) + sensoryCellIndex++)
                        .multiply(BigDecimal.valueOf(inputSymbolValue));
                currentAssociativeNeuronInputValue = currentAssociativeNeuronInputValue.add(currentCalculated);
            }
            associativeInputs.add(currentAssociativeNeuronInputValue.round(DEFAULT_BIG_DECIMAL_ROUND));
        }
        return associativeInputs;
    }

    public BigDecimal calculateReactiveInput(List<Integer> associativeOutputReactions) {
        BigDecimal resultInput = BigDecimal.ZERO;
        int associativeWeightIndex = 0;
        List<BigDecimal> associativeToReactiveWeights = numbersInfo.getReactiveWeights();
        for (int associativeOutputReaction : associativeOutputReactions) {
            BigDecimal associativeWeight = associativeToReactiveWeights.get(associativeWeightIndex++);
            resultInput = resultInput.add(associativeWeight.multiply(BigDecimal.valueOf(associativeOutputReaction)));
        }
        return resultInput;
    }

    public void setSensoryToAssociativeWeights(List<BigDecimal> sensoryToAssociativeWeights) {
        this.sensoryToAssociativeWeights = new ArrayList<>(sensoryToAssociativeWeights);
    }

    public List<BigDecimal> getAssociativeToReactiveWeights() {
        return associativeToReactiveWeights;
    }

    public void setAssociativeToReactiveWeights(List<BigDecimal> associativeToReactiveWeights) {
        this.associativeToReactiveWeights = associativeToReactiveWeights;
    }
}
