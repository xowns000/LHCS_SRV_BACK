package kr.co.hkcloud.palette3.chat.setting.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.setting.app.ChatSettingManageService;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingManageValidator;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.router.TeletalkRouterWebListener;
import kr.co.hkcloud.palette3.core.chat.router.app.RoutingToAgentManagerService;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatSettingManageRestController", description = "상담설정관리 REST 컨트롤러")
public class ChatSettingManageRestController {

    private final ChatSettingManageValidator chatSettingManageValidator;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final ChatSettingManageService chatSettingManageService;
    private final RoutingToAgentManagerService routingToAgentManagerService;

    private final TeletalkRouterWebListener teletalkRouterWebListener;


    /**
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 처리 결과 데이터
     *
     */
    @SystemEventLogAspectAnotation(value = "CHT_SETTING_UPDATE", note = "채팅-상담설정관리 갱신")
    @ApiOperation(value = "채팅설정관리-갱신", notes = "상담설정 정보 갱신")
    @PostMapping("/chat-api/setting/manage/modify")
    public Object updateRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.

        //        chatSettingManageValidator.validate(mjsonParams, result); //VALIDATION 체크
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingManageService.updateRtnCnslProp(objRetParams, jsonParams);

        //        JSONArray objArry = jsonParams.getDataObject();
        //        String strEnvCd = "";                               //환경설정코드
        //        String strEnvSetVal = "";                           //환경설정값
        //        String strEnvCdNm = "";                             //환경설정값 설명
        //        int objArrSize = objArry.size();
        //
        //        // 상담가능 허용 일괄 건수 , 여부
        //        String contChatagentYn = null;
        //        String contChatagentCnt = null;
        //
        //        //배열 사이즈만큼 처리
        //        if (!objArry.isEmpty())
        //        {
        //            for (int i = 0; i < objArrSize; i++)
        //            {
        //                //환경설정코드, 환경설정값을 가져온다.
        //                strEnvCd = objArry.getJSONObject(i).getString("STNG_CD");
        //                strEnvSetVal = objArry.getJSONObject(i).getString("STNG_VL");
        //                strEnvCdNm = objArry.getJSONObject(i).getString("STNG_CD_NM");
        //
        //                if (strEnvCd.equals("CONT_CHATAGENT_YN")) contChatagentYn = strEnvSetVal;
        //                if (strEnvCd.equals("CONT_CHATAGENT_CNT")) contChatagentCnt = strEnvSetVal;
        //
        //                //환경설정코드, 환경설정값을 세팅한다.
        //                jsonParams.setString("STNG_CD", strEnvCd);
        //                jsonParams.setString("STNG_VL", strEnvSetVal);
        //                jsonParams.setString("STNG_CD_NM", strEnvCdNm != null ? strEnvCdNm : "");
        //
        //                objRetParams = chatSettingManageService.updateRtnCnslProp(jsonParams);
        //            }
        //        }
        //
        //        // 상담가능허용수 일괄적용
        //        if (contChatagentYn != null && contChatagentYn.equals("Y"))
        //        {
        //            jsonParams.setString("MAX_CHAT_AGENT", contChatagentCnt);
        //            objRetParams = chatSettingManageService.updateAllMaxAgent(jsonParams);
        //        }

        //상담설정 정보 update 후 다시 채우자~ - 20200521 liy
        //텔레톡 상담 설정
        TelewebJSON outJson = routingToAgentManagerService.selectTalkEnv();
        HcTeletalkDbEnvironment.getInstance().setDbEnvironment(outJson);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 처리 결과 데이터
     *
     */
    @ApiOperation(value = "채팅설정관리-안내메시지삭제", notes = "채팅설정관리 안내메시지삭제")
    @PostMapping("/chat-api/setting/manage/guidance-mssage/delete")
    public Object deleteRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //필수객체정의
        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");    //client에서 전송받은 파라메터 정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        JSONArray objArry = jsonParams.getDataObject();
        String objArrMsgId[] = new String[objArry.size()];  //Mybatis에서 foreach문을 쓰기 위한 배열 생성
        int objArrSize = objArry.size();

