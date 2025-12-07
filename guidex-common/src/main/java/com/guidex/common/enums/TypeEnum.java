package com.guidex.common.enums;

/**
 * @author kled2
 * @date 2025/7/6
 */
public enum TypeEnum {
    FLOW(0, "flow"),
    CARVING(1, "carving"),
    ;

    private int code;
    private String desc;

    TypeEnum(int code, String desc) {
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
        for (TypeEnum value : TypeEnum.values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        return null;
    }
}
