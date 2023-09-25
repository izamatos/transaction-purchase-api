package com.wex.purchase.transaction.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Formatter {

    public static Double formatTwoDecimalPlaces(Double value) {
        BigDecimal decimal = BigDecimal.valueOf(value);
        decimal = decimal.setScale(2, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }
}
