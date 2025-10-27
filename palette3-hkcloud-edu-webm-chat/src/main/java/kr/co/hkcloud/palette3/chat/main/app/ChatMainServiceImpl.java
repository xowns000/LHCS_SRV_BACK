package kr.co.hkcloud.palette3.chat.main.app;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import kr.co.hkcloud.palette3.chat.dashboard.app.ChatDashboardCounselService;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingBannedWordUtils;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatEscalatingRepository;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatStompVO;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.util.PaletteDataFormatUtils;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.app.FileDbMngService;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCustomerInformationListService;
import kr.co.hkcloud.palette3.setting.customer.dto.CustomerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import org.json.simple.parser.ParseException;


@Slf4j
@RequiredArgsConstructor
@Service("chatMainService")
public class ChatMainServiceImpl implements ChatMainService
{
    private final InnbCreatCmmnService              innbCreatCmmnService;
    private final ChatSettingBannedWordUtils        chatSettingBannedWordUtils;
    private final ChatDashboardCounselService       chatDashboardCounselService;
    private final TwbComDAO                         mobjDao;
    private final TalkRedisChatEscalatingRepository redisChatEscalatingRepository;
    private final PaletteFilterUtils                paletteFilterUtils;
    private final TransToKakaoService               transToKakaoService;
    private final PaletteDataFormatUtils            paletteDataFormatUtils;
    private final PaletteCmmnService                paletteCmmnService;
    private final SettingCustomerInformationListService settingCustomerInformationListService;
    private final FileDbMngService fileDbMngService;

    @Value("${palette.security.private.alg}")
    private String P_ALG;

