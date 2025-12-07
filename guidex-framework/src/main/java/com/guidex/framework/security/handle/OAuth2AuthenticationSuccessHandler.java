package com.guidex.framework.security.handle;

import com.guidex.common.core.domain.entity.SysUser;
import com.guidex.common.core.domain.model.LoginUser;
import com.guidex.common.utils.SecurityUtils;
import com.guidex.framework.web.service.TokenService;
import com.guidex.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author kled2
 * @date 2025/5/6
 */
@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private HttpSessionOAuth2AuthorizationRequestRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        log.info("成功解析 OAuth2 用户信息: {}", oauthUser.getAttributes());

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        if (email == null) {
            log.error("Google 登录失败，未获取到邮箱");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "邮箱为空");
            return;
        }

        // 用户注册或获取
        SysUser user = userService.selectUserByEmail(email);
        if (user == null) {
            user = new SysUser();
            user.setUserName(name);
            user.setNickName(name);
            user.setEmail(email);
            user.setAvatar(picture);
            String password = UUID.randomUUID().toString();
            user.setPassword(SecurityUtils.encryptPassword(password));
            if (!userService.registerUser(user)) {
                log.error("注册新用户失败: {}", email);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "用户注册失败");
                return;
            }
        }

        // 生成 JWT Token
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUserId());
        loginUser.setUser(user);
        String token = tokenService.createToken(loginUser);

        // 清除授权残留
        authorizationRequestRepository.removeAuthorizationRequest(request, response);

        // 重定向并携带 token（可选：跳转页面或写入 cookie）
        response.sendRedirect("/auth/google/success?token=" + token);
    }
}

