package com.ntukhpi.storozhuk.corrector;

import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AlphaWeightCorrector extends WeightCorrector {

    public AlphaWeightCorrector(NumbersInfo numbersInfo) {
        super(numbersInfo);
    }

    @Override
    public void correctReactiveInputWeights(List<BigDecimal> currentWeights, List<Integer> activeWeightsBinaries,
            boolean isPositiveSymbolRecognized) {
        List<Integer> activeWeightIndexes = new ArrayList<>(currentWeights.size() / 2);
        int activeWeightBinaryIndex = 0;
        for (int activeWeightsBinary : activeWeightsBinaries) {
            if (activeWeightsBinary == 1) {
                activeWeightIndexes.add(activeWeightBinaryIndex);
            }
            activeWeightBinaryIndex++;
        }
        BigDecimal correctionStep = numbersInfo.getCorrectionStep();
        for (Integer activeWeightIndex : activeWeightIndexes) {
            BigDecimal weightValue = currentWeights.get(activeWeightIndex);
            if (isPositiveSymbolRecognized) {
                weightValue = weightValue.subtract(correctionStep);
                if (weightValue.compareTo(BigDecimal.ZERO) < 0) {
                    weightValue = BigDecimal.ZERO;
                }
            } else {
                weightValue = weightValue.add(correctionStep);
                if (weightValue.compareTo(BigDecimal.ONE) > 0) {
                    weightValue = BigDecimal.ONE;
                }
            }
            currentWeights.set(activeWeightIndex, weightValue);
        }
        numbersInfo.updateAssociativeOutputWeightsPanel(currentWeights);
    }
}
