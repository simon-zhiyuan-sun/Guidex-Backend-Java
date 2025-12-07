package com.guidex.common.enums;

/**
 * @author kled2
 * @date 2025/5/20
 */
public enum ThumbEnum {
    THUMB_NONE(0, "无"),
    THUMB_UP(1, "点赞"),
    THUMB_DOWN(2, "点踩");

    private int code;
    private String desc;

    ThumbEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
