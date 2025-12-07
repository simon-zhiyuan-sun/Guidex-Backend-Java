package com.guidex.system.mapper;

import java.util.List;
import com.guidex.system.domain.Feedback;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author guidex
 * @date 2025-07-31
 */
public interface FeedbackMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public Feedback selectFeedbackById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param feedback 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<Feedback> selectFeedbackList(Feedback feedback);

    /**
     * 新增【请填写功能名称】
     * 
     * @param feedback 【请填写功能名称】
     * @return 结果
     */
    public int insertFeedback(Feedback feedback);

    /**
     * 修改【请填写功能名称】
     * 
     * @param feedback 【请填写功能名称】
     * @return 结果
     */
    public int updateFeedback(Feedback feedback);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteFeedbackById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFeedbackByIds(Long[] ids);
}
