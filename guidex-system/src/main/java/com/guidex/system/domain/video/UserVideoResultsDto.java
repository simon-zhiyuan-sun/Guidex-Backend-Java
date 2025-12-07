package com.guidex.system.domain.video;

import com.guidex.system.domain.issues.UserAnalysisIssues;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author kled2
 * @date 2025/5/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVideoResultsDto extends UserVideoResults{
    private List<UserAnalysisIssues> issues;
    private String feedbackContent;
}
