package kr.co.hkcloud.palette3.common.innb.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.exception.FdlException;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service("innbCreatCmmnService")
public class InnbCreatCmmnServiceImpl implements InnbCreatCmmnService {

    @Resource(name = "twbSeqIdGnrService")
    private EgovIdGnrService twbSeqIdGnrService;

    @Resource(name = "readyOffSeqIdGnrService")
    private EgovIdGnrService readyOffSeqIdGnrService;

    @Resource(name = "prohibiteSeqIdGnrService")
    private EgovIdGnrService prohibiteSeqIdGnrService;

    @Resource(name = "sndrKeySeqIdGnrService")
    private EgovIdGnrService sndrKeySeqIdGnrService;

    @Resource(name = "custcoIdSeqIdGnrService")
    private EgovIdGnrService custcoIdSeqIdGnrService;

    @Resource(name = "inqryTypCdSeqIdGnrService")
    private EgovIdGnrService inqryTypCdSeqIdGnrService;

    //    @Resource(name = "orgFileSeqIdGnrService")
    //    private EgovIdGnrService orgFileSeqIdGnrService;

    @Resource(name = "orgLongTextSeqIdGnrService")
    private EgovIdGnrService orgLongTextSeqIdGnrService;

    @Resource(name = "orgSmsMngSeqIdGnrService")
    private EgovIdGnrService orgSmsMngSeqIdGnrService;

    @Resource(name = "orgNtcnTalkTmplMngSeqIdGnrService")
    private EgovIdGnrService orgNtcnTalkTmplMngSeqIdGnrService;

    @Resource(name = "orgNtcnTalkHistSeqIdGnrService")
    private EgovIdGnrService orgNtcnTalkHistSeqIdGnrService;

    @Resource(name = "smsSendHistSeqIdGnrService")
    private EgovIdGnrService smsSendHistSeqIdGnrService;

    @Resource(name = "pltPhnQAEvalShtSeqIdGnrService")
    private EgovIdGnrService pltPhnQAEvalShtSeqIdGnrService;

    @Resource(name = "chtQAEvalRstSeqIdGnrService")
    private EgovIdGnrService chtQAEvalRstSeqIdGnrService;

    @Resource(name = "chtQAEvalShtSeqIdGnrService")
    private EgovIdGnrService chtQAEvalShtSeqIdGnrService;

    @Resource(name = "chtQAEvalSeqIdGnrService")
    private EgovIdGnrService chtQAEvalSeqIdGnrService;

    @Resource(name = "pltPhnCnslSeqIdGnrService")
    private EgovIdGnrService pltPhnCnslSeqIdGnrService;

    @Resource(name = "pltPhnCnslDtlSeqIdGnrService")
    private EgovIdGnrService pltPhnCnslDtlSeqIdGnrService;

    @Resource(name = "pltPhnClbKSeqIdGnrService")
    private EgovIdGnrService pltPhnClbKSeqIdGnrService;

    @Resource(name = "pltPhnCnslPrtHstSeqIdGnrService")
    private EgovIdGnrService pltPhnCnslPrtHstSeqIdGnrService;

    @Resource(name = "pltPhnCnslHstChngSeqIdGnrService")
    private EgovIdGnrService pltPhnCnslHstChngSeqIdGnrService;

    @Resource(name = "pltPhnCnslRsbSeqIdGnrService")
    private EgovIdGnrService pltPhnCnslRsbSeqIdGnrService;

    @Resource(name = "pltPhnEmailSndHstSeqIdGnrService")
    private EgovIdGnrService pltPhnEmailSndHstSeqIdGnrService;

    @Resource(name = "pltCnslUnityHstService")
    private EgovIdGnrService pltCnslUnityHstService;

    @Resource(name = "loginLogIdGnrService")
    private EgovIdGnrService loginLogIdGnrService;

    @Resource(name = "phoneQaEvlGnrService")
    private EgovIdGnrService phoneQaEvlGnrService;

    @Resource(name = "phoneQaQsGnrService")
    private EgovIdGnrService phoneQaQsGnrService;

    @Resource(name = "phoneQaVeGnrService")
    private EgovIdGnrService phoneQaVeGnrService;

    @Resource(name = "phoneQaGnrService")
    private EgovIdGnrService phoneQaGnrService;

    @Resource(name = "phoneQaDataGnrService")
    private EgovIdGnrService phoneQaDataGnrService;

    @Resource(name = "phoneQaDataRstGnrService")
    private EgovIdGnrService phoneQaDataRstGnrService;

    @Resource(name = "phoneLmEvaGnrService")
    private EgovIdGnrService phoneLmEvaGnrService;

    @Resource(name = "phoneLmQsGnrService")
    private EgovIdGnrService phoneLmQsGnrService;

    @Resource(name = "phoneLmVeGnrService")
    private EgovIdGnrService phoneLmVeGnrService;

    @Resource(name = "phoneLmEvaRstGnrService")
    private EgovIdGnrService phoneLmEvaRstGnrService;

    @Resource(name = "phoneLmGnrService")
    private EgovIdGnrService phoneLmGnrService;

    @Resource(name = "phoneLmDataGnrService")
    private EgovIdGnrService phoneLmDataGnrService;

    @Resource(name = "phoneLmDataRstGnrService")
    private EgovIdGnrService phoneLmDataRstGnrService;

    @Resource(name = "notificationTalkService")
    private EgovIdGnrService notificationTalkService;

    @Resource(name = "PhoneSmsManageService")
    private EgovIdGnrService PhoneSmsManageService;

    @Resource(name = "IdGnrService")
    private EgovIdGnrService idGnrService;

    @Resource(name = "OgnzIdGnrService")
    private EgovIdGnrService ognzIdGnrService;

    @Resource(name = "CuttTypeIdGnrService")
    private EgovIdGnrService cuttTypeIdGnrService;

    @Resource(name = "PstIdGnrService")
    private EgovIdGnrService pstIdGnrService;

    @Resource(name = "PhnIpExtIdGnrService")
    private EgovIdGnrService phnIpExtIdGnrService;

    @Resource(name = "VocRcptIdGnrService")
    private EgovIdGnrService vocRcptIdGnrService;

    @Resource(name = "PhnCuttIdGnrService")
    private EgovIdGnrService phnCuttIdGnrService;

    @Resource(name = "PhnCuttChgHistIdGnrService")
    private EgovIdGnrService phnCuttChgHistIdGnrService;

    @Resource(name = "PhnCuttRsvtIdGnrService")
    private EgovIdGnrService phnCuttRsvtIdGnrService;

