package com.gitlab.amirmehdi.service.dto.sahra.enums;

/**
 * author : Amirmehdi Naghavi
 * 6/4/2019
 */
public enum OrderSource {
    Web(1),
    Notification(2),
    Algorithmic(3),
    API(4),
    Admin(5),
    InitialOffering(6),
    Backoffice(7),
    Old(8),
    Mobile(9);

    private final int value;

    OrderSource(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OrderSource get(int fromValue) {
        switch (fromValue) {
            case 1:
                return Web;
            case 2:
                return Notification;
            case 3:
                return Algorithmic;
            case 4:
                return API;
            case 5:
                return Admin;
            case 6:
                return InitialOffering;
            case 7:
                return Backoffice;
            case 8:
                return Old;
            case 9:
                return Mobile;
        }
        throw new RuntimeException("invalid value");
    }
}
