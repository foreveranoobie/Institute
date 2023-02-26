package com.ntukhpi.storozhuk.corrector;

import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class GammaWeightCorrector extends WeightCorrector {

    public GammaWeightCorrector(NumbersInfo numbersInfo) {
        super(numbersInfo);
    }

    @Override
    public void correctReactiveInputWeights(List<BigDecimal> currentWeights, List<Integer> activeWeightsBinaries,
            boolean isPositiveSymbolRecognized) {
        int activeNeuronsCount = 0;
        int totalNeuronsCount = 0;
        int activeWeightBinaryIndex = 0;
        List<Integer> activeWeightIndexes = new ArrayList<>();

        for (int activeWeightsBinaryValue : activeWeightsBinaries) {
            BigDecimal currentActiveWeight = currentWeights.get(activeWeightBinaryIndex);
            if (isActiveWeight(currentActiveWeight)) {
                totalNeuronsCount++;
                if (activeWeightsBinaryValue == 1) {
                    activeWeightIndexes.add(activeWeightBinaryIndex);
                    activeNeuronsCount++;
                }
            }
            activeWeightBinaryIndex++;
        }

        BigDecimal correctionStep = numbersInfo.getCorrectionStep();

        BigDecimal inactiveWeightsCorrection = correctionStep.multiply(BigDecimal.valueOf(activeNeuronsCount))
                .divide(BigDecimal.valueOf(totalNeuronsCount), 5, RoundingMode.HALF_DOWN);

        BigDecimal activeWeightsCorrection = correctionStep.add(inactiveWeightsCorrection);

        int weightIndex = 0;
        List<BigDecimal> updatedWeights = new ArrayList<>(currentWeights.size());
        for (BigDecimal weight : currentWeights) {
            if (activeWeightIndexes.contains(weightIndex++)) {
                if (isPositiveSymbolRecognized) {
                    updatedWeights.add(makeGammaCorrectionForWeightSubtracted(weight, activeWeightsCorrection));
                } else {
                    updatedWeights.add(makeGammaCorrectionForWeightAdded(weight, activeWeightsCorrection));
                }
            } else {
                if (isPositiveSymbolRecognized) {
                    updatedWeights.add(makeGammaCorrectionForWeightSubtracted(weight, inactiveWeightsCorrection));
                } else {
                    updatedWeights.add(makeGammaCorrectionForWeightAdded(weight, inactiveWeightsCorrection));
                }
            }
        }
        numbersInfo.updateAssociativeOutputWeightsPanel(updatedWeights);
    }

    private BigDecimal makeGammaCorrectionForWeightAdded(BigDecimal initialWeight, BigDecimal correction) {
        initialWeight = initialWeight.add(correction);
        if (initialWeight.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        } else if (initialWeight.compareTo(BigDecimal.ONE) > 0) {
            return BigDecimal.ONE;
        }
        return initialWeight;
    }

    private BigDecimal makeGammaCorrectionForWeightSubtracted(BigDecimal initialWeight, BigDecimal correction) {
        initialWeight = initialWeight.subtract(correction);
        if (initialWeight.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        } else if (initialWeight.compareTo(BigDecimal.ONE) > 0) {
            return BigDecimal.ONE;
        }
        return initialWeight;
    }

    private boolean isActiveWeight(BigDecimal weightValue) {
        return weightValue.compareTo(BigDecimal.ZERO) != 0 && weightValue.compareTo(BigDecimal.ONE) != 0;
    }
}
