package com.sparta.newsfeedteamproject.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
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
    public void apiLog(JoinPoint joinPoint) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String methodType = request.getMethod();
        String requestUrl = request.getRequestURI();
        String methodName = joinPoint.getSignature().getName();

        log.info("HTTP Method : " + methodType);
        log.info("Request URL : " + requestUrl);
        log.info("Request Method Name : " + methodName);
    }
}
