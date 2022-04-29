package com.huqz.exception;

import com.huqz.core.Result;
import com.huqz.core.ResultCode;
import com.huqz.core.ResultGenerator;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;

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
        messagingException.printStackTrace();
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
        fileTypeException.printStackTrace();
        return ResultGenerator.fail(ResultCode.INVALID_FILE_SUFFIX, "无效的文件");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result MissingServletRequestParameterException(MissingServletRequestParameterException missingServletRequestParameterException) {
        missingServletRequestParameterException.printStackTrace();
        return ResultGenerator.fail(ResultCode.INVALID_ARGS, "参数不合法");
    }

    /**
     * 接受json数据格式时，没有json数据或者数据在解析时类型不一致导致的错误
     * @param httpMessageNotReadableException ;
     * @return Result
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result HttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) {
        httpMessageNotReadableException.printStackTrace();
        return ResultGenerator.fail(ResultCode.INVALID_ARGS, "参数不合法");
    }

    @ExceptionHandler(FileNotFoundException.class)
    public Result fileNotFoundException(FileNotFoundException fileNotFoundException) {
        fileNotFoundException.printStackTrace();
        return ResultGenerator.fail(ResultCode.NOT_FOUNT, "文件不存在");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
        httpRequestMethodNotSupportedException.printStackTrace();
        return ResultGenerator.fail(ResultCode.METHOD_NOT_ALLOWED, "不支持的请求方式");
    }
}
