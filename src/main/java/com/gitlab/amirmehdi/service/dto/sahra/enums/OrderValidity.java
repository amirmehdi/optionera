package com.gitlab.amirmehdi.service.dto.sahra.enums;


import com.gitlab.amirmehdi.domain.enumeration.Validity;

/**
 * author : Amirmehdi Naghavi
 * 6/4/2019
 */
public enum OrderValidity {
    Day(1),
    GoodTillCancelled(2),
    GoodTillDate(3),
    FillAndKill(4);
    private final int value;

    OrderValidity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Validity toValidity() {
        switch (this) {
            case Day:
                return Validity.DAY;
            case FillAndKill:
                return Validity.FILL_AND_KILL;
        }
        throw new RuntimeException("unhandled order validity");
    }

    public static OrderValidity fromOrderValidity(Validity validity) {
        switch (validity) {
            case DAY:
                return Day;
            case FILL_AND_KILL:
                return FillAndKill;
        }
        throw new RuntimeException("unhandled order validity");
    }

    public static OrderValidity get(int fromValue) {
        switch (fromValue) {
            case 1:
                return Day;
            case 2:
                return GoodTillCancelled;
            case 3:
                return GoodTillDate;
            case 4:
                return FillAndKill;
        }
        throw new RuntimeException("invalid value");
    }
}
