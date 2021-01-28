package levels;

import java.math.BigDecimal;

public class FourthLevel extends Level {
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private String withoutNumber;

    public FourthLevel(String name, BigDecimal minValue) {
        super(name);
        this.minValue = minValue;
        this.maxValue = minValue;
        withoutNumber = name.replaceAll("\\[\\d+, [A-Za-z0-9]+\\]", "");
    }

    public String getWithoutNumber() {
        return withoutNumber;
    }

    public boolean equals(Object oppositeLevel) {
        return withoutNumber.trim().equals(((FourthLevel) oppositeLevel).getWithoutNumber().trim());
    }

    public boolean isInRange(BigDecimal value) {
        return value.subtract(minValue).doubleValue() >= 0 && value.subtract(maxValue).doubleValue() <= 0;
    }

    public void correctRange(FourthLevel unknown) {
        double currMaxValue = getMaxValue().doubleValue();
        double currMinValue = getMinValue().doubleValue();
        double unknownValue = unknown.getMinValue().doubleValue();
        if (unknownValue > currMaxValue) {
            if (getName().contains("max]")) {
                setName(getName().replace("max]", unknown.getMinValue() + "]"));
            } else {
                setName(getName().replace(getMaxValue() + "]", unknown.getMinValue() + "]"));
            }
            setMaxValue(unknown.getMinValue());
        } else if (unknownValue < currMinValue) {
            setName(getName().replace("[" + getMinValue(), "[" + unknown.getMinValue()));
            setMinValue(unknown.getMinValue());
        }
    }

    public void setMinValue(BigDecimal newMinValue) {
        minValue = newMinValue;
    }

    public void setMaxValue(BigDecimal newMaxValue) {
        maxValue = newMaxValue;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }
}
