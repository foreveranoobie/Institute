package com.ntukhpi.storozhuk.analyzer;

import static com.ntukhpi.storozhuk.util.MathUtil.ONE_SYMBOL_BIG_DECIMAL_ROUND;

import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class HammingAnalyzer {

    private NumbersInfo numbersInfo;

    public HammingAnalyzer(NumbersInfo numbersInfo) {
        this.numbersInfo = numbersInfo;
    }

    public void calculateWeightsForS() {
        List<List<Integer>> sBipolarInputsList = numbersInfo.getsBipolarInputs();
        List<List<BigDecimal>> sWeights = new ArrayList<>(sBipolarInputsList.size());
        for (List<Integer> sBipolarInputs : sBipolarInputsList) {
            List<BigDecimal> currentWeight = new ArrayList<>(sBipolarInputs.size());
            for (Integer sBipolarInput : sBipolarInputs) {
                float floatBipolarValue = sBipolarInput;
                currentWeight.add(BigDecimal.valueOf(floatBipolarValue / 2).round(ONE_SYMBOL_BIG_DECIMAL_ROUND));
            }
            sWeights.add(currentWeight);
        }
        numbersInfo.setWeightsForSNeurons(sWeights);
    }

    public void calculateUInputsForInputSymbol(List<Integer> inputBipolarValues) {
        BigDecimal m = numbersInfo.getM();

        List<List<BigDecimal>> sInputValues = numbersInfo.getWeightsForSNeurons();
        List<BigDecimal> uValues = new ArrayList<>(numbersInfo.getTotalSymbolsCount());

        for (int symbolIndex = 0; symbolIndex < numbersInfo.getTotalSymbolsCount(); symbolIndex++) {
            BigDecimal currentUValue = new BigDecimal(BigInteger.ZERO);

            List<BigDecimal> sCurrentInputValues = sInputValues.get(symbolIndex);

            int bipolarIndex = 0;

            for (BigDecimal sCurrentInputValue : sCurrentInputValues) {
                currentUValue = currentUValue.add(sCurrentInputValue
                        .multiply(BigDecimal.valueOf(inputBipolarValues.get(bipolarIndex++))));
            }

            uValues.add(currentUValue.add(m));
        }
        numbersInfo.setuInputValuesForZNeurons(uValues);
    }

    public void calculateUOutputsForZNeurons() {
        List<BigDecimal> zNeuronsOutputs = new ArrayList<>(numbersInfo.getzNeuronsCount());

        for (BigDecimal neuronInput : numbersInfo.getuInputValuesForZNeurons()) {
            if (neuronInput.compareTo(BigDecimal.ZERO) <= 0) {
                zNeuronsOutputs.add(new BigDecimal(BigInteger.ZERO));
            } else if (neuronInput.compareTo(numbersInfo.getValueUn()) <= 0) {
                zNeuronsOutputs.add(neuronInput.multiply(numbersInfo.getK()));
            } else {
                zNeuronsOutputs.add(numbersInfo.getValueUn());
            }
        }

        numbersInfo.setuOutputValuesForZNeurons(zNeuronsOutputs);
    }
}
