package com.guidex.system.controller.issues;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.guidex.common.enums.ThumbEnum;
import com.guidex.common.utils.SecurityUtils;
import com.guidex.system.domain.video.UserVideo;
import com.guidex.system.domain.video.UserVideoResults;
import com.guidex.system.service.IUserVideoResultsService;
import com.guidex.system.service.IUserVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.guidex.common.annotation.Log;
import com.guidex.common.core.controller.BaseController;
import com.guidex.common.core.domain.AjaxResult;
import com.guidex.common.enums.BusinessType;
import com.guidex.system.domain.issues.UserAnalysisIssues;
import com.guidex.system.service.IUserAnalysisIssuesService;
import com.guidex.common.utils.poi.ExcelUtil;
import com.guidex.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 *
 * @author guidex
 * @date 2025-05-16
 */
@RestController
@RequestMapping("/system/issues")
public class UserAnalysisIssuesController extends BaseController {
    @Autowired
    private IUserAnalysisIssuesService userAnalysisIssuesService;

    @Autowired
    private IUserVideoService userVideoService;

    @Autowired
    private IUserVideoResultsService userVideoResultsService;

    /**
     * 查询【请填写功能名称】列表 todo 待优化
     */
    @GetMapping("/list")
    public TableDataInfo list(UserAnalysisIssues userAnalysisIssues) {
        startPage();
        List<UserAnalysisIssues> list = new ArrayList<>();
        if (userAnalysisIssues.getResultId() == null) {
            Long userId = SecurityUtils.getUserId();
            UserVideo userVideo = new UserVideo();
            userVideo.setUserId(userId);
            // userVideo.setStatus();
            List<UserVideo> userVideos = userVideoService.selectUserVideoList(userVideo);
            List<Long> videoIds = userVideos.stream().map(UserVideo::getId).collect(Collectors.toList());

            UserVideoResults userVideoResults = new UserVideoResults();
            List<UserVideoResults> resultList = new ArrayList<>();
            for (Long videoId : videoIds) {
                userVideoResults.setVideoId(videoId);
                List<UserVideoResults> userVideoResults1 = userVideoResultsService.selectUserVideoResultsList(userVideoResults);
                resultList.addAll(userVideoResults1);
            }

            List<Long> resultIds = resultList.stream().map(UserVideoResults::getId).collect(Collectors.toList());
            for (Long resultId : resultIds) {
                UserAnalysisIssues condition = new UserAnalysisIssues();
                BeanUtils.copyProperties(userAnalysisIssues, condition);
                condition.setResultId(resultId);
                List<UserAnalysisIssues> userAnalysisIssues1 = userAnalysisIssuesService.selectUserAnalysisIssuesList(condition);
                list.addAll(userAnalysisIssues1);
            }
        } else {
            list = userAnalysisIssuesService.selectUserAnalysisIssuesList(userAnalysisIssues);
        }

        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:issues:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserAnalysisIssues userAnalysisIssues) {
        List<UserAnalysisIssues> list = userAnalysisIssuesService.selectUserAnalysisIssuesList(userAnalysisIssues);
        ExcelUtil<UserAnalysisIssues> util = new ExcelUtil<UserAnalysisIssues>(UserAnalysisIssues.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userAnalysisIssuesService.selectUserAnalysisIssuesById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:issues:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserAnalysisIssues userAnalysisIssues) {
        return toAjax(userAnalysisIssuesService.insertUserAnalysisIssues(userAnalysisIssues));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:issues:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserAnalysisIssues userAnalysisIssues) {
        return toAjax(userAnalysisIssuesService.updateUserAnalysisIssues(userAnalysisIssues));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:issues:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userAnalysisIssuesService.deleteUserAnalysisIssuesByIds(ids));
    }

    @PutMapping("/thumb-up/{id}")
    public AjaxResult thumbUp(@PathVariable Long id) {
        UserAnalysisIssues userAnalysisIssues = userAnalysisIssuesService.selectUserAnalysisIssuesById(id);
        int currentThumb = userAnalysisIssues.getThumb().intValue();

        int newThumb;
        if (currentThumb == ThumbEnum.THUMB_UP.getCode()) {
            newThumb = ThumbEnum.THUMB_NONE.getCode(); // 取消点赞
        } else {
            newThumb = ThumbEnum.THUMB_UP.getCode();   // 点赞或从点踩转为点赞
        }

        userAnalysisIssues.setThumb((long) newThumb);
        return toAjax(userAnalysisIssuesService.updateUserAnalysisIssues(userAnalysisIssues));
    }


    @PutMapping("/thumb-down/{id}")
    public AjaxResult thumbDown(@PathVariable Long id) {
        UserAnalysisIssues userAnalysisIssues = userAnalysisIssuesService.selectUserAnalysisIssuesById(id);
        int currentThumb = userAnalysisIssues.getThumb().intValue();

        int newThumb;
        if (currentThumb == ThumbEnum.THUMB_DOWN.getCode()) {
            newThumb = ThumbEnum.THUMB_NONE.getCode();
        } else {
            newThumb = ThumbEnum.THUMB_DOWN.getCode();
        }

        userAnalysisIssues.setThumb((long) newThumb);
        return toAjax(userAnalysisIssuesService.updateUserAnalysisIssues(userAnalysisIssues));
    }
}
