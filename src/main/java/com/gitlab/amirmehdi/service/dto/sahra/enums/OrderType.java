package com.gitlab.amirmehdi.service.dto.sahra.enums;


import com.gitlab.amirmehdi.domain.enumeration.Side;

/**
 * author : Amirmehdi Naghavi
 * 6/4/2019
 */
public enum OrderType {
    BUY(1),
    SELL(2);
    private final int value;

    OrderType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Side toOrderSide() {
        switch (this) {
            case BUY:
                return Side.BUY;
            case SELL:
                return Side.SELL;
        }
        throw new RuntimeException("unHandled side");
    }

    public static OrderType toOrderType(Side side) {
        switch (side) {
            case BUY:
                return BUY;
            case SELL:
                return SELL;
        }
        throw new RuntimeException("unHandled side");
    }

    public static OrderType get(int fromValue) {
        switch (fromValue) {
            case 1:
                return BUY;
            case 2:
                return SELL;
        }
        throw new RuntimeException("invalid value");
    }

}
