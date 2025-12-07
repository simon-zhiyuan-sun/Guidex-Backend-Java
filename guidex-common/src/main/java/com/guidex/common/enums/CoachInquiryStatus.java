package com.guidex.common.enums;

/**
 * @author kled2
 * @date 2025/7/31
 */
public enum CoachInquiryStatus {
    UN_INQUIRY(0, "未咨询"),
    INQUIRING(1, "咨询中"),
    INQUIRY_SUCCESS(2, "已被回复"),
    ;

    private int code;
    private String desc;

    CoachInquiryStatus(int code, String desc) {
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
