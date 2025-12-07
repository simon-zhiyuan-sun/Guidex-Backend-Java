package com.guidex.framework.security.thirdparty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author kled2
 * @date 2025/6/17
 */
@Data
@AllArgsConstructor
public class AppleUserInfo {
    private String sub;     // Apple 返回的用户唯一标识
    private String email;   // 可能为 null
}
