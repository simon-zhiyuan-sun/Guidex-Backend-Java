package com.guidex.system.controller.video;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidex.common.annotation.Log;
import com.guidex.common.core.controller.BaseController;
import com.guidex.common.core.domain.AjaxResult;
import com.guidex.common.core.domain.entity.SysUser;
import com.guidex.common.core.domain.model.LoginUser;
import com.guidex.common.core.page.TableDataInfo;
import com.guidex.common.enums.BusinessType;
import com.guidex.common.enums.CoachInquiryStatus;
import com.guidex.common.utils.SecurityUtils;
import com.guidex.common.utils.poi.ExcelUtil;
import com.guidex.system.domain.Feedback;
import com.guidex.system.domain.FeedbackDto;
import com.guidex.system.domain.issues.UserAnalysisIssues;
import com.guidex.system.domain.video.*;
import com.guidex.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】Controller
 *
 * @author guidex
 * @date 2025-04-19
 */
@RestController
@RequestMapping("/system/results")
public class UserVideoResultsController extends BaseController {
    @Autowired
    private IUserVideoResultsService userVideoResultsService;

    @Autowired
    private IUserAnalysisIssuesService userAnalysisIssuesService;

    @Autowired
    private IFeedbackService feedbackService;

    @Autowired
    private IUserVideoService userVideoService;

    @Autowired
    private ISysUserService sysUserService;

    public static final Logger log = LoggerFactory.getLogger(UserVideoResultsController.class);


    /**
     * 查询【请填写功能名称】列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserVideoResults userVideoResults) { // todo 分页再说
        Long userId = SecurityUtils.getUserId();
        startPage(); // 分页的是结果集

        // 改为一次 join 查询
        List<UserVideoResultsDto> list = userVideoResultsService.selectUserResultsWithVideo(userId);

        // 批量查出 issues（避免 N+1）
        List<Long> resultIds = list.stream().map(UserVideoResultsDto::getId).collect(Collectors.toList());
        if (!resultIds.isEmpty()) {
            List<UserAnalysisIssues> allIssues = userAnalysisIssuesService.selectUserAnalysisIssuesByResultIds(resultIds);
            Map<Long, List<UserAnalysisIssues>> issueMap = allIssues.stream()
                    .collect(Collectors.groupingBy(UserAnalysisIssues::getResultId));

            // 设置 issues
            for (UserVideoResultsDto dto : list) {
                dto.setIssues(issueMap.getOrDefault(dto.getId(), Collections.emptyList()));
            }
        }

        return getDataTable(list);
    }

    @GetMapping("/coach/list-inquiring")
    public AjaxResult listInquiring() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // if (!Objects.equals(loginUser.getUser().getUserType(), "01")) {
        //     return error("您不是教练");
        // }

        UserVideoResults condition = new UserVideoResults();
        condition.setCoachInquiryStatus((long) CoachInquiryStatus.INQUIRING.getCode());
        List<UserVideoResults> userVideoResults = userVideoResultsService.selectUserVideoResultsList(condition);
        List<ListInquiringDto> list = userVideoResults.stream()
                .map(item -> {
                    ListInquiringDto dto = new ListInquiringDto();
                    // 继承父类属性复制
                    BeanUtils.copyProperties(item, dto);
                    // 单独处理新增字段 studentName
                    dto.setStudentName(sysUserService.selectUserById(userVideoService.selectUserVideoById(item.getVideoId()).getUserId()).getUserName());
                    return dto;
                })
                .collect(Collectors.toList());
        return AjaxResult.success(list);
    }

    @GetMapping("/coach/list-inquiring-success")
    public AjaxResult listInquiringSuccess() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        Long coachId = user.getUserId();

        UserVideoResults condition = new UserVideoResults();
        condition.setCoachUserId(coachId);
        List<UserVideoResults> userVideoResults = userVideoResultsService.selectUserVideoResultsList(condition);
        return success(userVideoResults);
    }

    @PostMapping("/coach/feedback")
    public AjaxResult coachFeedback(@RequestBody FeedbackDto dto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser coach = loginUser.getUser();

        UserVideoResults record = userVideoResultsService.selectUserVideoResultsById(dto.getRecordId());
        if (record.getCoachInquiryStatus() != CoachInquiryStatus.INQUIRING.getCode()) {
            return error("该请求已处理或未发出请求");
        }
        record.setCoachInquiryStatus((long) CoachInquiryStatus.INQUIRY_SUCCESS.getCode());
        record.setCoachUserId(coach.getUserId());
        record.setCoachName(coach.getNickName());
        userVideoResultsService.updateUserVideoResults(record);

        Feedback feedback = new Feedback();
        feedback.setCoachId(coach.getUserId());
        feedback.setCoachName(coach.getNickName());
        feedback.setRecordId(dto.getRecordId());
        feedback.setContent(dto.getContent());
        feedbackService.insertFeedback(feedback);


        return success();
    }


    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:results:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserVideoResults userVideoResults) {
        List<UserVideoResults> list = userVideoResultsService.selectUserVideoResultsList(userVideoResults);
        ExcelUtil<UserVideoResults> util = new ExcelUtil<UserVideoResults>(UserVideoResults.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) throws JsonProcessingException {
        UserVideoResults userVideoResults = userVideoResultsService.selectUserVideoResultsById(id);
        UserVideoResultsDto dto;
        ObjectMapper objectMapper = new ObjectMapper();
        dto = objectMapper.convertValue(userVideoResults, UserVideoResultsDto.class);
        UserAnalysisIssues condition = new UserAnalysisIssues();
        condition.setResultId(id);
        List<UserAnalysisIssues> userAnalysisIssues = userAnalysisIssuesService.selectUserAnalysisIssuesList(condition);
        dto.setIssues(userAnalysisIssues);

        Feedback feedback = new Feedback();
        feedback.setRecordId(id);
        List<Feedback> feedbacks = feedbackService.selectFeedbackList(feedback);
        if (!feedbacks.isEmpty()) {
            dto.setFeedbackContent(feedbacks.get(0).getContent());
        }

        return AjaxResult.success(dto);

    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:results:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserVideoResults userVideoResults) {
        return toAjax(userVideoResultsService.insertUserVideoResults(userVideoResults));
    }

    /**
     * 修改【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserVideoResults userVideoResults) {
        return toAjax(userVideoResultsService.updateUserVideoResults(userVideoResults));
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userVideoResultsService.deleteUserVideoResultsByIds(ids));
    }

    /**
     * 逻辑删除
     */
    @DeleteMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        return userVideoResultsService.logicDelete(id) > 0 ? success() : error();
    }

    @PutMapping("/inquire-coach/{recordId}")
    public AjaxResult inquireCoach(@PathVariable Long recordId, @RequestBody CoachInquiryDetail detail) {
        UserVideoResults userVideoResults = userVideoResultsService.selectUserVideoResultsById(recordId);
        if (!(userVideoResults.getCoachInquiryStatus() == (CoachInquiryStatus.UN_INQUIRY.getCode()))) {
            return success("该记录已被回复或教练正在查看");
        }
        userVideoResults.setCoachInquiryStatus((long) CoachInquiryStatus.INQUIRING.getCode());
        if(detail.getDetail()!=null){
            userVideoResults.setCoachInquiryDetail(detail.getDetail());
        }
        if (userVideoResultsService.updateUserVideoResults(userVideoResults) > 0) {
            return success("成功");
        } else {
            return error("服务器内部错误，请稍后");
        }
    }
}