        //배열 사이즈만큼 처리
        if (!objArry.isEmpty()) {
            for (int i = 0; i < objArrSize; i++) {
                objArrMsgId[i] = objArry.getJSONObject(i).getString("MSG_ID");
            }
        }

        jsonParams.setObject("MSG_ID", 0, objArrMsgId);

        chatSettingManageService.deleteRtnMsg(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return
     *
     */
    @ApiOperation(value = "상담설정정보 리스트 조회", notes = "상담설정정보 리스트 조회")
    @PostMapping("/chat-api/setting/manage/inqire")
    public Object selectRtnCnslProp(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingManageService.selectRtnCnslProp(jsonParams);

        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     *
     */
    @ApiOperation(value = "채팅설정관리-이력관리목록", notes = "상담가능시간 이력관리 리스트 조회")
    @PostMapping("/chat-api/setting/manage/hist-manage/inqire")
    public Object selectRtnHistoryOfWorkTime(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = chatSettingManageService.selectRtnHistoryOfWorkTime(mjsonParams);

        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     *
     */
    @ApiOperation(value = "채팅설정관리-이력관리처리", notes = "상담가능시간 이력관리 리스트 저장")
    @PostMapping("/chat-api/setting/manage/hist-manage/process")
    public Object processRtnHistoryOfWorkTime(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String strDataFlag = mjsonParams.getString("DATA_FLAG");

        TelewebJSON jsonHistoryOfWorkTimeLast = null;
        TelewebJSON jsonHistoryOfWorkTimeBefore = null;

        if (strDataFlag.equals(TwbCmmnConst.TRANS_INS)) {
            jsonHistoryOfWorkTimeLast = chatSettingManageService.selectRtnHistoryOfWorkTimeLast(mjsonParams);
        } else if (strDataFlag.equals(TwbCmmnConst.TRANS_UPD)) {
            jsonHistoryOfWorkTimeBefore = chatSettingManageService.selectRtnHistoryOfWorkTimeBefore(mjsonParams);
        }

        if (checkHistoryOfWorkTime(mjsonParams, objRetParams, jsonHistoryOfWorkTimeLast, jsonHistoryOfWorkTimeBefore)) {
            if (strDataFlag.equals(TwbCmmnConst.TRANS_INS)) {
                objRetParams = insertRtnHistoryOfWorkTime(mjsonParams, jsonHistoryOfWorkTimeLast);
            } else if (strDataFlag.equals(TwbCmmnConst.TRANS_UPD)) {
                objRetParams = updateRtnHistoryOfWorkTime(mjsonParams, jsonHistoryOfWorkTimeBefore);
            }
        }

        return objRetParams;
    }


    private boolean checkHistoryOfWorkTime(TelewebJSON jsonParams, TelewebJSON objRetParams, TelewebJSON jsonHistoryOfWorkTimeLast,
        TelewebJSON jsonHistoryOfWorkTimeBefore) throws TelewebApiException {
        String strDataFlag = jsonParams.getString("DATA_FLAG");

        String MANAGE_DATE_FROM = jsonParams.getString("MANAGE_DATE_FROM");
        String WORK_TIME_FROM = jsonParams.getString("WORK_TIME_FROM");
        String WORK_TIME_TO = jsonParams.getString("WORK_TIME_TO");

        if (StringUtils.equals(WORK_TIME_FROM, WORK_TIME_TO)) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "상담가능시간(FROM)과 상담가능시간(TO)이 같을 수 없습니다.");
            return false;
        }

        if (strDataFlag.equals(TwbCmmnConst.TRANS_INS)) {
            String MANAGE_DATE_FROM_LAST = jsonHistoryOfWorkTimeLast.getString("MANAGE_DATE_FROM");
            String MANAGE_DATE_TO_LAST = jsonHistoryOfWorkTimeLast.getString("MANAGE_DATE_TO");
            String WORK_TIME_FROM_LAST = jsonHistoryOfWorkTimeLast.getString("WORK_TIME_FROM");
            String WORK_TIME_TO_LAST = jsonHistoryOfWorkTimeLast.getString("WORK_TIME_TO");
            //          String ID = jsonHistoryOfWorkTimeLast.getString("ID");

            if (MANAGE_DATE_FROM_LAST.compareTo(MANAGE_DATE_FROM) >= 0) {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "신규 등록일 경우 '이전 관리 시작일'보다 '신규 관리시작일'이 작거나 같을 수 없습니다.");
                return false;
            }

            if (WORK_TIME_FROM_LAST.compareTo(WORK_TIME_TO_LAST) > 0) {
                if (WORK_TIME_TO_LAST.compareTo(WORK_TIME_FROM) >= 0) {
                    objRetParams.setHeader("ERROR_FLAG", true);
                    objRetParams.setHeader("ERROR_MSG", "신규 등록일 경우 '이전 상담가능시간(TO)'이 '신규 상담가능시간(FROM)'보다 크거나 같을 수 없습니다.");
                    return false;
                }
            }
        } else if (strDataFlag.equals(TwbCmmnConst.TRANS_UPD)) {
            if (jsonHistoryOfWorkTimeBefore.getDataObject().isEmpty()) {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "최초 상담가능시간은 수정할 수 없습니다.");
                return false;
            }
            if (StringUtils.isEmpty(jsonParams.getString("ID"))) {
                throw new IllegalArgumentException("ID is necessary");
            }

            String MANAGE_DATE_FROM_BEFORE = jsonHistoryOfWorkTimeBefore.getString("MANAGE_DATE_FROM");
            String MANAGE_DATE_TO_BEFORE = jsonHistoryOfWorkTimeBefore.getString("MANAGE_DATE_TO");
            String WORK_TIME_FROM_BEFORE = jsonHistoryOfWorkTimeBefore.getString("WORK_TIME_FROM");
            String WORK_TIME_TO_BEFORE = jsonHistoryOfWorkTimeBefore.getString("WORK_TIME_TO");
            //          String ID = jsonHistoryOfWorkTimeBefore.getString("ID");

            if (MANAGE_DATE_FROM_BEFORE.compareTo(MANAGE_DATE_FROM) >= 0) {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "수정 등록일 경우 '이전 관리 시작일'보다 '수정 관리시작일'이 작거나 같을 수 없습니다.");
                return false;
            }

