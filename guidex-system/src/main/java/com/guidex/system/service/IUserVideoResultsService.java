package com.guidex.system.service;

import java.util.List;
import com.guidex.system.domain.video.UserVideoResults;
import com.guidex.system.domain.video.UserVideoResultsDto;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author guidex
 * @date 2025-04-19
 */
public interface IUserVideoResultsService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public UserVideoResults selectUserVideoResultsById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param userVideoResults 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<UserVideoResults> selectUserVideoResultsList(UserVideoResults userVideoResults);

    /**
     * 新增【请填写功能名称】
     * 
     * @param userVideoResults 【请填写功能名称】
     * @return 结果
     */
    public int insertUserVideoResults(UserVideoResults userVideoResults);

    /**
     * 修改【请填写功能名称】
     * 
     * @param userVideoResults 【请填写功能名称】
     * @return 结果
     */
    public int updateUserVideoResults(UserVideoResults userVideoResults);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteUserVideoResultsByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteUserVideoResultsById(Long id);

    int logicDelete(Long id);

    List<UserVideoResultsDto> selectUserResultsWithVideo(Long userId);
}
