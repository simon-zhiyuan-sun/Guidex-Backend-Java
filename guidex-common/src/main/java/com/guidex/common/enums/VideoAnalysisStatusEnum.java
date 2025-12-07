package com.guidex.common.enums;

/**
 * @author kled2
 * @date 2025/4/27
 */
public enum VideoAnalysisStatusEnum {
    UN_ANALYZED(0, "未分析"),
    ANALYZING(1, "分析中"),
    ANALYZE_SUCCESS(2, "分析成功"),
    ANALYZE_FAILED(3, "分析失败"),
    ANALYZE_CANCELED(4, "取消分析"),
    ;

    VideoAnalysisStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    private int code;
    private String desc;
    public int getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
