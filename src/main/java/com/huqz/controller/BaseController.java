package com.huqz.controller;

import com.huqz.core.Result;
import com.huqz.core.ResultCode;
import com.huqz.core.ResultGenerator;
import com.huqz.exception.MailCodeException;
import com.huqz.model.Category;
import com.huqz.model.User;
import com.huqz.pojo.userDTO.CodeDTO;
import com.huqz.pojo.userDTO.MailDTO;
import com.huqz.pojo.userDTO.RegDTO;
import com.huqz.pojo.userDTO.ResetDTO;
import com.huqz.service.*;
import com.huqz.utils.CheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private MailService mailService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private CacheService cacheService;

    @PostMapping("/register")
    public Result register(@Validated @RequestBody RegDTO regDTO) throws MailCodeException {

        if (regDTO.getNickname() == null || "".equals(regDTO.getNickname())) {
            regDTO.setNickname("新用户");
        }

        // 查询用户名和邮箱是否被占用
        User user = userService.getByUsername(regDTO.getUsername());
        if (user != null) return ResultGenerator.fail(ResultCode.EXISTING_USERNAME, "用户名已存在");
        user = userService.getByMail(regDTO.getMail());
        if (user != null) return ResultGenerator.fail(ResultCode.EXISTING_MAIL, "该邮箱已注册");

        // 验证码是否正确
        MailDTO mailDTO = cacheService.getByToken(regDTO.getToken());
        System.out.println("@mailDTO: " + mailDTO.toString());
        if (!mailDTO.getMailCode().equals(regDTO.getMailCode())) {
            return ResultGenerator.fail(ResultCode.INVALID_MAIL_CODE, "验证码错误");
        }

        user = new User();
        user.setUsername(regDTO.getUsername());
        user.setPassword(regDTO.getPassword());
        user.setNickname(regDTO.getNickname());
        user.setMail(regDTO.getMail());
        userService.save(user);

        // 删除验证码
        cacheService.delMailCode(regDTO.getToken());
        return ResultGenerator.ok();
    }

//    @PostMapping("/forget")
    public Result forget(@Validated @RequestBody CodeDTO codeDTO) throws MessagingException {
        // 忘记密码，发送重置邮件
        String username = codeDTO.getUsername();
        String mail = codeDTO.getMail();

        if (username == null && mail == null) {
            return ResultGenerator.fail(ResultCode.INVALID_ARGS, "参数不合法");
        }

        // 查询数据库获取 user 对象
        User user;
        if (username != null) {
            user = userService.getByUsername(username);
        }else {
            user = userService.getByMail(mail);
        }

        if (user == null) return ResultGenerator.fail(ResultCode.INVALID_ACCOUNT, "账户不存在");

        String token = authTokenService.genToken();
        String code = codeService.genCode();

        MailDTO mailDto = new MailDTO();
        mailDto.setUsername(user.getUsername());
        mailDto.setNickname(user.getNickname());
        mailDto.setMail(user.getMail());
        mailDto.setMailCode(code);

        cacheService.storeMailCode(mailDto, token);
        mailService.sendForgetMail(mailDto);

        return ResultGenerator.token("发送邮件成功", token);
    }

    @PostMapping("/reset")
    public Result reset(@Validated @RequestBody ResetDTO resetDto) throws MailCodeException {
        // 附带验证码和新密码重置
        String token = resetDto.getToken();

        MailDTO mailDTO = cacheService.getByToken(token);

        if (!mailDTO.getMailCode().equals(resetDto.getMailCode())) {
            return ResultGenerator.fail(ResultCode.INVALID_MAIL_CODE, "验证码错误");
        }

        String username = mailDTO.getUsername();
        User user = userService.getByUsername(username);
        userService.updatePasswordById(resetDto.getPassword(), user.getId());

        // 删除验证码
        cacheService.delMailCode(token);
        return ResultGenerator.ok();
    }

    @PostMapping("/send_reg_mail")
    public Result send(@Validated @RequestBody MailDTO mailDTO) throws MessagingException {

        // 查询用户名和邮箱是否被占用
        User user = userService.getByUsername(mailDTO.getUsername());
        if (user != null) return ResultGenerator.fail(ResultCode.EXISTING_USERNAME, "该用户名已存在");
        user = userService.getByMail(mailDTO.getMail());
        if (user != null) return ResultGenerator.fail(ResultCode.EXISTING_MAIL, "该邮箱已注册");

        String token = authTokenService.genToken();
        String code = codeService.genCode();
        mailDTO.setMailCode(code);
        cacheService.storeMailCode(mailDTO, token);
        mailService.sendRegisterMail(mailDTO);
        return ResultGenerator.token("发送邮件成功", token);
    }

    @PostMapping("/send_forget_mail")
    public Result sendMail(@RequestBody Map<String, String> map) throws MessagingException {

        String mail = map.get("mail");
        String username = map.get("username");

        User user;
        if (mail != null && !"".equals(mail) && CheckUtils.checkMail(mail)) {
            user = userService.getByMail(mail);
            if (user == null) return ResultGenerator.fail(ResultCode.ERROR_ACCOUNT_OR_EMAIL, "该邮箱不存在");
        } else if (username != null && !"".equals(username) && CheckUtils.checkUsername(username)) {
            user = userService.getByUsername(username);
            if (user == null) return ResultGenerator.fail(ResultCode.ERROR_ACCOUNT_OR_EMAIL, "该用户名不存在");
        }else {
            return ResultGenerator.fail(ResultCode.INVALID_ARGS, "不合法的参数");
        }

        String token = authTokenService.genToken();
        String code = codeService.genCode();

        MailDTO mailDTO = new MailDTO();
        mailDTO.setUsername(user.getUsername());
        mailDTO.setMail(user.getMail());
        mailDTO.setNickname(user.getNickname());
        mailDTO.setMailCode(code);

        cacheService.storeMailCode(mailDTO, token);
        mailService.sendForgetMail(mailDTO);

        return ResultGenerator.token("发送邮件成功", token);
    }

}
