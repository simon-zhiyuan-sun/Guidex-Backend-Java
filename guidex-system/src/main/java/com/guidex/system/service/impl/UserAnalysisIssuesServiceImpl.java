package com.guidex.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guidex.system.mapper.issues.UserAnalysisIssuesMapper;
import com.guidex.system.domain.issues.UserAnalysisIssues;
import com.guidex.system.service.IUserAnalysisIssuesService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author guidex
 * @date 2025-05-16
 */
@Service
public class UserAnalysisIssuesServiceImpl implements IUserAnalysisIssuesService 
{
    @Autowired
    private UserAnalysisIssuesMapper userAnalysisIssuesMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public UserAnalysisIssues selectUserAnalysisIssuesById(Long id)
    {
        return userAnalysisIssuesMapper.selectUserAnalysisIssuesById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param userAnalysisIssues 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<UserAnalysisIssues> selectUserAnalysisIssuesList(UserAnalysisIssues userAnalysisIssues)
    {
        return userAnalysisIssuesMapper.selectUserAnalysisIssuesList(userAnalysisIssues);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param userAnalysisIssues 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertUserAnalysisIssues(UserAnalysisIssues userAnalysisIssues)
    {
        return userAnalysisIssuesMapper.insertUserAnalysisIssues(userAnalysisIssues);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param userAnalysisIssues 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateUserAnalysisIssues(UserAnalysisIssues userAnalysisIssues)
    {
        return userAnalysisIssuesMapper.updateUserAnalysisIssues(userAnalysisIssues);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteUserAnalysisIssuesByIds(Long[] ids)
    {
        return userAnalysisIssuesMapper.deleteUserAnalysisIssuesByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteUserAnalysisIssuesById(Long id)
    {
        return userAnalysisIssuesMapper.deleteUserAnalysisIssuesById(id);
    }

    @Override
    public List<UserAnalysisIssues> selectUserAnalysisIssuesByResultIds(List<Long> resultIds) {
        return userAnalysisIssuesMapper.selectUserAnalysisIssuesByResultIds(resultIds);
    }
}
