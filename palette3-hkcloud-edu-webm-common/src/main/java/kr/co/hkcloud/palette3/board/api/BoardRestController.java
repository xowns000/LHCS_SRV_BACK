package kr.co.hkcloud.palette3.board.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.board.app.BoardService;
import kr.co.hkcloud.palette3.board.util.BoardValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "BoardRestController", description = "실시간공지사항 REST 컨트")
public class BoardRestController {

    private final BoardService boardService;
    private final BoardValidator boardValidator;


    /**
     *
     * @param  inHashMap
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "게시판-목록-조회", notes = "게시판 목록을 조회한다")
    @PostMapping("/api/board/list")
    public Object selectRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");        //client에서 전송받은 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성

        //필수객체정의
        TelewebJSON objRetData1 = new TelewebJSON(mjsonParams);     //부분반환파라미터생성

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);
        log.info("LOG확인 >>>>>>>>>>>>>>>>>");
        log.info("<<<<<<<<<<<<<<<" + mjsonParams);
        log.trace("LOG확인 >>>>>>>>>>>>>>>>>");
        log.trace("<<<<<<<<<<<<<<<" + mjsonParams);
        //DAO검색 메서드 호출
        String brdId = mjsonParams.getString("BRD_ID");

        if ("1".equals(brdId) || "7".equals(brdId)) { // 시스템 공지 사항 또는 챗봇 공지사항

            //Validation 체크 
            boardValidator.validate(mjsonParams, result);
            if (result.hasErrors()) {
                throw new TelewebValidationException(result.getAllErrors());
            }

            objRetParams = boardService.selectRtnBrdSystem(mjsonParams);
        } else {
            objRetParams = boardService.selectRtnBrdList(mjsonParams);
        }
        objRetData1 = boardService.selectRtnBrdCheck(mjsonParams);

        objRetParams.setHeader("MASTER_CNT", objRetData1.getHeaderInt("COUNT"));
        objRetParams.setDataObject("DATA_MASTER", objRetData1.getDataObject(TwbCmmnConst.G_DATA));

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @author lyj
     * @since 2019.12.08
     */
    @ApiOperation(value = "게시판-공지여부조회", notes = "게시판 공지여부를 조회한다")
    @PostMapping("/api/board/notice-at/inqire")
    public Object selectIsRtNoticeBoad(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성

        //필수객체정의
        TelewebJSON objRetData1 = new TelewebJSON(mjsonParams);     //부분반환파라미터생성

        //DAO검색 메서드 호출
        objRetParams = boardService.selectIsRtNoticeBoad(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @author lyj
     * @since 2019.12.08
     */
    @ApiOperation(value = "게시판-공지게시설정-처리", notes = "게시판 공지게시 설정을 처리한다")
    @PostMapping("/api/board/notice-ntce-estbs/process")
    public Object updateRtnCancelNoice(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성

        String NOTICE_STATE = mjsonParams.getString("NOTICE_STATE");
        String BRD_NO = mjsonParams.getString("BRD_NO");

        //중지 키값 체크
        checkRtnCancelKey(NOTICE_STATE, BRD_NO);
        //중지 & 중지해제된 건이 존재하는지 확인한다.
        if (haveCanceled(mjsonParams, NOTICE_STATE, BRD_NO)) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 처리된 건이 존재합니다.");
            objRetParams.setHeader("ERROR_TYPE", "W");
        } else {
            JSONArray arrExtractUpdate = mjsonParams.getDataObject("DATA");

            objRetParams = updateRtnExtractUpdate(mjsonParams, arrExtractUpdate);
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 중지&중지해제된 건이 존재하는지 확인한다.
     *
     * @param  jsonParams
     * @param  NOTICE_STATE
     * @param  BRD_NO
     * @return true/false(true: 중지&중지해제된 건이 존재함)
     */
    private boolean haveCanceled(TelewebJSON jsonParams, String NOTICE_STATE, String BRD_NO) throws TelewebApiException {
        TelewebJSON jsonSelect = new TelewebJSON(jsonParams);
        TelewebJSON josnRetern = new TelewebJSON(jsonParams);
        jsonSelect.setDataObject(jsonParams.getDataObject(TwbCmmnConst.G_DATA));
        jsonSelect.setString("NOTICE_STATE", NOTICE_STATE);
        jsonSelect.setString("BRD_NO", BRD_NO);

        josnRetern = boardService.selectHaveCanceledYN(jsonSelect);
        int returnCnt = josnRetern.getHeaderInt("TOT_COUNT");

        if (returnCnt > 0) {
            String strHaveCanceledYN = josnRetern.getString("HAVE_CANCELED_YN");
            if (StringUtils.equals(strHaveCanceledYN, "Y")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    // 중지하기위한 키값 체크
    private void checkRtnCancelKey(String NOTICE_STATE, String BRD_NO) throws TelewebApiException {
        if (StringUtils.isEmpty(NOTICE_STATE)) {
            throw new IllegalArgumentException("NOTICE_STATE is necessary");
        }
        if (StringUtils.isEmpty(BRD_NO)) {
            throw new IllegalArgumentException("BRD_NO is necessary");
        }
    }


    // 중지&중지해제 처리
    private TelewebJSON updateRtnExtractUpdate(TelewebJSON jsonParams, JSONArray arrExtractUpdate) throws TelewebApiException {
        TelewebJSON objRetJson = new TelewebJSON(jsonParams);

        int index = 0;

        for (Object objTemp : arrExtractUpdate) {
            JSONObject objExtractUpdate = (JSONObject) objTemp;

            TelewebJSON jsonUpdate = new TelewebJSON(jsonParams);
            jsonUpdate.setDataObject(jsonParams.getDataObject(TwbCmmnConst.G_DATA));
            jsonUpdate.setString("NOTICE_STATE", objExtractUpdate.getString("NOTICE_STATE"));
            jsonUpdate.setString("BRD_NO", objExtractUpdate.getString("BRD_NO"));

            index += boardService.updateRtnCancelNoice(jsonUpdate).getHeaderInt("TOT_COUNT");
        }

        objRetJson.setHeader("TOT_COUNT", index);
        objRetJson.setHeader("COUNT", index);
        objRetJson.setHeader("ERROR_FLAG", false);
        if (index > 0) {
            objRetJson.setHeader("ERROR_MSG", "정상 처리 되었습니다.");
        } else {
            objRetJson.setHeader("ERROR_MSG", "처리 데이터가 존재 하지 않습니다.");
        }

        return objRetJson;
    }


    /**
     * 실시간공지사항 상세팝업 RestController
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "게시판-상세팝업-조회", notes = "게시판 상세팝업 조회한다")
    @PostMapping("/api/board/detail-popup/selectPstDetail")
    public Object selectRtnDetailPopup(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //필수객체정의
        TelewebJSON objRetData1 = new TelewebJSON(mjsonParams);     //부분반환파라미터생성
        //        TelewebJSON objRetData2 = new TelewebJSON(mjsonParams);     //부분반환파라미터생성

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        updateRtnSelectNo(jsonParams);

        //DAO검색 메서드 호출
        objRetParams = boardService.selectRtnBrdDetail(jsonParams);

        String strRmkYN = objRetParams.getString("RMK_YN", 0);
        //        String strFileYN = objRetParams.getString("FILE_YN", 0);

        if (strRmkYN.equals("Y")) {
            objRetData1 = boardService.selectRtnBrdRmk(jsonParams);
        }

        objRetParams.setHeader("RMK_CNT", objRetData1.getHeaderInt("COUNT"));
        objRetParams.setDataObject("DATA_RMK", objRetData1.getDataObject(TwbCmmnConst.G_DATA));

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 실시간공지사항 상세팝업 RestController - 조회수 증가 방지
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "게시판-상세팝업-조회 - 조회수 증가 방지", notes = "게시판 상세팝업 조회한다 - 조회수 증가 방지")
    @PostMapping("/api/board/detail-popup/selectNo-noChange/inqire")
    public Object selectRtnDetailPopupNoSelNo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //필수객체정의
        TelewebJSON objRetData1 = new TelewebJSON(mjsonParams);     //부분반환파라미터생성
        //        TelewebJSON objRetData2 = new TelewebJSON(mjsonParams);     //부분반환파라미터생성

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //DAO검색 메서드 호출
        objRetParams = boardService.selectRtnBrdDetail(jsonParams);

        String strRmkYN = objRetParams.getString("RMK_YN", 0);
        //        String strFileYN = objRetParams.getString("FILE_YN", 0);

        if (strRmkYN.equals("Y")) {
            objRetData1 = boardService.selectRtnBrdRmk(jsonParams);
        }

        objRetParams.setHeader("RMK_CNT", objRetData1.getHeaderInt("COUNT"));
        objRetParams.setDataObject("DATA_RMK", objRetData1.getDataObject(TwbCmmnConst.G_DATA));

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "게시판-상세팝업-처리", notes = "게시판 상세팝업 처리한다")
    @PostMapping("/api/board/detail-popup/process")
    public Object processRtnDetailPopup(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //client에서 전송받은 파라메터 생성
        //        TelewebJSON mjsonParams     = (TelewebJSON)inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        String strTransFlag = jsonParams.getString("DATA_FLAG");        //화면에서 전송된 플래그 설정

        //데이터 체크가 통과할 경우만 처리
        if (validationProcess(jsonParams, objRetParams)) {
            //신규/수정 상태에 따라 insert, update 함수 호출
            if (strTransFlag.equals(TwbCmmnConst.TRANS_INS)) {
                objRetParams = insertRtnDetailPopup(jsonParams);
            }
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "게시판-상세팝업-삭제", notes = "게시판 상세팝업 삭제한다")
    @PostMapping("/api/board/detail-popup/delete")
    public Object deleteRtnDetailPopup(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //client에서 전송받은 파라메터 생성
        //        TelewebJSON mjsonParams     = (TelewebJSON)inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //데이터 체크가 통과할 경우만 처리
        if (validationDeleteDetailPopup(jsonParams, objRetParams)) {
            //Validation 체크 
            boardValidator.validate(mjsonParams, result);
            if (result.hasErrors()) {
                throw new TelewebValidationException(result.getAllErrors());
            }

            //데이터 삭제 처리 - 실제로는 삭제하지 않고 업데이트로 처리(flag) -- 하위정보에 관련하여 사용하지 않음으로 처리
            objRetParams = boardService.deleteRtnBrdAll(jsonParams);

            //파일폴더(첨부파일, 이미지) 삭제 처리
            //deleteFiles(jsonParams);
        }

        //최종결과값 반환
        return objRetParams;
    }

    //    /**
    //     * 파일 삭제
    //     *
    //     * @param path 파일 경로
    //     */
    //    private static boolean deleteDirectory(File path) throws TelewebApiException
    //    {
    //        if(!path.exists()) { return false; }
    //
    //        File[] files = path.listFiles();
    //
    //        for(File file : files) {
    //            if(file.isDirectory()) {
    //                deleteDirectory(file);
    //            }
    //            else {
    //                file.delete();
    //            }
    //        }
    //
    //        return path.delete();
    //    }


    /**
     * 데이터 삭제시 데이터체크 템플릿
     *
     * @param  jsonParams          전송된 파라메터 데이터
     * @param  objRetParams        반환파라메터
     * @return true:처리가능상태, false:처리불가능상태
     * @throws TelewebApiException
     */
    private boolean validationDeleteDetailPopup(TelewebJSON jsonParams, TelewebJSON objRetParams) throws TelewebApiException {
        return true;
    }


    /**
     * 신규 데이터 처리 템플릿
     *
     * @param  jsonParams          신규등록할 정보
     * @param  objDAO              DAO 객체
     * @return 처리건수
     * @throws TelewebApiException
     */
    @Transactional
    private TelewebJSON insertRtnDetailPopup(TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        //신규 처리 시 필요한 추가 로직 처리
        jsonParams.setString("REG_USER_ID", jsonParams.getString("PROC_ID"));

        //데이터 신규등록
        objRetParams = boardService.insertRtnBrdRmk(jsonParams);

        return objRetParams;
    }


    /**
     * 수정 조회 수 수정 데이터 처리
     *
     * @param  jsonParams          수정할 정보
     * @param  objDAO              DAO 객체
     * @return 처리건수
     * @throws TelewebApiException
     */
    @Transactional
    private TelewebJSON updateRtnSelectNo(TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        //수정 처리 시 필요한 추가 로직 처리

        //데이터 수정처리
        objRetParams = boardService.updateRtnSelectNo(jsonParams);

        return objRetParams;
    }


    /**
     *
     * @param  jsonParams          수정할 정보
     * @param  objDAO              DAO 객체
     * @return 처리건수
     * @throws TelewebApiException
     */
    @ApiOperation(value = "게시판-상세팝업-삭제댓글-처리", notes = "게시판 상세팝업 삭제댓글을 처리한다")
    @PostMapping("/api/board/detail-popup/delete-answer/process")
    public Object deleteRtnRow(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //client에서 전송받은 파라메터 생성
        //        TelewebJSON mjsonParams     = (TelewebJSON)inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        int intRetValue = 0;                                            //처리결과건수

        //데이터 체크가 통과할 경우만 처리
        if (validationDeleteRow(jsonParams, objRetParams)) {
            //데이터 삭제 처리
            objRetParams = boardService.DELETE_MEW_BRD_RMK(mjsonParams);
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 데이터 삭제시 데이터체크 템플릿
     *
     * @param  jsonParams          전송된 파라메터 데이터
     * @param  objRetParams        반환파라메터
     * @return true:처리가능상태, false:처리불가능상태
     * @throws TelewebApiException
     */
    private boolean validationDeleteRow(TelewebJSON jsonParams, TelewebJSON objRetParams) throws TelewebApiException {
        return true;
    }


    /**
     *
     * @author lyj
     * @since 2019.12.08
     */
    @ApiOperation(value = "게시판-상세팝업-버튼권한-조회", notes = "게시판 상세팝업 버튼권한을 조회한다")
    @PostMapping("/api/board/detail-popup/button-author/inqire")
    public Object selectRtBtnAuth(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //client에서 전송받은 파라메터 생성
        //        TelewebJSON mjsonParams     = (TelewebJSON)inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = boardService.selectRtBtnAuthRole(mjsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * BRD_ID에 따라 PLT_MENU 테이블에서 메뉴명(MENU_NM) 조회
     *
     * @author LDU
     * @since 2021.05.31
     */
    @ApiOperation(value = "게시판-상세팝업-버튼권한-조회", notes = "게시판 상세팝업 버튼권한을 조회한다")
    @PostMapping("/api/board/menu-name/inqire")
    public Object selectRtnMenuName(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = boardService.selectRtnMenuName(mjsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 실시간공지사항 처리팝업 RestController 데이터 조회 템플릿
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "게시판-처리팝업-조회", notes = "게시판 처리팝업 조회한다")
    @PostMapping("/api/board/process-popup/inqire")
    public Object selectRtnProcessPopup(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //        TelewebJSON objRetData2 = new TelewebJSON(mjsonParams);

        //DAO검색 메서드 호출
        objRetParams = boardService.selectRtnBrdDetail(mjsonParams);

        //        String strFileYN = objRetParams.getString("FILE_YN", 0);
        //
        //        if(strFileYN.equals("Y")) {
        //            objRetData2 = boardService.selectRtnBrdFile(mjsonParams);
        //        }

        //        objRetParams.setHeader("FILE_CNT", objRetData2.getHeaderInt("COUNT"));

        //그리드의 헤더값을 추가해야 한다.페이징 그리드 일 경우 DATA그룹명_PAGES_CNT 값도 넣어야 한다.
        //        objRetParams.setHeader("DATA_FILE_COUNT", objRetData2.getHeaderInt("COUNT"));
        //        objRetParams.setHeader("DATA_FILE_TOT_COUNT", objRetData2.getHeaderInt("TOT_COUNT"));

        //        objRetParams.setDataObject("DATA_FILE", objRetData2.getDataObject(TwbCmmnConst.G_DATA));

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 실시간공지사항 처리팝업 RestController 데이터 조회 템플릿
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "게시판-처리팝업-조회", notes = "게시판 처리팝업 조회한다")
    @PostMapping("/api/board/fnct/inqire")
    public Object selectRtnBoardFunction(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성

        objRetParams = boardService.selectRtnBoardFunction(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    //    /**
    //     * 매핑된 board-process-popup.js에서 호출하지 않음 데이터 처리 템플릿
    //     *
    //     * @param  없음(DefaultService에  mjsonParams 전역변수에 정의되어 있음)
    //     * @return                     TelewebJSON 형식의 처리 결과 데이터
    //     * @throws TelewebApiException
    //     */
    //    @ApiOperation(value = "데이터 처리 템플릿",
    //                  notes = "데이터 처리 템플릿")
    //    @PostMapping("/api/TwbBbs04/processRtn")
    //    public Object processRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    //    {
    //        //반환 파라메터 생성
    //        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
    //
    //        //전송된 파라메터 반환
    //        TelewebJSON jsonParams = new TelewebJSON();
    //        TelewebJSON jsonFileParams = new TelewebJSON();
    //        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
    //        jsonFileParams.setDataObject(mjsonParams.getDataObject("ATTACH"));
    //        String strTransFlag = jsonParams.getString("DATA_FLAG");        //화면에서 전송된 플래그 설정
    //        String strFileGroupKey = innbCreatCmmnService.getSeqNo(mjsonParams, "TWB");
    //
    //        //화면에서 등록자, 부서코드정보 전달필요
    //        String strRegID = jsonParams.getString("REG_ID");
    //        String strRegDeptCode = jsonParams.getString("REG_DEPT_CODE");
    //
    //        //데이터 체크가 통과할 경우만 처리
    //        if(validationProcess(jsonParams, objRetParams)) {
    //            //첨부파일이 존재할 경우 공통을 이용하여 첨부된 파일을 저장한다.
    //            if(mjsonParams.getDataObject("ATTACH") != null && jsonFileParams.getDataObject("DATA").size() > 0) {
    //
    //                objComFileBiz.saveFile(jsonFileParams, strFileGroupKey, strRegID, strRegDeptCode);
    //
    //                jsonParams.setDataObject("ATTACH", jsonFileParams.getDataObject(TwbCmmnConst.G_DATA));
    //            }
    //
    //            //신규/수정 상태에 따라 insert, update 함수 호출
    //            if(strTransFlag.equals(TwbCmmnConst.TRANS_INS)) {
    //                objRetParams = insertRtn(mjsonParams);
    //            }
    //            else if(strTransFlag.equals(TwbCmmnConst.TRANS_UPD)) {
    //                objRetParams = updateRtn(mjsonParams);
    //            }
    //        }
    //
    //        //최종결과값 반환
    //        return objRetParams;
    //    }
    //
    //
    //    /**
    //     * 매핑된 board-process-popup.js에서 호출하지 않음 데이터 삭제 템플릿
    //     *
    //     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
    //     * @return                    TelewebJSON 형식의 처리 결과 데이터
    //     */
    //    @ApiOperation(value = "데이터 삭제 템플릿",
    //                  notes = "데이터 삭제 템플릿")
    //    @PostMapping("/api/TwbBbs04/deleteRtn")
    //    public Object deleteRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    //    {
    //        //client에서 전송받은 파라메터 생성
    //        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
    //        //반환 파라메터 생성
    //        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
    //
    //        //데이터 체크가 통과할 경우만 처리
    //        if(validationDelete(mjsonParams, objRetParams)) {
    //
    //            // [파일] objComFileBiz.deleteFile: 게시판-실시간공지사항-첨부파일삭제
    //            objComFileBiz.deleteFile(objRetParams);
    //
    //            objRetParams = boardService.DELETE_MEW_BRD_FILE(mjsonParams);
    //        }
    //
    //        //최종결과값 반환
    //        return objRetParams;
    //    }

    //    /**
    //     * 매핑된 board-process-popup.js에서 호출하지 않음 임시서버 데이터 삭제 처리 템플릿
    //     *
    //     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
    //     * @return                    TelewebJSON 형식의 처리 결과 데이터
    //     */
    //    public void deleteRtnTemp(HashMap<String, Object> inHashMap) throws TelewebApiException
    //    {
    //        //client에서 전송받은 파라메터 생성
    //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
    //        //반환 파라메터 생성
    //        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
    //
    //        //필수객체정의
    //        File mstrRepositoryTempPath = bbsProperties.getRepository().getTempDir();     //Repository Temp Path
    //        String strTempMiddlePath = "";  //임시디렉토리에 저장되어 있는 파일의 중간 패스정보
    //        String strTempFileName = "";    //임시디렉토리에 저장되어 있는 파일이름
    //        String strTempFilePath = "";    //임시디렉토리에 있는 파일의 전체경로
    //        try {
    //            //전송된 파라메터 반환
    //            TelewebJSON jsonParams = new TelewebJSON();
    //            jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
    //
    //            strTempMiddlePath = jsonParams.getString("PATH_DIR");
    //            strTempFileName = jsonParams.getString("FILENAME");
    //            strTempFilePath = mstrRepositoryTempPath + strTempMiddlePath + strTempFileName;
    //
    //            //임시디렉토리 파일을 삭제한다.
    //            try {
    //                File objDelFile = new File(strTempFilePath);
    //                objDelFile.delete();
    //
    //            }
    //            catch(Exception e) {}
    //
    //        }
    //        catch(Exception e) {
    //            throw e;
    //        }
    //    }


    /**
     * 데이터 처리시 데이터체크 템플릿
     *
     * @param  jsonParams          전송된 파라메터 데이터
     * @param  objRetParams        반환파라메터
     * @return true:처리가능상태, false:처리불가능상태
     * @throws TelewebApiException
     */
    private boolean validationProcess(TelewebJSON jsonParams, TelewebJSON objRetParams) throws TelewebApiException {

        return true;

    }


    /**
     * 데이터 삭제시 데이터체크 템플릿
     *
     * @param  jsonParams          전송된 파라메터 데이터
     * @param  objRetParams        반환파라메터
     * @return true:처리가능상태, false:처리불가능상태
     * @throws TelewebApiException
     */
    private boolean validationDelete(TelewebJSON jsonParams, TelewebJSON objRetParams) throws TelewebApiException {
        boolean blnRetVal = false;
        //공통 항목에 대한 데이터 체크
        if (jsonParams.getString("FILE_KEY").equals("")) {
            //조건이 맞지 않을 경우 objRetParams 반환파라메터에 상태와 메시지를 설정하여 반환한다.
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "삭제할 첨부파일이 없습니다.");
        }
        return blnRetVal;
    }

    //    /**
    //     * 신규 데이터 처리 템플릿
    //     *
    //     * @param  jsonParams          신규등록할 정보
    //     * @param  objDAO              DAO 객체
    //     * @return                     처리건수
    //     * @throws TelewebApiException
    //     */
    //    private TelewebJSON insertRtn(TelewebJSON jsonParams) throws TelewebApiException
    //    {
    //        TelewebJSON objJsonParams = new TelewebJSON();
    //        TelewebJSON objRetParams = new TelewebJSON();
    //
    //        objJsonParams.setDataObject(jsonParams.getDataObject("DATA"));
    //        //신규 처리 시 필요한 추가 로직 처리
    //        if(objJsonParams.getString("REPLY_YN").equals("1")) {
    //            objJsonParams.setString("LVL_NO", Integer.toString(objJsonParams.getInt("LVL_NO") + 1));
    //            objJsonParams.setString("UPPER_BRD_NO", objJsonParams.getString("BRD_NO"));
    //        }
    //        else {
    //            objJsonParams.setString("LVL_NO", "0");
    //            objJsonParams.setString("UPPER_BRD_NO", "0");
    //        }
    //        objJsonParams.setString("FST_USER_ID", objJsonParams.getString("PROC_ID"));
    //        objJsonParams.setString("LAST_USER_ID", jsonParams.getString("PROC_ID"));
    //        objJsonParams.setString("ORD_SEQ", "0");
    //
    //        TelewebJSON objSeqParams = boardService.selectRtnBrdMaxBrd(jsonParams);
    //        objJsonParams.setString("BRD_NO", objSeqParams.getString("BRD_NO"));
    //
    //        //데이터 신규등록
    //        objRetParams = boardService.insertRtnBrd(objJsonParams);
    //
    //        //첨부파일이 존재할 경우 공통을 이용하여 첨부된 파일을 저장한다.
    //        if(jsonParams.getDataObject("ATTACH") != null) {
    //            for(int i = 0; i < jsonParams.getDataObject("ATTACH").size(); i++) {
    //
    //                TelewebJSON objJsonFileParams = new TelewebJSON();
    //                objJsonFileParams.setString("BRD_ID", objJsonParams.getString("BRD_ID"));
    //                objJsonFileParams.setString("BRD_NO", objJsonParams.getString("BRD_NO"));
    //                objJsonFileParams.setString("FILE_KEY", jsonParams.getString("ATTACH", "FILE_KEY", i));
    //                objJsonFileParams.setString("PROC_ID", objJsonParams.getString("PROC_ID"));
    //
    //                insertRtnProcess(objJsonFileParams);
    //            }
    //        }
    //        return objRetParams;
    //    }


    /**
     * 첨부파일 테이블에 인서트한다.
     *
     * @param  jsonParams          저장할 파일정보
     * @return 저장건수
     * @throws TelewebApiException
     * @author MPC R&D Team
     */
    public Object insertRtnProcess(TelewebJSON jsonParams) throws TelewebApiException {

        return boardService.INSERT_MEW_BRD_FILE(jsonParams);
    }

    //    /**
    //     * 수정 데이터 처리 템플릿
    //     *
    //     * @param  jsonParams          수정할 정보
    //     * @param  objDAO              DAO 객체
    //     * @return                     처리건수
    //     * @throws TelewebApiException
    //     */
    //    private TelewebJSON updateRtn(TelewebJSON jsonParams) throws TelewebApiException
    //    {
    //        TelewebJSON objRetParams = new TelewebJSON();
    //
    //        //수정 처리 시 필요한 추가 로직 처리
    //        jsonParams.setString("LAST_USER_ID", jsonParams.getString("PROC_ID"));
    //        //데이터 수정처리
    //        objRetParams = boardService.updateRtnBrd(jsonParams);
    //
    //        //첨부파일이 존재할 경우 공통을 이용하여 첨부된 파일을 저장한다.
    //        if(jsonParams.getDataObject("ATTACH") != null) {
    //            for(int i = 0; i < jsonParams.getDataObject("ATTACH").size(); i++) {
    //
    //                TelewebJSON objJsonFileParams = new TelewebJSON();
    //                objJsonFileParams.setString("BRD_ID", jsonParams.getString("BRD_ID"));
    //                objJsonFileParams.setString("BRD_NO", jsonParams.getString("BRD_NO"));
    //                objJsonFileParams.setString("FILE_KEY", jsonParams.getString("ATTACH", "FILE_KEY", i));
    //                objJsonFileParams.setString("PROC_ID", jsonParams.getString("PROC_ID"));
    //
    //                insertRtn(objJsonFileParams);
    //            }
    //        }
    //
    //        return objRetParams;
    //    }


    /**
     * 응원메시지 수정
     *
     * @author LDU
     * @since 2021.05.31
     */
    @ApiOperation(value = "응원메시지-수정", notes = "응원메시지를 수정한다")
    @PostMapping("/api/topmsg/update")
    public Object updateTopmsgRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = boardService.updateTopmsgRtn(mjsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 프로필아이콘 수정
     *
     * @author LDU
     * @since 2021.05.31
     */
    @ApiOperation(value = "프로필아이콘-수정", notes = "프로필아이콘을 수정한다")
    @PostMapping("/api/topicon/update")
    public Object updateTopiconRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = boardService.updateTopiconRtn(mjsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        //최종결과값 반환
        return objRetParams;
    }

    // 여기부터


    /**
     *
     * 메서드 설명		: 게시글 조회
     * @Method Name    : selectBoardList
     * @date            : 2023. 5. 23
     * @author        : 김성태
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
    @ApiOperation(value = "게시글 조회", notes = "게시글을 조회한다")
    @PostMapping("/api/board/selectBoardList")
    public Object selectBoardList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = boardService.selectBoardList(mjsonParams);

        return objRetParams;
    }

    /**
     *
     * 메서드 설명		: 게시글 상세 조회
     * @Method Name    : selectPstDetail
     * @date            : 2023. 12. 08
     * @author        : 나준영
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
    @ApiOperation(value = "게시글 상세 조회", notes = "게시글을 상세 조회한다")
    @PostMapping("/api/board/selectPstDetail")
    public Object selectPstDetail(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = boardService.selectBoardList(mjsonParams);

        return objRetParams;
    }

    /**
     *
     * 메서드 설명		: 게시글 유형별 개수 조회
     * @Method Name    : selectBrdTpCnt
     * @date            : 2023. 7. 18
     * @author        : 김성태
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
    @ApiOperation(value = "게시글 유형별 개수 조회", notes = "게시글 유형별 개수를 조회한다")
    @PostMapping("/api/board/selectBoardTpCnt")
    public Object selectBoardTpCnt(@TelewebJsonParam TelewebJSON jsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = boardService.selectBoardTpCnt(jsonParams);

        return objRetParams;
    }


    /**
     *
     * 메서드 설명		: 게시글 신규 등록 및 수정
     * @Method Name    : upsertBoard
     * @date            : 2023. 5. 18
     * @author        : 김성태
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "BOARD_PROC", note = "게시물 변경(등록,수정)")
    @Transactional
    @ApiOperation(value = "게시물 신규 등록 및 수정", notes = "게시물을 신규 등록 및 수정한다")
    @PostMapping("/api/board/boardUpsert")
    public Object upsertBoard(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = boardService.upsertBoard(mjsonParams);

        return objRetParams;
    }


    /**
     *
     * 메서드 설명		: 게시글 삭제
     * @Method Name    : deleteBoard
     * @date            : 2023. 5. 23
     * @author        : 김성태
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "BOARD_DEL", note = "게시글 삭제")
    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제한다")
    @PostMapping("/api/board/boardDelete")
    public Object deleteBoard(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = boardService.deleteBoard(mjsonParams);

        return objRetParams;
    }


    /**
     *
     * 메서드 설명		: 조회수 증가
     * @Method Name    : increaseCnt
     * @date            : 2023. 5. 24
     * @author        : 김성태
     * @version        : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "게시글 조회수 증가", notes = "게시글 조회수를 증가시킨다")
    @PostMapping("/api/board/increaseCnt")
    public Object increaseCnt(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = boardService.increaseCnt(jsonParams);

        return objRetParams;
    }

    /**
     *
     * 메서드 설명		: 조회수 조회
     * @Method Name    : selectBoardList
     * @date            : 2023. 7. 07
     * @author        : 김성태
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
    @ApiOperation(value = "조회수 조회", notes = "조회수를 조회한다")
    @PostMapping("/api/board/selectCnt")
    public Object selectCnt(@TelewebJsonParam TelewebJSON jsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = boardService.selectCnt(jsonParams);

        return objRetParams;
    }

    /**
     *
     * 메서드 설명		: 파일 리스트 조회
     * @Method Name    : boardFileList
     * @date            : 2023. 6. 20
     * @author        : 김성태
     * @version        : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "파일 리스트 조회", notes = "파일 리스트 조회")
    @PostMapping("/api/board/file-list")
    public Object boardFileList(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = boardService.boardFileList(jsonParams);

        return objRetParams;
    }

    /**
     *
     * 메서드 설명		: 게시글파일 전체 삭제
     * @Method Name    : deleteBoardFile
     * @date            : 2023. 6. 16
     * @author        : 김태준
     * @version        : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "게시글파일 전체 삭제", notes = "게시글파일 전체 삭제")
    @PostMapping("/api/board/file-delete")
    public Object deleteBoardFile(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = boardService.deleteBoardFile(jsonParams);

        return objRetParams;
    }


    /**
     *
     * 메서드 설명		: 이미지파일등록 그룹키 통일
     * @Method Name    : increaseCnt
     * @date            : 2023. 6. 16
     * @author        : 김태준
     * @version        : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "이미지파일등록 그룹키 통일", notes = "이미지파일등록 그룹키 통일")
    @PostMapping("/api/board/fileKey-unity/process")
    public Object fileKeyUnity(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = boardService.fileKeyUnity(jsonParams);

        return objRetParams;
    }


    /**
     *
     * 메서드 설명		: 이미지파일 리스트 조회
     * @Method Name    : increaseCnt
     * @date            : 2023. 6. 16
     * @author        : 김태준
     * @version        : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "이미지파일 리스트 조회", notes = "이미지파일 리스트 조회")
    @PostMapping("/api/board/chat/image-list")
    public Object chatImageList(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = boardService.chatImageList(jsonParams);

        return objRetParams;
    }


    /**
     *
     * 메서드 설명		: 이미지파일 단일 삭제
     * @Method Name    : increaseCnt
     * @date            : 2023. 6. 16
     * @author        : 김태준
     * @version        : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "이미지파일 단일 삭제", notes = "이미지파일 단일 삭제")
    @PostMapping("/api/board/chat/image-delete")
    public Object deleteChatImage(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = boardService.deleteChatImage(jsonParams);

        return objRetParams;
    }


}