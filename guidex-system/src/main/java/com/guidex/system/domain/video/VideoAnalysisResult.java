package com.guidex.system.domain.video;

import com.guidex.system.domain.issues.UserAnalysisIssues;
import lombok.Data;

import java.util.List;

/**
 * @author kled2
 * @date 2025/5/13
 */
@Data
public class VideoAnalysisResult{
    private int issue_count;
    private List<UserAnalysisIssues> issues;
}
