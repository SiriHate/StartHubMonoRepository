package org.siri_hate.notification_service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* org.siri_hate.notification_service.service..*(..))")
    private void serviceMethods() {}

    @Around("serviceMethods()")
    public Object aroundAllServiceMethodsAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String methodName = methodSignature.getName();
        LOGGER.debug("Service method: {} - was called", methodName);

        Object targetMethodResult;
        try {
            targetMethodResult = proceedingJoinPoint.proceed();
            LOGGER.debug("Service method: {} - has completed", methodName);
        } catch (Throwable throwable) {
            LOGGER.error("Exception occurred in service method {}: {}", methodName, throwable.getMessage());
            throw throwable;
        }

        return targetMethodResult;
    }
}