package kr.co.hkcloud.palette3.admin.custco.app;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.json.simple.parser.ParseException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import kr.co.hkcloud.palette3.admin.config.FlywayBuilder;
import kr.co.hkcloud.palette3.admin.custco.domain.IpccRollbackDTO;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.app.TwbUserBizService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.security.TeletalkAuthority;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.agent.app.SettingAgentManageService;
import kr.co.hkcloud.palette3.sse.app.SseService;
import kr.co.hkcloud.palette3.sse.message.model.SseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@Slf4j
@RequiredArgsConstructor
@Service("adminCertCustcoManageService")
public class AdminCertCustcoManageServiceImpl implements AdminCertCustcoManageService {

    private final SseService sseService;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;

    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final SettingAgentManageService settingAgentManageService;
    private final TwbUserBizService twbUserBizService;
    private final RestTemplate restTemplate;
    private final FlywayBuilder flywayBuilder;
    //예약된 스키마 ID는 사용 불가 - public, custco, ttalk, chatgpt
    private static List<String> RESERVED_SCHEMA_IDS = Arrays.asList("public", "custco", "ttalk", "chatgpt");
    
    private String namespace = "kr.co.hkcloud.palette3.admin.custco.dao.AdminCertCustcoManageMapper";

    /**
     * 기업고객관리 목록 조회
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectCertCustco(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectCertCustco", mjsonParams);
    }

    /**
     * 기업고객고객관리 - 기업고객 정보 조회
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectCertCustcoInfo(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectCertCustcoInfo", mjsonParams);
    }

    /**
     * 기업고객고객관리 - 서비스 설정 조회
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectServiceSetting(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectServiceSetting", mjsonParams);
    }

    /**
     * 기업고객고객관리 - 서비스 계정 정보 조회
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectServiceAccount(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectServiceAccount", mjsonParams);
    }

    /**
     * 기업고객고객관리 - IPCC 연동 설정 정보 조회
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectIpccSetting(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectIpccSetting", mjsonParams);
    }

    /**
     * 기업고객관리 - 기업고객 정보 등록
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON insertCertCustcoInfo(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //인증 고객사 ID 조회 및 인증 고객사 등록
        mjsonParams.setInt("CERT_CUSTCO_ID", innbCreatCmmnService.createSeqNo("CERT_CUSTCO_ID"));
        objRetParams = mobjDao.insert(namespace, "insertCertCustcoInfo",
            mjsonParams);
        objRetParams.setInt("CERT_CUSTCO_ID", mjsonParams.getInt("CERT_CUSTCO_ID"));

        //		//인증 고객 ID 및 인증 고객 등록
        //		mjsonParams.setInt("CERT_USER_ID", innbCreatCmmnService.createSeqNo("CERT_USER_ID"));
        //		mobjDao.insert(namespace, "insertCertUser", mjsonParams);
        //		
        //		//인증 고객사 고객 등록
        //		mobjDao.insert(namespace, "insertCertCustcoUser", mjsonParams);
        //		
        //		String aspCustKey  = mjsonParams.getString("ASP_CUST_KEY");
        //		if(aspCustKey != null && !"".equals(aspCustKey)) {
        //			MigrateResult result = buildDatabaseSchema(aspCustKey);
        //		}
        return objRetParams;
    }

    /**
     * 기업고객관리 - 기업고객 정보 수정
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON updateCertCustcoInfo(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.update(namespace, "updateCertCustcoInfo", mjsonParams);
    }

    /**
     * 인증_고객사 - 테넌트 고객사 데이터 동기화.
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON syncTenantCustco(TelewebJSON serviceAccountJSON, TelewebJSON certCustcoJSON,
        TelewebJSON serviceSettingJSON) throws TelewebAppException {
        TelewebJSON tenantCustco = mobjDao.select(namespace,
            "selectTenantCustco", serviceAccountJSON);
        certCustcoJSON.setString("CUSTCO_ID", tenantCustco.getString("CUSTCO_ID"));
        certCustcoJSON.setString("SRVC_STTS_CD", serviceAccountJSON.getString("SRVC_STTS_CD"));
        certCustcoJSON.setString("USER_ACNT_CNT", serviceSettingJSON.getString("USER_ACNT_CNT"));
        certCustcoJSON.setString("USER_LGN_SMS_CERT_YN", serviceSettingJSON.getString("USER_LGN_SMS_CERT_YN"));

        TelewebJSON objRetParams = mobjDao.update(namespace,
            "updateTenantCustco", certCustcoJSON);
        return objRetParams;
    }

    /**
     * 기업고객관리 - 서비스 설정 수정
     * 
     * @throws ParseException
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON updateServiceSetting(TelewebJSON mjsonParams) throws TelewebAppException, ParseException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //기업고객관리 - 서비스 설정 수정
        objRetParams = mobjDao.insert(namespace, "updateServiceSetting",
            mjsonParams);

        //기업고객관리 - 서비스 설정 - 서비스 정보 삭제(다른 서비스 상품의 서비스 정보가 있을 시 삭제.)
        objRetParams = mobjDao.delete(namespace, "deleteServiceOmniCoInfo",
            mjsonParams); //인증_고객사_서비스_기관 삭제
        objRetParams = mobjDao.delete(namespace, "deleteServiceOmniKeyInfo",
            mjsonParams); //인증_고객사_서비스_키 삭제
        objRetParams = mobjDao.delete(namespace, "deleteServiceInfo",
            mjsonParams); //인증 고객사 서비스 삭제

        String OMNIONE_PROVD_CO = "";
        StringBuffer sSrvcStat = new StringBuffer();
        //기업고객관리 - 서비스 설정 - 서비스 정보 등록/수정
        JSONArray dataArray = mjsonParams.getDataObject();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject dataObject = dataArray.getJSONObject(i);
            TelewebJSON srvcJsonParams = createTelewebJSON(dataObject);
            if (i > 0) {
                sSrvcStat.append("_##_");

                srvcJsonParams.setString("RGTR_ID", mjsonParams.getString("RGTR_ID"));
                srvcJsonParams.setString("CERT_CUSTCO_ID", mjsonParams.getString("CERT_CUSTCO_ID"));
            }

            String json = srvcJsonParams.getDataObject().get(0).toString();
            log.debug("=================json ::: " + json);
            String certCustcoId = srvcJsonParams.getString("SRVC_CERT_CUSTCO_ID");
            if (StringUtils.isEmpty(certCustcoId)) {
                mobjDao.insert(namespace, "insertServiceInfo", srvcJsonParams);
            } else {
                mobjDao.update(namespace, "updateServiceInfo", srvcJsonParams);
            }

            sSrvcStat.append(srvcJsonParams.getString("SRVC_GDS_DTL_CD"));
            sSrvcStat.append(",");
            sSrvcStat.append(srvcJsonParams.getString("USE_YN"));

            if ("OMNIONE".equals(srvcJsonParams.getString("SRVC_GDS_DTL_CD"))) {

                OMNIONE_PROVD_CO = srvcJsonParams.getString("OMNIONE_PROVD_CO").toString();
                OMNIONE_PROVD_CO = OMNIONE_PROVD_CO.replace("&#91;", "[").replace("&#93;", "]");

                srvcJsonParams.setString("OMNIONE_PROVD_CO", OMNIONE_PROVD_CO);
                srvcJsonParams.setString("USER_ID", mjsonParams.getString("USER_ID"));
                srvcJsonParams.setString("CUSTCO_CD", mjsonParams.getString("OMNIONE_CPCODE"));
                srvcJsonParams.setString("SRVC_KEY", mjsonParams.getString("OMNIONE_SITEKEY"));
                srvcJsonParams.setString("OMNIONE_PROVD_CO", ((TelewebJSON) createTelewebJSON(dataArray.getJSONObject(0))).getString(
                    "OMNIONE_PROVD_CO"));
                if (StringUtils.isEmpty(certCustcoId)) {
                    mobjDao.insert(namespace, "insertCertCustcoSrvcKey",
                        srvcJsonParams);
                } else {
                    mobjDao.update(namespace, "updateCertCustcoSrvcKey",
                        srvcJsonParams);
                }

                this.omnioneServiceProc(srvcJsonParams);
            }

            // 상담 이관 '사용' 시 담당자 옵션 저장
            if ("TRNSF".equals(srvcJsonParams.getString("SRVC_GDS_DTL_CD"))) {
                srvcJsonParams.setString("TARGET_OPT_CD", mjsonParams.getString("TRNSF_TRGT_OPT"));
                trnsfServiceProc(srvcJsonParams);
            }

        }

        JSONObject messageJson = new JSONObject();
        messageJson.put("SRVC_INFO", sSrvcStat.toString());
        if (!StringUtils.isEmpty(((TelewebJSON) createTelewebJSON(dataArray.getJSONObject(0))).getString("OMNIONE_PROVD_CO")))
            messageJson.put("OMNIONE_INST_NM", ((TelewebJSON) createTelewebJSON(dataArray.getJSONObject(0))).getString("OMNIONE_PROVD_CO")
                .toString().replace("&#91;", "[").replace("&#93;", "]"));

        log.debug("=================messageJson ::: " + messageJson);
        //인증 기관사 변경시 상담사에게 인증사 정보 PUSH
        SseMessage sseMessage = new SseMessage();
        sseMessage.setType(SseMessage.MessageType.ENV_SETTING);
        sseMessage.setSender("2");
        sseMessage.setReceiver("ALL");    // ALL은 전체 , userId 개인별
        sseMessage.setRoomId(mjsonParams.getString("ROOM_ID"));
        sseMessage.setSecond(-1); //필수 아님. 기본이 5초
        sseMessage.setPos("top"); //필수 아님. 기본이 top / bottom
        sseMessage.setMessage(messageJson.toString());    //메시지
        try {
            sseService.sendMessage(sseMessage);
            Thread.sleep(100);
        } catch (Exception e) {
            log.error("sseMessage is exception : " + e.getMessage());
        }

        return objRetParams;
    }

    public TelewebJSON omnioneServiceProc(TelewebJSON mjsonParams) throws TelewebAppException, ParseException {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        mobjDao.delete(namespace, "deleteCertCustcoSrvcInst", mjsonParams);

        String OMNIONE_PROVD_CO = mjsonParams.getString("OMNIONE_PROVD_CO").toString();
        OMNIONE_PROVD_CO = OMNIONE_PROVD_CO.replace("&#91;", "[").replace("&#93;", "]");
        mjsonParams.setString("OMNIONE_PROVD_CO", OMNIONE_PROVD_CO);

        mobjDao.insert(namespace, "insertCertCustcoSrvcInst", mjsonParams);

        return objRetParams;
    }

    /**
     * 서비스 저장 시 상담 이관 담당자 옵션을 저장한다.
     *
     * @param mjsonParams 요청 파라미터:<br>
     *                    - USE_YN          상담이관사용여부<br>
     *                    - CERT_CUSTCO_ID  인증고객ID<br>
     *                    - TARGET_OPT_CD   담당자옵션코드
     *                    - RGTR_ID         등록자(수정자)ID
     * @return TelewebJSON
     */
    private TelewebJSON trnsfServiceProc(TelewebJSON mjsonParams) {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        // 상담 이관 "사용"시
        if("Y".equals(mjsonParams.getString("USE_YN"))) {
            // 인증 고객사 이관 옵션 UPSERT
            mobjDao.insert(namespace, "insertCertCustcoTrnsfOpt", mjsonParams);
        }
        
        return objRetParams;
    }

