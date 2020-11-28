package com.gitlab.amirmehdi.service.dto.sahra.enums;


import com.gitlab.amirmehdi.domain.enumeration.OrderState;

/**
 * author : Amirmehdi Naghavi
 * 6/4/2019
 */
public enum OrderStatus {
    Created(1),
    Active(2),
    Executed(3),
    Canceled(4),
    Error(5),
    PartialExecuted(6);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public OrderState toOrderState() {
        switch (this.getValue()) {
            case 1:
                return OrderState.NONE;
            case 2:
                return OrderState.ACTIVE;
            case 3:
                return OrderState.EXECUTED;
            case 4:
                return OrderState.CANCELLED;
            case 5:
                return OrderState.ERROR;
            case 6:
                return OrderState.PARTIALLY_EXECUTED;
        }
        throw new RuntimeException("unhandled order status");
    }

    public static OrderStatus get(int fromValue) {
        switch (fromValue) {
            case 1:
                return Created;
            case 2:
                return Active;
            case 3:
                return Executed;
            case 4:
                return Canceled;
            case 5:
                return Error;
            case 6:
                return PartialExecuted;
        }
        throw new RuntimeException("invalid value");
    }
}
