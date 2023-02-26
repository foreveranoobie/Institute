package com.ntukhpi.storozhuk.util;

import java.math.MathContext;
import java.math.RoundingMode;

public class MathUtil {

    public static final MathContext DEFAULT_BIG_DECIMAL_ROUND = new MathContext(2, RoundingMode.HALF_DOWN);
    public static final MathContext THREE_SYMBOLS_BIG_DECIMAL_ROUND = new MathContext(3, RoundingMode.HALF_DOWN);
    public static final MathContext ONE_SYMBOL_BIG_DECIMAL_ROUND = new MathContext(1);
}
