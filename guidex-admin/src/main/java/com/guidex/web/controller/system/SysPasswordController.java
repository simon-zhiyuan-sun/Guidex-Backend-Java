package com.guidex.web.controller.system;

import com.guidex.common.constant.Constants;
import com.guidex.common.core.domain.AjaxResult;
import com.guidex.common.core.domain.entity.SysUser;
import com.guidex.common.core.domain.model.LoginUser;
import com.guidex.common.utils.SecurityUtils;
import com.guidex.common.utils.StringUtils;
import com.guidex.framework.web.service.EmailService;
import com.guidex.framework.web.service.TokenService;
import com.guidex.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * @author kled2
 * @date 2025/3/20
 */
@RestController
public class SysPasswordController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private TokenService tokenService;
    @Value("${app.password.reset-url}")
    private String resetUrl;
    @Autowired
    private EmailService emailService;

    /**
     * 发送重置密码邮件
     */
    @PostMapping("/password/reset")
    public AjaxResult resetPassword(@RequestBody Map<String, String> request) {
        String email = Optional.ofNullable(request.get("email")).orElseThrow(() -> new RuntimeException("Email address is empty."));
        SysUser user = userService.selectUserByEmail(email);
        if (user == null) {
            return AjaxResult.error("Email address is not registered.");
        }

        String token = tokenService.createResetPasswordToken(user);

        String resetLink = resetUrl + token;
        if (emailService.sendEmail(email, "Reset Password(AI-Skiing-Coach)", "Click the link to reset your password: " + resetLink)) { // todo 临时解决方案，后期需要配置专门的发送邮件服务
            return AjaxResult.success("Reset-link has been successfully sent to your email. If you did not receive the email, please check SPAM and TRASH inbox, thank you!");
        }
        return AjaxResult.error("Failed to send reset-link to your email.");
    }

    /**
     * 重置密码链接验证
     *
     * @param token
     * @return
     */
    @GetMapping("/reset-password")
    public AjaxResult showResetPasswordPage(@RequestParam("token") String token) {
        // 验证 token 是否有效
        if (tokenService.validateToken(token)) {
            return AjaxResult.success("Valid token.");
        }
        return AjaxResult.error("Invalid or expired token.");
    }

    /**
     * 确认重置密码
     *
     * @param request
     * @param
     * @return
     */
    @PostMapping("/password/confirm")
    public AjaxResult confirmResetPassword(HttpServletRequest request, @RequestBody() Map<String,String> passwordRequest) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        // 验证 token 是否有效
        if (!tokenService.validateToken(token)) {
            return AjaxResult.error("Invalid or expired token.");  // Token 无效或已过期，跳转到错误页面
        }
        LoginUser loginUser = tokenService.getLoginUser(request);
        // 更新用户密码
        SysUser user = userService.selectUserByEmail(loginUser.getUser().getEmail());
        if (user == null) {
            return AjaxResult.error("Email address is not registered.");
        }

        String newPassword = passwordRequest.get("newPassword");

        newPassword = SecurityUtils.encryptPassword(newPassword);
        if (userService.resetUserPasswordByEmail(user.getEmail(), newPassword) > 0)
        {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(newPassword);
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success("Password reset successfully!");
        }
        return AjaxResult.error("Unexpected error occurs while update password, please try again later.");
    }
}
