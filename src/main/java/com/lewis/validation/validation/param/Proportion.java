package com.lewis.validation.validation.param;

/**
 * @date : 2021/8/11
 */
public enum Proportion {
    FULL(100),
    FIFTY(50);

    private int value;

    Proportion(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
