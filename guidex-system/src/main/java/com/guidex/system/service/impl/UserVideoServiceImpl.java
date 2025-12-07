package com.guidex.system.service.impl;

import java.util.List;

import com.guidex.common.enums.VideoAnalysisStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guidex.system.mapper.video.UserVideoMapper;
import com.guidex.system.domain.video.UserVideo;
import com.guidex.system.service.IUserVideoService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author guidex
 * @date 2025-04-19
 */
@Service
public class UserVideoServiceImpl implements IUserVideoService 
{
    @Autowired
    private UserVideoMapper userVideoMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public UserVideo selectUserVideoById(Long id)
    {
        return userVideoMapper.selectUserVideoById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param userVideo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<UserVideo> selectUserVideoList(UserVideo userVideo)
    {
        return userVideoMapper.selectUserVideoList(userVideo);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param userVideo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertUserVideo(UserVideo userVideo)
    {
        return userVideoMapper.insertUserVideo(userVideo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param userVideo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateUserVideo(UserVideo userVideo)
    {
        return userVideoMapper.updateUserVideo(userVideo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteUserVideoByIds(Long[] ids)
    {
        return userVideoMapper.deleteUserVideoByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteUserVideoById(Long id)
    {
        return userVideoMapper.deleteUserVideoById(id);
    }

    @Override
    public int markAsFailed(Long id) {
        UserVideo userVideo = userVideoMapper.selectUserVideoById(id);
        userVideo.setStatus((long) VideoAnalysisStatusEnum.ANALYZE_FAILED.getCode());
        return userVideoMapper.updateUserVideo(userVideo);
    }

    @Override
    public int markAsSuccess(Long id) {
        UserVideo userVideo = userVideoMapper.selectUserVideoById(id);
        userVideo.setStatus((long) VideoAnalysisStatusEnum.ANALYZE_SUCCESS.getCode());
        return userVideoMapper.updateUserVideo(userVideo);
    }

    @Override
    public int markAsCanceled(Long id) {
        UserVideo userVideo = userVideoMapper.selectUserVideoById(id);
        userVideo.setStatus((long) VideoAnalysisStatusEnum.ANALYZE_CANCELED.getCode());
        return userVideoMapper.updateUserVideo(userVideo);
    }


}
