package kr.co.hkcloud.palette3.common.board.api;


import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.board.app.BoardService;
import kr.co.hkcloud.palette3.common.board.util.BoardCmmnValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.security.authentication.domain.PaletteUserDetailsVO;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "BoardCmmnRestController",
     description = "게시물공통 REST 컨트롤러")
public class BoardCmmnRestController
{
    private final TwbComDAO          twbComDao;
    private final BoardCmmnValidator boardCmmnValidator;
    private final BoardService       boardService;


    /**
     * 게시물 첨부파일 업로드
     * 
     * @param  TelewebJSON         mjsonParams
     * @param  BindingResult       result
     * @return                     Object
     * @throws TelewebApiException
     */
    @ApiOperation(value = "게시물 첨부파일 업로드",
                  notes = "게시물 첨부파일을 업로드한다.")
    @PostMapping("/api/board/common/ntt-atchmnfl/upload")
    public Object processRtnAttachFiles(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        //DB Access 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String strTransFlag = mjsonParams.getString("DATA_FLAG");       //화면에서 전송된 플래그 설정

        //데이터 체크가 통과할 경우만 처리
        if(validationProcess(mjsonParams, objRetParams)) {
            //신규/수정 상태에 따라 insert, update 함수 호출
            if(strTransFlag.equals(TwbCmmnConst.TRANS_INS)) {
                //Validation 체크 
                boardCmmnValidator.validate(mjsonParams, result);
                if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

                objRetParams = insertRtn(mjsonParams);
            }
            else if(strTransFlag.equals(TwbCmmnConst.TRANS_UPD)) {
                //Validation 체크 
                boardCmmnValidator.validate(mjsonParams, result);
                if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

                objRetParams = updateRtn(mjsonParams);
            }
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 데이터 처리시 데이터체크 템플릿
     * 
     * @param  jsonParams          전송된 파라메터 데이터
     * @param  objRetParams        반환파라메터
     * @return                     true:처리가능상태, false:처리불가능상태
     * @throws TelewebApiException
     */
    private boolean validationProcess(TelewebJSON jsonParams, TelewebJSON objRetParams) throws TelewebApiException
    {
        return true;
    }


    /**
     * 신규 데이터 처리 템플릿
     * 
     * @param  jsonParams          신규등록할 정보
     * @param  objDAO              DAO 객체
     * @return                     처리건수
     * @throws TelewebApiException
     */
    private TelewebJSON insertRtn(TelewebJSON objJsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //CUSTCO_ID/USER_ID는 서버에서 로딩
        //final String custcoId = ((PaletteUserDetailsVO) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getCustcoId();
        //final String userId = ((PaletteUserDetailsVO) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUserId();
        final String custcoId = objJsonParams.getString("ASP_NEWCUST_KEY");
        final String userId = objJsonParams.getString("USER_ID");

        objJsonParams.setString("CUSTCO_ID", custcoId);

        //신규 처리 시 필요한 추가 로직 처리
        if(objJsonParams.getString("REPLE_YN").equals("1")) {
            objJsonParams.setString("LVL_NO", Integer.toString(objJsonParams.getInt("LVL_NO") + 1));
        }
        else {
            objJsonParams.setString("LVL_NO", "0");
        }
        objJsonParams.setString("FST_USER_ID", userId);
        objJsonParams.setString("LAST_USER_ID", userId);
        objJsonParams.setString("ORD_SEQ", "0");

//        objJsonParams.setString("BRD_PATH", "/" + objJsonParams.getString("BRD_ID") + "_" + objJsonParams.getString("BRD_NO"));
//
//        //첨부파일이 존재할 경우 공통을 이용하여 첨부된 파일을 저장한다.
//        if(objJsonParams.getDataObject(TwbCmmnConst.G_DATA) != null && objJsonParams.getDataObject(TwbCmmnConst.G_DATA).size() > 0) {
//            if(objJsonParams.containsKey("FILENAME") && !"".equals(objJsonParams.getString("FILENAME"))) {
//                // [파일] twbComFileBiz.saveFile: 게시판-게시물등록
//                twbComFileBiz.saveFile(objJsonParams, objJsonParams.getString("USER_ID"), objJsonParams.getString("WRTR_DRPT_CD"));
//            }
//        }

        //데이터 신규등록
        objRetParams = boardService.insertRtnBrd(objJsonParams);

        return objRetParams;
    }


    /**
     * 수정 데이터 처리 템플릿
     * 
     * @param  jsonParams          수정할 정보
     * @param  objDAO              DAO 객체
     * @return                     처리건수
     * @throws TelewebApiException
     */
    private TelewebJSON updateRtn(TelewebJSON jsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        //CUSTCO_ID/USER_ID는 서버에서 로딩
        //final String custcoId = ((PaletteUserDetailsVO) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getCustcoId();
        //final String userId = ((PaletteUserDetailsVO) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUserId();
        final String custcoId = jsonParams.getString("ASP_NEWCUST_KEY");
        final String userId = jsonParams.getString("USER_ID");

        //수정 처리 시 필요한 추가 로직 처리
        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setString("LAST_USER_ID", userId);

//        jsonParams.setString("FILE_GROUP_KEY", jsonParams.getString("OLD_FILE_GROUP_KEY")); //이전 첨부파일에 대한 정보를 위한 초기 그룹키 셋팅
//        jsonParams.setString("BRD_PATH", "/" + jsonParams.getString("BRD_ID") + "_" + jsonParams.getString("BRD_NO"));
//
//        //첨부파일이 존재할 경우 공통을 이용하여 첨부된 파일을 저장한다.
//        if(jsonParams.getDataObject(TwbCmmnConst.G_DATA) != null && jsonParams.getDataObject(TwbCmmnConst.G_DATA).size() > 0) {
//            if(jsonParams.containsKey("FILENAME") && !"".equals(jsonParams.getString("FILENAME"))) {
//                // [파일] twbComFileBiz.saveFile: 게시판-게시물수정
//                twbComFileBiz.saveFile(jsonParams, jsonParams.getString("USER_ID"), jsonParams.getString("WRTR_DRPT_CD"));
//            }
//        }
//
//        //이전 첨부파일이 존재할경우 이전파일에 대한 파일그룹키 수정처리
//        if(!"".equals(jsonParams.getString("OLD_FILE_GROUP_KEY"))) {
//            objRetParams = boardService.updateBfAttachDataUpDate(jsonParams);
//        }

        //데이터 수정처리
        objRetParams = boardService.updateRtnBrd(jsonParams);

        return objRetParams;
    }


    /**
     * 첨부파일 테이블에 인서트한다.
     * 
     * @param  jsonParams          저장할 파일정보
     * @return                     저장건수
     * @throws TelewebApiException
     * @author                     MPC R&D Team
     */
    private TelewebJSON insertRtnProcess(TelewebJSON jsonParams) throws TelewebApiException
    {
        return boardService.INSERT_MEW_BRD_FILE(jsonParams);
    }

//    /**
//     * 게시물 첨부파일 삭제
//     * 
//     * @param  TelewebJSON         mjsonParams
//     * @return                     Object
//     * @throws TelewebApiException
//     */
//    @ApiOperation(value = "게시물 첨부파일 삭제",
//                  notes = "게시물 첨부파일을 삭제한다")
//    @PostMapping("/api/board/common/ntt-atchmnfl/delete")
//    public Object deleteRtnAttachFiles(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        //반환 파라메터 생성
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//        //DB Access 파라메터 생성
//        TelewebJSON objParams01 = new TelewebJSON(mjsonParams);
//        TelewebJSON objParams02 = new TelewebJSON(mjsonParams);
//
//        String custcoId = mjsonParams.getString("CUSTCO_ID");
//
//        objRetParams.setHeader("ERROR_FLAG", false);
//        int _idx01 = 0;
//        int _idx02 = 0;
//
//        JSONArray objArray = mjsonParams.getDataObject();
//        JSONObject objJson = new JSONObject();
//        if(!objArray.isEmpty()) {
//            for(int i = 0; i < objArray.size(); i++) {
//                objJson = objArray.getJSONObject(i);
//                if(!objJson.isEmpty() && !objJson.isNullObject()) {
//                    if(objJson.containsKey("FILE_GROUP_KEY") && !"".equals(objJson.getString("FILE_GROUP_KEY")) && objJson.containsKey("FILE_KEY") && !"".equals(objJson.getString("FILE_KEY"))) {
//                        objParams01.setString("FILE_GROUP_KEY", _idx01, objJson.getString("FILE_GROUP_KEY"));
//                        objParams01.setString("FILE_KEY", _idx01, objJson.getString("FILE_KEY"));
//                        objParams01.setString("CUSTCO_ID", _idx01, custcoId);
//                        _idx01++;
//                    }
//                    else if(objJson.containsKey("FILENAME") && !"".equals(objJson.getString("FILENAME"))) {
//                        objParams02.setString("FILENAME", _idx02, objJson.getString("FILENAME"));
//                        objParams02.setString("CUSTCO_ID", _idx02, custcoId);
//                        _idx02++;
//                    }
//                }
//            }
//        }
//        if(_idx01 > 0) {
//            //첨부파일 삭제(키 삭제)
//            // [파일] twbComFileBiz.deleteFile: 게시판-게시물첨부파일삭제
//            twbComFileBiz.deleteFile(objParams01);
//        }
//        if(_idx02 > 0) {
//            //첨부파일 삭제(임시파일 삭제)
//            // [파일] twbComFileBiz.deleteFile: 게시판-게시물임시파일삭제
//            twbComFileBiz.deleteTempFile(objParams02);
//        }
//
//        //게시판 테이블의 그룹파일키 널 업데이터 처리
//        checkAttachFilCnt(mjsonParams);
//
//        //최종결과값 반환
//        return objRetParams;
//    }


    /**
     * 데이터 처리시 데이터체크 템플릿
     * 
     * @param  jsonParams          전송된 파라메터 데이터
     * @param  objRetParams        반환파라메터
     * @return                     true:처리가능상태, false:처리불가능상태
     * @throws TelewebApiException
     */
    private void checkAttachFilCnt(TelewebJSON jsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.common.file.dao.FileCmmnMapper", "SELECT_ATTACH_FILE_CNT", jsonParams);

        //게시판 테이블의 그룹파일키 널 업데이터 처리
        if("0".equals(objRetParams.getString("ATTACH_CNT"))) {
            //게시판ID, 번호가 존재할 경우 처리프로세스
            if(!"".equals(jsonParams.getString("BRD_ID")) && !"".equals(jsonParams.getString("BRD_NO"))) {
                boardService.UPDATE_BRD_FILEGRPCNT(jsonParams);
            }
        }
    }
    
    /**
   	 * 
   	 * 게시판 문의 조회 (테스트)
   	 * @Method Name  	: pstQstnSelect
   	 * @date   			: 2023. 10. 05.
   	 * @author   		: KTJ
   	 * @version     	: 1.0
   	 * ----------------------------------------
   	 * @param  request	게시판 문의 API 연동 (테스트)
   	 * @return 
   	 * @throws Exception
   	 */
    @ApiOperation(value = "게시판 문의 조회 (테스트)",
                  notes = "게시판 문의 조회 (테스트)")
    @PostMapping("/api/pst/qstn/select/test")
    public String pstQstnSelect(HttpServletRequest request) throws Exception
    {
    	TelewebJSON objSetParams = new TelewebJSON();  //반환 파라메터 생성
    	
		String tenantId = request.getParameter("TENANT_ID"); 	//테넌트아이디
        String custcoId = request.getParameter("CUSTCO_ID"); 	//고객사id 
        
        //게시판 검색조건
        String pstTypeCd = request.getParameter("PST_TYPE_CD"); //게시판 코드 
        String pstTtl = request.getParameter("PST_TTL"); 		//게시글 이름 
        String userNm = request.getParameter("USER_NM"); 		//사용자명
        String editorNm = request.getParameter("EDITOR_NM"); 	//에디터 명
        String boardListFrom = request.getParameter("BOARD_LIST_FROM"); //게시판 리스트
        
        //파라미터 체크
		Enumeration<String> paramNames = request.getParameterNames();
		  
		while(paramNames.hasMoreElements()) { 
			String paramName = paramNames.nextElement(); 
			String paramValue = request.getParameter(paramName); 
			log.info("paramName : " + paramName + " paramValue : " + paramValue); 
			
			if(paramName.equals("CUSTCO_ID")) { //
				objSetParams.setString("CUSTCO_ID", paramValue);
				log.info("CUSTCO_ID : " + paramValue);
			} 
		}
		 
		//파라미터 오류
        if(tenantId == null|| tenantId.equals("") || tenantId == "") {
            log.info("TENANT_ID is null.");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "-1");
            jsonObject.put("message", "Check [TENANT_ID]");
            return jsonObject.toString();
        }else if(custcoId == null|| custcoId.equals("") || custcoId == "") {
        	log.info("[CUSTCO_ID] is null.");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "-2");
            jsonObject.put("message", "Check [CUSTCO_ID]");
            return jsonObject.toString();
        }else if(pstTypeCd == null|| pstTypeCd.equals("") || pstTypeCd == "") {
        	log.info("[PST_TYPE_CD] is null.");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "-3");
            jsonObject.put("message", "Check [PST_TYPE_CD]");
            return jsonObject.toString();
        }else if(custcoId == null|| custcoId.equals("") || custcoId == "") {
        	log.info("[BOARD_LIST_FROM] is null.");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "-4");
            jsonObject.put("message", "Check [BOARD_LIST_FROM]");
            return jsonObject.toString();
        }
        
        objSetParams.setString("CUSTCO_ID", custcoId);
        objSetParams.setString("USER_ID", "2");
        objSetParams.setString("PST_TYPE_CD", pstTypeCd);
        objSetParams.setString("PST_TTL", pstTtl);
        objSetParams.setString("USER_NM", userNm);
        objSetParams.setString("EDITOR_NM", editorNm);
        objSetParams.setString("BOARD_LIST_FROM", boardListFrom);
      
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = boardService.pstQstnSelect(objSetParams);
        ;
        
        log.info("정상적으로 조회되었습니다.");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "0"); //{\"result_code\":\"0\",\"result_msg\":\"정상적으로 등록되었습니다.\"}
        jsonObject.put("message", "정상적으로 조회되었습니다.");
        if(objRetParams.getDataObject("DATA").size()>0) {
            jsonObject.put("data", objRetParams.getDataObject("DATA"));
        } else {
            jsonObject.put("data", "data is empty");
        }
        
        return jsonObject.toString();
    }
}