            if (WORK_TIME_FROM_BEFORE.compareTo(WORK_TIME_TO_BEFORE) > 0) {
                if (WORK_TIME_TO_BEFORE.compareTo(WORK_TIME_FROM) >= 0) {
                    objRetParams.setHeader("ERROR_FLAG", true);
                    objRetParams.setHeader("ERROR_MSG", "수정 등록일 경우 '이전 상담가능시간(TO)'이 '수정 상담가능시간(FROM)'보다 크거나 같을 수 없습니다.");
                    return false;
                }
            }
        }

        return true;
    }


    private String selectRtnYesterday(TelewebJSON jsonParams, String strToday) throws TelewebApiException {
        if (StringUtils.isEmpty(strToday)) {
            throw new IllegalArgumentException("strToday is necessary");
        }

        TelewebJSON jsonSelect = new TelewebJSON(jsonParams);
        jsonSelect.setString("strToday", strToday);

        return chatSettingManageService.selectRtnYesterday(jsonSelect).getString("YESTERDAY");
    }


    private TelewebJSON insertRtnHistoryOfWorkTime(TelewebJSON jsonParams,
        TelewebJSON jsonHistoryOfWorkTimeLast) throws TelewebApiException {
        //      String MANAGE_DATE_FROM_LAST = jsonHistoryOfWorkTimeLast.getString("MANAGE_DATE_FROM");
        //      String MANAGE_DATE_TO_LAST = jsonHistoryOfWorkTimeLast.getString("MANAGE_DATE_TO");
        //      String WORK_TIME_FROM_LAST = jsonHistoryOfWorkTimeLast.getString("WORK_TIME_FROM");
        //      String WORK_TIME_TO_LAST = jsonHistoryOfWorkTimeLast.getString("WORK_TIME_TO");
        String ID = jsonHistoryOfWorkTimeLast.getString("ID");

        TelewebJSON jsonUpdateLast = new TelewebJSON(jsonParams);
        jsonUpdateLast.setString("ID", ID);
        //      jsonUpdateLast.setString("MANAGE_DATE_FROM", jsonParams.getString(""));
        jsonUpdateLast.setString("MANAGE_DATE_TO", selectRtnYesterday(jsonParams, jsonParams.getString("MANAGE_DATE_FROM")));
        //      jsonUpdateLast.setString("WORK_TIME_FROM", jsonParams.getString(""));
        //      jsonUpdateLast.setString("WORK_TIME_TO", jsonParams.getString(""));
        jsonUpdateLast.setString("USE_YN", "Y");
        //      jsonUpdateLast.setString("REG_ID", "");
        //      jsonUpdateLast.setString("REG_DTTM", "");
        jsonUpdateLast.setString("UPD_ID", jsonParams.getString("SESSION_ID"));
        //      jsonUpdateLast.setString("UPD_DTTM", "");
        jsonUpdateLast.setString("PROC_ID", jsonParams.getString("SESSION_ID"));
        //      jsonUpdateLast.setString("IT_PROCESSING", "");

        chatSettingManageService.updateRtnHistoryOfWorkTime(jsonUpdateLast);

        TelewebJSON jsonInsert = new TelewebJSON(jsonParams);
        jsonInsert.setString("ID", innbCreatCmmnService.getSeqNo(jsonParams, "TWB"));
        jsonInsert.setString("MANAGE_DATE_FROM", jsonParams.getString("MANAGE_DATE_FROM"));
        jsonInsert.setString("MANAGE_DATE_TO", "21001231");
        jsonInsert.setString("WORK_TIME_FROM", jsonParams.getString("WORK_TIME_FROM"));
        jsonInsert.setString("WORK_TIME_TO", jsonParams.getString("WORK_TIME_TO"));
        jsonInsert.setString("USE_YN", "Y");
        jsonInsert.setString("REG_ID", jsonParams.getString("SESSION_ID"));
        //      jsonProcess.setString("REG_DTTM", "");
        //      jsonProcess.setString("UPD_ID", "");
        //      jsonProcess.setString("UPD_DTTM", "");
        jsonInsert.setString("PROC_ID", jsonParams.getString("SESSION_ID"));
        //      jsonProcess.setString("IT_PROCESSING", "");

        return chatSettingManageService.insertRtnHistoryOfWorkTime(jsonInsert);
    }


    private TelewebJSON updateRtnHistoryOfWorkTime(TelewebJSON jsonParams,
        TelewebJSON jsonHistoryOfWorkTimeBefore) throws TelewebApiException {
        //      String MANAGE_DATE_FROM_LAST = jsonHistoryOfWorkTimeBefore.getString("MANAGE_DATE_FROM");
        //      String MANAGE_DATE_TO_LAST = jsonHistoryOfWorkTimeBefore.getString("MANAGE_DATE_TO");
        //      String WORK_TIME_FROM_LAST = jsonHistoryOfWorkTimeBefore.getString("WORK_TIME_FROM");
        //      String WORK_TIME_TO_LAST = jsonHistoryOfWorkTimeBefore.getString("WORK_TIME_TO");
        String ID = jsonHistoryOfWorkTimeBefore.getString("ID");

        TelewebJSON jsonUpdateBefore = new TelewebJSON(jsonParams);
        jsonUpdateBefore.setString("ID", ID);
        //      jsonUpdateBefore.setString("MANAGE_DATE_FROM", jsonParams.getString(""));
        jsonUpdateBefore.setString("MANAGE_DATE_TO", selectRtnYesterday(jsonParams, jsonParams.getString("MANAGE_DATE_FROM")));
        //      jsonUpdateBefore.setString("WORK_TIME_FROM", jsonParams.getString(""));
        //      jsonUpdateBefore.setString("WORK_TIME_TO", jsonParams.getString(""));
        jsonUpdateBefore.setString("USE_YN", "Y");
        //      jsonUpdateBefore.setString("REG_ID", "");
        //      jsonUpdateBefore.setString("REG_DTTM", "");
        jsonUpdateBefore.setString("UPD_ID", jsonParams.getString("SESSION_ID"));
        //      jsonUpdateBefore.setString("UPD_DTTM", "");
        jsonUpdateBefore.setString("PROC_ID", jsonParams.getString("SESSION_ID"));
        //      jsonUpdateBefore.setString("IT_PROCESSING", "");

        chatSettingManageService.updateRtnHistoryOfWorkTime(jsonUpdateBefore);

        TelewebJSON jsonUpdate = new TelewebJSON(jsonParams);
        jsonUpdate.setString("ID", jsonParams.getString("ID"));
        jsonUpdate.setString("MANAGE_DATE_FROM", jsonParams.getString("MANAGE_DATE_FROM"));
        //      jsonUpdate.setString("MANAGE_DATE_TO", "21001231");
        jsonUpdate.setString("WORK_TIME_FROM", jsonParams.getString("WORK_TIME_FROM"));
        jsonUpdate.setString("WORK_TIME_TO", jsonParams.getString("WORK_TIME_TO"));
        jsonUpdate.setString("USE_YN", "Y");
        //      jsonUpdate.setString("REG_ID", "");
        //      jsonUpdate.setString("REG_DTTM", "");
        jsonUpdate.setString("UPD_ID", jsonParams.getString("SESSION_ID"));
        //      jsonUpdate.setString("UPD_DTTM", "");
        jsonUpdate.setString("PROC_ID", jsonParams.getString("SESSION_ID"));
        //      jsonUpdate.setString("IT_PROCESSING", "");

        return chatSettingManageService.updateRtnHistoryOfWorkTime(jsonUpdate);
    }


    /**
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 처리 결과 데이터
     *
     */
    @ApiOperation(value = "채팅설정관리-상담허용수-조회", notes = "(우측상단)메뉴 상담설정팝업에서 상담허용수를 조회한다.")
    @PostMapping("/chat-api/setting/manage/cnslt-perm-cnt/inqire")
    public Object selectRtnCnsltPermCnt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return chatSettingManageService.selectRtnCnsltPermCnt(mjsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "팔레트메인-상담설정-상담허용수설정", notes = "팔레트메인 상담설정팝업에서 상담허용수를 설정한다")
    @PostMapping("/chat-api/setting/manage/cnslt-perm-cnt/modify")
    public Object updateMaxAgentAndViewBaseScriptYnByUserId(@TelewebJsonParam TelewebJSON mjsonParams,
        BindingResult result) throws TelewebApiException {
        log.debug("*************************** mjsonParams, result ***********************************" + mjsonParams + result);
        //chatSettingManageValidator.validate(mjsonParams, result); //VALIDATION 체크
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }

        return chatSettingManageService.updateMaxAgentAndViewBaseScriptYnByUserId(mjsonParams);
    }


    /**
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 처리 결과 데이터
     *
     */
    @ApiOperation(value = "채팅 상담 허용수 개별 설정", notes = "채팅 상담 허용수 개별 설정")
    @PostMapping("/chat-api/setting/manage/strgChtLmtCnt")
    public Object updateChtLmtCnt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = chatSettingManageService.updateChtLmtCnt(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 처리 결과 데이터
     *
     */
    @ApiOperation(value = "상담사 채팅 상태 변경", notes = "상담사 채팅 상태 변경")
    @PostMapping("/chat-api/setting/manage/strgUserStat")
    public Object updateUserStat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = chatSettingManageService.updateUserStat(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 처리 결과 데이터
     *
     */
    @ApiOperation(value = "관리자 채팅 배치 관리", notes = "관리자 채팅 배치 관리")
    @PostMapping("/chat-api/setting/manage/adminBetch")
    public Object adminBetch(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        String betchYN = mjsonParams.getString("BETCH");
        
        if(betchYN.equals("RESTART")) {
        	teletalkRouterWebListener.contextInitialized(null);
        } else if(betchYN.equals("STOP")) {
        	teletalkRouterWebListener.contextDestroyed(null);
        } else if(betchYN.equals("START")) {
        	teletalkRouterWebListener.startBetch();
        }
        objRetParams.setString("betchStatus",teletalkRouterWebListener.betchStatus());

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 처리 결과 데이터
     *
     */
    @ApiOperation(value = "관리자 채팅 배치 관리", notes = "관리자 채팅 배치 관리")
    @PostMapping("/chat-api/setting/manage/betchStatus")
    public Object betchStatus(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams.setString("betchStatus",teletalkRouterWebListener.betchStatus());
        return objRetParams;
    }
}
