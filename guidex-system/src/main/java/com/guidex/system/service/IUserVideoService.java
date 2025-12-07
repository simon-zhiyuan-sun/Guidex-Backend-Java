package com.guidex.system.service;

import java.util.List;
import com.guidex.system.domain.video.UserVideo;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author guidex
 * @date 2025-04-19
 */
public interface IUserVideoService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public UserVideo selectUserVideoById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param userVideo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<UserVideo> selectUserVideoList(UserVideo userVideo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param userVideo 【请填写功能名称】
     * @return 结果
     */
    public int insertUserVideo(UserVideo userVideo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param userVideo 【请填写功能名称】
     * @return 结果
     */
    public int updateUserVideo(UserVideo userVideo);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteUserVideoByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteUserVideoById(Long id);

    int markAsFailed(Long id);

    int markAsSuccess(Long id);

    int markAsCanceled(Long id);
}
