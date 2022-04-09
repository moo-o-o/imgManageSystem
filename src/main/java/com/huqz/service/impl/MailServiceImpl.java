package com.huqz.service.impl;

import com.huqz.pojo.dto.MailDTO;
import com.huqz.service.CodeService;
import com.huqz.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private CodeService codeService;

    @Autowired
    private TemplateEngine templateEngine;

    // 发送人
    private final String from = "awsl.simple@qq.com(小V)";

    public void sendSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from + "(图片管理系统)");
        message.setTo(from);
        message.setSubject("测试邮件");
        message.setText("测试邮件正文内容");

        System.out.println(message.toString());
        javaMailSender.send(message);
    }

    public void sendMail(MailDTO mailDto, String subject, String template) throws MessagingException {
        Map<String, Object> map = new HashMap<>();
        map.put("nickname", mailDto.getNickname());
        map.put("username", mailDto.getUsername());
        map.put("mail", mailDto.getMail());
        map.put("mailCode", mailDto.getMailCode());

        Context context = new Context();
        context.setVariables(map);
        String emailText = templateEngine.process(template, context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(mailDto.getMail());             // 设置邮件接受者
        helper.setSubject(subject);                  // 设置邮件主题
        helper.setText(emailText, true);        // 设置邮件内容，true表示发送html格式
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendRegisterMail(MailDTO mailDto) throws MessagingException {
        sendMail(mailDto, "图片管理系统：账号注册", "mail/RegisterCode");
    }

    @Override
    public void sendForgetMail(MailDTO mailDto) throws MessagingException {
        sendMail(mailDto, "图片管理系统：找回密码", "mail/ForgetCode");
    }

    @Override
    public void sendResetMail(MailDTO mailDto) throws MessagingException {
        sendMail(mailDto, "图片管理系统：修改邮箱", "mail/UpdateMailCode");
    }
}
