package com.huqz.controller;

import com.huqz.core.Result;
import com.huqz.core.ResultCode;
import com.huqz.core.ResultGenerator;
import com.huqz.exception.FileTypeException;
import com.huqz.exception.MailCodeException;
import com.huqz.model.ApiKey;
import com.huqz.model.User;
import com.huqz.pojo.imgDTO.FileDTO;
import com.huqz.pojo.userDTO.MailDTO;
import com.huqz.service.*;
import com.huqz.utils.CheckUtils;
import com.huqz.utils.DesensitizedUtils;
import com.huqz.utils.FilesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/me")
public class UserController {

    @Autowired
    private FilesUtils filesUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private MailService mailService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ApiKeyService apiKeyService;


    @GetMapping
    public Result getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer userId = principal.getId();
        User user = userService.getById(userId);
        if (user == null) return ResultGenerator.fail(ResultCode.UNAUTHORIZED, "请先登录");

        Map<String, Object> res = new HashMap<>();
        res.put("id", userId);
        res.put("username", user.getUsername());
        res.put("nickname", user.getNickname());
        // 邮箱脱敏
        res.put("email", DesensitizedUtils.deEmail(user.getMail()));
        res.put("headImg", user.getHeadImg());

        return ResultGenerator.ok(res);
    }

    @PostMapping("/pwd")
    public Result updatePwd(@RequestBody @Validated Map<String, String> map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        String oldPassword = map.get("oldPassword");
        String newPassword = map.get("newPassword");
        String verifyPassword = map.get("verifyPassword");

        if ("".equals(oldPassword) || "".equals(newPassword) || "".equals(verifyPassword))
            return ResultGenerator.fail(ResultCode.INVALID_ARGS, "不合法的参数");

        if (!CheckUtils.checkPassword(newPassword)) return ResultGenerator.fail(ResultCode.INVALID_ARGS, "密码不合法");

        if (!newPassword.equals(verifyPassword)) return ResultGenerator.fail(ResultCode.FAIL, "两次密码不一致");

        if (oldPassword.equals(newPassword)) return ResultGenerator.fail(ResultCode.THE_SAME_PASSWORD, "新密码不能与原密码一致");

        boolean matches = new BCryptPasswordEncoder(10).matches(oldPassword, principal.getPassword());
        if (!matches) return ResultGenerator.fail(ResultCode.PASSWORD_ERROR, "原密码错误");

        principal.setPassword(newPassword);
        userService.updateById(principal);
        return ResultGenerator.ok();
    }

    @PostMapping("/email")
    public Result updateEmail(@RequestBody Map<String, String> map) throws MailCodeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        String mail = map.get("mail");
        String token = map.get("token");
        String mailCode = map.get("mailCode");
        if ("".equals(token) || "".equals(mailCode) || "".equals(mail))
            return ResultGenerator.fail(ResultCode.INVALID_ARGS, "不合法的参数");
        if (!CheckUtils.checkMail(mail))
            return ResultGenerator.fail(ResultCode.INVALID_ARGS, "邮箱格式错误");

        MailDTO mailDTO = cacheService.getByToken(token);
        if (mailDTO == null)
            return ResultGenerator.fail(ResultCode.EXPIRED_MAIL_CODE, "验证码已过期或为获取验证码");
        if (!mailDTO.getMail().equals(mail))
            return ResultGenerator.fail(ResultCode.ERROR_REGISTER_DATA, "该验证码仅用于指定的邮箱");
        if (!mailDTO.getMailCode().equals(mailCode))
            return ResultGenerator.fail(ResultCode.INVALID_MAIL_CODE, "验证码错误");

        cacheService.delMailCode(token);
        principal.setMail(mailDTO.getMail());
        userService.updateById(principal);
        return ResultGenerator.ok();
    }

    @PostMapping("/avatar")
    public Result updateHead(@RequestParam("headImage")MultipartFile file) throws FileTypeException, Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        FileDTO upload = filesUtils.save(file, "head");
        String path = upload.getPath();
        String filename = path.substring(path.lastIndexOf(File.separator) + 1);
        principal.setHeadImg(filename);

        userService.updateById(principal);
        return ResultGenerator.ok(upload);
    }

    @PostMapping("/nickname")
    public Result updateNickname(@RequestBody Map<String, String> map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        String nickname = map.get("nickname");
        if ("".equals(nickname)) return ResultGenerator.fail(ResultCode.INVALID_ARGS, "不合法的参数");

        principal.setNickname(nickname);
        userService.updateById(principal);
        return ResultGenerator.ok();

    }

    @PostMapping("/send_upload_mail")
    public Result sendMail(@RequestBody Map<String, String> map) throws MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        String mail = map.get("mail");
        if ("".equals(mail)) return ResultGenerator.fail(ResultCode.INVALID_ARGS, "不合法的参数");
        if (!CheckUtils.checkMail(mail))
            return ResultGenerator.fail(ResultCode.INVALID_ARGS, "邮箱格式错误");

        User u = userService.getByMail(mail);
        if (u != null) return ResultGenerator.fail(ResultCode.EXISTING_MAIL, "该邮箱已存在");

        String token = authTokenService.genToken();
        String code = codeService.genCode();

        MailDTO mailDTO = new MailDTO();
        mailDTO.setUsername(principal.getUsername());
        mailDTO.setMail(mail);
        mailDTO.setNickname(principal.getNickname());
        mailDTO.setMailCode(code);

        cacheService.storeMailCode(mailDTO, token);
        mailService.sendResetMail(mailDTO);

        return ResultGenerator.token("发送邮件成功", token);
    }

    @GetMapping("/key")
    public Result getApiKey() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer userId = principal.getId();

        List<ApiKey> keyList = apiKeyService.getByUserId(userId);
        return ResultGenerator.ok(keyList);
    }

    @DeleteMapping("/key/{key}")
    public Result removeApiKey(@PathVariable String key) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer userId = principal.getId();
        ApiKey a = apiKeyService.getByUserIdAndApiKey(userId, key);
        if (a == null) return ResultGenerator.ok();

        apiKeyService.removeById(a.getId());
        return ResultGenerator.ok();
    }
}