    @Resource(name = "CuttItgrtHistIdGnrService")
    private EgovIdGnrService cuttItgrtHistIdGnrService;

    @Resource(name = "PhnDsptchHistIdGnrService")
    private EgovIdGnrService phnDsptchHistIdGnrService;

    @Resource(name = "CustTelNoIdGnrService")
    private EgovIdGnrService custTelNoIdGnrService;

    @Resource(name = "CustIdGnrService")
    private EgovIdGnrService custIdGnrService;

    @Resource(name = "CustChgHstryIdGnrService")
    private EgovIdGnrService custChgHstryIdGnrService;

    @Resource(name = "CustItgrtHstryIdGnrService")
    private EgovIdGnrService custItgrtHstryIdGnrService;

    @Resource(name = "CustAgreHstryIdGnrService")
    private EgovIdGnrService custAgreHstryIdGnrService;

    @Resource(name = "BbsIdGnrService")
    private EgovIdGnrService bbsIdGnrService;

    @Resource(name = "SrvyIdGnrService")
    private EgovIdGnrService srvyIdGnrService;
    @Resource(name = "SrvyQitemGroupIdGnrService")
    private EgovIdGnrService srvyQitemGroupIdGnrService;
    @Resource(name = "SrvyQitemIdGnrService")
    private EgovIdGnrService srvyQitemIdGnrService;
    @Resource(name = "QitemChcIdGnrService")
    private EgovIdGnrService qitemChcIdGnrService;
    @Resource(name = "SrvyTrgtIdGnrService")
    private EgovIdGnrService srvyTrgtIdGnrService;

    @Resource(name = "ObdCpiIdGnrService")
    private EgovIdGnrService obdCpiIdGnrService;
    @Resource(name = "ObdCpiAltmntHstryGnrService")
    private EgovIdGnrService obdCpiAltmntHstryGnrService;
    @Resource(name = "ObdCpiCuslGnrService")
    private EgovIdGnrService obdCpiCuslGnrService;
    @Resource(name = "ObdCpiCustGnrService")
    private EgovIdGnrService obdCpiCustGnrService;
    @Resource(name = "ObdCpiCustDtlGnrService")
    private EgovIdGnrService obdCpiCustDtlGnrService;

    @Resource(name = "MenuIdGnrService")
    private EgovIdGnrService menuIdGnrService;

    @Resource(name = "BtnIdGnrService")
    private EgovIdGnrService btnIdGnrService;

    @Resource(name = "QAPlanIdGnrService")
    private EgovIdGnrService qaPlanIdGnrService;

    @Resource(name = "QACyclIdGnrService")
    private EgovIdGnrService qaCyclIdGnrService;

    @Resource(name = "QAQltylIdGnrService")
    private EgovIdGnrService qaQltylIdGnrService;

    @Resource(name = "QAQltyClsfIdGnrService")
    private EgovIdGnrService qaQltyClsfIdGnrService;

    @Resource(name = "QAEvlArtclIdGnrService")
    private EgovIdGnrService qaEvlArtclIdGnrService;

    @Resource(name = "QATrgtIdGnrService")
    private EgovIdGnrService qaTrgtIdGnrService;

    @Resource(name = "SysMsgIdGnrService")
    private EgovIdGnrService sysMsgIdGnrService;

    @Resource(name = "QstnTypeIdGnrService")
    private EgovIdGnrService qstnTypeIdGnrService;

    @Resource(name = "ScheduleGetIdGnrService")
    private EgovIdGnrService scheduleGetIdGnrService;

    @Resource(name = "SndrKeyGnrService")
    private EgovIdGnrService sndrKeyGnrService;

    @Resource(name = "FbdwdIdGnrService")
    private EgovIdGnrService fbdwdIdGnrService;

    @Resource(name = "msgIdGnrService")
    private EgovIdGnrService msgIdGnrService;

    @Resource(name = "msgGroupIdGnrService")
    private EgovIdGnrService msgGroupIdGnrService;

    @Resource(name = "UserIdGnrService")
    private EgovIdGnrService UserIdGnrService;

    @Resource(name = "PstnIdGnrService")
    private EgovIdGnrService pstnIdGnrService;

    @Resource(name = "HldyIdGnrService")
    private EgovIdGnrService hldyIdGnrService;

    @Resource(name = "CtcIdGnrService")
    private EgovIdGnrService ctcIdGnrService;

    @Resource(name = "ChtCuttIdGnrService")
    private EgovIdGnrService chtCuttIdGnrService;

    @Resource(name = "ChtCuttDtlIdGnrService")
    private EgovIdGnrService chtCuttDtlIdGnrService;

    @Resource(name = "PswdLogIdGnrService")
    private EgovIdGnrService PswdLogIdGnrService;

    @Resource(name = "MtsHstryIdGnrService")
    private EgovIdGnrService mtsHstryIdGnrService;

    @Resource(name = "ChtRdyIdGnrService")
    private EgovIdGnrService chtRdyIdGnrService;

    @Resource(name = "ChtCuttHstryIdGnrService")
    private EgovIdGnrService chtCuttHstryIdGnrService;

    @Resource(name = "KmsTmplIdGnrService")
    private EgovIdGnrService kmsTmplIdGnrService;

    @Resource(name = "KmsClsfIdGnrService")
    private EgovIdGnrService kmsClsfIdGnrService;

    @Resource(name = "KmsClsfAuthrtIdGnrService")
    private EgovIdGnrService kmsClsfAuthrtIdGnrService;

    @Resource(name = "KmsContsIdGnrService")
    private EgovIdGnrService kmsContsIdGnrService;

    @Resource(name = "KmsContsRvwHstryIdGnrService")
    private EgovIdGnrService kmsContsRvwHstryIdGnrService;

    @Resource(name = "ChtRdyHstryIdGnrService")
    private EgovIdGnrService chtRdyHstryIdGnrService;

    @Resource(name = "ChtUserHstryIdGnrService")
    private EgovIdGnrService chtUserHstryIdGnrService;

    @Resource(name = "KmsGuideIdGnrService")
    private EgovIdGnrService kmsGuideIdGnrService;

    @Resource(name = "KmsGuideCuttIdGnrService")
    private EgovIdGnrService kmsGuideCuttIdGnrService;

    @Resource(name = "KmsKeywdIdGnrService")
    private EgovIdGnrService kmsKeywdIdGnrService;

    @Resource(name = "KmsRelLnkIdGnrService")
    private EgovIdGnrService KmsRelLnkIdGnrService;

    @Resource(name = "tmplMngeTmplClsfIdGnrService")
    private EgovIdGnrService tmplMngeTmplClsfIdGnrService;

