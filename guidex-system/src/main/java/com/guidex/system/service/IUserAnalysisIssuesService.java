package com.guidex.system.service;

import java.util.List;
import com.guidex.system.domain.issues.UserAnalysisIssues;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author guidex
 * @date 2025-05-16
 */
public interface IUserAnalysisIssuesService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public UserAnalysisIssues selectUserAnalysisIssuesById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param userAnalysisIssues 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<UserAnalysisIssues> selectUserAnalysisIssuesList(UserAnalysisIssues userAnalysisIssues);

    /**
     * 新增【请填写功能名称】
     * 
     * @param userAnalysisIssues 【请填写功能名称】
     * @return 结果
     */
    public int insertUserAnalysisIssues(UserAnalysisIssues userAnalysisIssues);

    /**
     * 修改【请填写功能名称】
     * 
     * @param userAnalysisIssues 【请填写功能名称】
     * @return 结果
     */
    public int updateUserAnalysisIssues(UserAnalysisIssues userAnalysisIssues);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteUserAnalysisIssuesByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteUserAnalysisIssuesById(Long id);

    List<UserAnalysisIssues> selectUserAnalysisIssuesByResultIds(List<Long> resultIds);
}
