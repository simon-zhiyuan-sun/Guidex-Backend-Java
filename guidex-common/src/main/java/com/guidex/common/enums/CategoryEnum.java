package com.guidex.common.enums;

/**
 * @author kled2
 * @date 2025/7/6
 */
public enum CategoryEnum {
    SKI(0, "ski"),
    SNOWBOARD(1, "snowboard"),
    ;
    private int code;
    private String desc;

    CategoryEnum(int code, String desc) {
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
        for (CategoryEnum value : CategoryEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        return null;
    }
}

