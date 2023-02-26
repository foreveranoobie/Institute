package com.ntukhpi.storozhuk.analyzer;

import static com.ntukhpi.storozhuk.util.MathUtil.DEFAULT_BIG_DECIMAL_ROUND;
import static com.ntukhpi.storozhuk.util.MathUtil.THREE_SYMBOLS_BIG_DECIMAL_ROUND;

import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MaxnetAnalyzer {

    private final NumbersInfo numbersInfo;

    public MaxnetAnalyzer(NumbersInfo numbersInfo) {
        this.numbersInfo = numbersInfo;
    }

    public void calculateOutput() {
        List<List<BigDecimal>> outputValuesIterations = new ArrayList<>();
        outputValuesIterations.add(new ArrayList<>(numbersInfo.getuOutputValuesForZNeurons()));

        List<BigDecimal> previousValuesIteration = new ArrayList<>(numbersInfo.getuOutputValuesForZNeurons());
        List<BigDecimal> currentValuesIteration;
        do {
            currentValuesIteration = new ArrayList<>(numbersInfo.getTotalSymbolsCount());
            for (int index = 0; index < numbersInfo.getTotalSymbolsCount(); index++) {
                BigDecimal previousOutputValue = previousValuesIteration.get(index);
                List<BigDecimal> allValuesExceptCalculatedCurrent = new ArrayList<>(previousValuesIteration);
                allValuesExceptCalculatedCurrent.remove(index);

                BigDecimal calculatedCurrentOutputValue =
                        calculateCurrentOutputValue(previousOutputValue, allValuesExceptCalculatedCurrent)
                                .round(DEFAULT_BIG_DECIMAL_ROUND);

                currentValuesIteration.add(calculatedCurrentOutputValue);
            }
            outputValuesIterations.add(currentValuesIteration);
            previousValuesIteration = new ArrayList<>(currentValuesIteration);
        } while (!isInTerminalStage(currentValuesIteration));

        numbersInfo.setResultValuesIterations(outputValuesIterations);
    }

    private BigDecimal calculateCurrentOutputValue(BigDecimal previousOutputValue,
            List<BigDecimal> otherPreviousValues) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal sumOfPreviousValues = BigDecimal.ZERO;
        for (BigDecimal otherPreviousValue : otherPreviousValues) {
            sumOfPreviousValues = sumOfPreviousValues.add(otherPreviousValue);
        }
        sumOfPreviousValues = sumOfPreviousValues.multiply(numbersInfo.getEpsilon());
        result = previousOutputValue.subtract(sumOfPreviousValues);

        if (result.compareTo(BigDecimal.ZERO) <= 0) {
            return new BigDecimal(BigInteger.ZERO);
        }
        return result;
    }

    private boolean isInTerminalStage(List<BigDecimal> values) {
        int zeroesCount = (int) values.stream()
                .filter(value -> value.setScale(3, RoundingMode.HALF_DOWN).compareTo(BigDecimal.ZERO) == 0).count();
        return zeroesCount >= values.size() - 1;
    }
}
