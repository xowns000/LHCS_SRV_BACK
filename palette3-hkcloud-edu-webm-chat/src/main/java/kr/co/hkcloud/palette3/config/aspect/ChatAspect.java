package kr.co.hkcloud.palette3.config.aspect;

import java.util.LinkedHashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import kr.co.hkcloud.palette3.chat.main.util.ChatMainUtils;
import kr.co.hkcloud.palette3.config.aspect.util.AspectLoggerUtils;
import kr.co.hkcloud.palette3.config.aspect.util.AspectNameUtils;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebMainParamSignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
@Order(200)
public class ChatAspect
{
	private final ChatMainUtils chatMainUtils;
	
	private AspectLoggerUtils aspectLoggerUtils;
	private AspectNameUtils   aspectNameUtils;
	
    @Autowired
    private final PaletteJsonUtils paletteCmmnJsonUtils;
    
    @Autowired
    public void setAspectLoggerUtils(AspectLoggerUtils aspectLoggerUtils)
    {
        this.aspectLoggerUtils = aspectLoggerUtils;
    }
    
    @Autowired
    public void setAspectNameUtils(AspectNameUtils aspectNameUtils)
    {
        this.aspectNameUtils = aspectNameUtils;
    }
    
    /**
     * 채팅메인 REST컨트롤러 Advice
     * 파라미터 위변조 검증 
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
	@SuppressWarnings("unchecked")
	@Before("execution (* kr.co.hkcloud.palette.chat..api.*VueRestController.*(..)) ")
    public void vueRestControllerBeforeAdvice(JoinPoint joinPoint) throws Throwable
    {
		 StringBuffer sbLog = new StringBuffer("\n");
	 	 sbLog.append("---------------------------------------------\n");
	     sbLog.append("vueRestControllerAroundAdvice Data\n");
	     sbLog.append("---------------------------------------------\n");
	     
	     String bodyJSONString 	= "";
	     String headerSignature	= "";
	     
	     Object[] arguments = joinPoint.getArgs();
	     for (Object arg : arguments) {	    	  
    		 
	    	 // header signature
	    	 if(arg instanceof LinkedHashMap) {
	    		 headerSignature =  (String)((LinkedHashMap<Object, Object>)arg).get("x-teletalk-signature");
	    	 }
	    	 
	    	 // body string 
	    	 if(arg instanceof JSONObject) {
	    		 bodyJSONString =  arg.toString();
	    	 }
	    	  
	     }
     
	     if(bodyJSONString != null && !"".equals(bodyJSONString))
	     {
	    	   if(headerSignature == null || "".equals(headerSignature))	{
	    		   
	    		   log.debug("bodyJSONString={}" , bodyJSONString);
		           log.debug("headerSignature={}" ,headerSignature);
	    		   throw new TelewebMainParamSignatureException();
	    	   }
           
	           try {
	        	   	        	   
	        	    String json = paletteCmmnJsonUtils.jsonToString(bodyJSONString);
	        	    chatMainUtils.signatureValidation(headerSignature, json);
	           } catch(TelewebMainParamSignatureException e) {
	          	 
		          	log.error("MainParamSignatureException", e);
		          	log.debug("bodyJSONString={}" , bodyJSONString);
		            log.debug("headerSignature={}" ,headerSignature);
		            
		            throw e;
	           }
	     }
	   
    }

	
    /**
     * 
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution (* kr.co.hkcloud.palette3.infra.chat.util.TeletalkReceiverUtils.checkValidationMessage(..)) && !@annotation(kr.co.hkcloud.palette3.config.aspect.NoAspectAround)")
    public Object teletalkReceiverUtilsAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        String methodpath = aspectNameUtils.getMehodPath(proceedingJoinPoint);
        log.trace("\n===============================================================================\n<!--T E L E T A L K  R E C E I V E R  U T I L S  START\n{}\n\n", methodpath);
        
        StopWatch stopWatch = new StopWatch(methodpath);
        /////////////////////////////////////////////////////////////////////////////////
        //BEFORE
        stopWatch.start("before");
        aspectLoggerUtils.teletalkReceiverUtilsBeforeLog(proceedingJoinPoint);

        
        stopWatch.stop();
        /////////////////////////////////////////////////////////////////////////////////
        //PROCEED
        stopWatch.start("proceed");
        Object result = proceedingJoinPoint.proceed();
        
        
        stopWatch.stop();
        /////////////////////////////////////////////////////////////////////////////////
        //AFTER
        stopWatch.start("after");
        aspectLoggerUtils.teletalkReceiverUtilsAfterLog(stopWatch, result);
        
        log.trace("\n\n{}\nT E L E T A L K  R E C E I V E R  U T I L S  END-->\n===============================================================================", methodpath);
        return result;
    }
}
