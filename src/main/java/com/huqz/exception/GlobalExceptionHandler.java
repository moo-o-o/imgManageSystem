package com.huqz.exception;

import com.huqz.core.Result;
import com.huqz.core.ResultCode;
import com.huqz.core.ResultGenerator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MailCodeException.class)
    public Result MailCodeException(MailCodeException mailCodeException) {
        mailCodeException.printStackTrace();
        return ResultGenerator.fail(ResultCode.EXPIRED_MAIL_CODE, "验证码已过期或未获取验证码");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result MethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
//        List<String> errorList = new ArrayList<>();  // ["mail: 邮箱不合法;","username: 用户名不合法;"]
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String msg = String.format("%s: %s;", fieldError.getField(), fieldError.getDefaultMessage());
//            errorList.add(msg);
            System.out.println(msg);
        }
        methodArgumentNotValidException.printStackTrace();

        return ResultGenerator.fail(ResultCode.INVALID_ARGS, "参数不合法");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public Result UsernameNotFoundException(UsernameNotFoundException usernameNotFoundException) {
        usernameNotFoundException.printStackTrace();
        return ResultGenerator.fail(ResultCode.INVALID_ACCOUNT, "账号不存在");
    }

    @ExceptionHandler(MessagingException.class)
    public Result MessagingException(MessagingException messagingException) {
        return ResultGenerator.fail(ResultCode.SEND_MAIL_FAILED, "发送邮件失败");
    }

    @ExceptionHandler(Exception.class)
    public Result Exception(Exception e) {
        System.out.println("---------------------");
        e.printStackTrace();
        return ResultGenerator.fail(ResultCode.INTERNAL_SERVER_ERROR, "服务器内部错误");
    }

    @ExceptionHandler(FileTypeException.class)
    public Result FileTypeException(FileTypeException fileTypeException) {
        return ResultGenerator.fail(ResultCode.INVALID_FILE_SUFFIX, "无效的文件");
    }
}
