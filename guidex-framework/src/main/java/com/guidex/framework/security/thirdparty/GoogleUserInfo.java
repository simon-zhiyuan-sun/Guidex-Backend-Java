package com.guidex.framework.security.thirdparty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kled2
 * @date 2025/5/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUserInfo {
    private String email;
    private String name;
    private String picture;
}
