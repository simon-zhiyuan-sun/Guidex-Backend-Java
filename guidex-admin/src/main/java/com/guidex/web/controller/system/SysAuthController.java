package com.guidex.web.controller.system;

import com.guidex.common.core.domain.AjaxResult;
import com.guidex.common.core.domain.entity.SysUser;
import com.guidex.common.core.domain.model.LoginUser;
import com.guidex.common.utils.SecurityUtils;
import com.guidex.framework.security.thirdparty.*;
import com.guidex.framework.web.service.TokenService;
import com.guidex.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author kled2
 * @date 2025/3/21
 */
@RestController
@RequestMapping("/auth")
public class SysAuthController {
    private static final Logger log = LoggerFactory.getLogger(SysAuthController.class);
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;

    @Autowired
    private AppleTokenVerifier appleTokenVerifier;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/google/token")
    public AjaxResult googleLogin(@RequestBody GoogleLoginRequest request) {
        GoogleUserInfo googleUser = googleTokenVerifier.verify(request.getIdToken());
        if (googleUser == null || googleUser.getEmail() == null) {
            return AjaxResult.error("Google token 验证失败或未获取到邮箱");
        }

        String email = googleUser.getEmail();
        String name = googleUser.getName();
        String picture = googleUser.getPicture();

        // 查询或注册用户
        SysUser user = userService.selectUserByEmail(email);
        if (user == null) {
            user = new SysUser();
            user.setUserName(name);
            user.setNickName(name);
            user.setEmail(email);
            user.setAvatar(picture);
            String password = UUID.randomUUID().toString();
            user.setPassword(SecurityUtils.encryptPassword(password));
            boolean success = userService.registerUser(user);
            if (!success) {
                return AjaxResult.error("注册失败，请联系管理员");
            }
        }

        // 生成 JWT token
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUserId());
        loginUser.setUser(user);
        String token = tokenService.createToken(loginUser);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);

        return AjaxResult.success(result);
    }

    @PostMapping("/apple/token")
    public AjaxResult appleLogin(@RequestBody AppleLoginRequest request) {
        // 1. 验证 Apple 返回的 identityToken，获取用户信息
        AppleUserInfo appleUser = appleTokenVerifier.verify(request.getIdentityToken());
        if (appleUser == null || appleUser.getSub() == null) {
            return AjaxResult.error("Apple token 验证失败");
        }

        String sub = appleUser.getSub();
        String email = appleUser.getEmail();  // 注意：可能为 null，仅首次登录提供

        // 2. 查询或注册用户（可将 sub 存入自定义字段 appleSub）
        SysUser user = userService.selectUserByAppleSub(sub);
        if (user == null) {
            user = new SysUser();
            String nickname = (email != null) ? email.split("@")[0] : "apple_" + sub.substring(0, 6);
            user.setUserName(nickname);
            user.setNickName(nickname);
            user.setEmail(email);
            user.setAppleSub(sub);
            String password = UUID.randomUUID().toString();
            user.setPassword(SecurityUtils.encryptPassword(password));
            boolean success = userService.registerUser(user);
            if (!success) {
                return AjaxResult.error("注册失败，请联系管理员");
            }
        }

        // 3. 签发系统 JWT
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUserId());
        loginUser.setUser(user);
        String token = tokenService.createToken(loginUser);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);

        return AjaxResult.success(result);
    }

    @GetMapping("/google/success")
    public AjaxResult googleSuccess(@RequestParam(required = false) String token) {
        return AjaxResult.success("Google 登录成功", token);
    }

    @GetMapping("/google/failure")
    public AjaxResult googleFailure() {
        return AjaxResult.error("Google 登录失败，请检查您的账户信息或稍后再试。");
    }
}
