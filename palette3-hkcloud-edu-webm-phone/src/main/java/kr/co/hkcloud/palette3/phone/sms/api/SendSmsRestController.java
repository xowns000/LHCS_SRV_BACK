package kr.co.hkcloud.palette3.phone.sms.api;


import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.sms.app.SendSmsService;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@RestController
@Api(value = "SendSmsRestController",
     description = "문자발송 관리 REST 컨트롤러")
public class SendSmsRestController
{
	private final SendSmsService sendSmsService;
	
    /**
     *
     * @return
     */
    @ApiOperation(value = "mts 문자발송 요청",
            notes = "mts 문자 발송 요청")
    @PostMapping("/api/mts/send/sendSMS")
    public Object sendSMS(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException
    {
    	return sendSmsService.sendSMS(mjsonParams);
    }
    
    /**
    *
    * @return
    */
   @ApiOperation(value = "mts 문자발송 요청",
           notes = "mts 문자 발송 요청")
   @PostMapping("/api/mts/send/sendAtalk")
   public Object sendAtalk(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException
   {
       return sendSmsService.sendAtalk(mjsonParams);
   }
   
   /**
   *
   * @return
   */
  @ApiOperation(value = "mts 문자발송 결과",
          notes = "mts 문자 발송 결과")
  @PostMapping("/api/mts/send/getSndngRslt")
  public Object getSndngRslt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException
  {
	  TelewebJSON resultParam = new TelewebJSON();
	  resultParam = sendSmsService.getSndngRslt(mjsonParams);
//	  // ScheduledExecutorService 생성
//      ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//
//      // 실행할 작업 정의 (Runnable 또는 Callable 인터페이스 구현)
//      Runnable sndngRslt = () -> {
//    	  sendSmsService.getSndngRslt(mjsonParams);
//      };
//
//      // N초 후에 작업 실행
//      int delayInSeconds = 180; // 예: 180초 후에 실행
//      executorService.schedule(sndngRslt, delayInSeconds, TimeUnit.SECONDS);
//
//      // 프로그램이 종료되지 않게 하기 위해 잠시 대기
//      try {
//          Thread.sleep(6000); // 작업이 실행될 시간 + 여유 시간
//      } catch (InterruptedException e) {
//          e.printStackTrace();
//      }
	  return resultParam;
  }
}
