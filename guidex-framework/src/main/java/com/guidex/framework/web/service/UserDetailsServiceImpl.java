package com.guidex.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.guidex.common.core.domain.entity.SysUser;
import com.guidex.common.core.domain.model.LoginUser;
import com.guidex.common.enums.UserStatus;
import com.guidex.common.exception.ServiceException;
import com.guidex.common.utils.MessageUtils;
import com.guidex.common.utils.StringUtils;
import com.guidex.system.service.ISysUserService;

/**
 * 用户验证处理
 *
 * @author guidex
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 实际走的是根据邮箱查用户
     * @param input 可能是邮箱 或者用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException
    {
        // 先按邮箱查，再查用户名
        SysUser user = userService.selectUserByEmail(input);
        if (user == null) {
            user = userService.selectUserByUserName(input);
        }
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 优先用 email，如果为空就用用户名
        String identifier = StringUtils.isNotEmpty(user.getEmail()) ? user.getEmail() : user.getUserName();

        // 状态校验
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", identifier);
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", identifier);
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }

        // 密码校验（如图形验证码正确后）
        passwordService.validate(user);

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
