package it.interno.normalizzatore.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@Log4j2
public class NormalizzatoreIndirizziLogger {

    private static final String POINTCUT = "within(it.interno.normalizzatore.controller.*)";

    @AfterThrowing(pointcut = POINTCUT, throwing = "e")
    public void logAfterExceptionController(JoinPoint joinPoint, Throwable e) {

        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                constructLogMsg(joinPoint), e);

        String collect = Arrays.stream(e.getStackTrace()).map(String::valueOf).collect(Collectors.joining("\n"));
        log.error(collect);
    }

    private String constructLogMsg(JoinPoint joinPoint) {

        String args = Arrays.stream(joinPoint.getArgs()).map(String::valueOf).collect(Collectors.joining(",", "[", "]"));
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName());
        sb.append(":");
        sb.append(args);

        return sb.toString();
    }

}
