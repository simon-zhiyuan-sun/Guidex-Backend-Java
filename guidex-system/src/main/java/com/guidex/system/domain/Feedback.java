package com.guidex.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.guidex.common.annotation.Excel;
import com.guidex.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 feedback
 * 
 * @author guidex
 * @date 2025-07-31
 */
public class Feedback extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long recordId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long coachId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String coachName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String content;

    /** 扩展字段 */
    @Excel(name = "扩展字段")
    private String ext;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }
    public void setCoachId(Long coachId) 
    {
        this.coachId = coachId;
    }

    public Long getCoachId() 
    {
        return coachId;
    }
    public void setCoachName(String coachName) 
    {
        this.coachName = coachName;
    }

    public String getCoachName() 
    {
        return coachName;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setExt(String ext) 
    {
        this.ext = ext;
    }

    public String getExt() 
    {
        return ext;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("recordId", getRecordId())
            .append("coachId", getCoachId())
            .append("coachName", getCoachName())
            .append("content", getContent())
            .append("ext", getExt())
            .toString();
    }
}
