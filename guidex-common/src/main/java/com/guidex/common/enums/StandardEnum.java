package com.guidex.common.enums;

/**
 * @author kled2
 * @date 2025/7/6
 */
public enum StandardEnum {
    GENERAL(0, "general"),
    AASI(1, "AASI"),
    BASI(2, "BASI"),
    CASI(3, "CASI"),
    ;

    private int code;
    private String desc;

    StandardEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(int code) {
        for (StandardEnum value : StandardEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        return null;
    }
}
