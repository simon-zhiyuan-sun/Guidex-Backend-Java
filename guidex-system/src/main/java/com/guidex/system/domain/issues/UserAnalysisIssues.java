package com.guidex.system.domain.issues;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.guidex.common.annotation.Excel;
import com.guidex.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 user_analysis_issues
 *
 * @author guidex
 * @date 2025-05-16
 */
public class UserAnalysisIssues extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    private Long resultId;

    /**
     * issue类型
     * 0 edge transitions
     * 1 center of gravity
     * 2 body coordination
     * 3 pole usage
     * 4 stance width
     */
    private Long type;

    /**
     * 问题描述
     */
    @Excel(name = "问题描述")
    private String description;

    /**
     * 问题发生在第几秒
     */
    @Excel(name = "问题发生在第几秒")
    private Double time;

    /**
     * 模型对于这个问题的建议
     */
    @Excel(name = "模型对于这个问题的建议")
    private String suggestion;

    private Long thumb;

    /**
     * 封面图片
     */
    @Excel(name = "封面图片")
    private String coverUrl;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Double getTime() {
        return time;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Long getThumb(){
        return thumb;
    }

    public void setThumb(Long thumb){
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("resultId", getResultId())
                .append("type", getType())
                .append("description", getDescription())
                .append("time", getTime())
                .append("suggestion", getSuggestion())
                .append("thumb", getThumb())
                .append("coverUrl", getCoverUrl())
                .toString();
    }
}
