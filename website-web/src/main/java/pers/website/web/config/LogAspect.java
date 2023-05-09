package pers.website.web.config;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pers.website.common.utils.IpUtils;

import java.util.Arrays;

/**
 * 切面化规范处理日志
 *
 * @author ChenetChen
 * @since 2023/3/7 15:33
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    @Resource
    IpUtils ipUtils;

    private static class RequestLog{
        private final String url;
        private final String localIp;
        private final String remoteIp;
        private final String classMethod;
        private final Object[] args;

        public RequestLog(String url, String localIp, String remoteIp, String classMethod, Object[] args) {
            this.url = url;
            this.localIp = localIp;
            this.remoteIp = remoteIp;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog : {" +
                    "url='" + url + '\'' +
                    ", localIp='" + localIp + '\'' +
                    ", remoteIp='" + remoteIp + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }

    @Pointcut("execution(* pers.website.web.controller.*.*(..))")
    public void log(){

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        String url = null;
        String localIp = null;
        String remoteIp = null;
        String classMethod = null;
        Object[] args = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            HttpServletRequest request = attributes.getRequest();
            url = request.getRequestURL().toString();
            localIp = request.getLocalAddr();
            remoteIp = ipUtils.getIpAddress(request);
            classMethod = joinPoint.getSignature().getDeclaringTypeName()+":"+joinPoint.getSignature().getName();
            args = joinPoint.getArgs();
        }
        RequestLog requestLog = new RequestLog(url, localIp, remoteIp, classMethod, args);
        log.info("-----doBefore-----");
        log.info(requestLog.toString());
    }

    @After("log()")
    public void doAfter(){
        log.info("-----doAfter-----");
    }

    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturning(Object result){
        log.info("-----doAfterReturning-----");
        log.info("Result : {}",result);
    }
}
