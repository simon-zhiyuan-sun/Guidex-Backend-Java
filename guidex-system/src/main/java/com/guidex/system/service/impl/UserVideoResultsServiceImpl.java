package com.guidex.system.service.impl;

import java.util.List;

import com.guidex.system.domain.video.UserVideoResultsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guidex.system.mapper.video.UserVideoResultsMapper;
import com.guidex.system.domain.video.UserVideoResults;
import com.guidex.system.service.IUserVideoResultsService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author guidex
 * @date 2025-04-19
 */
@Service
public class UserVideoResultsServiceImpl implements IUserVideoResultsService 
{
    @Autowired
    private UserVideoResultsMapper userVideoResultsMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public UserVideoResults selectUserVideoResultsById(Long id)
    {
        return userVideoResultsMapper.selectUserVideoResultsById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param userVideoResults 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<UserVideoResults> selectUserVideoResultsList(UserVideoResults userVideoResults)
    {
        return userVideoResultsMapper.selectUserVideoResultsList(userVideoResults);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param userVideoResults 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertUserVideoResults(UserVideoResults userVideoResults)
    {
        return userVideoResultsMapper.insertUserVideoResults(userVideoResults);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param userVideoResults 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateUserVideoResults(UserVideoResults userVideoResults)
    {
        return userVideoResultsMapper.updateUserVideoResults(userVideoResults);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteUserVideoResultsByIds(Long[] ids)
    {
        return userVideoResultsMapper.deleteUserVideoResultsByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteUserVideoResultsById(Long id)
    {
        return userVideoResultsMapper.deleteUserVideoResultsById(id);
    }

    @Override
    public int logicDelete(Long id) {
        UserVideoResults userVideoResults = userVideoResultsMapper.selectUserVideoResultsById(id);
        userVideoResults.setIsDeleted(1L);
        return userVideoResultsMapper.updateUserVideoResults(userVideoResults);
    }

    @Override
    public List<UserVideoResultsDto> selectUserResultsWithVideo(Long userId) {
        return userVideoResultsMapper.selectUserResultsWithVideo(userId);
    }
}
