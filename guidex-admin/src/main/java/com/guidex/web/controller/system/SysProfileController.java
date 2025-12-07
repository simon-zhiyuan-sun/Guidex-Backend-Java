package com.guidex.web.controller.system;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import com.guidex.common.core.service.IAvatarStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.guidex.common.annotation.Log;
import com.guidex.common.config.guidexConfig;
import com.guidex.common.core.controller.BaseController;
import com.guidex.common.core.domain.AjaxResult;
import com.guidex.common.core.domain.entity.SysUser;
import com.guidex.common.core.domain.model.LoginUser;
import com.guidex.common.enums.BusinessType;
import com.guidex.common.utils.SecurityUtils;
import com.guidex.common.utils.file.FileUploadUtils;
import com.guidex.common.utils.file.MimeTypeUtils;
import com.guidex.framework.web.service.TokenService;
import com.guidex.system.service.ISysUserService;

/**
 * 个人信息 业务处理
 *
 * @author guidex
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IAvatarStorageService avatarStorage;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }

    /**
     * 修改用户个人资料
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestParam(value = "nickName", required = false) String nickName,
                                    @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile,
                                    @RequestParam(value = "sex", required = false) String sex) throws IOException {
        LoginUser loginUser = getLoginUser();
        SysUser currentUser = loginUser.getUser();
        Long userId = currentUser.getUserId();
        String avatarUrl = null;

        try {
            if (avatarFile != null && !avatarFile.isEmpty()) {
                // 先删旧头像
                avatarStorage.delete(currentUser.getAvatar());
                // 再上传新头像
                avatarUrl = avatarStorage.save(avatarFile, userId);
            }
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }


        if (nickName != null) {
            currentUser.setNickName(nickName);
        }
        if (avatarUrl != null) {
            currentUser.setAvatar(avatarUrl);
        }
        String message = "操作成功";
        if (sex != null) {
            if (sex.equals("0") || sex.equals("1") || sex.equals("2")) {
                currentUser.setSex(sex);
            } else {
                message = "性别参数输入有误，仅支持0，1，2";
            }
        }

        if (userService.updateUserProfile(currentUser) > 0) {
            // 更新缓存用户信息
            tokenService.setLoginUser(loginUser);
            return success(message);
        }
        return error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        LoginUser loginUser = getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return error("新密码不能与旧密码相同");
        }
        newPassword = SecurityUtils.encryptPassword(newPassword);
        if (userService.resetUserPwd(userName, newPassword) > 0) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(newPassword);
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtils.upload(guidexConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return error("上传图片异常，请联系管理员");
    }

    /**
     * 注册成为教练
     */
    @PutMapping("/register-as-coach")
    public AjaxResult registerCoach(){
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        if(Objects.equals(user.getUserType(), "01")){
            return success("您已经是教练");
        }
        if(userService.registerAsCoach(user.getUserId())>0){
            // 刷新缓存用户信息
            loginUser.setUser(user);
            tokenService.setLoginUser(loginUser);
            return success("注册教练成功");
        }
        return success("您已经是教练");
    }
}
