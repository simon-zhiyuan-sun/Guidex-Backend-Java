package com.guidex.system.domain.video;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.guidex.common.annotation.Excel;
import com.guidex.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 user_video
 * 
 * @author guidex
 * @date 2025-04-19
 */
public class UserVideo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 关联用户id */
    @Excel(name = "关联用户id")
    private Long userId;

    /** 原始文件名 */
    @Excel(name = "原始文件名")
    private String fileName;

    /** 视频路径 */
    @Excel(name = "视频路径")
    private String videoUrl;

    /** 封面路径 */
    @Excel(name = "封面路径")
    private String coverUrl;

    /** 分析状态（0未分析 1分析中 2分析成功 3分析失败） */
    @Excel(name = "分析状态", readConverterExp = "0=未分析,1=分析中,2=分析成功,3=分析失败")
    private Long status;

    /** 上传时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date uploadTime;

    /** 视频时长 */
    @Excel(name = "视频时长")
    private Long duration;

    /** 分析完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "分析完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date analyzeTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setVideoUrl(String videoUrl) 
    {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() 
    {
        return videoUrl;
    }
    public void setCoverUrl(String coverUrl) 
    {
        this.coverUrl = coverUrl;
    }

    public String getCoverUrl() 
    {
        return coverUrl;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setUploadTime(Date uploadTime) 
    {
        this.uploadTime = uploadTime;
    }

    public Date getUploadTime() 
    {
        return uploadTime;
    }
    public void setDuration(Long duration) 
    {
        this.duration = duration;
    }

    public Long getDuration() 
    {
        return duration;
    }
    public void setAnalyzeTime(Date analyzeTime) 
    {
        this.analyzeTime = analyzeTime;
    }

    public Date getAnalyzeTime() 
    {
        return analyzeTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("fileName", getFileName())
            .append("videoUrl", getVideoUrl())
            .append("coverUrl", getCoverUrl())
            .append("status", getStatus())
            .append("uploadTime", getUploadTime())
            .append("duration", getDuration())
            .append("analyzeTime", getAnalyzeTime())
            .toString();
    }
}
