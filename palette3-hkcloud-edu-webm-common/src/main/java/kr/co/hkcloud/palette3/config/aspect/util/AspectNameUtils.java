package kr.co.hkcloud.palette3.config.aspect.util;


import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class AspectNameUtils
{
    /**
     * 클래스 경로
     * 
     * @param  proceedingJoinPoint
     * @return
     * @throws TelewebUtilException
     */
    public String getClassPath(ProceedingJoinPoint proceedingJoinPoint) throws TelewebUtilException
    {
        return proceedingJoinPoint.getTarget().getClass().getName();
    }


    /**
     * 메서드 경로
     * 
     * @param  proceedingJoinPoint
     * @return
     * @throws TelewebUtilException
     */
    public String getMehodPath(ProceedingJoinPoint proceedingJoinPoint) throws TelewebUtilException
    {
        return proceedingJoinPoint.getSignature().toLongString();
    }


    /**
     * 메서드명
     * 
     * @param  proceedingJoinPoint
     * @return
     * @throws TelewebUtilException
     */
    public String getMehodName(ProceedingJoinPoint proceedingJoinPoint) throws TelewebUtilException
    {
        return proceedingJoinPoint.getSignature().getName();
    }
}
