package com.gitlab.amirmehdi.service.dto.sahra.enums;

/**
 * author : Amirmehdi Naghavi
 * 6/4/2019
 */
public enum OrderCreditSource {
    Broker(1),
    Saman(2),
    Melat(3);

    private final int value;

    OrderCreditSource(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OrderCreditSource get(int fromValue) {
        switch (fromValue) {
            case 1:
                return Broker;
            case 2:
                return Saman;
            case 3:
                return Melat;

        }
        throw new RuntimeException("invalid value");
    }
}
