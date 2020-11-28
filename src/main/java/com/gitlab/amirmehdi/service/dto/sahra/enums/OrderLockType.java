package com.gitlab.amirmehdi.service.dto.sahra.enums;

/**
 * author : Amirmehdi Naghavi
 * 6/4/2019
 */
public enum OrderLockType {
    UnLock(1),
    LockForCreate(2),
    LockForEdit(3),
    LockForCancel(4);

    private final int value;

    OrderLockType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OrderLockType get(int fromValue) {
        switch (fromValue) {
            case 1:
                return UnLock;
            case 2:
                return LockForCreate;
            case 3:
                return LockForEdit;
            case 4:
                return LockForCancel;
        }
        throw new RuntimeException("invalid value");
    }
}