    @Value("${palette.security.private.key}")
    private String P_KEY;
        /**
         * 상담 대기에서 상담 이력 테이블로 이관
         */
        @Transactional(propagation = Propagation.REQUIRED,
                       rollbackFor = {Exception.class, SQLException.class},
                       readOnly = false)
        public TelewebJSON processRtnTalkHistInfo(TelewebJSON jsonParams) throws TelewebAppException
        {
            log.trace("processRtnTalkHistInfo :::");
    
            //ESCALATING:SET:REDIS - 대기->상담 이관중
            {
                redisChatEscalatingRepository.setStompVO(ChatStompVO.builder().userKey(jsonParams.getString("CUST_ID")).agentId(jsonParams.getString("USER_ID")).build());
            }
    
            TelewebJSON objRetParams = new TelewebJSON();
            TelewebJSON objTempParams = new TelewebJSON();
            TelewebJSON rdyDelParams = new TelewebJSON();
            jsonParams.setString("CHT_CUTT_ID", jsonParams.getString("CHT_CUTT_ID"));
            
            if(!StringUtils.isEmpty(jsonParams.getString("CHT_RDY_ID"))) {
                jsonParams.setString("CHT_RDY_ID", jsonParams.getString("CHT_RDY_ID")); //상담 대기중인 CHT_CUTT_ID는 CHT_RDY_ID임
    
                //해당 건이 전달 건인지 확인 하기 위해 데이터를 가져 온다.
                objTempParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnTalkTransChk", jsonParams);
    
                //상담대기(PLT_CHT_RDY) -> 상담대기이력(PLT_CHT_USER_RDY_HSTRY)
                jsonParams.setString("CHT_USER_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_USER_HSTRY_ID")));
                mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkHistDetailInfo", jsonParams);
            }
            
            log.info("################" + jsonParams);
            
            //상담이력(PLT_CHT_CUTT) 상태 상담 진행중으로 변경
            mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRtnTalkHistInfo", jsonParams);
    
            //상담이력(PLT_CHT_CUTT) -> 상담이력 히스토리(PLT_CHT_CUTT_HSTRY)
            //상담대기->상담이력 갈 필요 없음
            //jsonParams.setString("CHT_CUTT_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_HSTRY_ID")));
            //mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertTalkContactHist", jsonParams);
            
            //상담 대기 테이블에서 해당 데이터 삭제
            //삭제전 대기테이블에 해당 대기이력이 있는지 확인
            if(!StringUtils.isEmpty(jsonParams.getString("CHT_RDY_ID"))) {
                rdyDelParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "rdyDelCnt", jsonParams);
                if(!rdyDelParams.getString("CNT").equals("0")) {
                    mobjDao.delete("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "deleteRtnTalkReadyInfo", jsonParams);
                }
            }
    
            //해당 건이 전달 건인지 확인 한 후 기존 대화 내용을 새로운 상담 건에 저장 한다.
            if(!objTempParams.getString("DLVR_CUTT_HSTRY_ID").equals("") && !objTempParams.getString("DLVR_CUTT_HSTRY_ID").equals("0")) {
                jsonParams.setString("DLVR_CUTT_HSTRY_ID", objTempParams.getString("DLVR_CUTT_HSTRY_ID"));
    
                //상담유형 저장 -전달받은 상담유형으로 세팅
                mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRtnTalkHistCnslInfo", jsonParams);
    
                //대화내역 데이터 가져오기
                objTempParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnTalkTransDtl", jsonParams);
                JSONArray jsonArray = objTempParams.getDataObject();
                JSONArray returnJsonArray = new JSONArray();
                if(jsonArray != null) {
                    int intCuttDtlSize = jsonArray.size();
                    for(int i = 0; i < intCuttDtlSize; i++) {
                        TelewebJSON jsonCuttDtlParams = new TelewebJSON(objTempParams);
                        jsonCuttDtlParams.setString("CHT_CUTT_DTL_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));
                        jsonCuttDtlParams.setString("CHT_CUTT_ID", objTempParams.getString("CHT_CUTT_ID"));
                        jsonCuttDtlParams.setString("CUSTCO_ID", objTempParams.getString("CUSTCO_ID"));
                        jsonCuttDtlParams.setString("CUSL_ID", jsonArray.getJSONObject(i).getString("CUSL_ID"));
                        jsonCuttDtlParams.setString("RCPTN_DSPTCH_CD", jsonArray.getJSONObject(i).getString("RCPTN_DSPTCH_CD"));
                        jsonCuttDtlParams.setString("RCPTN_SNDPTY_ID", jsonArray.getJSONObject(i).getString("RCPTN_SNDPTY_ID"));
                        jsonCuttDtlParams.setString("MSG_TYPE_CD", jsonArray.getJSONObject(i).getString("MSG_TYPE_CD"));
                        
                        jsonCuttDtlParams.setString("RCPTN_DSPTCH_MSG", jsonArray.getJSONObject(i).getString("RCPTN_DSPTCH_MSG"));
                        jsonCuttDtlParams.setString("CHG_RCPTN_DSPTCH_MSG", jsonArray.getJSONObject(i).getString("CHG_RCPTN_DSPTCH_MSG"));
                        jsonCuttDtlParams.setString("IMG_URL", jsonArray.getJSONObject(i).getString("IMG_URL"));
                        jsonCuttDtlParams.setString("FILE_GROUP_KEY", jsonArray.getJSONObject(i).getString("FILE_GROUP_KEY"));
                        jsonCuttDtlParams.setString("AUTO_RSPNS_MSG", jsonArray.getJSONObject(i).getString("AUTO_RSPNS_MSG"));
                        jsonCuttDtlParams.setString("LNK_INFO", jsonArray.getJSONObject(i).getString("LNK_INFO"));
                        jsonCuttDtlParams.setString("VIDEO_PATH", jsonArray.getJSONObject(i).getString("VIDEO_PATH"));
                        jsonCuttDtlParams.setString("VIDEO_URL", jsonArray.getJSONObject(i).getString("VIDEO_URL"));
                        jsonCuttDtlParams.setString("VIDEO_THUMNAIL_PATH", jsonArray.getJSONObject(i).getString("VIDEO_THUMNAIL_PATH"));
                        jsonCuttDtlParams.setString("READ_YN", jsonArray.getJSONObject(i).getString("READ_YN"));
                        jsonCuttDtlParams.setString("SYS_MSG_ID", jsonArray.getJSONObject(i).getString("SYS_MSG_ID"));
                        jsonCuttDtlParams.setString("RGTR_ID", jsonArray.getJSONObject(i).getString("RGTR_ID"));
                        jsonCuttDtlParams.setString("REG_DT", jsonArray.getJSONObject(i).getString("REG_DT"));
                        jsonCuttDtlParams.setString("MDFR_ID", jsonArray.getJSONObject(i).getString("MDFR_ID"));
                        jsonCuttDtlParams.setString("MDFCN_DT", jsonArray.getJSONObject(i).getString("MDFCN_DT"));
                        //상담이력 상세 테이블에 저장
                        mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkHistDetailInfoTrans", jsonCuttDtlParams);
                    }
                }
            } else {
                //전달건이 아니라면 상담 자동인사여부를 체크해서 자동인사 메시지를 전송한다
    //            TelewebJSON autoGreetParams = new TelewebJSON();
    //            autoGreetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectAutoGreetYn", jsonParams);
                String custcoId = jsonParams.getString("CUSTCO_ID");
                String chnClsfCd = jsonParams.getString("CHN_CLSF_CD");
                String custWaitMsgYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "AUTO_GREETING_YN");
                //이메일, 게시판 자동인사 제외
                if(custWaitMsgYn.equals("Y") && !("EMAIL".equals(chnClsfCd) || "BBS".equals(chnClsfCd))) {
    //            if(autoGreetParams.getString("STNG_VL").equals("Y")) {
                    String sysMsgId = "29";     //자동인사 시스템메시지id
                    String chtUserKey = jsonParams.getString("CHT_USER_KEY");
                    String sndrKey = jsonParams.getString("SNDR_KEY");
                    TelewebJSON messageJson = HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, sysMsgId);
                    transToKakaoService.sendSystemMsg(chtUserKey, sndrKey, messageJson, chnClsfCd); //고객에게 시스템메시지 전송
                }
                
                if(jsonParams.getString("CUTT_STTS_CD").equals("WAIT")) {
                	//선택한 상담이 전달 또는 콜백이 아닌 일반 대기 상태라면
                	//챗봇사용여부 체크하여 회원정보 기준으로 챗봇상담인지 일반상담인지 체크
                	//회원정보는 하루마다 재인증해야하기 때문에 하루기준 회원정보 조회
                	if(transToKakaoService.chatbotYn(jsonParams.getString("SNDR_KEY"))) {
                		//챗봇사용여부 = Y
                        TelewebJSON cntChbtUserParams = new TelewebJSON();
                        cntChbtUserParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectChbtUser", jsonParams);
                        if(cntChbtUserParams.getString("CNT").equals("0")) {
                        	//회원인증받지 않은 챗봇 = 일반상담
                        	jsonParams.setString("CHBT_STTS_CD","N");
                        } else {
                        	//회원인증 받은 챗봇 = 챗봇상담
                        	jsonParams.setString("CHBT_STTS_CD","Y");
                        }
                    	mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateChbtCutt", jsonParams);
                	}
                }
            }
    
            //화면에 보여줄 상담 이력 정보를 가져온다.
            objTempParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnTalkHistInfo", jsonParams);
            
            //최종결과값 반환
            return objRetParams;
        }


    /**
     * 상담이력을 임시저장한다.
     * 
     * @Transactional            해당 함수가 트랜젝션 처리 됨
     * @param         jsonParams
     * @return                   TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateRtnTalkHistTemp(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //상담이력 테이블에 저장
        jsonParams.setString("CHT_CUTT_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_HSTRY_ID")));
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRtnTalkHistTemp", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 상담 후 상담이력 저장한다.
     * 
     * @Transactional            해당 함수가 트랜젝션 처리 됨
     * @param         jsonParams
     * @return                   TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateRtnTalkHist(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        TelewebJSON jsonTempParam = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnTalkHistInfo", jsonParams);

        /*
         * // 비대면URL 전송 건수 조회 TelewebJSON objRetParams2 = new TelewebJSON(); objRetParams2 = selectUntactUrlSendCnt(jsonParams); int untactCnt = 0;
         * 
         * if (objRetParams2 != null && objRetParams2.getHeaderInt("COUNT") > 0) { untactCnt = objRetParams2.getInt("UNTACT_URL_COUNT"); jsonParams.setInt("UNTACT_URL_COUNT", untactCnt); }
         */
        jsonParams.setInt("UNTACT_URL_COUNT", 0);

        //상담이력 테이블에 저장
        jsonParams.setString("CHT_CLSF_CD", jsonTempParam.getString("CHT_CLSF_CD"));

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRtnTalkHist", jsonParams);

        //상담이력의 이력 남김.
        jsonParams.setString("CHT_CUTT_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_HSTRY_ID")));
        mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertTalkContactHist", jsonParams);

        objRetParams.setString("CUTT_STTS_CD", jsonParams.getString("CUTT_STTS_CD"));
        objRetParams.setString("PRCS_RSLT_CD", jsonParams.getString("PRCS_RSLT_CD")); // 콜백 처리를 위한 식별값 추가 , 20171117 sjh 

        //고객/에이전트반응시간 통계 업데이트
        //mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateResponseTime", jsonParams);
        //무응답시간 통계 업데이트
        //mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateNonResponseTime", jsonParams);

        // 콜백 저장시 티톡은 알림 필요 ( 티톡 토큰만료시 예외가 필요함)
        if("TTALK".equals(jsonTempParam.getString("CHN_CLSF_CD")) && "4".equals(jsonParams.getString("PRCS_RSLT_CD")) && jsonTempParam.getString("SNDR_KEY") != null) {

            transToKakaoService.sendCallbackYnMsg(jsonTempParam.getString("CHT_USER_KEY"), jsonTempParam
                .getString("CUSTCO_ID"), jsonParams.getString("PRCS_RSLT_CD") != null && "CLBK_WAIT".equals(jsonParams.getString("PRCS_RSLT_CD")) ? "Y" : "N", jsonTempParam.getString("CHN_CLSF_CD"));
        }
        
        
        try {
            log.info("--ALTMNT_RDY_REG_DT--상담이력의 날짜--" + jsonTempParam.getString("ALTMNT_RDY_REG_DT"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(jsonTempParam.getString("ALTMNT_RDY_REG_DT").substring(0,8));
            log.info("--date--상담이력날짜 폼 변경--" + date);
            
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1;
            int day = now.get(Calendar.DAY_OF_MONTH);
            log.info("--year--오늘년--" + year);
            log.info("--month--오늘월--" + month);
            log.info("--day--오늘일--" + day);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year2 = cal.get(Calendar.YEAR);
            int month2 = cal.get(Calendar.MONTH) + 1;
            int day2 = cal.get(Calendar.DAY_OF_MONTH);
            log.info("--year2--상담이력년--" + year2);
            log.info("--month2--상담이력월--" + month2);
            log.info("--day2--상담이력일--" + day2);
            
            boolean isToday = year == year2 && month == month2 && day == day2;
            log.info("--date--날짜 비교--" + isToday);
            
            //저장할 상담이 오늘 진행한 상담이 아니라면 통계에 업데이트 해줘야 함
            if(!isToday) {
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateChtStat", jsonParams);
            }
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            log.info("==통계 테이블 업데이트중 오류==" + e);
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 전달 처리 시, 상담이력을 저장한다.
     * 
     * @Transactional            해당 함수가 트랜젝션 처리 됨
     * @param         jsonParams
     * @return                   TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateRtnTalkHistByTrans(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //상담이력 테이블에 저장
        mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRtnTalkHistByTransChat", jsonParams);

        //상담이력의 이력 남김.
        jsonParams.setString("CHT_CUTT_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_HSTRY_ID")));
        mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertTalkContactHist", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 상담유형 저장
     * 
     * @Transactional            해당 함수가 트랜젝션 처리 됨
     * @param         jsonParams
     * @return                   TelewebJSON 형식의 처리 결과 데이터
     */
    /*
     * @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class, SQLException.class }, readOnly = false) public TelewebJSON processRtnTwbTalkReasonTyp(TelewebJSON jsonParams) throws
     * TelewebAppException { TelewebJSON objRetParams = new TelewebJSON();
     * 
     * if (jsonParams.getString("TRANS_STATUS").equals(TwbCmmnConst.TRANS_INS)) { // TelewebJSON objTempParams1 = new TelewebJSON(); // objTempParams1.setString("CNSL_TYP_CD",
     * jsonParams.getString("CNSL_TYP_CD")); 
     * 
     * TelewebJSON objTempParams = mobjDao.select("com.hcteletalk.teletalk.mng.resontyp.dao.TalkMngReasonTypMapper", "selectRtnDupReasonTyp", jsonParams); if (objTempParams.getHeaderInt("TOT_COUNT") > 0) {
     * objRetParams.setHeader("ERROR_FLAG", true); objRetParams.setHeader("ERROR_MSG", "해당 요청구분코드가 이미 존재합니다."); } else { mobjDao.update("com.hcteletalk.teletalk.mng.resontyp.dao.TalkMngReasonTypMapper",
     * "updateRtnTwbTalkReasonTyp", jsonParams); } } else { //상담이력 테이블에 저장 mobjDao.update("com.hcteletalk.teletalk.mng.resontyp.dao.TalkMngReasonTypMapper", "updateRtnTwbTalkReasonTyp", jsonParams); }
     * 
     * //최종결과값 반환 return objRetParams; }
     */
    /**
     * 전달받은 URL에 있는 text를 모두 읽어서 반환한다.
     * 
     * @param  jsonParams
     * @return            TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON getAllText(TelewebJSON jsonParams) throws IOException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        URL url = null;
        url = new URL(jsonParams.getString("URL"));

        URLConnection httpConn = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        objRetParams.setString("TEXT", response.toString());
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * DB에 저장되어 있는 모든 text를 읽어서 반환한다.(장문)
     * 
     * @param  jsonParams
     * @return            TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON getAllTextByDB(TelewebJSON jsonParams) throws TelewebAppException
    {

        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getAllTextByDB", jsonParams);

        return objRetParams;

    }


    /**
     * 고객 정보 입력창(고객에게 보낸 link)에서 고객이 입력한 정보 저장
     * 
     * @param  Object HashMap
     * @return        TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON insertUserInformation(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        TelewebJSON Cnt = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "countCustomerUser", jsonParams);
        if(Integer.parseInt(Cnt.getString("CNT")) > 0) {
            mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateCustomerInfo", jsonParams);
        }
        else {
            mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertCustomerInfo", jsonParams);
        }
        return objRetParams;
    }


    /**
     * 기존 상담 내용 불러오기.
     * 
     * @param  Object                       HashMap
     * @return                              TelewebJSON 형식의 ERROR_FLAG
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnChatContent(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        log.info("!@#!@#!@#jsonParams" + jsonParams);
        String chnClsfCd = jsonParams.getString("CHN_CLSF_CD");
        //이메일 상담일 시, 채팅_상담_이메일 테이블에서 조회.
        if("EMAIL".equals(chnClsfCd)) {
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnChatCuttEmail", jsonParams);
            if (!StringUtils.isEmpty(objRetParams.getString("FILE_GROUP_KEY"))) {
                objRetParams.setObject("FILE_LIST", 0, fileDbMngService.selectFileList(objRetParams).getDataObject());
            }
        } else if("BBS".equals(chnClsfCd)) {
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnChatCuttBbs", jsonParams);
        } else {
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnChatContent", jsonParams);
        }
        

        //고객명 마스킹처리
        int jsonSize = objRetParams.getHeaderInt("COUNT");
        if(objRetParams != null && jsonSize > 0) {
            for(int i = 0; i < jsonSize; i++) {
                String strCustNm = paletteDataFormatUtils.getFormatData("class_nameEnc", objRetParams.getString("CUSTOMER_NM", i));
                objRetParams.setString("CUSTOMER_NM", i, strCustNm);

                String strCustNm3 = paletteDataFormatUtils.getFormatData("class_nameEnc", objRetParams.getString("CUSTOMER_NM3", i));
                objRetParams.setString("CUSTOMER_NM3", i, strCustNm3);
            }
        }

        //금칙어
        if(!"EMAIL".equals(chnClsfCd) && !"BBS".equals(chnClsfCd)) {
            //JSONArray talkJsonArray = chatSettingBannedWordUtils.parseContents(objRetParams, jsonParams.getString("ADMIN_CHK"), jsonParams.getString("CUSTCO_ID"));
            JSONArray talkJsonArray = chatSettingBannedWordUtils.parseContents(objRetParams, jsonParams.getString("ADMIN_CHK"), jsonParams.getString("CUSTCO_ID"));
    
            objRetParams.remove("DATA");
            objRetParams.setDataObject("DATA", talkJsonArray);
        }
        
        TelewebJSON bfrChatParams = new TelewebJSON();
        TelewebJSON aftrChatParams = new TelewebJSON();
        bfrChatParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectBfrChat", jsonParams);
        JSONArray bfrArr = new JSONArray();
        bfrArr = bfrChatParams.getDataObject();
        if(bfrArr.size()!=0) {
            objRetParams.setString("BFR_CHT", bfrChatParams.getString("CHT_CUTT_ID"));
        } else {
            objRetParams.setString("BFR_CHT", "");
        }
        aftrChatParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectAftrChat", jsonParams);
        JSONArray aftrArr = new JSONArray();
        aftrArr = aftrChatParams.getDataObject();
        if(aftrArr.size()!=0) {
            objRetParams.setString("AFTR_CHT", aftrChatParams.getString("CHT_CUTT_ID"));
        } else {
            objRetParams.setString("AFTR_CHT", "");
        }

        return objRetParams;
    }


    /**
     * 상담원 및 관리자 정보 불러오기.
     * 
     * @param  Object                       HashMap
     * @return                              TelewebJSON 형식의 ERROR_FLAG
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUserNm(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnUserNm", jsonParams);

        //금칙어
        JSONArray talkJsonArray = chatSettingBannedWordUtils.parseContents(objRetParams, jsonParams.getString("ADMIN_CHK"), jsonParams.getString("CUSTCO_ID"));
//        JSONArray talkJsonArray = chatSettingBannedWordUtils.parseContents(objRetParams, jsonParams.getString("ADMIN_CHK"), "ADMIN");

        objRetParams.remove("DATA");
        objRetParams.setDataObject("DATA", talkJsonArray);

        return objRetParams;
    }


    /**
     * 메시지 삭제처리(메시지 내용 업데이트)
     * 
     * @param  Object HashMap
     * @return        TelewebJSON 형식의 ERROR_FLAG
     */
    public TelewebJSON updateRemoveConent(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        try {
            //삭제할 메시지의 타입 체크
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRemoveConentCheck", jsonParams);
            if(objRetParams.getString(TwbCmmnConst.G_DATA, "TYPE", 0).equals("remove_message")) {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "삭제 처리된 메시지입니다.");
            }
            else {
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRemoveConent", jsonParams);
            }

        }
        catch(Exception e) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "메시지 삭제 처리과정에서 오류가 발생하였습니다.");
            e.printStackTrace();
        }
        return objRetParams;
    }


    /**
     * 상담원이 입력한 내용 금칙어 필터링 처리.
     * 
     * @param  Object HashMap
     * @return        TelewebJSON 형식의 ERROR_FLAG
     */
    public TelewebJSON parseProhibiteByMessage(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        JSONArray talkJsonArray = new JSONArray();
        talkJsonArray = chatSettingBannedWordUtils.parseContents_4(jsonParams, ((JSONObject) (jsonParams.getDataObject().get(0))).getString("CUSTCO_ID"));
//        talkJsonArray = chatSettingBannedWordUtils.parseContents_4(jsonParams, "ADMIN");
        objRetParams.remove("DATA");
        objRetParams.setDataObject("DATA", talkJsonArray);

        return objRetParams;
    }


    /**
     * 사용자 메모 및 주의고객여부 저장
     * 
     * @param  Object HashMap
     * @return        TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateRtnMemoAndAtentCustomer(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "");

//        // 연락처 및 이메일을 수정한다.
//        if(jsonParams.getString("CUSTOMER_PHONE_NO") != null && jsonParams.getString("CUSTOMER_EML")  != null)
//        {
//            mobjDao.update("kr.co.hkcloud.palette.chat.main.dao.ChatMainMapper", "modifyUserPhoneNumberAndEmail", jsonParams);
//        }

        TelewebJSON countParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getCustomerUserCount", jsonParams);

        if(countParams.getInt("CNT") == 0) {        // 고객이 존재하는지 확인
            //mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertMemoAndAtentCustomer", jsonParams );
        }
        else {

            /*
             * TelewebJSON tmpParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getRtnMemoAndAtentCustomer", jsonParams);
             * 
             * // 고객유형(VIP) 업데이트 mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "modifyCustomerType", jsonParams);
             * 
             * if(tmpParams.getString("ATENT_CUSTOMER") == null || tmpParams.getString("ATENT_CUSTOMER").equals("") || tmpParams.getString("ATENT_CUSTOMER").equals("N")) { // 주의고객 여부 확인
             * mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "newAtentCustomer", jsonParams); // 주의고객 신규 등록 } else { mobjDao.update("kr.co.hkcloud.palette.chat.main.dao.ChatMainMapper",
             * "modifyAtentCustomer", jsonParams); // 주의고객 업데이트 }
             * 
             * if(tmpParams.getString("CUSTOMER_MEMO") == null || tmpParams.getString("CUSTOMER_MEMO").equals("")) { // 고객메모 존재여부 확인 mobjDao.update("kr.co.hkcloud.palette.chat.main.dao.ChatMainMapper", "newCustomerMemo",
             * jsonParams); // 고객메모 신규 등록 } else { mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "modifyCustomerMemo", jsonParams); // 고객메모 업데이트 }
             */
        }
        return objRetParams;
    }


    /**
     * 채팅메인-고객정보저장
     * 
     * @param  Object HashMap
     * @return        TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateRtnChatCustomerInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "");

        TelewebJSON countParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getCustomerUserCount", jsonParams);

        if(countParams.getInt("CNT") == 0) {        // 고객이 존재하는지 확인
            mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnChatCustomerInfo", jsonParams);
            TelewebJSON PNumCNT = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectCntPhoneNo", jsonParams);
            if(PNumCNT.getInt("CNT") > 1) {
                mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateTalkUserKey", jsonParams);
                mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRtnChatCustomerInfo", jsonParams);
                mobjDao.delete("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "deleteOneUserInfo", jsonParams);
            }
        }
        else {
            mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRtnChatCustomerInfo", jsonParams);
            TelewebJSON PNumCNT = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectCntPhoneNo", jsonParams);
            if(PNumCNT.getInt("CNT") > 1) {
                mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateTalkUserKey", jsonParams);
                mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRtnChatCustomerInfo", jsonParams);
                mobjDao.delete("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "deleteOneUserInfo", jsonParams);
            }
        }
        return objRetParams;
    }


    /**
     * 사용자 존재여부 확인 및 insert(기본정보만)
     * 
     * @param Object HashMap
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.DEFAULT,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON chkUserInCustomer(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getCustomerUserCount", jsonParams);   // 고객메모 업데이트

        if(objRetParams.getInt("CNT") == 0) {
            mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertUserInCustomer", jsonParams);
        }

        return objRetParams;
    }


    /**
     * 대기중에 고객포기건인지 체크
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnGiveupUserReady(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //대기중에 고객포기건인지 체크
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnGiveupUserReady", jsonParams);
        if(objRetParams.getInt("GIVEUP_CNT") == 0) {
            //세션만료처리
            //String consultAutoEndToCustomer = TelewebTalkInOut.getInstance().getTalkConfig().getString("consultAutoEndToCustomer"); //20180404 lgc 시스템 메시지 DB로 변경
            String consultAutoEndToCustomer = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(((JSONObject) (jsonParams.getDataObject().get(0))).getString("CUSTCO_ID"), "18");
            objRetParams.setString("consultAutoEndToCustomer", consultAutoEndToCustomer);
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 상담목록 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        log.debug("**************************jsonParams*****************************" + jsonParams);

        //대기중에 고객포기건인지 체크
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnTalkList", jsonParams);

        //고객명 마스킹 처리
//        if(objRetParams.getHeaderInt("TOT_COUNT") > 0 && objRetParams.containsKey("CUSTOMER_NM") && !objRetParams.getString("CUSTOMER_NM").isEmpty()) {
//            String nameEnc = paletteDataFormatUtils.getFormatData("class_nameEnc", objRetParams.getString("CUSTOMER_NM"));  // 고객명 (마스킹)
//            objRetParams.setString("CUSTOMER_NM", nameEnc);
//        }

        //금칙어
        //JSONArray talkJsonArray = chatSettingBannedWordUtils.parseContentTalkTile(objRetParams, jsonParams.getString("ADMIN_CHK"), jsonParams.getString("CUSTCO_ID"));
        JSONArray talkJsonArray = chatSettingBannedWordUtils.parseContentTalkTile(objRetParams, jsonParams.getString("ADMIN_CHK"), jsonParams.getString("CUSTCO_ID"));

        objRetParams.remove("DATA");
        objRetParams.setDataObject("DATA", talkJsonArray);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 상담완료목록 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkSearch(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //대기중에 고객포기건인지 체크
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnTalkSearch", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }
    @Transactional(readOnly = true)
    public TelewebJSON selectBbsParams(TelewebJSON jsonParam) throws TelewebAppException {

        TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectBbsParams", jsonParam);
        return objRetParams;
    }
    @Transactional(readOnly = false)
    public TelewebJSON insertBbsAnswer(TelewebJSON jsonParam) throws TelewebAppException {

        TelewebJSON objRetParams = new TelewebJSON();
        String chtCuttBbsAnswrId = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_BBS_ANSWR_ID")); //로그 시컨스정보 생성
        jsonParam.setString("CHT_CUTT_BBS_ANSWR_ID", chtCuttBbsAnswrId);
        jsonParam.setString("ANSWR_ID", jsonParam.getString("USER_ID"));
        jsonParam.setString("CUTT_STTS_CD", "CMPL");
        jsonParam.setString("CUTT_CN", jsonParam.getString("ANSWR_CN"));
        jsonParam.setString("CLBK_YN", "N");

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateCuttSttsBbs", jsonParam);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertBbsAnswer", jsonParam);

        return objRetParams;
    }



    /**
     * 상담목록 조회4
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkSearch4(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnTalkSearch4", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 상담이력목록(탭) 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkSearch5(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnTalkSearch5", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 상담이력 상세 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkHistInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //대기중에 고객포기건인지 체크
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnTalkHistInfo", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 고객 정보 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkCustInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        log.debug("jsonParams!@#!#!@#!@#!@#" + jsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnTalkCustInfo", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 개인현황판 조회
     * 
     * @param  jsonParams
     * @return            TelewebJSON 형식의 조회결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectPrivateHist(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        Map<String, String> mapTwbTalkStat = chatDashboardCounselService.getWorkTime(jsonParams, "Y");
        jsonParams.setString("YESTERDAY_WORK_TIME_START", mapTwbTalkStat.get("YESTERDAY_WORK_TIME_START"));
        jsonParams.setString("TODAY_WORK_TIME_START", mapTwbTalkStat.get("TODAY_WORK_TIME_START"));
        jsonParams.setString("TODAY_WORK_TIME_END", mapTwbTalkStat.get("TODAY_WORK_TIME_END"));

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectPrivateHist", jsonParams);

        return objRetParams;
    }


    /**
     * 인사말 발송 여부와 인사말 데이타를 내려준다.
     * 
     * @param  jsonParams
     * @return            TelewebJSON 형식의 조회결과 데이터
     */
    @Transactional(readOnly = false)
    public TelewebJSON getGreetingMessage(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //재로그인하고 상담중인 건 다시 상담하게 될 때 자동인사가 2번 나갈 수 있음
        //상담이력에서 자동인사여부 체크 by liy
        TelewebJSON autoGreetingJson = new TelewebJSON();
        Boolean isSendAutoGreeting = false;

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectTalkContactAutoGreeting", jsonParams);

        String autoGreetingYn = objRetParams.getString("AUTO_GREETING_YN");
        if(objRetParams != null && "Y".equals(autoGreetingYn)) {
            isSendAutoGreeting = true;
        }

//        //상담사자동인사 사용여부
        if(!isSendAutoGreeting && "Y".equals(HcTeletalkDbEnvironment.getInstance().getString(((JSONObject) (jsonParams.getDataObject().get(0))).getString("CUSTCO_ID"), "AUTO_GREETING_YN"))) {
            StringBuffer sbGreetingMsg = new StringBuffer();

            // 고객문의유형 사용할 때
            if("Y".equals(HcTeletalkDbEnvironment.getInstance().getString(((JSONObject) (jsonParams.getDataObject().get(0))).getString("CUSTCO_ID"), "INQRY_TYPE_YN"))) {
                String s1 = paletteFilterUtils
                    .filter2(HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(((JSONObject) (jsonParams.getDataObject().get(0))).getString("CUSTCO_ID"), "28"));
                sbGreetingMsg.append(s1);
            }
            else {
                String s2 = paletteFilterUtils
                    .filter2(HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(((JSONObject) (jsonParams.getDataObject().get(0))).getString("CUSTCO_ID"), "29"));
                sbGreetingMsg.append(s2);
            }

            //2019.04.30 모빌리티 자동인사메시지 사용자명에서 닉네임으로 변경
            String greetingMsg = String.format(sbGreetingMsg.toString(), jsonParams.getString("USER_NICK_NAME"));

            //상담이력에 자동인사여부 Y로 업데이트
            {
//                dataProcess.updateTalkContactAutoGreetingY(autoGreetingJson);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateTalkContactAutoGreetingY", jsonParams);
            }

            objRetParams.setString("AUTO_GREETING_YN", "N");
            objRetParams.setString("GREETING_MSG", greetingMsg);
        }
        else {

            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateTalkContactAutoGreetingY", jsonParams);

            objRetParams.setString("AUTO_GREETING_YN", "Y");
            objRetParams.setString("GREETING_MSG", "");
        }

        return objRetParams;
    }


    /**
     * 상담진행상태별 건수 조회
     * 
     * @param  jsonParams
     * @return            TelewebJSON 형식의 조회결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectTalkStateProcessByUser(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectTalkStateProcessByUser", jsonParams);

        return objRetParams;
    }


    /**
     * twb.TwbTalk.updateSystemMsgSkip
     * 
     * @param  mjsonParams
     * @return
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateSystemMsgSkip(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateSystemMsgSkip", jsonParams);

        return objRetParams;
    }


    /**
     * twb.TwbTalk.insertSystemMsgSkip
     * 
     * @param  mjsonParams
     * @return
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertSystemMsgSkip(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertSystemMsgSkip", jsonParams);

        return objRetParams;
    }


    /**
     * 상담정보 조회
     * 
     * @param  jsonParams
     * @return            TelewebJSON 형식의 조회결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON getChatInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectChatInfo", jsonParams);

        return objRetParams;
    }


    /**
     * 스크립트 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON searchScripts(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        String scriptTit = jsonParams.getString("SCRIPT_TIT");
        if(scriptTit != null) {
            jsonParams.setString("SCRIPT_TIT", scriptTit.trim());
            if(scriptTit.trim().indexOf("%") > -1) {
                jsonParams.setString("SCRIPT_TIT", scriptTit.trim().replaceAll("%", ""));
            }
        }

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.script.dao.ChatScriptManageMapper", "selectRtnScripts", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 비대면링크 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    /*
     * @Transactional(readOnly = true) public TelewebJSON searchUntacts(TelewebJSON jsonParams) throws TelewebAppException { TelewebJSON objRetParams = new TelewebJSON();
     * 
     * String scriptTit = jsonParams.getString("BTN_NM"); if(scriptTit != null) { jsonParams.setString("BTN_NM", scriptTit.trim()); if(scriptTit.trim().indexOf("%") > -1) { jsonParams.setString("BTN_NM",
     * scriptTit.trim().replaceAll("%", "")); } }
     * 
     * objRetParams = mobjDao.select("com.hcteletalk.teletalk.mng.untact.dao.TalkMngUntactUrlMapper", "selectRtnUntacts", jsonParams);
     * 
     * //최종결과값 반환 return objRetParams; }
     */


    /**
     * 컨텐츠 문의유형 레벨 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkInqryInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnTalkInqryInfo", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 상담원 상태 업데이트 , 대기중 삽입
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertTalkReadyOff(TelewebJSON jsonParams) throws TelewebAppException
    {
        //client에서 전송받은 파라메터 생성
        //        TelewebJSON mjsonParams     = (TelewebJSON)inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON();

        String strLogID = Integer.toString(innbCreatCmmnService.createSeqNo("USER_LOG_ID")); //로그 시컨스정보 생성
        jsonParams.setString("USER_LOG_ID", strLogID);
        log.info(strLogID);
        //objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.status.dao.ChatStatusAgentAwayHistoryMapper", "insertUserAwayHst", jsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectTalkReadyOffUserId", jsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);

        // 이석상태 연속 선택 처리
        if(objRetParams.getInt("CNT") > 0) {
            if(!objRetParams.getString("USER_CHT_STAT").equals(jsonParams.getString("USER_CHT_STAT"))) {
                //1. 채팅OFF 종료시간 업데이트
                mobjDao.update("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "updateTalkReadyOffInEndTime", jsonParams);
                //2. 채팅OFF 히스토리 기록
                jsonParams.setString("CHT_RDY_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_RDY_HSTRY_ID")));
                mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "insertTalkReadyOffHist", jsonParams);
                //3. 채팅OFF 초기화
                mobjDao.delete("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "deleteTalkReadyOff", jsonParams);
                //4. 채팅OFF 등록
                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "insertTalkReadyOff", jsonParams);
            }
        }
        else {
            //1. 채팅OFF 등록
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "insertTalkReadyOff", jsonParams);
        }
        //이석 상태 등록

        // 채팅 대기 등록 또는 해제 
        if(jsonParams.getString("USER_CHT_STAT") != null && jsonParams.getString("USER_CHT_STAT").startsWith("CHT_WAIT")) {
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertTalkReady", jsonParams);
        }
        else {
            mobjDao.delete("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "deleteTalkReady", jsonParams);
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 스크립트 명령어 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON existScriptCommand(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.script.dao.ChatScriptManageMapper", "existScriptCommand", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 스크립트 신규 등록
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.script.dao.ChatScriptManageMapper", "insertRtnScriptMng", jsonParams);

        return objRetParams;
    }


    /**
     * 스크립트 수정
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.script.dao.ChatScriptManageMapper", "updateRtnScriptMng", jsonParams);

        return objRetParams;
    }


    /**
     * 스크립트 삭제
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.script.dao.ChatScriptManageMapper", "deleteRtnScriptMng", jsonParams);

        return objRetParams;
    }


    /**
     * 대기시간 초과 건이 있는지 체크 한다.
     * 
     * @param  inHashMap
     * @return           objRetParams
     */

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON checkReadyTimeout(TelewebJSON jsonParams) throws TelewebAppException
    {
        log.trace("syncChatStatus :::");

        int retryTimeOut = HcTeletalkDbEnvironment.getInstance().getString("CUSTCO_ID", "RETRY_READY_MAX_TIMEOUT") != null ? Integer
            .valueOf(HcTeletalkDbEnvironment.getInstance().getString("CUSTCO_ID", "RETRY_READY_MAX_TIMEOUT")) : 0;
        int maxRetryCnt = HcTeletalkDbEnvironment.getInstance().getString("CUSTCO_ID", "RETRY_READY_MAX_CNT") != null ? Integer
            .valueOf(HcTeletalkDbEnvironment.getInstance().getString("CUSTCO_ID", "RETRY_READY_MAX_CNT")) : 0;

        TelewebJSON objRetParams = new TelewebJSON();
        int readyTimeoutCnt = 0;
        if(retryTimeOut != 0) {
            jsonParams.setInt("RETRY_TIMEOUT", retryTimeOut);
            jsonParams.setInt("MAX_RETRY_CNT", maxRetryCnt);
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectTimeoutTalkUserReady", jsonParams);  // 재배정 대상이 있는지 조사한다.

            JSONArray talkUserKeyLoop = objRetParams.getDataObject();

            // 자동재분배를 위해 대기 시간 초과건 처리 11->10 , user_id 초기화  
            if(talkUserKeyLoop.size() > 0) {
                String talkUserKey = talkUserKeyLoop.getJSONObject(0).getString("TALK_USER_KEY");
                String retryCnt = talkUserKeyLoop.getJSONObject(0).getString("RETRY_ROUNTING_CNT");

                jsonParams.setString("TALK_USER_KEY", talkUserKey);
                jsonParams.setInt("RETRY_ROUNTING_CNT", (Integer.parseInt(retryCnt) + 1));
                jsonParams.setString("TALK_READY_CD", "10");
                jsonParams.setString("USER_ID", "");

                mobjDao.update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "updateTalkUserReady", jsonParams);

                //고객대기에서 대기이력테이블에 데이터 저장
                jsonParams.setString("CHT_USER_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_USER_HSTRY_ID")));
                mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkReadyHist", jsonParams);

                readyTimeoutCnt = talkUserKeyLoop.size();
            }
        }

        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "");
        objRetParams.setInt("READY_TIMEOUT", readyTimeoutCnt);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 사용자 챗온 검사
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON isReadyUser(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "isReadyUser", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 스크립트 상세조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON getScript(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.script.dao.ChatScriptManageMapper", "getScript", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 스크립트 단축키 저장
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertUserScriptShortKey(TelewebJSON jsonParams, JSONArray commands) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        if(commands != null && commands.size() > 0) {
            for(int i = 0; i < commands.size(); i++) {
                JSONObject command = commands.getJSONObject(i);

                String shortKey = command.getString("SHORT_KEY");
                String actionType = command.getString("ACTION_TYPE");

                if(shortKey != null && !"".equals(shortKey)) {

                    jsonParams.setString("SHORT_KEY", shortKey);
                    jsonParams.setString("ACTION_TYPE", actionType);
                    jsonParams.setString("SHORT_KEY_VALUE", "");

                    objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.script.dao.ChatScriptManageMapper", "insertUserScriptShortKey", jsonParams);
                }

            }

        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 스크립트 단축키 삭제
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteUserScriptShortKey(TelewebJSON jsonParams, JSONArray commands) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        // 다른스크립트로 등록 된 단축키 삭제 
        if(commands != null && commands.size() > 0) {
            for(int i = 0; i < commands.size(); i++) {
                JSONObject command = commands.getJSONObject(i);

                String shortKey = command.getString("SHORT_KEY");
                String actionType = command.getString("ACTION_TYPE");

                //if(shortKey != null && !"".equals(shortKey)) {

                jsonParams.setString("SHORT_KEY", shortKey);
                jsonParams.setString("ACTION_TYPE", actionType);

                objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.chat.script.dao.ChatScriptManageMapper", "deleteUserShortKey", jsonParams);
                //}

            }

        }

        // 해당스크립트 단축키 삭제 
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.chat.script.dao.ChatScriptManageMapper", "deleteUserScriptShortKey", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 20201028 메시지 전체 READ 여부 값 업데이트
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateIsReadTalkAll(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateIsReadTalkAll", jsonParams);

        return objRetParams;
    }


    /**
     * 20201028 메시지 전체 READ 여부 값 업데이트
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateIsReadTalk(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateIsReadTalk", jsonParams);

        return objRetParams;
    }


    /**
     * Dashboard 건수 조회
     * 
     * @param  jsonParams
     * @return            TelewebJSON 형식의 조회결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectTalkStateDashboard(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON objRetParamsTmp = new TelewebJSON();

        objRetParams.setString("ACCEPT_CNT", "0");
        objRetParams.setString("ACCEPTINQRY_CNT", "0");
        objRetParams.setString("READY_CNT", "0");
        objRetParams.setString("COUNSEL_CNT", "0");
        objRetParams.setString("AFTER_PROC_CNT", "0");

        //고객대기상태 조회
        objRetParamsTmp = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectUserReadyStatusInTalkMain", jsonParams);
        if(objRetParamsTmp.getHeaderInt("COUNT") > 0) {
            JSONArray arr = new JSONArray();
            arr = objRetParamsTmp.getDataObject();
            for(int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                switch(obj.getString("TALK_READY_CD"))
                {
                    case "09":
                        objRetParams.setString("ACCEPT_CNT", obj.getString("CNT"));
                        break;
                    case "10":
                        objRetParams.setString("ACCEPTINQRY_CNT", obj.getString("CNT"));
                        break;
                    case "11":
                        objRetParams.setString("READY_CNT", obj.getString("CNT"));
                        break;
                }
            }
        }

        //상담상태 조회
        objRetParamsTmp = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectContactStatusInTalkMain", jsonParams);
        if(objRetParamsTmp.getHeaderInt("COUNT") > 0) {
            objRetParams.setString("COUNSEL_CNT", objRetParamsTmp.getString("NUMBER_DURING_CHATTING"));
            objRetParams.setString("AFTER_PROC_CNT", objRetParamsTmp.getString("NUMBER_DURING_AFTER_TREATMENT"));
        }

        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "");

        return objRetParams;
    }


    /**
     * 고객정보 고객명을 저장한다.
     * 
     * @Transactional            해당 함수가 트랜젝션 처리 됨
     * @param         jsonParams
     * @return                   TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateRtnCustInfoEai(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //고객정보 테이블에 저장
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "updateRtnCustInfoEai", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 비대면URL 전송 건수 조회
     * 
     * @param  jsonParams
     * @return            TelewebJSON 형식의 조회결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectUntactUrlSendCnt(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //비대면URL 전송 건수 조회
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectUntactUrlSendCnt", jsonParams);

        return objRetParams;
    }


    /**
     * 주의고객 리스트 중 가장 최근 리스트를 반환한다.
     * 
     * @param  jsonArray
     * @return
     * @throws TelewebAppException
     */
    public JSONObject getLastCustAtent(JSONArray jsonArray) throws TelewebAppException
    {
        JSONObject jsonReturn = new JSONObject();

        int arrSize = 0;

        if(jsonArray != null) {
            arrSize = jsonArray.size();
        }

        if(arrSize > 0) {
            for(int i = 0; i < arrSize; i++) {
                JSONObject jsonTemp = jsonArray.getJSONObject(i);
                String trtDtm = jsonTemp.getString("trtDtm");
                String LastTrtDtm = null;
                java.sql.Timestamp currentTime = null;
                java.sql.Timestamp LastTime = null;

                if(jsonReturn.has("trtDtm")) {
                    LastTrtDtm = jsonReturn.getString("trtDtm");
                    LastTime = java.sql.Timestamp.valueOf(getTimestampFormat(LastTrtDtm));  //가장최근시간
                }

                currentTime = java.sql.Timestamp.valueOf(getTimestampFormat(trtDtm));   //현재시간

                //가장 최근 시간인 경우, 리턴할 json에 저장
                if(LastTime == null) {
                    jsonReturn = jsonTemp;
                }
                else if(currentTime.getTime() > LastTime.getTime()) {
                    jsonReturn = jsonTemp;
                }

            }
        }

        return jsonReturn;
    }


    /**
     * 문자열 Timestamp 로 형변환한다.
     * 
     * @param  str
     * @return
     * @throws TelewebAppException
     */
    private String getTimestampFormat(String str) throws TelewebAppException
    {
        String temp = str;
        StringBuffer sb = new StringBuffer();

        temp = temp.replaceAll("-", "");
        temp = temp.replaceAll("\\.", "");
        temp = temp.replaceAll(" ", "");

        sb.append(temp.substring(0, 4));
        sb.append("-");
        sb.append(temp.substring(4, 6));
        sb.append("-");
        sb.append(temp.substring(6, 8));
        sb.append(" ");
        sb.append(temp.substring(8, 10));
        sb.append(":");
        sb.append(temp.substring(10, 12));
        sb.append(":");
        sb.append(temp.substring(12, 14));
        sb.append(".");
        sb.append(temp.substring(14, temp.length()));

        return sb.toString();
    }


    /**
     * 20210119 추가 상담원상태조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectUserSta(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectTalkReadyOffUserId", jsonParams);

        return objRetParams;
    }


    /**
     * 20211202 회사로고 경로 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getImgSrc(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectImgSrc", jsonParams);

        return objRetParams;
    }


    /**
     * 20220411 채팅채널 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getSenderKey(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectSenderKey", jsonParams);

        return objRetParams;
    }


    /**
     * 책깔피 등록
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON regiMarkUp(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateMarkUp", jsonParams);

        return objRetParams;
    }


    /**
     * 채팅가능여부 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getChatYN(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getChatYN", jsonParams);

        return objRetParams;
    }
    
    /**
     * 발신프로필키 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getSender(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getSender", jsonParams);

        return objRetParams;
    }
    
    /**
     * uuid 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getUuid(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getUuid", jsonParams);

        return objRetParams;
    }

    
    /**
     * 상담이력 강제생성
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON chatCnslForceRegist(TelewebJSON jsonParams) throws TelewebAppException
    {
        
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        //1. 채팅 이력 등록 / 수정
        if(jsonParams.getString("CHT_CUTT_ID").equals("") || jsonParams.getString("CHT_CUTT_ID") == null) {
            jsonParams.setString("CHT_CUTT_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_ID")));
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertChatForceCnsl", jsonParams);         //채팅상담이력등록
            
            //jsonParams.setString("TALK_CONTACT_ID", Integer.toString(innbCreatCmmnService.createSeqNo("ITGRT_HSTRY_ID")));
            //objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "insertForceUnity", jsonParams);          //톱합이력등록
            objRetParams = paletteCmmnService.cuttItgrtHistReg(jsonParams);

        } else {
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateChtCutt", jsonParams);          //채팅이력수정
        }


        //2. 채팅 확장 정보 저장 (PLT_CHT_CUTT_DTL_EXPSN)
        if(jsonParams.containsKey("EXPSN_ATTR") && !jsonParams.getString("EXPSN_ATTR").isEmpty()){
            String expsnAttrStr = jsonParams.getString("EXPSN_ATTR");
            expsnAttrStr = expsnAttrStr.replace("&#91;", "[").replace("&#93;", "]");
            JSONArray arryExpsnAttr = JSONArray.fromObject(expsnAttrStr);

            if (arryExpsnAttr.size() > 0) {
                for (Object expsnAttr : arryExpsnAttr) {
                    TelewebJSON expsnAttrParams = new TelewebJSON();

                    expsnAttrParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                    expsnAttrParams.setString("PP_ALG_PP", P_ALG);
                    expsnAttrParams.setString("PP_KEY_PP", P_KEY);
                    expsnAttrParams.setString("CHT_CUTT_ID", jsonParams.getString("CHT_CUTT_ID"));
//                        expsnAttrParams.setString("CHG_HSTRY_ID", jsonParams.getString("CHG_HSTRY_ID"));
                    expsnAttrParams.setString("EXPSN_ATTR_COL_ID", (String) ((JSONObject) expsnAttr).get("EXPSN_ATTR_COL_ID"));
                    expsnAttrParams.setString("ATTR_ID", (String) ((JSONObject) expsnAttr).get("ATTR_ID"));
                    expsnAttrParams.setString("ATTR_VL", (String) ((JSONObject) expsnAttr).get("V_POST_PARAM"));
                    expsnAttrParams.setString("INDI_INFO_ENCPT_YN", (String) ((JSONObject) expsnAttr).get("INDI_INFO_ENCPT_YN"));
                    expsnAttrParams.setString("RSVT_PHN_CUTT_ID", jsonParams.getString("RSVT_PHN_CUTT_ID"));
                    expsnAttrParams.setString("USER_ID", jsonParams.getString("USER_ID"));
//                        expsnAttrParams.setString("CLBK_ID", jsonParams.getString("CLBK_ID"));
//                        expsnAttrParams.setString("CPI_ID", jsonParams.getString("CPI_ID"));
//                        expsnAttrParams.setString("RSVT_ID", jsonParams.getString("RSVT_ID"));

                    if(jsonParams.getString("CHT_CUTT_ID").equals("") || jsonParams.getString("CHT_CUTT_ID") == null) { // 등록
                        cuttExpsnAttrReg(expsnAttrParams); //채팅 상담 상세 확장 정보 저장
                    }else{ // 수정
                        cuttExpsnAttrMerge(expsnAttrParams);
                    }
                }
            }
        }
        
        return objRetParams;
    }
    
    
    /**
     * 고객사 채팅 카운트
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON chatCnslCount(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "chatCnslCount", jsonParams);

        return objRetParams;
    }
    
    /**
     * 인입된 상담 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON selectNowCutt(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectNowCutt", jsonParams);

        return objRetParams;
    }
    
    /**
     * 상담유형 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON selectCuttType(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON resParams = new TelewebJSON();
        jsonParams.setString("SRCH_CUTT_TYPE_ID", jsonParams.getString("CUTT_TYPE_ID"));
        
        for(int i=0;i<Integer.parseInt(jsonParams.getString("CUTT_TYPE_LMT_LVL_CD"));i++) {
            if(!objRetParams.getString("CUTT_TYPE_LVL").equals("1")) {
                objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectCuttType", jsonParams);
                if(objRetParams.getString("CUTT_TYPE_SE_CD")== null || objRetParams.getString("CUTT_TYPE_SE_CD").equals("")) {
                    resParams.setString(Integer.toString(i),objRetParams.getString("CUTT_TYPE_ID"));
                    break;
                } else {
                    resParams.setString(Integer.toString(i),objRetParams.getString("CUTT_TYPE_ID"));
                    jsonParams.setString("SRCH_CUTT_TYPE_ID", objRetParams.getString("UP_CUTT_TYPE_ID"));
                }
            }
        }

        return resParams;
    }

    
    /**
     * 콜백 대기이력으로
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertClbkRdy(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON resParams = new TelewebJSON();
        jsonParams.setString("CHT_RDY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_RDY_ID")));
        resParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertClbkRdy", jsonParams);

        return resParams;
    }

    
    /**
     * 채팅 키워드 검색 리스트 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getCuttDetailList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParams = new TelewebJSON();
        retParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getCuttDetailList", jsonParams);

        return retParams;
    }

    
    /**
     * 게시판문의 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON selectPstQstn(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectPstQstn", jsonParams);
        
        objRetParams.getString("DATA");
        String pstQstnChnId = objRetParams.getString("PST_QSTN_CHN_ID");
//        String custcoId = objRetParams.getString("CUSTCO_ID");
//        String sndrKey = objRetParams.getString("SNDR_KEY");
        String custcoId = jsonParams.getString("CUSTCO_ID");
        String sndrKey = jsonParams.getString("SNDR_KEY");
        String apiUri = objRetParams.getString("API_URI");
        String pstUserKey = objRetParams.getString("PST_USER_KEY");
        String pstTtlKey = objRetParams.getString("PST_TTL_KEY");
        String pstCnKey = objRetParams.getString("PST_CN_KEY");
        String pstDtKey = objRetParams.getString("PST_DT_KEY");
        String selectApi = objRetParams.getString("SELECT_API");
        String dataPath = objRetParams.getString("DATA_PATH");
        String dataType = objRetParams.getString("DATA_TYPE");
        String prKey = objRetParams.getString("PR_KEY");
        String selectApiKey = objRetParams.getString("SELECT_API_KEY");
        String[] selectApiKeys = selectApiKey.split(",");
        String selectApiValue = objRetParams.getString("SELECT_API_VALUE");
        String[] selectApiValues = selectApiValue.split(",");
        String apiTy = objRetParams.getString("API_TY");
        String insertApi = objRetParams.getString("INSERT_API");
        String scsCd = objRetParams.getString("SCS_CD");
        String insertApiKey = objRetParams.getString("INSERT_API_KEY");
        String[] insertApiKeys = insertApiKey.split(",");
        String insertApiValue = objRetParams.getString("INSERT_API_VALUE");
        String[] insertApiValues = insertApiValue.split(",");


        log.info("param" + objRetParams);
        log.info("param" + apiTy);
        TelewebJSON resultParams = new TelewebJSON(jsonParams);
        
        String result = "";
        try {
            //전송할 파라미터 설정
            if(apiTy.equals("FORM")) {
                HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
                factory.setConnectTimeout(5000); //타임아웃 설정 5초
                factory.setReadTimeout(5000);//타임아웃 설정 5초

                RestTemplate restTemplate = new RestTemplate(factory);

                HttpHeaders header = new HttpHeaders();
                String url = apiUri + selectApi;     //외부 API주소

                //외부 API로 보낼 HEADER부분
                header.add("Content-Type", "application/x-www-form-urlencoded");
                header.add("Authorization", "bearer null");
                //header.add("X-Bottalks-Auth-key", certifyKey);

                //외부 API로 보낼 BODY부분
                MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

                int selKeyLeng = selectApiKeys.length;
                for(int i=0;i<selKeyLeng;i++) {
                    parameters.add(selectApiKeys[i], selectApiValues[i]);
                }

                log.debug("parameters ====" + parameters.toString());
                log.debug("header ====" + header.toString());
                log.debug("url=======" + url);

                //외부 API로 보내기
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(parameters, header);

                //외부 API로 보낸 결과 저장
                result = restTemplate.postForObject(new URI(url), request, String.class);
                log.info("result" + result);
            } else if(apiTy.equals("PARAM")) {
                HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
                factory.setConnectTimeout(5000); //타임아웃 설정 5초
                factory.setReadTimeout(5000);//타임아웃 설정 5초

                RestTemplate restTemplate = new RestTemplate(factory);

                HttpHeaders header = new HttpHeaders();

                String param = "?";
                
                int selKeyLeng = selectApiKeys.length;
                for(int i=0;i<selKeyLeng;i++) {
                    if(i != 0) {
                        param = param + "&";
                    }
                    param = param + selectApiKeys[i] + "=" + selectApiValues[i];
                }
                
                String url = apiUri + selectApi + param;     //외부 API주소

                //외부 API로 보낼 HEADER부분
                header.add("Content-Type", "application/x-www-form-urlencoded");
                header.add("Cert-Custco-Tenant-Id", "public");
                header.add("Cert-Custco-Id", "1");
                header.add("Authorization", "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwbHQzMCIsImF1dGgiOiJST0xFXzEiLCJpbmZvIjoiY3VzdGNvSWQ6OjEsdXNlcklkOjoxLGxnbklkOjpwbHQzMCIsImV4cCI6MTY5NzA3NDU2N30.Qm1OhUNmp0zSE7iPVen7SIFndYmqow7KKqRDjukALTs");
                //header.add("X-Bottalks-Auth-key", certifyKey);

                //외부 API로 보낼 BODY부분
                MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
                
                parameters.add("", "");
                
                log.debug("header ====" + header.toString());
                log.debug("url=======" + url);

                //외부 API로 보내기
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(parameters, header);

                //외부 API로 보낸 결과 저장
                result = restTemplate.postForObject(new URI(url), request, String.class);
                log.info("result2" + result);
            }

        }
        catch(HttpClientErrorException | HttpServerErrorException e) {

            System.out.println(e.toString());

        }
        catch(Exception e) {

            System.out.println(e.toString());
        }
        String[] dataPathArr = dataPath.split(",");
        String[] dataTypeArr = dataType.split(",");
        log.info("dataPathArr" + dataPathArr);
        log.info("dataTypeArr" + dataTypeArr);
        int leng = dataTypeArr.length;
        JSONArray resultDataArray = new JSONArray();
        JSONObject jsonResultObj = new JSONObject();
        log.info("leng" + leng);
        for(int i=0;i<leng;i++) {
            log.info("==i ==" + i);
            log.info("== dataTypeArr[i] ==" + dataTypeArr[i]);
            log.info("== dataPathArr[i] ==" + dataPathArr[i]);
            if(i==0) {
                if(dataTypeArr[i].equals("JSON")) {
                    JSONObject jsonObj = JSONObject.fromObject(JSONSerializer.toJSON(result));
                    log.info("dataPathArr[i]" + dataPathArr[i]);
                    jsonResultObj = (JSONObject) jsonObj.get(dataPathArr[i]);
                    log.info("jsonResultObj" + jsonResultObj);
                } else if(dataTypeArr[i].equals("JSONARRAY")) {
                    JSONObject jsonObj = JSONObject.fromObject(JSONSerializer.toJSON(result));
                    log.info("dataPathArr[i]" + dataPathArr[i]);
                    resultDataArray = (JSONArray) jsonObj.get(dataPathArr[i]);
                    log.info("resultDataArray" + resultDataArray);
                }
            } else {
                if(dataTypeArr[i].equals("JSON")) {
                    jsonResultObj = (JSONObject) jsonResultObj.get(dataPathArr[i]);
                    log.info("==i ===" + i + "==== jsonResultObj ===" + jsonResultObj);
                } else if(dataTypeArr[i].equals("JSONARRAY")) {
                    resultDataArray = (JSONArray) jsonResultObj.get(dataPathArr[i]);
                    log.info("==i ===" + i + "==== resultDataArray ===" + resultDataArray);
                }
            }
        }
        log.info("resultDataArray" + resultDataArray);
        
        
        JSONObject resultObj = new JSONObject();
        
        for(int i=0; i < resultDataArray.size(); i++) {
            System.out.println("======== result : " + i + " ========");
            resultObj = (JSONObject) resultDataArray.get(i);
            System.out.println("======== result : resultObj = "+ resultObj);
            
            String pstId = resultObj.getString(prKey);
            String pstUser = resultObj.getString(pstUserKey);
            String pstTtl = resultObj.getString(pstTtlKey);
            String pstCn = resultObj.getString(pstCnKey);
            String pstDt = resultObj.getString(pstDtKey);
            pstDt = pstDt.replaceAll("-", "").replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "");
            if(pstDt.length() < 14) {
                String fill = "";
                for(int n=0;n<14-pstDt.length();n++) {
                    fill = fill + "0";
                }
                pstDt = pstDt+fill;
            } else if(pstDt.length() > 14) {
                pstDt = pstDt.substring(0, 14);
            }

            TelewebJSON objPstParam = new TelewebJSON(jsonParams);
            objPstParam.setString("SNDR_KEY", sndrKey);
            objPstParam.setString("CUSTCO_ID", custcoId);
            objPstParam.setString("PST_ID", pstId);
            objPstParam.setString("PST_USER", pstUser);
            objPstParam.setString("PST_TTL", pstTtl);
            objPstParam.setString("PST_CN", pstCn);
            objPstParam.setString("PST_DT", pstDt);
            System.out.println("======== result : objPstParam = "+ objPstParam);
            
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectPstQstnCount", objPstParam);
//              if(objRetParams.getString("CNT").equals("0")) {
            if(objRetParams.getString("CNT").equals("999")) {
                CustomerVO customerVO = new CustomerVO();
                customerVO.setCustomerId(pstUser);
                customerVO.setTalkUserKey(pstUser);
                customerVO.setCustcoId(custcoId);
                customerVO.setSndrKey(sndrKey);
                customerVO.setChnClsfCd("PST");
                settingCustomerInformationListService.mergeCustomerBaseInfo(customerVO);
                objPstParam.setString("PST_QSTN_CUTT_ID", Integer.toString(innbCreatCmmnService.createSeqNo("PST_QSTN_CUTT_ID")));
                objPstParam.setString("CHT_RDY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_RDY_ID")));
                objPstParam.setString("CHT_CUTT_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_ID")));
                //게시판 문의 -> 문의 테이블에 삽입
                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertPstQstn", objPstParam);
                //게시판 문의 -> 대기 테이블에 삽입
                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertPstQstnToChtCuttRdy", objPstParam);
                //게시판 문의 -> 이력 테이블에 삽입
                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertPstQstnToChtCutt", objPstParam);
//              } else {
            } else if(objRetParams.getString("CNT").equals("998")) {
                //게시판 문의 -> 문의 테이블에 업데이트
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updatePstQstn", objPstParam);
            }
            
            log.info("result" + resultObj);
        }
        
        //JSONArray pstQstnData = new JSONArray();
        
        return resultParams;
        //objRetParams = mobjDao.insert("kr.co.hkcloud.palette.setting.system.dao.SettingSystemCorporateAccountManageMapper", "insertBotalk", jsonParams);
        //}
        //else {

        //}
    }

    
    /**
     * 채팅 설정값 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getChtStng(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParams = new TelewebJSON();
        retParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getChtStng", jsonParams);

        return retParams;
    }


    
    /**
     * 3자채팅을 위한 특정 채팅 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getTripleCutt(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParams = new TelewebJSON();
        retParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getTripleCutt", jsonParams);

        return retParams;
    }
    
    /**
     * 챗봇채널에대한 데이터 가져오기
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getChbtUserData(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParams = new TelewebJSON();
        retParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getChbtUserData", jsonParams);

        return retParams;
    }


    
    /**
     * 챗봇채널에대한 데이터 가져오기
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getChbtData(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParams = new TelewebJSON();
        retParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getChbtData", jsonParams);

        return retParams;
    }


    
    /**
     * 고객 챗봇 고객 리스트 가져오기 - 확인용
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getChbtUser(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParams = new TelewebJSON();
        retParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getChbtUser", jsonParams);

        return retParams;
    }


    
    /**
     * 고객 챗봇 전체 내역 가져오기 - 확인용
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getChbtUserHsty(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParams = new TelewebJSON();
        retParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getChbtUserHsty", jsonParams);

        return retParams;
    }


    
    /**
     * 현재 고객이 상담중인지 체크
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON chkInputPsblty(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParams = new TelewebJSON();
        retParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "chkInputPsblty", jsonParams);

        return retParams;
    }


    /**
     * 채팅 확장 속성 저장
     *
     * @param  jsonParams
     * @return           objRetParams
     */
    @Override
    public TelewebJSON cuttExpsnAttrReg(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "cuttExpsnAttrReg", jsonParams);

        return objRetParams;
    }

    /**
     * 채팅 확장 속성 수정
     *
     * @param  jsonParams
     * @return           objRetParams
     */
    @Override
    public TelewebJSON cuttExpsnAttrMerge(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "cuttExpsnAttrMerge", jsonParams);

        return objRetParams;
    }
}
