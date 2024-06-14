package com.sparta.newsfeedteamproject.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j(topic = "API Request Information")
@Aspect
@Component
public class ApiReqInfoAop {

    @Pointcut("execution(* com.sparta.newsfeedteamproject.controller..*(..))")
    private void controller() {
    }

    @Before("controller()")
    public void apiLog() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.info("Request URL : " + request.getRequestURI());
        log.info("HTTP Method : " + request.getMethod());
    }
}
