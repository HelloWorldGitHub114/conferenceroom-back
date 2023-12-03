package com.xd11z.myserver.myserver.interceptor;

import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.xd11z.myserver.annotation.PassToken;
import com.xd11z.myserver.annotation.UserLoginToken;
import com.xd11z.myserver.util.TokenTool;
import com.xd11z.myserver.util.logger;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * 拦截类，用于拦截请求判断token
 */
public class AuthenticationInterceptor implements HandlerInterceptor {


    /**
     * 预处理回调方法,实现处理器的预处理，第三个参数为响应的处理器,自定义Controller,
     * 返回值为true表示继续流程（如调用下一个拦截器或处理器）或者接着执行
     * postHandle()和afterCompletion()；false表示流程中断，不会继续调用其他的拦截器或处理器，中断执行。
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return 是否允许通过
     * @throws Exception 抛出的异常由全局异常处理器处理并给前端回复
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");// 从 http 请求头中取出 token
        logger.write(token);//写个日志，可删掉
        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 验证 token
                try {
                    TokenTool.verify(token);
                } catch (SignatureVerificationException e) {
                    throw new RuntimeException("无效签名！");
                }catch (TokenExpiredException e){
                    throw new RuntimeException("token过期");
                }catch (AlgorithmMismatchException e){
                    throw new RuntimeException("算法不一致");
                }catch (Exception e){
                    throw new RuntimeException("token无效！");
                }
                return true;
            }
        }
        return true;
    }

    /**
     * 后处理回调方法，实现处理器的后处理（DispatcherServlet进行视图返回渲染之前进行调用），
     * 此时我们可以通过modelAndView（模型和视图对象）对模型数据进行处理或对视图进行处理，modelAndView也可能为null
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the handler (or {@link HandlerMethod}) that started asynchronous
     * execution, for type and/or instance examination
     * @param modelAndView the {@code ModelAndView} that the handler returned
     * (can also be {@code null})
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {

    }

    /**
     * 整个请求处理完毕回调方法,该方法也是需要当前对应的Interceptor的preHandle()的返回值为true时才会执行，
     * 也就是在DispatcherServlet渲染了对应的视图之后执行。
     * 用于进行资源清理。整个请求处理完毕回调方法。
     * 如性能监控中我们可以在此记录结束时间并输出消耗时间，还可以进行一些资源清理，
     * 类似于try-catch-finally中的finally，但仅调用处理器执行链中
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the handler (or {@link HandlerMethod}) that started asynchronous
     * execution, for type and/or instance examination
     * @param ex any exception thrown on handler execution, if any; this does not
     * include exceptions that have been handled through an exception resolver
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {

    }
}