    @Resource(name = "tmplMngeSmsTmplIdGnrService")
    private EgovIdGnrService tmplMngeSmsTmplIdGnrService;

    @Resource(name = "pltPhnClbkIdGnrService")
    private EgovIdGnrService pltPhnClbkIdGnrService;

    @Resource(name = "tmplMngeAtalkTmplIdGnrService")
    private EgovIdGnrService tmplMngeAtalkTmplIdGnrService;

    @Resource(name = "CertCustcoIdGnrService")
    private EgovIdGnrService certCustcoIdGnrService;

    @Resource(name = "CertUserIdGnrService")
    private EgovIdGnrService certUserIdGnrService;

    @Resource(name = "CertCustcoDsptchNoIdGnrService")
    private EgovIdGnrService certCustcoDsptchNoIdGnrService;

    @Resource(name = "CustcoDsptchNoIdGnrService")
    private EgovIdGnrService custcoDsptchNoIdGnrService;

    @Resource(name = "PstQstnChnIdGnrService")
    private EgovIdGnrService pstQstnChnIdGnrService;

    @Resource(name = "MtsSndngHstryGroupIdGnrService")
    private EgovIdGnrService mtsSndngHstryGroupIdGnrService;

    @Resource(name = "ShortKeyIdGnrService")
    private EgovIdGnrService shortKeyIdGnrService;

    @Resource(name = "CertSmsHstryIdGnrService")
    private EgovIdGnrService certSmsHstryIdGnrService;
    
    @Resource(name = "ChtChttEmlIdGnrService")
    private EgovIdGnrService chtChttEmlIdGnrService;
    
    @Resource(name = "EmlSndngIdGnrService")
    private EgovIdGnrService emlSndngIdGnrService;
    
    @Resource(name = "ClctJobMngIdGnrService")
    private EgovIdGnrService clctJobMngIdGnrService;
    
    @Resource(name = "SrvyClsfIdGnrService")
    private EgovIdGnrService srvyClsfIdGnrService;

    @Resource(name = "LayoutIdGnrService")
    private EgovIdGnrService layoutIdGnrService;

    @Resource(name = "LkagMstIdGnrService")
    private EgovIdGnrService lkagMstIdGnrService;

    @Resource(name = "LkagParamArtclIdGnrService")
    private EgovIdGnrService lkagParamArtclIdGnrService;

    @Resource(name = "LkagRspnsArtclIdGnrService")
    private EgovIdGnrService lkagRspnsArtclIdGnrService;


    @Resource(name = "TabIdGnrService")
    private EgovIdGnrService tabIdGnrService;

    @Resource(name = "LayoutListIdGnrService")
    private EgovIdGnrService layoutListIdGnrService;

    @Resource(name = "LayoutListGroupIdGnrService")
    private EgovIdGnrService layoutListGroupIdGnrService;

    @Resource(name = "LayoutListGroupDtlIdGnrService")
    private EgovIdGnrService layoutListGroupDtlIdGnrService;

    @Resource(name = "LayoutListDwnGroupIdGnrService")
    private EgovIdGnrService layoutListDwnGroupIdGnrService;

    @Resource(name = "LayoutListDwnGroupDtlIdGnrService")
    private EgovIdGnrService layoutListDwnGroupDtlIdGnrService;

    @Resource(name = "LkagCertCustcoIdGnrService")
    private EgovIdGnrService lkagCertCustcoIdGnrService;

    @Resource(name = "LkagCertCustcoHeadIdGnrService")
    private EgovIdGnrService lkagCertCustcoHeadIdGnrService;

    @Resource(name = "LkagMngIdGnrService")
    private EgovIdGnrService lkagMngIdGnrService;
    
    @Resource(name = "SrvySttsHstryIdGnrService")
    private EgovIdGnrService srvySttsHstryIdGnrService;
    
    @Resource(name = "SrvyExlTrgtIdGnrService")
    private EgovIdGnrService srvyExlTrgtIdGnrService;

    @Resource(name = "ChtCuttBbsIdGnrService")
    private EgovIdGnrService chtCuttBbsIdGnrService;

    @Resource(name = "ChtCuttBbsAnswrIdGnrService")
    private EgovIdGnrService chtCuttBbsAnswrIdGnrService;

    @Resource(name = "RsvtIdGnrService")
    private EgovIdGnrService rsvtIdGnrService;
    
    @Resource(name = "RsvtCuttGuideIdGnrService")
    private EgovIdGnrService rsvtCuttGuideIdGnrService;

    @Resource(name = "VsrRsvtIdGnrService")
    private EgovIdGnrService vsrRsvtIdGnrService;
    
    @Resource(name = "VsrRsvtHstryIdGnrService")
    private EgovIdGnrService vsrRsvtHstryIdGnrService;

    @Resource(name = "TrnsfCuttIdGnrService")
    private EgovIdGnrService trnsfCuttIdGnrService;

    @Resource(name = "CuttTrnsfIdGnrService")
    private EgovIdGnrService cuttTrnsfIdGnrService;

    @Resource(name = "CuttTrnsfHstryIdGnrService")
    private EgovIdGnrService cuttTrnsfHstryIdGnrService;

    @Resource(name = "CmntIdGnrService")
    private EgovIdGnrService cmntIdGnrService;

    @Resource(name = "CuttTrnsfAltmntHstryIdGnrService")
    private EgovIdGnrService cuttTrnsfAltmntHstryIdGnrService;


    //    게시판 문의 관련
    //    @Resource(name = "PstQstnCuttIdGnrService")
    //    private EgovIdGnrService pstQstnCuttIdGnrService;

    /**
     * 데이터베이스 시퀀스오브젝트와 업무구분을 이용하여 유니크한 키를 생성하여 반환한다.
     * 
     * method에 synchronized 제거함 - liy
     * 
     * 
     * @param strBizCase 업무구분(대문자3) : 예>TWB(텔레웹업무구분자)
     * @return YYYYMMDDHHMISSMSC + TWB + 00000
     * @throws TelewebAppException
     * @author MPC R&D Team
     */
    @Override
    @Transactional
    public String getSeqNo(TelewebJSON jsonParams, String strBizCase) throws TelewebAppException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS", java.util.Locale.KOREA);
        String strSeqKey = formatter.format(new Date()) + strBizCase;
        String seq = "";

        seq = RandomStringUtils.randomAlphanumeric(5);

        strSeqKey = strSeqKey + seq;

        return strSeqKey;

