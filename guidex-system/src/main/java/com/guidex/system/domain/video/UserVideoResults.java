package com.guidex.system.domain.video;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.guidex.common.annotation.Excel;
import com.guidex.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 user_video_results
 *
 * @author guidex
 * @date 2025-04-19
 */
public class UserVideoResults extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 对应视频id
     */
    @Excel(name = "对应视频id")
    private Long videoId;

    /**
     * 异常点数量
     */
    @Excel(name = "异常点数量")
    private Long issueCount;

    /**
     * $column.columnComment
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String resultDetail;

    /**
     * 结果生成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结果生成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    private Long coachInquiryStatus;

    private String coachInquiryDetail;

    private Long coachUserId;

    private String coachName;

    /**
     * 可能的动作评分
     */
    @Excel(name = "可能的动作评分")
    private Long score;

    /**
     * 逻辑删 以防误删 0未删除 1已删除
     */
    @Excel(name = "逻辑删 以防误删 0未删除 1已删除")
    private Long isDeleted;

    private String skiLocation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date skiTime;

    private String skiTitle;

    private Long category;

    private Long type;

    private Long standard;

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getStandard() {
        return standard;
    }

    public void setStandard(Long standard) {
        this.standard = standard;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setIssueCount(Long issueCount) {
        this.issueCount = issueCount;
    }

    public Long getIssueCount() {
        return issueCount;
    }

    public void setResultDetail(String resultDetail) {
        this.resultDetail = resultDetail;
    }

    public String getResultDetail() {
        return resultDetail;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getScore() {
        return score;
    }

    public void setIsDeleted(Long isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getIsDeleted() {
        return isDeleted;
    }

    public String getSkiLocation() {
        return skiLocation;
    }

    public void setSkiLocation(String skiLocation) {
        this.skiLocation = skiLocation;
    }

    public Date getSkiTime() {
        return skiTime;
    }

    public void setSkiTime(Date skiTime) {
        this.skiTime = skiTime;
    }

    public String getSkiTitle() {
        return skiTitle;
    }

    public void setSkiTitle(String skiTitle) {
        this.skiTitle = skiTitle;
    }

    public Long getCoachInquiryStatus() {
        return coachInquiryStatus;
    }

    public void setCoachInquiryStatus(Long coachInquiryStatus) {
        this.coachInquiryStatus = coachInquiryStatus;
    }

    public String getCoachInquiryDetail() {
        return coachInquiryDetail;
    }

    public void setCoachInquiryDetail(String coachInquiryDetail) {
        this.coachInquiryDetail = coachInquiryDetail;
    }

    public Long getCoachUserId() {
        return coachUserId;
    }

    public void setCoachUserId(Long coachUserId) {
        this.coachUserId = coachUserId;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("videoId", getVideoId())
                .append("issueCount", getIssueCount())
                .append("resultDetail", getResultDetail())
                .append("createdTime", getCreatedTime())
                .append("score", getScore())
                .append("isDeleted", getIsDeleted())
                .append("skiLocation", getSkiLocation())
                .append("skiTime", getSkiTime())
                .append("skiTitle", getSkiTitle())
                .toString();
    }
}
