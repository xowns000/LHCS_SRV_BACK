package kr.co.hkcloud.palette3.message.api;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.message.app.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "MessageRestController",
     description = "쪽지 REST 컨트롤러")
public class MessageRestController {
	
		private final MessageService messageService;
		
		
		/**
		 * 
		 * 메서드 설명		: 쪽지 보내기 및 회신
	     * @Method Name  	: sendMsg
	     * @date   			: 2023. 6. 14
	     * @author   		: 김성태
	     * @version     	: 1.0
	     * ----------------------------------------
		 * 
		 * @param mjsonParams
		 * @param result
		 * @return
		 * @throws TelewebApiException
		 */
		@ApiOperation(value = "쪽지 보내기 및 회신",
		      	  notes = "쪽지를 보내거나 회신한다")
		@PostMapping("/api/message/sendMsg")
		public Object sendMsg(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
		{
		
		  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		
		  log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
		  log.debug("<<<<<<<<<<<<<<<" + mjsonParams);
		
		  objRetParams = messageService.sendMsg(mjsonParams);
		
		  return objRetParams;
		}
		
	
		/**
		 * 
	     * 메서드 설명		: 받은쪽지 및 보낸쪽지 조회
	     * @Method Name  	: selectMsgList
	     * @date   			: 2023. 6. 14
	     * @author   		: 김성태
	     * @version     	: 1.0
	     * ----------------------------------------
		 * @param mjsonParams
		 * @param result
		 * @return
		 * @throws TelewebApiException
		 */
		@ApiOperation(value = "받은쪽지 및 보낸쪽지 조회",
	      	  notes = "받은쪽지/보낸쪽지를 조회한다")
		@PostMapping("/api/message/selectMsgList")
		public Object selectMsgList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
		{
		
		  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		
		  log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
		  log.debug("<<<<<<<<<<<<<<<" + mjsonParams);
		
		  objRetParams = messageService.selectMsgList(mjsonParams);
		
		  return objRetParams;
		}
		
		
	    /**
	     * 
	     * 메서드 설명		: 회신쪽지 조회
	     * @Method Name  	: selectMsgList2
	     * @date   			: 2023. 6. 14
	     * @author   		: 김성태
	     * @version     	: 1.0
	     * ----------------------------------------
		 * @param mjsonParams
		 * @param result
		 * @return
		 * @throws TelewebApiException
		 */
		@ApiOperation(value = "회신쪽지 조회",
		      	  notes = "회신받은 쪽지를 조회한다")
		@PostMapping("/api/message/selectMsgList2")
		public Object selectMsgList2(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
		{
		
		  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		
		  log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
		  log.debug("<<<<<<<<<<<<<<<" + mjsonParams);
		
		  objRetParams = messageService.selectMsgList2(mjsonParams);
		
		  return objRetParams;
		}
		
	    /**
	     * 
	     * 메서드 설명		: 사용자 조회
	     * @Method Name  	: selectUser
	     * @date   			: 2023. 7. 10
	     * @author   		: 김성태
	     * @version     	: 1.0
	     * ----------------------------------------
		 * @param mjsonParams
		 * @param result
		 * @return
		 * @throws TelewebApiException
		 */
		@ApiOperation(value = "사용자 조회",
		      	  notes = "사용자를 조회한다")
		@PostMapping("/api/message/selectUser")
		public Object selectUser(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
		{
		
		  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		
		  log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
		  log.debug("<<<<<<<<<<<<<<<" + mjsonParams);
		
		  objRetParams = messageService.selectUser(mjsonParams);
		
		  return objRetParams;
		}
		
		/**
		 * 
	     * 메서드 설명		: 최신 받은 쪽지 조회
	     * @Method Name  	: selectNewMsg
	     * @date   			: 2023. 7. 19
	     * @author   		: 김성태
	     * @version     	: 1.0
	     * ----------------------------------------
		 * @param mjsonParams
		 * @param result
		 * @return
		 * @throws TelewebApiException
		 */
		@ApiOperation(value = "최신 받은 쪽지 조회",
	      	  notes = "최신 받은 쪽지를 조회한다")
		@PostMapping("/api/message/selectNewMsg")
		public Object selectNewMsg(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
		{
		
		  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		
		  objRetParams = messageService.selectNewMsg(mjsonParams);
		
		  return objRetParams;
		}
		
		/**
		 * 
	     * 메서드 설명		: 쪽지 개수 조회
	     * @Method Name  	: selectNewMsg
	     * @date   			: 2023. 7. 19
	     * @author   		: 김성태
	     * @version     	: 1.0
	     * ----------------------------------------
		 * @param mjsonParams
		 * @param result
		 * @return
		 * @throws TelewebApiException
		 */
		@ApiOperation(value = "쪽지 개수 조회",
	      	  notes = "쪽지 개수를 조회한다")
		@PostMapping("/api/message/selectMsgCnt")
		public Object selectMsgCnt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
		{
		
		  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		
		  objRetParams = messageService.selectMsgCnt(mjsonParams);
		
		  return objRetParams;
		}

		/**
		 *
	     * 메서드 설명		: 쪽지 조회 여부 업데이트
	     * @Method Name  	: updateInqMsg
	     * @date   			: 2023. 8. 30
	     * @author   		: 나준영
	     * @version     	: 1.0
	     * ----------------------------------------
		 * @param mjsonParams
		 * @param result
		 * @return
		 * @throws TelewebApiException
		 */
		@ApiOperation(value = "쪽지 조회 여부 업데이트",
	      	  notes = "쪽지 조회 여부 업데이트")
		@PostMapping("/api/message/updateInqMsg")
		public Object updateInqMsg(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
		{

		  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

		  objRetParams = messageService.updateInqMsg(mjsonParams);

		  return objRetParams;
		}

}