    /**
     * 인증_고객사_서비스 - 테넌트 고객사_서비스 데이터 동기화.
     * 
     * @param serviceAccountJSON
     * @param certCustcoJSON
     * @param serviceSettingJSON
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON syncTenantCustcoSrvc(TelewebJSON serviceAccountJSON, TelewebJSON srvcGdsDtlJSON) throws TelewebAppException {
        TelewebJSON tenantCustco = mobjDao.select(namespace,
            "selectTenantCustco", serviceAccountJSON);

        JSONArray srvcGdsDtlList = srvcGdsDtlJSON.getDataObject();
        TelewebJSON tenantCustcoSrvcJSON = mobjDao.select(namespace,
            "selectTenantCustcoSrvc", tenantCustco);
        JSONArray tenantCustcoSrvcList = tenantCustcoSrvcJSON.getDataObject();
        //삭제 로직
        for (int t = 0; t < tenantCustcoSrvcList.size(); t++) {
            JSONObject tenantCustcoSrvc = tenantCustcoSrvcList.getJSONObject(t);
            boolean isDeleted = true;
            for (int c = 0; c < srvcGdsDtlList.size(); c++) {
                JSONObject srvcGdsDtl = srvcGdsDtlList.getJSONObject(c);
                if (tenantCustcoSrvc.getString("SRVC_GDS_DTL_CD").equals(srvcGdsDtl.getString("SRVC_GDS_DTL_CD"))) {
                    isDeleted = false;
                    break;
                }
            }
            if (isDeleted) {
                TelewebJSON objRetParams = mobjDao.delete(namespace,
                    "deleteTenantCustcoSrvc", createTelewebJSON(tenantCustcoSrvc));
            }
        }

        //저장/수정
        //삭제 반영된 내용 재 조회
        tenantCustcoSrvcJSON = mobjDao.select(namespace,
            "selectTenantCustcoSrvc", tenantCustco);
        tenantCustcoSrvcList = tenantCustcoSrvcJSON.getDataObject();
        for (int c = 0; c < srvcGdsDtlList.size(); c++) {
            JSONObject srvcGdsDtlObj = srvcGdsDtlList.getJSONObject(c);
            srvcGdsDtlObj.put("CUSTCO_ID", tenantCustco.getString("CUSTCO_ID"));

            boolean isInsert = true;
            for (int t = 0; t < tenantCustcoSrvcList.size(); t++) {
                JSONObject tenantCustcoSrvcObj = tenantCustcoSrvcList.getJSONObject(t);
                //발신 번호가 같은게 있으면 update, 없으면 insert
                if (tenantCustcoSrvcObj.get("SRVC_GDS_DTL_CD").equals(srvcGdsDtlObj.get("SRVC_GDS_DTL_CD"))) {
                    isInsert = false;
                    break;
                }
            }

            if (isInsert) {
                mobjDao.insert(namespace, "insertTenantCustcoSrvc",
                    createTelewebJSON(srvcGdsDtlObj));
            } else {
                mobjDao.update(namespace, "updateTenantCustcoSrvc",
                    createTelewebJSON(srvcGdsDtlObj));
            }
        }
        return null;
    }

    /**
     * 기업고객관리 - 서비스 계정 정보 수정
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON updateServiceAccount(TelewebJSON mjsonParams) throws TelewebAppException {

        //IPCC 연동 설정이 되었다면, 서비스 상태 변경 시 IPCC 에도 반영.
        TelewebJSON ipccSetting = selectIpccSetting(mjsonParams);

        if (!StringUtils.isEmpty(ipccSetting.getString("IPCC_SRVR_ID"))) {
            TelewebJSON serviceAccount = selectServiceAccount(mjsonParams);

            //서비스 상태가 변경되었다면 변경된 상태 값 업데이트
            if (!StringUtils.isEmpty(serviceAccount.getString("SRVC_STTS_CD")) && !serviceAccount.getString("SRVC_STTS_CD").equals(
                mjsonParams.getString("SRVC_STTS_CD"))) {
                TelewebJSON certCustcoInfo = selectCertCustcoInfo(mjsonParams);
                TelewebJSON ipccSrvr = selectIpccSrvr(ipccSetting);

                String apiUri = ipccSrvr.getString("API_URI");
                MultiValueMap<String, String> params = getParamsCust(ipccSetting.getString("IPCC_SRVR_ID"), certCustcoInfo, mjsonParams);
                JSONObject custResult = sendRestTemplate(apiUri, params);
                boolean isCustSuccess = custResult.containsKey("result") ? (custResult.getString("result").equals("success") ? true : false)
                    : false;
                if (!isCustSuccess) {
                    throw new TelewebAppException("IPCC " + (StringUtils.isEmpty(ipccSetting.getString("IPCC_SRVR_ID")) ? "cust_ins"
                        : "cust_upt") + " FAIL : " + custResult.getString("result"));
                }
            }

        }

        TelewebJSON objRetParams = mobjDao.update(namespace,
            "updateServiceAccount", mjsonParams);

        //인증 사용자 건수 조회(이미 저장되었는지 확인 - '서비스 계정 정보' 최초 저장 시에 저장됨)
        TelewebJSON certUserRetParams = mobjDao.select(namespace,
            "selectCertUser", mjsonParams);
        if (certUserRetParams.getHeaderInt("COUNT") == 0) {
            String LGN_ID = mjsonParams.getString("LGN_ID");
            //고객사 superUser 계정 생성(플랫폼개발본부 전용 계정)
            mjsonParams.setInt("CERT_USER_ID", innbCreatCmmnService.createSeqNo("CERT_USER_ID"));
            mjsonParams.setString("LGN_ID", "hkc#" + mjsonParams.getString("ASP_CUST_KEY"));
            mobjDao.insert(namespace, "insertCertUser", mjsonParams);

            //superUser 계정 전용 ID 인증_고객사_사용자 등록
            mobjDao.insert(namespace, "insertCertCustcoUser", mjsonParams);

            //인증_사용자_ID 생성 및 인증_사용자 등록
            mjsonParams.setInt("CERT_USER_ID", innbCreatCmmnService.createSeqNo("CERT_USER_ID"));
            mjsonParams.setString("LGN_ID", LGN_ID);
            mobjDao.insert(namespace, "insertCertUser", mjsonParams);

            //인증_고객사_사용자 등록
            mobjDao.insert(namespace, "insertCertCustcoUser", mjsonParams);

            //고객사별 설문 전용 ID 생성
            mjsonParams.setInt("CERT_USER_ID", innbCreatCmmnService.createSeqNo("CERT_USER_ID"));
            mjsonParams.setString("LGN_ID", mjsonParams.getString("ASP_CUST_KEY") + "svy");
            mobjDao.insert(namespace, "insertCertUser", mjsonParams);

            //설문 전용 ID 인증_고객사_사용자 등록
            mobjDao.insert(namespace, "insertCertCustcoUser", mjsonParams);
        }

        return objRetParams;
    }

    /**
     * 기업고객관리 - IPCC 연동 설정 수정
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON updateIpccSetting(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        IpccRollbackDTO ipccRollbackDTO = new IpccRollbackDTO();
        List<IpccRollbackDTO> ipccRollbackList = new ArrayList<IpccRollbackDTO>();

        //데이터 array
        JSONArray dataArray = mjsonParams.getDataObject();

        TelewebJSON ipccSetting = selectIpccSetting(mjsonParams);
        TelewebJSON certCustcoInfo = selectCertCustcoInfo(mjsonParams);
        TelewebJSON serviceAccount = selectServiceAccount(mjsonParams);
        TelewebJSON ipccSrvr = selectIpccSrvr(mjsonParams);

        String currentIpccSrvrId = ipccSetting.getString("IPCC_SRVR_ID");

        //기존에 저장이 안되어 있고 신규로 저장을 하는 경우, IPCC API로 업체관리 - 업체 등록, 
        //기존에 저장이 되어있는 경우 업체관리 - 업체 수정
        String apiUri = ipccSrvr.getString("API_URI");
        MultiValueMap<String, String> params = getParamsCust(currentIpccSrvrId, certCustcoInfo, serviceAccount);
        JSONObject custResult = sendRestTemplate(apiUri, params);
        boolean isCustSuccess = custResult.containsKey("result") ? (custResult.getString("result").equals("success") ? true : false)
            : false;
        if (!isCustSuccess) {
            throw new TelewebAppException("IPCC " + (StringUtils.isEmpty(currentIpccSrvrId) ? "cust_ins" : "cust_upt") + " FAIL : "
                + custResult.getString("result"));
        }

        //기업고객관리 - IPCC 연동 설정 수정
        objRetParams = mobjDao.insert(namespace, "updateIpccSetting",
            mjsonParams);

        //기업고객관리 - IPCC 연동 설정 - 전화 번호 정보 삭제(기존 데이터와 신규 데이터 비교하여 삭제된 번호 찾아서 삭제)
        TelewebJSON currentCustcoDsptchNoList = selectCertCustcoDsptchNo(mjsonParams);
        JSONArray currentCustcoDsptchNoArray = currentCustcoDsptchNoList.getDataObject();
        for (int i = 0; i < currentCustcoDsptchNoArray.size(); i++) {
            //초기 설정은 삭제 대상, 신규에도 해당 번호가 있다면 삭제 대상 아님.
            boolean isDeleted = true;
            JSONObject currentCustcoDsptchNoObj = currentCustcoDsptchNoArray.getJSONObject(i);
            String currentId = currentCustcoDsptchNoObj.getString("CERT_CUSTCO_DSPTCH_NO_ID");
            for (int n = 0; n < dataArray.size(); n++) {
                JSONObject newCustcoDsptchNoObj = dataArray.getJSONObject(n);
                String newId = newCustcoDsptchNoObj.getString("CERT_CUSTCO_DSPTCH_NO_ID");
                //현재(db에 저장된) 번호가 신규에도 있다면 삭제 대상 아님. 신규 번호 목록에 현재 번호가 없다면 삭제 대상.
                if (currentId.equals(newId)) {
                    isDeleted = false;
                    break;
                }
            }

            //신규 넘어온 번호 목록에 현재 번호(db에 저장된)가 없다면 삭제.
            if (isDeleted) {
                TelewebJSON dsptchNoJsonParams = createTelewebJSON(currentCustcoDsptchNoObj);

                //삭제
                //IPCC 에 대표번호 삭제 요청 후 삭제.
                MultiValueMap<String, String> trunkParams = getParamsTrunk("trunk_del", ipccSrvr, serviceAccount, dsptchNoJsonParams);
                JSONObject trunkResult = sendRestTemplate(apiUri, trunkParams);
                boolean isTrunkSuccess = trunkResult.containsKey("result") ? (trunkResult.getString("result").equals("success") ? true
                    : false) : false;
                if (!isTrunkSuccess) {
                    throw new TelewebAppException("IPCC trunk_del API FAIL : " + trunkResult.getString("result"));
                }

                mobjDao.delete(namespace, "deleteCustcoDsptchNo",
                    dsptchNoJsonParams);
            }
        }

        //기업고객관리 - IPCC 연동 설정 - 전화 번호 정보 등록/수정
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject dataObject = dataArray.getJSONObject(i);
            TelewebJSON dsptchNoJsonParams = createTelewebJSON(dataObject);
            if (i > 0) {
                dsptchNoJsonParams.setString("CERT_CUSTCO_ID", mjsonParams.getString("CERT_CUSTCO_ID"));
            }

            String json = dsptchNoJsonParams.getDataObject().get(0).toString();
            log.debug("=================json ::: " + json);
            String certCustcoDsptchNoId = dsptchNoJsonParams.getString("CERT_CUSTCO_DSPTCH_NO_ID");
            if (StringUtils.isEmpty(certCustcoDsptchNoId)) {
                //IPCC 에 대표번호 등록 요청 후 등록.
                String req = "trunk_ins";
                MultiValueMap<String, String> trunkParams = getParamsTrunk(req, ipccSrvr, serviceAccount, dsptchNoJsonParams);
                JSONObject trunkResult = sendRestTemplate(apiUri, trunkParams);
                boolean isTrunkSuccess = trunkResult.containsKey("result") ? (trunkResult.getString("result").equals("success") ? true
                    : false) : false;
                if (!isTrunkSuccess) {
                    throw new TelewebAppException("IPCC " + req + " API FAIL : " + trunkResult.getString("result"));
                }

                dsptchNoJsonParams.setInt("CERT_CUSTCO_DSPTCH_NO_ID", innbCreatCmmnService.createSeqNo("CERT_CUSTCO_DSPTCH_NO_ID"));
                mobjDao.insert(namespace, "insertCustcoDsptchNo",
                    dsptchNoJsonParams);
            } else {
                //IPCC 에 대표번호 수정 요청 후 수정.
                String req = "trunk_upt";
                MultiValueMap<String, String> trunkParams = getParamsTrunk(req, ipccSrvr, serviceAccount, dsptchNoJsonParams);
                JSONObject trunkResult = sendRestTemplate(apiUri, trunkParams);
                boolean isTrunkSuccess = trunkResult.containsKey("result") ? (trunkResult.getString("result").equals("success") ? true
                    : false) : false;
                if (!isTrunkSuccess) {
                    throw new TelewebAppException("IPCC " + req + " API FAIL : " + trunkResult.getString("result"));
                }

                mobjDao.update(namespace, "updateCustcoDsptchNo",
                    dsptchNoJsonParams);
            }
        }
        return objRetParams;
    }

    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON updateLocaleInfoSetting(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String LANG_LIST = mjsonParams.getString("LANG_LIST").toString();
        LANG_LIST = LANG_LIST.replace("&#91;", "[").replace("&#93;", "]");
        mjsonParams.setString("LANG_LIST", LANG_LIST);

        //기업고객관리 - 언어 설정 수정
        objRetParams = mobjDao.insert(namespace, "upsertLocaleInfoSetting",
            mjsonParams);

        return objRetParams;
    }

    /**
     * 신규 테넌트 스키마 생성.
     */
    public TelewebJSON createTenant(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetJson = new TelewebJSON(mjsonParams);

        String schemaId = mjsonParams.getString("SCHEMA_ID");
        if (schemaId != null && !"".equals(schemaId)) {
            MigrateResult result = buildDatabaseSchema(schemaId);
        }

        return objRetJson;
    }

