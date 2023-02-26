package com.ntukhpi.storozhuk.corrector;

import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.math.BigDecimal;
import java.util.List;

public abstract class WeightCorrector {

    protected NumbersInfo numbersInfo;

    public WeightCorrector(NumbersInfo numbersInfo) {
        this.numbersInfo = numbersInfo;
    }

    public abstract void correctReactiveInputWeights(List<BigDecimal> currentWeights, List<Integer> activeWeightsBinaries,
            boolean isNegativeReactive);
}
