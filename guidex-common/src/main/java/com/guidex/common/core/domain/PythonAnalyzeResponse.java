package com.guidex.common.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author kled2
 * @date 2025/5/13
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PythonAnalyzeResponse {
    private String video_id;
    private String status;
    private String analysis_result;
}