        /*
         * try {
         * 
         * //유니크한 키를 생성한다. if(strBizCase.equals("WRD")) { seq = Optional.ofNullable(prohibiteSeqIdGnrService.getNextStringId()).orElse(""); } else if(strBizCase.equals("INQRY")) { seq =
         * Optional.ofNullable(inqryTypCdSeqIdGnrService.getNextStringId()).orElse(""); } else { seq = Optional.ofNullable(twbSeqIdGnrService.getNextStringId()).orElse(""); }
         * 
         * strSeqKey = strSeqKey + seq; return strSeqKey;
         * 
         * } catch(FdlException e) { throw new TelewebAppException(e.getLocalizedMessage()); }
         */
    }

    /**
     * 데이터베이스 시퀀스오브젝트와 업무구분을 이용하여 유니크한 키를 생성하여 반환한다. method에 synchronized 제거함 - liy
     * 
     * @param strBizCase 업무구분(대문자3) : 예>TWB(텔레웹업무구분자)
     * @return YYYYMMDDHHMISSMSC + TWB + 00000
     * @throws TelewebAppException
     * @author MPC R&D Team
     */
    @Override
    @Transactional
    public String getSeqNo(String tableName) throws TelewebAppException {
        String seq = "";

        try {

            //유니크한 키를 생성한다.
            if (tableName == null)
                throw new FdlException();

            if (tableName.equals("TWB_SEQ01")) {
                seq = Optional.ofNullable(twbSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("TWB_SEQ_TALK_READY_OFF")) {
                seq = Optional.ofNullable(readyOffSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("TWB_SEQ_PROHIBITE")) {
                seq = Optional.ofNullable(prohibiteSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("TWB_SEQ_SNDR_KEY")) {
                seq = Optional.ofNullable(sndrKeySeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("TWB_SEQ_CUSTCO_ID")) {
                seq = Optional.ofNullable(custcoIdSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("TWB_SEQ_INQRY_TYP_CD")) {
                seq = Optional.ofNullable(inqryTypCdSeqIdGnrService.getNextStringId()).orElse("");
            }
            //        else if(tableName.equals("TWB_SEQ_FILE")) {
            //            seq = Optional.ofNullable(orgFileSeqIdGnrService.getNextStringId()).orElse("");
            //        }
            else if (tableName.equals("TWB_TALK_CONTACT_ORG_CONT_SEQ")) {
                seq = Optional.ofNullable(orgLongTextSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_SMS_TMPL")) {
                seq = Optional.ofNullable(orgSmsMngSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_NTCN_TALK_TMPL")) {
                seq = Optional.ofNullable(orgNtcnTalkTmplMngSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("NTCN_TALK_SEND_HIST")) {
                seq = Optional.ofNullable(orgNtcnTalkHistSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_SMS_SND_HST")) {
                seq = Optional.ofNullable(smsSendHistSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_QA_EVAL_SHT_SEQ")) {
                seq = Optional.ofNullable(pltPhnQAEvalShtSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_CHT_QA_EVAL_RST_SEQ")) {
                seq = Optional.ofNullable(chtQAEvalRstSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_CHT_QA_EVAL_SHT_SEQ")) {
                seq = Optional.ofNullable(chtQAEvalShtSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_CHT_QA_EVAL_SEQ")) {
                seq = Optional.ofNullable(chtQAEvalSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_CNSL")) {
                seq = Optional.ofNullable(pltPhnCnslSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_CNSL_DTL")) {
                seq = Optional.ofNullable(pltPhnCnslDtlSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_CLBK")) {
                seq = Optional.ofNullable(pltPhnClbKSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_CNSL_RSV")) {
                seq = Optional.ofNullable(pltPhnCnslRsbSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_EML_SND_HST")) {
                seq = Optional.ofNullable(pltPhnEmailSndHstSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_CNSL_PRT_HST")) {
                seq = Optional.ofNullable(pltPhnCnslPrtHstSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("CHNG_HIST_SEQ")) {
                seq = Optional.ofNullable(pltPhnCnslHstChngSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_CNSL_UNITY_HST")) {
                seq = Optional.ofNullable(pltCnslUnityHstService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_QA_EVA")) {
                seq = Optional.ofNullable(phoneQaEvlGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_QA_QS")) {
                seq = Optional.ofNullable(phoneQaQsGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_QA_VE")) {
                seq = Optional.ofNullable(phoneQaVeGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_QA")) {
                seq = Optional.ofNullable(phoneQaGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_QA_DATA")) {
                seq = Optional.ofNullable(phoneQaDataGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_QA_DATA_RST")) {
                seq = Optional.ofNullable(phoneQaDataRstGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_LM_EVA")) {
                seq = Optional.ofNullable(phoneLmEvaGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_LM_QS")) {
                seq = Optional.ofNullable(phoneLmQsGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_LM_VE")) {
                seq = Optional.ofNullable(phoneLmVeGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_LM_EVA_RST")) {
                seq = Optional.ofNullable(phoneLmEvaRstGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_LM")) {
                seq = Optional.ofNullable(phoneLmGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_LM_DATA")) {
                seq = Optional.ofNullable(phoneLmDataGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_LM_DATA_RST")) {
                seq = Optional.ofNullable(phoneLmDataRstGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("MTS_ATALK_MSG")) {
                seq = Optional.ofNullable(notificationTalkService.getNextStringId()).orElse("");
            } else if (tableName.equals("MTS_ATALK_MSG_BAT")) {
                seq = Optional.ofNullable(notificationTalkService.getNextStringId()).orElse("");
            } else if (tableName.equals("MTS_SMS_MSG")) {
                seq = Optional.ofNullable(PhoneSmsManageService.getNextStringId()).orElse("");
            } else if (tableName.equals("MTS_MMS_MSG")) {
                seq = Optional.ofNullable(PhoneSmsManageService.getNextStringId()).orElse("");
            } else if (tableName.equals("MTS_SMS_MSG_BAT")) {
                seq = Optional.ofNullable(PhoneSmsManageService.getNextStringId()).orElse("");
            } else if (tableName.equals("MTS_MMS_MSG_BAT")) {
                seq = Optional.ofNullable(PhoneSmsManageService.getNextStringId()).orElse("");
            }

            return seq;

        } catch (FdlException e) {
            throw new TelewebAppException(e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int createSeqNo(String tableName) throws TelewebAppException {
        int seq = 0;

        try {

            //유니크한 키를 생성한다.
            if (tableName == null)
                throw new FdlException();

            if (tableName.equals("ATTR_ID")) {//확장속성 (테이블 : PLT_EXPSN_ATTR)
                seq = Integer.parseInt(Optional.ofNullable(idGnrService.getNextStringId()).orElse(""));
            }
            if (tableName.equals("CUTT_TYPE_ID")) {//상담유형 (테이블 : PLT_CUTT_TYPE)
                seq = Integer.parseInt(Optional.ofNullable(cuttTypeIdGnrService.getNextStringId()).orElse(""));
            }
            if (tableName.equals("DEPT_ID")) {//조직 (테이블 : PLT_OGNZ)
                seq = Integer.parseInt(Optional.ofNullable(ognzIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("PHN_IP_EXT_ID")) {//전화_IP_내선 (테이블 : PLT_PHN_IP_EXT)
                seq = Integer.parseInt(Optional.ofNullable(phnIpExtIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("VOC_RCPT_ID")) {//VOC 접수 (테이블 : PLT_VOC_RCPT)
                seq = Integer.parseInt(Optional.ofNullable(vocRcptIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("PHN_CUTT_ID")) {//전화 상담 (테이블 : PLT_PHN_CUTT)
                seq = Integer.parseInt(Optional.ofNullable(phnCuttIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CHG_HSTRY_ID")) {//전화 상담 변경 이력 (테이블 : PLT_PHN_CUTT_CHG_HSTRY)
                seq = Integer.parseInt(Optional.ofNullable(phnCuttChgHistIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CUTT_RSVT_ID")) {//전화 상담 예약 (테이블 : PLT_PHN_CUTT_RSVT)
                seq = Integer.parseInt(Optional.ofNullable(phnCuttRsvtIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("ITGRT_HSTRY_ID")) {//상담 통합 이력 (테이블 : PLT_CUTT_ITGRT_HSTRY)
                seq = Integer.parseInt(Optional.ofNullable(cuttItgrtHistIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("DSPTCH_HSTRY_ID")) {//전화 발신 이력 (테이블 : PLT_PHN_DSPTCH_HSTRY)
                seq = Integer.parseInt(Optional.ofNullable(phnDsptchHistIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CUST_TELNO_ID")) {//고객 전화번호 (테이블 : PLT_CUST_TELNO)
                seq = Integer.parseInt(Optional.ofNullable(custTelNoIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CUST_ID")) {//고객 (테이블 : PLT_CUST)
                seq = Integer.parseInt(Optional.ofNullable(custIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CUST_CHG_HSTRY_ID")) {//고객 정보 변경 이력 (테이블 : PLT_CUST_INFO_CHG_HSTRY)
                seq = Integer.parseInt(Optional.ofNullable(custChgHstryIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CUST_ITGRT_HSTRY_ID")) {//고객 통합 이력 (테이블 : PLT_CUST_ITGRT_HSTRY)
                seq = Integer.parseInt(Optional.ofNullable(custItgrtHstryIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CUST_AGRE_HSTRY_ID")) {//고객 동의 이력 (테이블 : PLT_CUST_AGRE_HSTRY)
                seq = Integer.parseInt(Optional.ofNullable(custAgreHstryIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("BBS_ID")) {//게시판 (테이블 : PLT_BBS)
                seq = Integer.parseInt(Optional.ofNullable(bbsIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("PST_ID")) {//게시글 (테이블 : PLT_PST)
                seq = Integer.parseInt(Optional.ofNullable(pstIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("SRVY_ID")) {//설문조사계획 (테이블 : PLT_SRVY)
                seq = Integer.parseInt(Optional.ofNullable(srvyIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("SRVY_QITEM_GROUP_ID")) {//설문문항그룹 (테이블 : PLT_SRVY_QITEM_GROUP)
                seq = Integer.parseInt(Optional.ofNullable(srvyQitemGroupIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("SRVY_QITEM_ID")) {//설문_문항 (테이블 : PLT_SRVY_QITEM)
                seq = Integer.parseInt(Optional.ofNullable(srvyQitemIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("QITEM_CHC_ID")) {//설문_문항_선택 (테이블 : PLT_SRVY_QITEM_CHC)
                seq = Integer.parseInt(Optional.ofNullable(qitemChcIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("SRVY_TRGT_ID")) {//설문_대상_ID (테이블 : PLT_SRVY_TRGT)
                seq = Integer.parseInt(Optional.ofNullable(srvyTrgtIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CPI_ID")) {//캠페인계획 (테이블 : PLT_OBD_CPI)
                seq = Integer.parseInt(Optional.ofNullable(obdCpiIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("ALTMNT_HSTRY")) {//캠페인_배정_이력 (테이블 : PLT_OBD_CPI_ALTMNT_HSTRY)
                seq = Integer.parseInt(Optional.ofNullable(obdCpiAltmntHstryGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CUSL_ID")) {//캠페인_상담사 (테이블 : PLT_OBD_CPI_CUSL)
                seq = Integer.parseInt(Optional.ofNullable(obdCpiCuslGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("OBD_CUST_ID")) {//캠페인_고객 (테이블 : PLT_OBD_CPI_CUST)
                seq = Integer.parseInt(Optional.ofNullable(obdCpiCustGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("ATTR_ID")) {//캠페인_고객_상세 (테이블 : PLT_OBD_CPI_CUST_DTL)
                seq = Integer.parseInt(Optional.ofNullable(obdCpiCustDtlGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("MENU_ID")) {//메뉴 (테이블 : PLT_MENU)
                seq = Integer.parseInt(Optional.ofNullable(menuIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("BTN_ID")) {//메뉴 (테이블 : PLT_MENU)
                seq = Integer.parseInt(Optional.ofNullable(btnIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("QA_PLAN_ID")) {//설문조사계획 (테이블 : PLT_QA_PLAN)
                seq = Integer.parseInt(Optional.ofNullable(qaPlanIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("QA_CYCL_ID")) {//설문조사차수 (테이블 : PLT_QA_CYCL)
                seq = Integer.parseInt(Optional.ofNullable(qaCyclIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("QA_QLTY_ID")) {//설문조사차수 (테이블 : PLT_QA_CYCL)
                seq = Integer.parseInt(Optional.ofNullable(qaQltylIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("QLTY_CLSF_ID")) {//설문조사차수 (테이블 : PLT_QA_CYCL)
                seq = Integer.parseInt(Optional.ofNullable(qaQltyClsfIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("EVL_ARTCL_ID")) {//설문조사차수 (테이블 : PLT_QA_CYCL)
                seq = Integer.parseInt(Optional.ofNullable(qaEvlArtclIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("QA_TRGT_ID")) {//설문조사차수 (테이블 : PLT_QA_CYCL)
                seq = Integer.parseInt(Optional.ofNullable(qaTrgtIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("SYS_MSG_ID")) {//메뉴 (테이블 : PLT_MENU)
                seq = Integer.parseInt(Optional.ofNullable(sysMsgIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("QSTN_TYPE_ID")) {//메뉴 (테이블 : PLT_MENU)
                seq = Integer.parseInt(Optional.ofNullable(qstnTypeIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("SCHDL_ID")) {//스케쥴 (테이블 : PLT_SCHDL)
                seq = Integer.parseInt(Optional.ofNullable(scheduleGetIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("SNDR_KEY")) {//스케쥴 (테이블 : PLT_SCHDL)
                seq = Integer.parseInt(Optional.ofNullable(sndrKeyGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("FBDWD_ID")) {//스케쥴 (테이블 : PLT_SCHDL)
                seq = Integer.parseInt(Optional.ofNullable(fbdwdIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("MSG_ID")) {//스케쥴 (테이블 : PLT_MSG)
                seq = Integer.parseInt(Optional.ofNullable(msgIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("MSG_GROUP_ID")) {//스케쥴 (테이블 : PLT_MSG)
                seq = Integer.parseInt(Optional.ofNullable(msgGroupIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("USER_ID")) {//사용자관리 (테이블 : PLT_USER)
                seq = Integer.parseInt(Optional.ofNullable(UserIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("PSTN_ID")) {//게시글 (테이블 : PLT_PSTN)
                seq = Integer.parseInt(Optional.ofNullable(pstnIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("HLDY_ID")) {//스케쥴 (테이블 : PLT_MSG)
                seq = Integer.parseInt(Optional.ofNullable(hldyIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CTC_ID")) {//스케쥴 (테이블 : PLT_CTC)
                seq = Integer.parseInt(Optional.ofNullable(ctcIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CHT_CUTT_ID")) {//스케쥴 (테이블 : PLT_CTC)
                seq = Integer.parseInt(Optional.ofNullable(chtCuttIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CHT_CUTT_DTL_ID")) {//스케쥴 (테이블 : PLT_CTC)
                seq = Integer.parseInt(Optional.ofNullable(chtCuttDtlIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("PSWD_LOG_ID")) {//비밀번호변경로그 (테이블 : PLT_USER_PSWD_LOG)
                seq = Integer.parseInt(Optional.ofNullable(PswdLogIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("MTS_SNDNG_HSTRY_ID")) {//MTS발송이력 (테이블 : MTS_SNDNG_HSTRY)
                seq = Integer.parseInt(Optional.ofNullable(mtsHstryIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CHT_RDY_ID")) {//스케쥴 (테이블 : PLT_CTC)
                seq = Integer.parseInt(Optional.ofNullable(chtRdyIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CHT_CUTT_HSTRY_ID")) {//스케쥴 (테이블 : PLT_CTC)
                seq = Integer.parseInt(Optional.ofNullable(chtCuttHstryIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("KMS_TMPL_ID")) {//스케쥴 (테이블 : PLT_CTC)
                seq = Integer.parseInt(Optional.ofNullable(kmsTmplIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("KMS_CLSF_ID")) {// 지식분류 (테이블 : PLT_KMS_CLSF)
                seq = Integer.parseInt(Optional.ofNullable(kmsClsfIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("KMS_CLSF_AUTHRT_ID")) {// 지식분류권한 (테이블 : PLT_KMS_CLSF_AUTHRT)
                seq = Integer.parseInt(Optional.ofNullable(kmsClsfAuthrtIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("KMS_CONTS_ID")) {// 지식컨텐츠 (테이블 : PLT_KMS_CONTS)
                seq = Integer.parseInt(Optional.ofNullable(kmsContsIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("KMS_CONTS_RVW_HSTRY_ID")) {// 지식컨텐츠 검토이력 (테이블 : PLT_KMS_CONTS_RVW_HSTRY)
                seq = Integer.parseInt(Optional.ofNullable(kmsContsRvwHstryIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CHT_RDY_HSTRY_ID")) {//스케쥴 (테이블 : PLT_CTC)
                seq = Integer.parseInt(Optional.ofNullable(chtRdyHstryIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CHT_USER_HSTRY_ID")) {//스케쥴 (테이블 : PLT_CTC)
                seq = Integer.parseInt(Optional.ofNullable(chtUserHstryIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("KMS_GUIDE_ID")) {//스케쥴 (테이블 : PLT_CTC)
                seq = Integer.parseInt(Optional.ofNullable(kmsGuideIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("KMS_GUIDE_CUTT_ID")) {//스케쥴 (테이블 : PLT_CTC)
                seq = Integer.parseInt(Optional.ofNullable(kmsGuideCuttIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("KMS_KEYWD_ID")) {//스케쥴 (테이블 : PLT_CTC)
                seq = Integer.parseInt(Optional.ofNullable(kmsKeywdIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("KMS_REL_LNK_ID")) {//스케쥴 (테이블 : PLT_CTC)
                seq = Integer.parseInt(Optional.ofNullable(KmsRelLnkIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("TMPL_CLSF_ID")) {// 문자템플릿유형 (테이블 : PLT_TMPL_CLSF)
                seq = Integer.parseInt(Optional.ofNullable(tmplMngeTmplClsfIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("SMS_TMPL_ID")) {// 문자템플릿 (테이블 : PLT_SMS_TMPL)
                seq = Integer.parseInt(Optional.ofNullable(tmplMngeSmsTmplIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("PLT_PHN_CLBK_ID")) {// 문자템플릿 (테이블 : PLT_PHN_CLBK)
                seq = Integer.parseInt(Optional.ofNullable(pltPhnClbkIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("ATALK_ID")) {// 알림톡템플릿 (테이블 : PLT_ATALK)
                seq = Integer.parseInt(Optional.ofNullable(tmplMngeAtalkTmplIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CERT_CUSTCO_ID")) {//인증 고객사 (테이블 : CUSTCO.PLT_CERT_CUSTCO)
                seq = Integer.parseInt(Optional.ofNullable(certCustcoIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CERT_USER_ID")) {//인증 사용자 (테이블 : CUSTCO.PLT_CERT_USER)
                seq = Integer.parseInt(Optional.ofNullable(certUserIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CERT_CUSTCO_DSPTCH_NO_ID")) {//인증_고객사_발신_번호 (테이블 : CUSTCO.PLT_CERT_CUSTCO_DSPTCH_NO)
                seq = Integer.parseInt(Optional.ofNullable(certCustcoDsptchNoIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CUSTCO_DSPTCH_NO_ID")) {//고객사_발신_번호 (테이블 : PLT_CUSTCO_DSPTCH_NO)
                seq = Integer.parseInt(Optional.ofNullable(custcoDsptchNoIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("PST_QSTN_CHN_ID")) {//고객사_발신_번호 (테이블 : PLT_CHT_PST_QSTN_CHN)
                seq = Integer.parseInt(Optional.ofNullable(pstQstnChnIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("MTS_SNDNG_HSTRY_GROUP_ID")) {//MTS문자 대량발송 그룹 ID
                seq = Integer.parseInt(Optional.ofNullable(mtsSndngHstryGroupIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("SHORT_KEY_ID")) {//단축_키_ID
                seq = Integer.parseInt(Optional.ofNullable(shortKeyIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CERT_SMS_HSTRY_ID")) {//2차_인증_이력_ID
                seq = Integer.parseInt(Optional.ofNullable(certSmsHstryIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CHT_CUTT_EML_ID")) {//채팅_상담_이메일_ID
                seq = Integer.parseInt(Optional.ofNullable(chtChttEmlIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("EML_SNDNG_ID")) {//이메일_발송_ID
                seq = Integer.parseInt(Optional.ofNullable(emlSndngIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("JOB_MNG_ID")) {//작업_관리_ID(테이블 : PLT_CLCT_JOB_MNG)
                seq = Integer.parseInt(Optional.ofNullable(clctJobMngIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("SRVY_CLSF_ID")) {// 설문분류 (테이블 : PLT_SRVY_CLSF)
                seq = Integer.parseInt(Optional.ofNullable(srvyClsfIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("LKAG_MST_ID")) {// 연동마스터 (테이블 : PLT_LKAG_MST)
                seq = Integer.parseInt(Optional.ofNullable(lkagMstIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("PARAM_ARTCL_ID")) {// 연동관리-전문(REQUEST) (테이블 : PLT_LKAG_PARAM_ARTCL)
                seq = Integer.parseInt(Optional.ofNullable(lkagParamArtclIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("RSPNS_ARTCL_ID")) {// 연동관리-전문(REQUEST) (테이블 : PLT_LKAG_RSPNS_ARTCL)
                seq = Integer.parseInt(Optional.ofNullable(lkagRspnsArtclIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CERT_CUSTCO_LKAG_ID")) {// 연동매핑테이블 (테이블 : PLT_CERT_CUSTCO_LKAG)
                seq = Integer.parseInt(Optional.ofNullable(lkagCertCustcoIdGnrService.getNextStringId()).orElse(""));
            }  else if (tableName.equals("LKAG_ID")) {// 연동마스터 (테이블 : PLT_LKAG_MNG)
                seq = Integer.parseInt(Optional.ofNullable(lkagMngIdGnrService.getNextStringId()).orElse(""));
            }  else if (tableName.equals("HEAD_ID")) {// 연동마스터 (테이블 : PLT_LKAG_HEAD)
                seq = Integer.parseInt(Optional.ofNullable(lkagCertCustcoHeadIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("LAYOUT_ID")) {// 레이아웃 (테이블 : PLT_LAYOUT)
                seq = Integer.parseInt(Optional.ofNullable(layoutIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("TAB_ID")) {// 레이아웃 탭 (테이블 : PLT_LAYOUT_TAB)
                seq = Integer.parseInt(Optional.ofNullable(tabIdGnrService.getNextStringId()).orElse(""));
            }else if (tableName.equals("LIST_ID")) {// 레이아웃 탭 (테이블 : PLT_LAYOUT_TAB)
                seq = Integer.parseInt(Optional.ofNullable(layoutListIdGnrService.getNextStringId()).orElse(""));
            }else if (tableName.equals("LIST_GROUP_ID")) {// 레이아웃 탭 (테이블 : PLT_LAYOUT_TAB)
                seq = Integer.parseInt(Optional.ofNullable(layoutListGroupIdGnrService.getNextStringId()).orElse(""));
            }else if (tableName.equals("LIST_GROUP_DTL_ID")) {// 레이아웃 탭 (테이블 : PLT_LAYOUT_TAB)
                seq = Integer.parseInt(Optional.ofNullable(layoutListGroupDtlIdGnrService.getNextStringId()).orElse(""));
            }else if (tableName.equals("LIST_DWN_GROUP_ID")) {// 레이아웃 탭 (테이블 : PLT_LAYOUT_TAB)
                seq = Integer.parseInt(Optional.ofNullable(layoutListDwnGroupIdGnrService.getNextStringId()).orElse(""));
            }else if (tableName.equals("LIST_DWN_GROUP_DTL_ID")) {// 레이아웃 탭 (테이블 : PLT_LAYOUT_TAB)
                seq = Integer.parseInt(Optional.ofNullable(layoutListDwnGroupDtlIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("SRVY_STTS_HSTRY_ID")) { //설문 상태 이력(테이블: PLT_SRVY_STTS_HSTRY)
                seq = Integer.parseInt(Optional.ofNullable(srvySttsHstryIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("SRVY_EXL_TRGT_ID")) { //설문_제외_대상(테이블: PLT_SRVY_EXL_TRGT)
                seq = Integer.parseInt(Optional.ofNullable(srvyExlTrgtIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CHT_CUTT_BBS_ID")) { // (고객문의,상품문의) 채팅_상담_게시판(테이블: PLT_CHT_CUTT_BBS)
                seq = Integer.parseInt(Optional.ofNullable(chtCuttBbsIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CHT_CUTT_BBS_ANSWR_ID")) { // (고객문의,상품문의) 채팅_상담_게시판(테이블: PLT_CHT_CUTT_BBS)
                seq = Integer.parseInt(Optional.ofNullable(chtCuttBbsAnswrIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("RSVT_ID")) { // (상담예약) 상담예약(테이블: PLT_RSVT)
                seq = Integer.parseInt(Optional.ofNullable(rsvtIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("RSVT_CUTT_GUIDE_ID")) { // (상담예약) 상담예약내용(테이블: PLT_RSVT_CUTT_GUIDE)
                seq = Integer.parseInt(Optional.ofNullable(rsvtCuttGuideIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("VST_RSVT_ID")) { // (방문예약) 방문예약(테이블: PLT_VST_RSVT)
            	seq = Integer.parseInt(Optional.ofNullable(vsrRsvtIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("VST_RSVT_HSTRY_ID")) { // (방문예약이력) 방문예약이력(테이블: PLT_VST_RSVT_HSTRY)
            	seq = Integer.parseInt(Optional.ofNullable(vsrRsvtHstryIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("TRNSF_CUTT_ID")) { // (이관 상담) 이관 상담(테이블: PLT_TRNSF_CUTT) custco 스키마에 있음
            	seq = Integer.parseInt(Optional.ofNullable(trnsfCuttIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CUTT_TRNSF_ID")) { // (상담 이관) 상담 이관(테이블: PLT_CUTT_TRNSF)
            	seq = Integer.parseInt(Optional.ofNullable(cuttTrnsfIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CUTT_TRNSF_HSTRY_ID")) { // (상담 이관 이력) 상담 이관 이력(테이블: PLT_CUTT_TRNSF_HSTRY)
            	seq = Integer.parseInt(Optional.ofNullable(cuttTrnsfHstryIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CMNT_ID")) { // 게시글 댓글 ID (테이블: PLT_PST_CMNT)
              seq = Integer.parseInt(Optional.ofNullable(cmntIdGnrService.getNextStringId()).orElse(""));
            } else if (tableName.equals("CUTT_TRNSF_ALTMNT_HSTRY_ID")) {//상담 이관 배정 이력 (테이블 : PLT_CUTT_TRNSF_ALTMNT_HSTRY)
                seq = Integer.parseInt(Optional.ofNullable(cuttTrnsfAltmntHstryIdGnrService.getNextStringId()).orElse(""));
            }

            //            else if(tableName.equals("PST_QSTN_CUTT_ID")) {//게시판문의ID (테이블 : PLT_CHT_PST_QSTN)
            //            	seq = Integer.parseInt(Optional.ofNullable(pstQstnCuttIdGnrService.getNextStringId()).orElse(""));
            //            } //게시판 문의 관련
            return seq;

        } catch (FdlException e) {
            throw new TelewebAppException(e.getLocalizedMessage());
        }
    }

    /**
     * 데이터베이스 시퀀스오브젝트와 업무구분을 이용하여 유니크한 키를 생성하여 반환한다. method에 synchronized 제거함 - liy
     * 
     * @param strBizCase 업무구분(대문자3) : 예>TWB(텔레웹업무구분자)
     * @return YYYYMMDDHHMISSMSC + TWB + 00000
     * @throws TelewebAppException
     * @author MPC R&D Team
     */
    @Transactional
    public String getSeqNo(String tableName, String strBizCase) throws TelewebAppException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS", java.util.Locale.KOREA);
        String strSeqKey = formatter.format(new Date()) + strBizCase;
        String seq = "";
        try {

            //유니크한 키를 생성한다.
            if (tableName == null)
                throw new FdlException();

            if (tableName.equals("TWB_SEQ01")) {
                seq = Optional.ofNullable(twbSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("TWB_SEQ_TALK_READY_OFF")) {
                seq = Optional.ofNullable(readyOffSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("TWB_SEQ_PROHIBITE")) {
                seq = Optional.ofNullable(prohibiteSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("TWB_SEQ_SNDR_KEY")) {
                seq = Optional.ofNullable(sndrKeySeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("TWB_SEQ_CUSTCO_ID")) {
                seq = Optional.ofNullable(custcoIdSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("TWB_SEQ_INQRY_TYP_CD")) {
                seq = Optional.ofNullable(inqryTypCdSeqIdGnrService.getNextStringId()).orElse("");
            }
            //        else if(tableName.equals("TWB_SEQ_FILE")) {
            //            seq = Optional.ofNullable(orgFileSeqIdGnrService.getNextStringId()).orElse("");
            //        }
            else if (tableName.equals("TWB_TALK_CONTACT_ORG_CONT_SEQ")) {
                seq = Optional.ofNullable(orgLongTextSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_SMS_TMPL")) {
                seq = Optional.ofNullable(orgSmsMngSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_NTCN_TALK_TMPL")) {
                seq = Optional.ofNullable(orgNtcnTalkTmplMngSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("NTCN_TALK_SEND_HIST")) {
                seq = Optional.ofNullable(orgNtcnTalkHistSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_SMS_SND_HST")) {
                seq = Optional.ofNullable(smsSendHistSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_QA_EVAL_SHT_SEQ")) {
                seq = Optional.ofNullable(pltPhnQAEvalShtSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_CHT_QA_EVAL_RST_SEQ")) {
                seq = Optional.ofNullable(chtQAEvalRstSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_CHT_QA_EVAL_SHT_SEQ")) {
                seq = Optional.ofNullable(chtQAEvalShtSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_CHT_QA_EVAL_SEQ")) {
                seq = Optional.ofNullable(chtQAEvalSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_CNSL")) {
                seq = Optional.ofNullable(pltPhnCnslSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_CNSL_DTL")) {
                seq = Optional.ofNullable(pltPhnCnslDtlSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_CLBK")) {
                seq = Optional.ofNullable(pltPhnClbKSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_CNSL_RSV")) {
                seq = Optional.ofNullable(pltPhnCnslRsbSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_EML_SND_HST")) {
                seq = Optional.ofNullable(pltPhnEmailSndHstSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_PHN_CNSL_PRT_HST")) {
                seq = Optional.ofNullable(pltPhnCnslPrtHstSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("CHNG_HIST_SEQ")) {
                seq = Optional.ofNullable(pltPhnCnslHstChngSeqIdGnrService.getNextStringId()).orElse("");
            } else if (tableName.equals("PLT_CNSL_UNITY_HST")) {
                seq = Optional.ofNullable(pltCnslUnityHstService.getNextStringId()).orElse("");
            } else if (tableName.equals("MTS_ATALK_MSG")) {
                seq = Optional.ofNullable(notificationTalkService.getNextStringId()).orElse("");
            } else if (tableName.equals("MTS_ATALK_MSG_BAT")) {
                seq = Optional.ofNullable(notificationTalkService.getNextStringId()).orElse("");
            } else if (tableName.equals("MTS_SMS_MSG")) {
                seq = Optional.ofNullable(PhoneSmsManageService.getNextStringId()).orElse("");
            } else if (tableName.equals("MTS_MMS_MSG")) {
                seq = Optional.ofNullable(PhoneSmsManageService.getNextStringId()).orElse("");
            } else if (tableName.equals("MTS_SMS_MSG_BAT")) {
                seq = Optional.ofNullable(PhoneSmsManageService.getNextStringId()).orElse("");
            } else if (tableName.equals("MTS_MMS_MSG_BAT")) {
                seq = Optional.ofNullable(PhoneSmsManageService.getNextStringId()).orElse("");
            }
            return strSeqKey + seq;

        } catch (FdlException e) {
            throw new TelewebAppException(e.getLocalizedMessage());
        }
    }

    /**
     * 로그인 시퀀스
     * 
     * @param tableName
     * @param strBizCase
     * @return
     * @throws TelewebAppException
     */
    @Transactional
    public String getLoginNextStringId() throws TelewebAppException {
        try {
            return loginLogIdGnrService.getNextStringId();
        } catch (FdlException e) {
            throw new TelewebAppException(e.getLocalizedMessage());
        }
    }
}
