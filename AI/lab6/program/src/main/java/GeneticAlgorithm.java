import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class GeneticAlgorithm {
    private DecimalFormat df;
    private Map<Float, Float> minFuncValues;

    public GeneticAlgorithm() {
        df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.CEILING);
        minFuncValues = new LinkedHashMap<>();
    }

    //[-10;20]
    public List<Float> doAlgorithm(List<Float> population) {
        List<Float> functions = new ArrayList<>(population.size());
        List<Float> fitness = new ArrayList<>(population.size());
        for (float num : population) {
            functions.add(function(num));
        }
        System.out.println(population);
        System.out.println(functions);
        getCurrentIterationMin(functions, population);
        float funcSums = 0f;
        for (float fun : functions) {
            funcSums += fun;
        }
        for (float fun : functions) {
            fitness.add((fun / funcSums) * 100);
        }
        List<Float> randomPullRange = new ArrayList<>(population.size());
        float currentRange;
        for (int index = 0; index < population.size(); index++) {
            currentRange = (float) (Math.round((Math.random() * (100)) * 100000d) / 100000d);
            randomPullRange.add(currentRange);
        }
        List<Float> fitnessRoulette = new ArrayList<>(100);
        float currentRouletteFitSum = 0f;
        for (float fit : fitness) {
            currentRouletteFitSum += fit;
            fitnessRoulette.add(currentRouletteFitSum);
        }
        List<Integer> roulettePairsRanged = new ArrayList<>();
        for (float pullItem : randomPullRange) {
            for (int index = 0; index < fitnessRoulette.size(); index++) {
                if (pullItem < fitnessRoulette.get(index)) {
                    roulettePairsRanged.add(index);
                    break;
                }
            }
        }
        List<Float> crossovers = getCrossovers(roulettePairsRanged, population);
        int locust;
        Random rand = new Random();
        List<Float> newPopulation = new ArrayList<>(crossovers.size());
        for (int index = 0; index < crossovers.size() - 1; index += 2) {
            locust = rand.nextInt(29) + 1;
            shuffleBits(crossovers.get(index), crossovers.get(index + 1), locust, newPopulation);
        }
        System.out.println(newPopulation + "\n-------------------");
        return newPopulation;
    }

    private void shuffleBits(float firstNumber, float secondNumber, int locust, List<Float> toAdd) {
        String firstBinary = Integer.toBinaryString(Float.floatToIntBits(firstNumber));
        String secondBinary = Integer.toBinaryString(Float.floatToIntBits(secondNumber));
        String firstLocust = firstBinary.substring(locust + (firstBinary.length() - 30));
        String secondLocust = secondBinary.substring(locust + (secondBinary.length() - 30));
        secondBinary = secondBinary.substring(0, locust + (secondBinary.length() - 30)) + firstLocust;
        firstBinary = firstBinary.substring(0, locust + (firstBinary.length() - 30)) + secondLocust;
        firstNumber = stringToFloat(firstBinary);
        secondNumber = stringToFloat(secondBinary);
        if (firstNumber > 20) {
            firstNumber = firstNumber % 20;
        } else if (firstNumber < -10) {
            firstNumber = firstNumber % -10;
        }
        if (secondNumber > 20) {
            secondNumber = secondNumber % 20;
        } else if (secondNumber < -10) {
            secondNumber = secondNumber % -10;
        }
        toAdd.add(firstNumber);
        toAdd.add(secondNumber);
    }

    private List<Float> getCrossovers(List<Integer> roulettePairs, List<Float> initialPopulation) {
        float initialThreshold = 0.4f;
        List<Float> crossovers = new ArrayList<>();
        float currentThreshold;
        for (int index = 0; index < roulettePairs.size() - 1; index++) {
            currentThreshold = (float) Math.random();
            if (currentThreshold <= initialThreshold) {
                crossovers.add(initialPopulation.get(index));
                crossovers.add(initialPopulation.get(index + 1));
            }
        }
        return crossovers;
    }

    public float function(float x) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        float result;
        result = (float) (1f / Math.pow(x - 0.3, 2) + 1f / Math.pow(x - 0.9, 2));
        return Float.parseFloat(df.format(result).replaceFirst(",", "."));
    }

    public float stringToFloat(String s) {
        boolean sign = s.length() == 32 && s.charAt(0) == '1';
        if (sign) {
            s = s.substring(1);
        }
        float convertedNum = 0;
        String exp = s.substring(0, s.length() - 23);
        s = s.replaceFirst(exp, "");
        int exponentValue = 0;
        for (int index = exp.length(); index < 8; index++) {
            exp = "0" + exp;
        }
        for (int index = 0; index < exp.length(); index++) {
            if (exp.charAt(index) == '1') {
                exponentValue += Math.pow(2, 7 - index);
            }
        }
        exponentValue -= 127;
        convertedNum += Math.pow(2, exponentValue);
        exponentValue--;
        for (int index = 0; index < s.length(); index++) {
            if (s.charAt(index) == '1') {
                convertedNum += Math.pow(2, exponentValue);
            }
            exponentValue--;
        }
        if (sign) {
            convertedNum *= -1;
        }
        return convertedNum;
    }

    private void getCurrentIterationMin(List<Float> functions, List<Float> population) {
        DecimalFormat resultDecimalFormat = new DecimalFormat("#.###");
        Iterator<Float> functionsIterator = functions.iterator();
        int index = 0;
        int minIndex = 0;
        float min = functionsIterator.next();
        float current;
        while (functionsIterator.hasNext()) {
            current = functionsIterator.next();
            index++;
            if (current < min && !resultDecimalFormat.format(current).equals("0.0")) {
                minIndex = index;
                min = current;
            }
        }
        minFuncValues.put(population.get(minIndex), min);
    }

    public Map<Float, Float> getMinFuncValues() {
        return minFuncValues;
    }
}
