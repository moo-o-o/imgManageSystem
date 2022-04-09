package com.huqz.service;

import com.huqz.pojo.userDTO.MailDTO;

import javax.mail.MessagingException;

public interface MailService {
    void sendRegisterMail(MailDTO mailDto) throws MessagingException;
    void sendForgetMail(MailDTO mailDto) throws MessagingException;
    void sendResetMail(MailDTO mailDto) throws MessagingException;
}
