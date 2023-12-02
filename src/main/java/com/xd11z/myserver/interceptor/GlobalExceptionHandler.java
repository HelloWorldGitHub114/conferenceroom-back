package com.xd11z.myserver.interceptor;

import com.xd11z.myserver.entity.ServerResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获器，用于处理拦截器拦截抛出的错误，给前端一个回应
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ServerResponse handleException(Exception e) {
        String msg = e.getMessage();
        //没有错误信息就填默认的服务器出错
        if (msg == null || msg.equals("")) {
            msg = "服务器出错";
        }
        //也许这里应该依据不同的情况返回不同的code?
        return ServerResponse.fail(500, msg, null);
    }
}