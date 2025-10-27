package kr.co.hkcloud.palette3.chat.setting.app;

import java.sql.SQLException;
import java.util.Calendar;

import kr.co.hkcloud.palette3.core.util.PaletteUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.router.TeletalkRouterWebListener;
import kr.co.hkcloud.palette3.cron.app.CollectJobManageService;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service("chatSettingMessengerConnectManageService")
public class ChatSettingMessengerConnectManageServiceImpl implements ChatSettingMessengerConnectManageService//,ServletContextListener
{

    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO mobjDao;
    private final CollectJobManageService collectJobManageService;
    private final PaletteUtils paletteUtils;

    private final TeletalkRouterWebListener teletalkRouterWebListener;
    private String namespace = "kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingMessengerConnectManageMapper";

    /**
     * ASP채널연동목록 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = true)
    public TelewebJSON selectRtnPageAspCustChannelList(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectRtnPageAspCustChannelList", jsonParams);
    }

    /**
     * ASP고객사 연동채널 상세조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = true)
    public TelewebJSON selectRtnPageAspCustChannelDetail(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = mobjDao.select(namespace, "selectRtnPageAspCustChannelDetail", jsonParams);
        if ("BBS".equals(objRetParams.getString("CHN_CLSF_CD"))) {
            objRetParams.setObject("BBS_SETTING", 0, mobjDao.select(namespace, "selectRtnPageAspCustChannelDetailBbs", jsonParams)
                .getDataObject());
        } else if ("EMAIL".equals(objRetParams.getString("CHN_CLSF_CD"))) {
            objRetParams.setObject("EMAIL_SETTING", 0, mobjDao.select(namespace, "selectRtnPageAspCustChannelDetailEmail", jsonParams)
                .getDataObject());
        }
        return objRetParams;
    }

    /**
     * ASP고객사 연동채널 상세조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = true)
    public TelewebJSON selectRtnKaKaoConnStatus(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectRtnKaKaoConnStatus", jsonParams);
    }

    /**
     * ASP고객사 이용채널 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = true)
    public TelewebJSON selectRtnAspCustChannelInUse(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectRtnAspCustChannelInUse", jsonParams);
    }

    /**
     * ASP고객채널 신규등록
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON insertRtnAspCustChannel(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        String sndrKey = jsonParams.getString("SNDR_KEY");

        String chnClsfCd = jsonParams.getString("CHN_CLSF_CD");
        if (sndrKey.equals("")) {
            sndrKey = Integer.toString(innbCreatCmmnService.createSeqNo("SNDR_KEY"));
            jsonParams.setString("SNDR_KEY", sndrKey);

            String qstnTypeId = Integer.toString(innbCreatCmmnService.createSeqNo("QSTN_TYPE_ID"));
            jsonParams.setString("QSTN_TYPE_ID", qstnTypeId);
            log.info("jsonParams" + jsonParams);

            //채팅 채널 등록
            objRetParams = mobjDao.insert(namespace, "insertRtnAspCustChannel", jsonParams);

            if (jsonParams.getString("ATALK_YN").equals("Y")) {
                //위치정보 전송 템플릿 기본 등록
                String atalkLocalId = Integer.toString(innbCreatCmmnService.createSeqNo("ATALK_ID"));
                jsonParams.setString("ATALK_ID", atalkLocalId);
                jsonParams.setString("LOCAL_CN",
                    "#{CUS_NM} 회원님 안녕하세요.\n위치정보 안내드립니다.\n\n■ 위치명\n#{LOC_NM}\n\n■ 위   치\n#{LOC}\n\n■ 주   소\n#{ADDR}");
                objRetParams = mobjDao.insert(namespace, "insertBaseTmplLocal", jsonParams);
                //연락처정보 전송 템플릿 기본 등록
                String atalkContactId = Integer.toString(innbCreatCmmnService.createSeqNo("ATALK_ID"));
                jsonParams.setString("ATALK_ID", atalkContactId);
                jsonParams.setString("CONTACT_CN",
                    "#{CUS_NM} 회원님 안녕하세요.\n문의하신 연락처정보 안내드립니다.\n\n■ 연락처\n#{TEL}\n\n■ 지역\n#{LOC}\n\n■ 담당부서\n#{OBJ}\n\n■ 담당업무\n#{WORK}");
                objRetParams = mobjDao.insert(namespace, "insertBaseTmplContact", jsonParams);
                //설문조사 링크 전송 템플릿 기본 등록
                String atalkSvyId = Integer.toString(innbCreatCmmnService.createSeqNo("ATALK_ID"));
                jsonParams.setString("ATALK_ID", atalkSvyId);
                jsonParams.setString("SVY_CN",
                    "#{CUS_NM} 회원님 안녕하세요.\n더욱 편리하고 신뢰할수있는 콜센터가 되도록 고객님의 소중한 의견을 들려 주십시오.\n\n※ 설문조사는 서비스 개선을 위한 자료로만 활용됩니다. 감사합니다.\n\n■ 설문조사 참여하기\n#{LINK}");
                objRetParams = mobjDao.insert(namespace, "insertBaseTmplSvy", jsonParams);
            } else {
                if (!"BBS".equals(chnClsfCd) && !"EMAIL".equals(chnClsfCd)) {
                    //채팅 문의유형 등록 
                    objRetParams = mobjDao.insert(namespace, "insertQstnType", jsonParams);
                }
            }
            //사용자 채팅 허용수 등록(INSERT INTO PLT_CHT_CUTT_PM_STNG)
            objRetParams = mobjDao.insert(namespace, "insertChtCuttPm", jsonParams);
            //채팅 고객사 등록(INSERT INTO PLT_CHT_CUTT_CUSTCO)
            objRetParams = mobjDao.insert(namespace, "insertChtCuttCustco", jsonParams);

            //채팅 고객사 custco스키마에 등록(INSERT INTO PLT_CHT_CUTT_CUSTCO)
            objRetParams = mobjDao.insert(namespace, "insertCertCustco", jsonParams);

            if ("BBS".equals(chnClsfCd)) {
                //게시판 채널 등록
                objRetParams = mobjDao.insert(namespace, "insertRtnAspCustChannelBbs", jsonParams);

                //REG_API_ID, ORDR_INQ_API_ID, GDS_INQ_API_ID
                if( StringUtils.isNotEmpty(jsonParams.getString("REG_API_ID"))) {
                    //답변 인터페이스 param초기 세팅
                    jsonParams.setString("LKAG_ID", jsonParams.getString("REG_API_ID"));
                    mobjDao.insert(namespace, "insertBbsParamStng", jsonParams);
                }

                if( StringUtils.isNotEmpty(jsonParams.getString("ORDR_INQ_API_ID"))) {
                    //주문상세 인터페이스 param초기 세팅
                    jsonParams.setString("LKAG_ID", jsonParams.getString("ORDR_INQ_API_ID"));
                    mobjDao.insert(namespace, "insertBbsParamStng", jsonParams);
                }

                if( StringUtils.isNotEmpty(jsonParams.getString("GDS_INQ_API_ID"))) {
                    //상품조회 인터페이스 param초기 세팅
                    jsonParams.setString("LKAG_ID", jsonParams.getString("GDS_INQ_API_ID"));
                    mobjDao.insert(namespace, "insertBbsParamStng", jsonParams);
                }


            } else if ("EMAIL".equals(chnClsfCd)) {
                //이메일 채널 등록
                objRetParams = mobjDao.insert(namespace, "insertRtnAspCustChannelEmail", jsonParams);
            }
            if ("BBS".equals(chnClsfCd) || "EMAIL".equals(chnClsfCd)) {
                //수집_작업_관리 등록
                TelewebJSON collectParams = new TelewebJSON();
//                collectParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                collectParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                collectParams.setString("SNDR_KEY", sndrKey);
                //CLCT_JOB_CD - CHT_CHN_BBS, CHT_CHN_EMAIL
                collectParams.setString("JOB_MNG_ID", Integer.toString(innbCreatCmmnService.createSeqNo("JOB_MNG_ID")));
                collectParams.setString("CLCT_JOB_CD", "CHT_CHN_" + chnClsfCd);
                collectParams.setString("JOB_NM", jsonParams.getString("DSPTCH_PRF_NM"));
                collectParams.setString("STTS_CD", jsonParams.getString("SRVC_MAINT_YN").equals("Y") ? "WAIT" : "STOP");

                collectParams.setString("AFTR_JOB_BGNG_DT", collectJobManageService.selectNextJobStartDateTime(jsonParams.getString("CLCT_RPTT"), Calendar.MINUTE));
                collectParams.setString("LAST_SRCH_DT", paletteUtils.getShortString() );

                collectJobManageService.insertCollectJobManage(collectParams);
            }

            //teletalkRouterWebListener.contextDestroyed(null);
            //teletalkRouterWebListener.stopBetch();
            teletalkRouterWebListener.contextInitialized(null);
        } else {
            //채널정보 수정
            objRetParams = mobjDao.update(namespace, "updateRtnAspCustChannelDetail", jsonParams);
            //custco테이블도 같이 수정해줘야 함
            objRetParams = mobjDao.update(namespace, "updateRtnAspCustChannelCustcoTbl", jsonParams);
            
            //게시판, 이메일 채널의 경우 설정 테이블 업데이트
            if ("BBS".equals(chnClsfCd)) {
                //게시판 채널 등록
                objRetParams = mobjDao.insert(namespace, "updateRtnAspCustChannelDetailBbs", jsonParams);
            } else if ("EMAIL".equals(chnClsfCd)) {
                //이메일 채널 등록
                objRetParams = mobjDao.insert(namespace, "updateRtnAspCustChannelDetailEmail", jsonParams);
            }
            if ("BBS".equals(chnClsfCd) || "EMAIL".equals(chnClsfCd)) {
                //수집_작업_관리 수정
                TelewebJSON collectParams = new TelewebJSON();
                collectParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                collectParams.setString("SNDR_KEY", sndrKey);
                collectParams.setString("JOB_NM", jsonParams.getString("DSPTCH_PRF_NM"));
                collectJobManageService.updateCollectJobManage(collectParams);
            }
        }

        return objRetParams;
    }

    /**
     * ASP고객채널 업데이트
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON updateRtnAspCustChannelDetail(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.update(namespace, "updateRtnAspCustChannelDetail", jsonParams);
    }

    /**
     * ASP고객채널 삭제
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON deleteRtnAspCustChannelItem(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.delete(namespace, "deleteRtnAspCustChannelItem", jsonParams);
    }

    /**
     * ASP비즈채널 콤보데이터 조회
     * 
     * @param 없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = true)
    public TelewebJSON selectRtnAspBizChannelComboData(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectRtnAspBizChannelComboData", jsonParams);
    }

    /**
     * 채널상태 업데이트
     * 
     * @param 없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON chnStatChange(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = mobjDao.update(namespace, "chnStatChange", jsonParams);
        
        String chnClsfCd = jsonParams.getString("CHN_CLSF_CD");
        if ("BBS".equals(chnClsfCd) || "EMAIL".equals(chnClsfCd)) {
            //수집_작업_관리 수정
            TelewebJSON collectParams = new TelewebJSON();
            collectParams.setString("USER_ID", jsonParams.getString("USER_ID"));
            collectParams.setString("SNDR_KEY", jsonParams.getString("SNDR_KEY"));
            collectParams.setString("STTS_CD", jsonParams.getString("SRVC_MAINT_YN").equals("Y") ? "WAIT" : "STOP");
            collectJobManageService.updateCollectJobManage(collectParams);
        }
        return objRetParams;
    }

    /**
     * 채널 챗봇 사용여부 업데이트
     * 
     * @param 없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON chnChbtStatChange(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.update(namespace, "chnChbtStatChange", jsonParams);
    }

    /**
     * 게시판 문의 채널 설정
     * 
     * @param 없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON pstChnSet(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.update(namespace, "pstChnSet", jsonParams);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = true)
    public TelewebJSON selectBbsParamStngList(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectBbsParamStngList", jsonParams);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON updateBbsParamStng(TelewebJSON jsonParams) throws TelewebAppException {
        if(StringUtils.isNotEmpty(jsonParams.getString("initAnswrParamYn")) && jsonParams.getString("initAnswrParamYn").equals("Y") ) {
            mobjDao.update(namespace, "updateBbsParamAnswerYnInit", jsonParams);
        }
        return mobjDao.update(namespace, "updateBbsParamStng", jsonParams);
    }

}
