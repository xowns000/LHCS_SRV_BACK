package kr.co.hkcloud.palette3.phone.callback.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.callback.app.PhoneCallbackManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneCallbackManageRestController", description = "콜백접수조회 REST 컨트롤러")
public class PhoneCallbackManageRestController {
	private final PhoneCallbackManageService phoneCallbackManageService;

	/**
	 * 
	 * 콜백접수정보 조회
	 * 
	 * @Method Name : selectClbkInqList
	 * @date : 2023. 7. 19.
	 * @author : NJY
	 * @version : 1.0 ----------------------------------------
	 * @param mjsonParams
	 * @return TelewebJSON 형식의 조회결과 데이터
	 * @throws TelewebApiException
	 */
	@ApiOperation(value = "콜백접수 조회", notes = "콜백접수정보를 조회한다.")
	@PostMapping("/phone-api/callback/manage/list")
	public Object selectClbkMngList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
		// client에서 전송받은 파라메터 생성
		// TelewebJSON mjsonParams = (TelewebJSON)inHashMap.get("mjsonParams");

		// 반환 파라메터 생성
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
		jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

		// DAO 검색 메소드 호출
		objRetParams = phoneCallbackManageService.selectClbkMngList(mjsonParams);

		// 최종결과값 반환
		return objRetParams;
	}

	/**
	 * 콜백 완료처리
	 * 
	 * @param inHashMap
	 * @return TelewebJSON 형식의 처리 결과 데이터
	 */
	@ApiOperation(value = "콜백 완료처리", notes = "콜백 완료처리")
	@PostMapping("/phone-api/callback/manage/modify")
	public Object updateClbkMng(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

		TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
		TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
		jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
		String strTransFlag = mjsonParams.getString("DATA_FLAG"); // 화면에서 전송된 플래그 설정

		// 메시지 파라미터 배열
		for (int i = 0; i < mjsonParams.getSize(); i++) {

			// 파라미터 값
			jsonParams.setString("USER_ID", mjsonParams.getString("USER_ID"));
			jsonParams.setString("CUSTCO_ID", mjsonParams.getString("CUCTCO_ID"));
			jsonParams.setString("CLBK_ID", mjsonParams.getString("CLBK_ID", i));
			

			if (mjsonParams.getString("UPDT_DIV", i).equals("1")) {
				// 결과값이 없는 경우
				// 콜백 상태값 변경
				objRetParams = phoneCallbackManageService.updateClbkMng(jsonParams);

				// 콜백배분 등록
				objRetParams = phoneCallbackManageService.insertClbkMngDiv(jsonParams);

			} else {
				// 결과값이 있는 경우
				// 콜백 완료처리
				objRetParams = phoneCallbackManageService.updateClbkMngDiv(jsonParams);
			}
		}

		// 최종결과값 반환
		return objRetParams;
	}

	/**
	 * 
	 * 콜백배분정보 조회
	 * 
	 * @Method Name : selectClbkDivList
	 * @date : 2023. 7. 19.
	 * @author : NJY
	 * @version : 1.0 
	 * ----------------------------------------
	 * @param mjsonParams
	 * @return TelewebJSON 형식의 조회결과 데이터
	 * @throws TelewebApiException
	 */
	@ApiOperation(value = "콜백배분 조회", notes = "콜백배분정보를 조회한다.")
	@PostMapping("/phone-api/callback/manage/callback-dstb/inqire")
	public Object selectClbkDivList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
		// client에서 전송받은 파라메터 생성
		// TelewebJSON mjsonParams = (TelewebJSON)inHashMap.get("mjsonParams");

		// 반환 파라메터 생성
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

		// DAO 검색 메소드 호출
		objRetParams = phoneCallbackManageService.selectClbkDivList(mjsonParams);

//        objRetParams.setHeader("ERROR_FLAG", true);
//        objRetParams.setHeader("ERROR_MSG", "정상적으로 조회 되었습니다.");
//        objRetParams.setHeader("ERROR_FLAG", false);

		// 최종결과값 반환
		return objRetParams;
	}

	/**
	 * 콜백상담원배분정보를 조회한다.
	 * 
	 * @param mjsonParams
	 * @return TelewebJSON 형식의 조회결과 데이터
	 * @throws TelewebApiException
	 */
	@ApiOperation(value = "콜백상담원배분 조회", notes = "콜백상담원배분정보를 조회한다.")
	@PostMapping("/phone-api/callback/manage/agent-dstb/inqire")
	public Object selectClbkDstrDivList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
		// 반환 파라메터 생성
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

		// DAO 검색 메소드 호출
		objRetParams = phoneCallbackManageService.selectClbkDstrDivList(mjsonParams);

		// 최종결과값 반환
		return objRetParams;
	}

	/**
	 * 
	 * 콜백 배분 등록
	 * 
	 * @Method Name : insertClbkDiv
	 * @date : 2023. 7. 20.
	 * @author : NJY
	 * @version : 1.0 ----------------------------------------
	 * @param mjsonParams
	 * @return TelewebJSON 형식의 삭제 결과 데이터
	 * @throws TelewebApiException
	 */
	@ApiOperation(value = "콜백배분 등록", notes = "콜백배분 등록한다.")
	@PostMapping("/phone-api/callback/manage/regist")
	public Object insertClbkDiv(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

		TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
		String strTransFlag = mjsonParams.getString("DATA_FLAG"); // 화면에서 전송된 플래그 설정

		// 콜백배분처리 등록
		objRetParams = phoneCallbackManageService.insertClbkDiv(mjsonParams);

		// 최종결과값 반환
		return objRetParams;
	}

	/**
	 * 
	 * 콜백 배분 회수
	 * 
	 * @Method Name : deleteClbkDiv
	 * @date : 2023. 7. 19.
	 * @author : NJY
	 * @version : 1.0 ----------------------------------------
	 * @param mjsonParams
	 * @return TelewebJSON 형식의 삭제 결과 데이터
	 * @throws TelewebApiException
	 */
	@ApiOperation(value = "콜백배분 회수", notes = "콜백배분 회수")
	@PostMapping("/phone-api/callback/manage/delete")
	public Object deleteClbkDiv(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
		// 반환 파라메터 생성
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		/*
		 * TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
		 * jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
		 * 
		 * String strTransFlag = mjsonParams.getString("DATA_FLAG");
		 */
		/*
		 * for(int i = 0; i < mjsonParams.getSize(); i++) {
		 * 
		 * jsonParams.setString("CLBK_ID", mjsonParams.getString("CLBK_ID", i));
		 * jsonParams.setString("CUSL_ID", mjsonParams.getString("CUSL_ID", i)); //
		 * jsonParams.setString("COL_CNT", mjsonParams.getString("TRY_CNT", i));
		 * log.debug("######### jsonParams :"+ jsonParams);
		 * 
		 * objRetParams = phoneCallbackManageService.deleteClbkDiv(jsonParams); 잠깐 주석
		 * 
		 * objRetParams.setHeader("ERROR_FLAG", false);
		 * objRetParams.setHeader("ERROR_MSG", "정상 회수 되었습니다."); }
		 */
		// 최종결과값 반환
		log.debug("######### jsonParams :"+ mjsonParams);
		objRetParams = phoneCallbackManageService.deleteClbkDiv(mjsonParams);

		return objRetParams;
	}
}
