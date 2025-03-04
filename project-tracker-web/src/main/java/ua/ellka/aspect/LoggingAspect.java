package ua.ellka.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("execution(* ua.ellka.service.*.*(..))")
    private void serviceMethods() {}

    @Pointcut("execution(* ua.ellka.repo.*.*(..))")
    private void repoMethods() {}

    @Pointcut("execution(* ua.ellka.controller.*.*(..))")
    private void controllerMethods() {}

    @Around("serviceMethods() || repoMethods() || controllerMethods()")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // before calling the method
        log.info("Calling {}({})", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));

        Object retVal = joinPoint.proceed();

        // after returning from methods
        log.info("Returning {} = {}", joinPoint.getSignature().getName(), retVal);

        return retVal;
    }

    @AfterThrowing(pointcut = "serviceMethods() || repoMethods() || controllerMethods()", throwing = "throwable")
    public void logAfterThrow(Throwable throwable) {
        log.error("Exception thrown by {}({})", throwable.getStackTrace()[0].getMethodName(), throwable.getMessage());
    }
}
