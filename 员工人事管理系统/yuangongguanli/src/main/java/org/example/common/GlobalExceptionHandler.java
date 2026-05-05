package org.example.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 全局异常处理
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class) // @ExceptionHandler 表示处理指定异常
    public Result<?> handleException(Exception e){
        e.printStackTrace(); // 打印异常信息
        return Result.error("服务器异常" + e.getMessage()); //e.getMessage() 打印异常的具体信息
    }
}
