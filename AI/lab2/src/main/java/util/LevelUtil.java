package util;

import levels.DefaultLevel;
import levels.FirstLevel;
import levels.FourthLevel;
import levels.Level;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LevelUtil {
    private static Map<String, Function<String, Level>> levelFunctions;

    static {
        levelFunctions = new HashMap<>();
        levelFunctions.put("i=1", attr -> getFirstLevel(attr));
        levelFunctions.put("i=4", attr -> getFourthLevel(attr));
    }

    public static Level getLevel(String[] dividedAttribute) {
        Function levelFunction = levelFunctions.get(dividedAttribute[0]);
        if (levelFunction == null) {
            return new DefaultLevel(dividedAttribute[1]);
        }
        return (Level) levelFunction.apply(dividedAttribute[1]);
    }

    public static boolean compareAttributes(FirstLevel unknownLevel, FirstLevel firstEthalon, FirstLevel secondEthalon) {
        BigDecimal firstValue = firstEthalon.calculateStat(unknownLevel, firstEthalon);
        BigDecimal secondValue = secondEthalon.calculateStat(unknownLevel, secondEthalon);
        double firstMod = unknownLevel.getNumValue().subtract(firstValue).abs().doubleValue();
        double secondMod = unknownLevel.getNumValue().subtract(secondValue).abs().doubleValue();
        return firstMod <= secondMod;
    }

    public static boolean numValueIsInRange(Level unknownLevel, Level ethalon) {
        FourthLevel unknown = (FourthLevel) unknownLevel;
        FourthLevel ethalonLevel = (FourthLevel) ethalon;
        return numValueIsInRange(unknown, ethalonLevel);
    }

    public static boolean numValueIsInRange(FourthLevel unknownLevel, FourthLevel ethalon) {
        return ethalon.isInRange(unknownLevel.getMinValue());
    }

    public static Level getFirstLevel(String attributes) {
        BigDecimal numValue = new BigDecimal(attributes.replaceAll("[^\\d]", ""));
        return new FirstLevel(attributes, numValue);
    }

    public static Level getFourthLevel(String attributes) {
        BigDecimal numValue = new BigDecimal(attributes.replaceAll("[^\\d]", ""));
        return new FourthLevel(attributes.replace(numValue.toString(), "[" + numValue + ", max]"), numValue);
    }

    public static boolean compareAttributes(Level unknownLevel, Level firstEthalon, Level secondEthalon) {
        FirstLevel first = (FirstLevel) firstEthalon;
        FirstLevel second = (FirstLevel) secondEthalon;
        FirstLevel unknown = (FirstLevel) unknownLevel;
        return compareAttributes(unknown, first, second);
    }
}
