package com.kodekittu.movieticketbooking.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyUtils {

    public static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    public static final BigDecimal TAX_RATE = new BigDecimal("0.18");
    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100.00");

    private MoneyUtils() {
    }

    public static BigDecimal money(BigDecimal amount) {
        if (amount == null) {
            return ZERO;
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
