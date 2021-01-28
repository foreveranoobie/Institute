package levels;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FirstLevel extends Level {
    private BigDecimal numValue;
    private String withoutNumber;
    private int n;

    public FirstLevel(String name, BigDecimal numValue) {
        super(name);
        this.numValue = numValue;
        withoutNumber = name.replaceAll("\\d", "");
        n = 1;
    }

    public String getWithoutNumber() {
        return withoutNumber;
    }

    public BigDecimal getNumValue() {
        return numValue;
    }

    public void incrementN() {
        n++;
    }

    @Override
    public boolean equals(Object oppositeLevel) {
        return withoutNumber.trim().equals(((FirstLevel) oppositeLevel).getWithoutNumber().trim());
    }

    public BigDecimal calculateStat(FirstLevel unknownLevel, FirstLevel ethalonLevel) {
        BigDecimal newValue = ethalonLevel.getNumValue().multiply(new BigDecimal(n))
                .add(unknownLevel.getNumValue()).divide(new BigDecimal(n + 1), RoundingMode.HALF_UP);
        return newValue;
    }

    public void correctStat(FirstLevel unknownLevel) {
        BigDecimal newValue = getNumValue().multiply(new BigDecimal(n))
                .add(unknownLevel.getNumValue()).divide(new BigDecimal(n + 1), RoundingMode.HALF_UP);
        setName(getName().replace(numValue.toString(), newValue.toString()));
        numValue = newValue;
    }

    @Override
    public String toString() {
        return getName();
    }
}
