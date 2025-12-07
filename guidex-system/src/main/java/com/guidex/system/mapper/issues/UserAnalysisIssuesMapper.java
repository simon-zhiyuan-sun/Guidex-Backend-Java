package com.guidex.system.mapper.issues;

import java.util.List;
import com.guidex.system.domain.issues.UserAnalysisIssues;
import org.apache.ibatis.annotations.Param;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author guidex
 * @date 2025-05-16
 */
public interface UserAnalysisIssuesMapper 
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
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteUserAnalysisIssuesById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserAnalysisIssuesByIds(Long[] ids);

    List<UserAnalysisIssues> selectUserAnalysisIssuesByResultIds(@Param("resultIds") List<Long> resultIds);
}