    /**
     * flyway 업데이트.
     */
    public TelewebJSON migrationFlyway(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetJson = new TelewebJSON(mjsonParams);

        TelewebJSON objRetParams = selectAllSchemaIds();
        migrationFlyway(objRetParams.getDataObject());
        objRetJson.setHeader("ERROR_FLAG", false);
        objRetJson.setHeader("ERROR_MSG", "Flyway가 정상 실행되었습니다.");
        return objRetJson;
    }

    /**
     * custco의 기업고객관리 정보 조회(해당 테넌트의 고객사 정보 업데이트를 위해)
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectCertCustco4TenantSave(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectCertCustco4TenantSave",
            mjsonParams);
    }

    /**
     * 고객사 시스템 관리자 계정 생성
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON createCustcoUser(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetJson = new TelewebJSON(mjsonParams);
        mjsonParams = twbUserBizService.initPswd(mjsonParams);
        objRetJson = mobjDao.insert(namespace, "insertCustcoUser", mjsonParams);
        return objRetJson;
    }

    /**
     * custco의 기업고객관리 정보로 고객사 및 관련 데이터 생성
     * 사용자는 비밀번호 생성때문에 여기서 생성하고, 나머지 고객사 및 관련 데이터는 plt_proc_add_custco_data 프로시저를 호출하여 실행.
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON createCustcoData(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetJson = new TelewebJSON(mjsonParams);
        //사용자 

        //테넌트 고객사 업데이트
        objRetJson = mobjDao.update(namespace, "createCustcoData", mjsonParams);

        return objRetJson;
    }

    /**
     * 인증_고객사 - 서비스_생성_일시 업데이트
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON updateCustcoSrvcCrtDt(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.update(namespace, "updateCustcoSrvcCrtDt", mjsonParams);
    }

    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectTenantOfLgnId(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectTenantOfLgnId", mjsonParams);
    }

    /**
     * 스키마 ID 중복체크
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON dpcnChkSchemaId(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //예약된 스키마 ID는 사용 불가 - public, custco, ttalk, chatgpt
        String schemaId = mjsonParams.getString("SCHEMA_ID");
        if (schemaId != null && RESERVED_SCHEMA_IDS.contains(schemaId)) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 사용중입니다.");
        } else {
            objRetParams = mobjDao.select(namespace, "dpcnChkSchemaId",
                mjsonParams);
            if (objRetParams.getHeaderInt("COUNT") == 0) {
                objRetParams.setHeader("ERROR_FLAG", false);
                return objRetParams;
            } else {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "이미 사용중입니다.");
            }
        }
        return objRetParams;

    }

    /**
     * 로그인 ID 중복체크
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON dpcnChkLgnId(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select(namespace, "dpcnChkLgnId", mjsonParams);
        if (objRetParams.getHeaderInt("COUNT") == 0) {
            objRetParams.setHeader("ERROR_FLAG", false);
            return objRetParams;
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 사용중입니다.");
        }
        return objRetParams;

    }

    /**
     * ASP_CUST_KEY 중복체크
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON dpcnChkAspCustKey(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select(namespace, "dpcnChkAspCustKey",
            mjsonParams);
        if (objRetParams.getHeaderInt("COUNT") == 0) {
            objRetParams.setHeader("ERROR_FLAG", false);
            return objRetParams;
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 사용중입니다.");
        }
        return objRetParams;
    }

    /**
     * IPCC 연동 설정 - 발신_번호 중복체크
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON dpcnChkDsptchNo(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        JSONArray dsptchNoList = mjsonParams.getDataObject();
        for (int i = 0; i < dsptchNoList.size(); i++) {
            JSONObject dataObject = dsptchNoList.getJSONObject(i);

            TelewebJSON dsptchNoJsonParams = createTelewebJSON(dataObject);

            if (StringUtils.isEmpty(dsptchNoJsonParams.getString("CERT_CUSTCO_DSPTCH_NO_ID"))) {
                objRetParams = mobjDao.select(namespace, "dpcnChkDsptchNo",
                    dsptchNoJsonParams);
                if (objRetParams.getHeaderInt("COUNT") > 0) {
                    objRetParams.setHeader("ERROR_FLAG", true);
                    objRetParams.setHeader("ERROR_MSG", "발신 전화번호표시이름(DID 번호) '" + objRetParams.getString("RPRS_NO") + "'가 중복입니다.");
                    return objRetParams;
                }
            }
        }

        objRetParams.setHeader("ERROR_FLAG", false);

        return objRetParams;
    }

    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectSrvcGds(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectSrvcGds", mjsonParams);
    }

    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectSrvcGdsDtl(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectSrvcGdsDtl", mjsonParams);
    }

    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectIpccSrvr(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectIpccSrvr", mjsonParams);
    }

    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectCertCustcoDsptchNo(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectCertCustcoDsptchNo",
            mjsonParams);
    }

    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectCertCustcoLocaleInfo(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectCertCustcoLocaleInfo",
            mjsonParams);
    }
    
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON createDbUser4Oracle(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetJson = new TelewebJSON(mjsonParams);
        return mobjDao.update(namespace, "createDbUser4Oracle", mjsonParams);
    }

    /**
     * 모든 스키마 정보 조회
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectAllSchemaIds() throws TelewebAppException {
        return mobjDao.select(namespace, "selectAllSchemaIds",
            new TelewebJSON());
    }

    /**
     * 해당 IPCC 서버와 연동된 인증_고객사 목록
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectIpccSrvrCertCustco(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectIpccSrvrCertCustco",
            mjsonParams);
    }

    /**
     * 고객사별 현재 접속한 사용자 목록
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON selectUserList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectUserList", mjsonParams);
    }

    /**
     * 인증_IPCC_서버 API_URI - 테넌트 고객사 API_URI 데이터 동기화.
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON syncTenantCustcoApiUri(TelewebJSON serviceAccountJSON, TelewebJSON ipccSrvrJSON) throws TelewebAppException {
        TelewebJSON retJSON = new TelewebJSON();
        //테넌트 고객사 정보 조회(asp_cust_key로 custco_id 가져오기 위함)
        TelewebJSON tenantCustco = mobjDao.select(namespace,
            "selectTenantCustco", serviceAccountJSON);
        if (!tenantCustco.getString("API_URI").equals(ipccSrvrJSON.getString("API_URI"))) {
            ipccSrvrJSON.setString("CUSTCO_ID", tenantCustco.getString("CUSTCO_ID"));
            retJSON = mobjDao.update(namespace, "updateTenantCustcoApiUri",
                ipccSrvrJSON);
        }
        return retJSON;
    }

    /**
     * 인증_고객사_발신_번호 - 테넌트 고객사_발신_번호 데이터 동기화.
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON syncTenantCustcoDsptchNo(TelewebJSON serviceAccountJSON,
        TelewebJSON certCustcoDsptchNoListJSON) throws TelewebAppException {
        //테넌트 고객사 정보 조회(asp_cust_key로 custco_id 가져오기 위함)
        TelewebJSON tenantCustco = mobjDao.select(namespace,
            "selectTenantCustco", serviceAccountJSON);
        //테넌트 고객사_발신_번호 조회
        TelewebJSON tenantCustcoDsptchNoListJSON = mobjDao.select(namespace,
            "selectTenantCustcoDsptchNo", tenantCustco);

        JSONArray certCustcoDsptchNoList = certCustcoDsptchNoListJSON.getDataObject();
        JSONArray tenantCustcoDsptchNoList = tenantCustcoDsptchNoListJSON.getDataObject();
        //삭제항목 체크 및 삭제 - 테넌트 고객사_발신_번호의 발신_번호와 인증_고객사_발신_번호의 발신_번호 비교하여 동일한 것이 있으면 삭제 대상 아님, 없으면 삭제 대상.
        //고객사_발신_번호(DSPTCH_NO) 를 대표_번호(RPRS_NO) 로 변경 - IPCC와 컬럼이 꼬였음(팔레트 대표번호가 IPCC의 DID 번호임) by hjh. 20240524
        for (int t = 0; t < tenantCustcoDsptchNoList.size(); t++) {
            JSONObject tenantCustcoDsptchNoObj = tenantCustcoDsptchNoList.getJSONObject(t);
            //삭제 여부
            boolean isDeleted = true;
            for (int c = 0; c < certCustcoDsptchNoList.size(); c++) {
                JSONObject certCustcoDsptchNoObj = certCustcoDsptchNoList.getJSONObject(c);
                if (tenantCustcoDsptchNoObj.get("RPRS_NO").equals(certCustcoDsptchNoObj.get("RPRS_NO"))) {
                    //대표_번호가 같으면 삭제 대상 아님.
                    isDeleted = false;
                    break;
                }
            }

            if (isDeleted) {
                TelewebJSON dsptchNoJsonParams = createTelewebJSON(tenantCustcoDsptchNoObj);
                mobjDao.delete(namespace, "deleteTenantCustcoDsptchNo",
                    dsptchNoJsonParams);
            }
        }

        //insert 또는 update 처리
        tenantCustcoDsptchNoListJSON = mobjDao.select(namespace,
            "selectTenantCustcoDsptchNo", tenantCustco);
        for (int c = 0; c < certCustcoDsptchNoList.size(); c++) {
            JSONObject certCustcoDsptchNoObj = certCustcoDsptchNoList.getJSONObject(c);
            certCustcoDsptchNoObj.put("CUSTCO_ID", tenantCustco.getString("CUSTCO_ID"));

            boolean isInsert = true;
            for (int t = 0; t < tenantCustcoDsptchNoList.size(); t++) {
                JSONObject tenantCustcoDsptchNoObj = tenantCustcoDsptchNoList.getJSONObject(t);
                //발신 번호가 같은게 있으면 update, 없으면 insert
                //발신 번호 -> 대표번호 체크로 변경 - IPCC와 컬럼이 꼬였음(팔레트 대표번호가 IPCC의 DID 번호임)
                if (tenantCustcoDsptchNoObj.get("RPRS_NO").equals(certCustcoDsptchNoObj.get("RPRS_NO"))) {
                    isInsert = false;
                    break;
                }
            }

            if (isInsert) {
                certCustcoDsptchNoObj.put("CUSTCO_DSPTCH_NO_ID", innbCreatCmmnService.createSeqNo("CUSTCO_DSPTCH_NO_ID"));
                mobjDao.insert(namespace, "insertTenantCustcoDsptchNo",
                    createTelewebJSON(certCustcoDsptchNoObj));
            } else {
                mobjDao.update(namespace, "updateTenantCustcoDsptchNo",
                    createTelewebJSON(certCustcoDsptchNoObj));
            }
        }

        return null;
    }

    /**
     * 발신_번호 삭제 가능 여부 조회
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON chkDeleteDsptchNo(TelewebJSON serviceAccountJSON, TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        if (!StringUtils.isEmpty(serviceAccountJSON.getString("SRVC_CRT_DT"))) {
            objRetParams = mobjDao.select(namespace, "chkDeleteDsptchNo",
                mjsonParams);
            if (objRetParams.getHeaderInt("COUNT") > 0) {
                objRetParams.setHeader("ERROR_FLAG", true);
            } else {
                objRetParams.setHeader("ERROR_FLAG", false);
            }
        } else {
            objRetParams.setHeader("ERROR_FLAG", false);
        }
        return objRetParams;
    }
    
    
    

    private MigrateResult buildDatabaseSchema(String schemaId) throws TelewebAppException {
        MigrateResult result = null;
        if (schemaId != null && !"public".equals(schemaId)) {
            Flyway flyway = flywayBuilder.createFlyway(schemaId);
            result = flyway.migrate();
        }
        return result;
    }

    private void migrationFlyway(JSONArray data) throws TelewebAppException {
        for (int i = 0; i < data.size(); i++) {
            JSONObject obj = data.getJSONObject(i);
            if (obj.getString("SCHEMA_ID") != null && !"public".equals(obj.getString("SCHEMA_ID"))) {
                Flyway flyway = flywayBuilder.createFlyway(obj.getString("SCHEMA_ID"));
                flyway.migrate();
            }
        }

    }

    /**
     * IPCC API 요청 parameter 생성- 업체 관리
     * 
     * @param currentIpccSrvrId
     * @param ipccSrvr
     * @param certCustcoInfo
     * @param serviceAccount
     * @return
     */
    private MultiValueMap<String, String> getParamsCust(String currentIpccSrvrId, TelewebJSON certCustcoInfo, TelewebJSON serviceAccount) {
        /**
         * API URL : https://dev2.hkipcc.co.kr/api/palette_interface.php
         * 1. 업체 관리
         * 1-1. 업체 등록
         * 요청값 : REQ(cust_ins), CUSTCODE(업체코드), CNAME(업체명), COMPTEL(업체전화번호), COMPMAN(담당자이름), COMPMANTEL(담당자연락처), COMPSTATE(상태) - 1:사용, 2:중지,
         * 3:해지
         * 결과공통 : {"result":"success"} or {"result":"fail - ERROR MSG"}
         * 
         * 1-2. 업체 수정
         * 요청값 : REQ(cust_upt), CUSTCODE(업체코드 - Key), CNAME(업체명), COMPTEL(업체전화번호), COMPMAN(담당자이름), COMPMANTEL(담당자연락처), COMPSTATE(상태) - 1:사용,
         * 2:중지, 3:해지
         * 
         * 1-3. 업체 삭제 (오타로 인한 삭제, 테스트 삭제 등 특별한 경우에만 사용하세요. 대표번호까지 지워집니다.)
         * 요청값 : REQ(cust_del), CUSTCODE(업체코드 - Key)
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("REQ", StringUtils.isEmpty(currentIpccSrvrId) ? "cust_ins" : "cust_upt");			//REQ(cust_ins, cust_upt, cust_del)
        params.add("CUSTCODE", serviceAccount.getString("ASP_CUST_KEY"));								//CUSTCODE(업체코드)
        params.add("CNAME", certCustcoInfo.getString("CUSTCO_NM"));										//CNAME(업체명)
        params.add("COMPTEL", certCustcoInfo.getString("RPRS_PHN_NO"));									//COMPTEL(업체전화번호)
        params.add("COMPMAN", certCustcoInfo.getString("APLY_USER_NM"));								//COMPMAN(담당자이름)
        params.add("COMPMANTEL", certCustcoInfo.getString("APLY_USER_PHN_NO"));							//COMPMANTEL(담당자연락처)
        params.add("COMPSTATE", serviceAccount.getString("SRVC_STTS_CD").equals("ON") ? "1" : "2");		//COMPSTATE(상태)

        return params;
    }

    private MultiValueMap<String, String> getParamsTrunk(String req, TelewebJSON ipccSrvr, TelewebJSON serviceAccount,
        TelewebJSON dsptchNoJsonParams) {
        /**
         * 2. 대표번호 관리 : 테스트 did번호 07071730194
         * 2-1. 대표번호 입력
         * 요청값 : REQ(trunk_ins), CUSTCODE(업체코드), DID(DID번호), CIDNAME(대표번호), TRKNAME(국선명)
         * 결과공통 : {"result":"success"} or {"result":"fail - ERROR MSG"}
         * 2-2. 대표번호 수정
         * 요청값 : REQ(trunk_upt), CUSTCODE(업체코드 - Key), DID(DID번호 - Key), CIDNAME(대표번호), TRKNAME(국선명)
         */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("REQ", req);																			//REQ(trunk_ins, trunk_upt, trunk_del)
        params.add("CUSTCODE", serviceAccount.getString("ASP_CUST_KEY"));								//CUSTCODE(업체코드)
        //번호 꼬임으로 IPCC의 DID가 팔레트에서는 RPRS_NO 임
        params.add("DID", dsptchNoJsonParams.getString("RPRS_NO"));									//DID(DID번호)
        //번호 꼬임으로 IPCC의 CIDNAME가 팔레트에서는 DSPTCH_NO 임
        params.add("CIDNAME", dsptchNoJsonParams.getString("DSPTCH_NO"));									//CIDNAME(대표번호)
        params.add("TRKNAME", dsptchNoJsonParams.getString("DSPTCH_NM"));								//TRKNAME(국선명)

        return params;
    }

    /**
     * IPCC API 요청
     * 
     * @param url
     * @param parameters
     * @return
     */
    private JSONObject sendRestTemplate(String url, MultiValueMap<String, String> parameters) {
        // http 통신 결과가 들어오는 문자열 변수
        String result = null;
        // http 통신의 헤더값
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0");
        headers.add("Accept-Charset", StandardCharsets.UTF_8.toString());

        // http 통신의 value값
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        //POST 방식으로 IPCC 서버로 리퀘스트를 전송한다. 인자 (URL, HttpEntity request, 받을 클래스 형식);
        result = restTemplate.postForObject(URI.create(url.replace(":7777", "") + "/api/palette_interface.php"), request, String.class);
        //        result = "{\"result\":\"success\"}";

        JSONObject resutObject = new JSONObject();

        try {
            resutObject = JSONObject.fromObject(JSONSerializer.toJSON(result));
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("ERROR sendRestTemplate : ", e.toString());
        }

        return resutObject;
    }

    private TelewebJSON createTelewebJSON(JSONObject jsonObj) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObj);
        TelewebJSON telewebJSON = new TelewebJSON();
        telewebJSON.setDataObject(jsonArray);
        return telewebJSON;
    }

}
