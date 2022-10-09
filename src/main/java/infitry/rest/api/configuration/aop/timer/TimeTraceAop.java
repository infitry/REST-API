package infitry.rest.api.configuration.aop.timer;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class TimeTraceAop {

    @Pointcut("@annotation(infitry.rest.api.configuration.aop.timer.Timer)")
    private void enableTimer(){}

    @Around("execution(* infitry.rest.api.service..*(..)) || enableTimer()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("START : " + joinPoint.toString());
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            log.info("END : " + joinPoint + " " + timeMs + " ms");
        }
    }
}
