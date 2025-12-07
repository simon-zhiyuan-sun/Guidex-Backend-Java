package com.guidex.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guidex.system.mapper.FeedbackMapper;
import com.guidex.system.domain.Feedback;
import com.guidex.system.service.IFeedbackService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author guidex
 * @date 2025-07-31
 */
@Service
public class FeedbackServiceImpl implements IFeedbackService 
{
    @Autowired
    private FeedbackMapper feedbackMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public Feedback selectFeedbackById(Long id)
    {
        return feedbackMapper.selectFeedbackById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param feedback 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<Feedback> selectFeedbackList(Feedback feedback)
    {
        return feedbackMapper.selectFeedbackList(feedback);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param feedback 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertFeedback(Feedback feedback)
    {
        return feedbackMapper.insertFeedback(feedback);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param feedback 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateFeedback(Feedback feedback)
    {
        return feedbackMapper.updateFeedback(feedback);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteFeedbackByIds(Long[] ids)
    {
        return feedbackMapper.deleteFeedbackByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteFeedbackById(Long id)
    {
        return feedbackMapper.deleteFeedbackById(id);
    }
}
