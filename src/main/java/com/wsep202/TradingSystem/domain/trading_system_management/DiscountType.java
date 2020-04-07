package com.wsep202.TradingSystem.domain.trading_system_management;

import java.util.Arrays;
import com.wsep202.TradingSystem.domain.exception.*;

public enum DiscountType {

    OPEN_DISCOUNT("Open discount");
    public final String type;

    DiscountType(String type) {
        this.type = type;
    }

    public static DiscountType getDiscountType(String type) {
        return Arrays.stream(DiscountType.values())
                .filter(discountType -> discountType.type.equals(type))
                .findFirst().orElseThrow(() -> new DiscountTypeDontExistException(type));
    }
}
