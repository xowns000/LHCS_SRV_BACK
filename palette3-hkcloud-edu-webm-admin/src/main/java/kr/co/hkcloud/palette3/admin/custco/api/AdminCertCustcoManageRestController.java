package kr.co.hkcloud.palette3.admin.custco.api;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.admin.custco.app.AdminCertCustcoManageService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingSystemCertCustcoManageRestController", description = "설정시스템 인증고객사(테넌트) 관리 REST 컨트롤러")
public class AdminCertCustcoManageRestController {

    public static final String ENTER_INFO = "SSE_ROOM_ENTER_INFO"; // 룸에 입장한 클라이언트의 sessionId와 룸 id를 맵핑한 정보 저장

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;

    private final AdminCertCustcoManageService certCustcoManageService;
    @Value("${datasources.datasource.palette.db-vendor}")
    private String dbVendor;

    /**
     * 메서드 설명 : 기업고객관리 - 목록 조회
     */
    @ApiOperation(value = "기업고객관리 - 목록 조회", notes = "기업고객관리 - 목록을 조회한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/list")
    public Object selectCertCustco(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        //        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = certCustcoManageService.selectCertCustco(mjsonParams);

        JSONArray newObjRetParams = new JSONArray();
        JSONArray jsonObj = objRetParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj.size(); i++) {
            JSONObject objData = jsonObj.getJSONObject(i);

            List<String> userList = hashOpsEnterInfo.values(ENTER_INFO + "_" + objData.getString("ROOM_ID"));

            objData.put("ACCESS_USER_CNT", userList.size());
            newObjRetParams.add(objData);
        }

        objRetParams.setDataObject(newObjRetParams);

        return objRetParams;
    }

    @PostMapping("/admin-api/custco/cert-custco-manage/selectBox")
    public Object selectBoxCertCustco(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        return certCustcoManageService.selectCertCustco(jsonParams);
    }

    @ApiOperation(value = "기업고객관리 - 기업고객 정보 조회", notes = "기업고객관리 - 기업고객 정보를 조회한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/getCertCustcoInfo")
    public Object selectCertCustcoInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.selectCertCustcoInfo(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - 서비스 설정 조회", notes = "기업고객관리 - 서비스 설정을 조회한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/getServiceSetting")
    public Object selectServiceSetting(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.selectServiceSetting(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - 서비스 계정 정보 조회", notes = "기업고객관리 - 서비스 계정 정보를 조회한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/getServiceAccount")
    public Object selectServiceAccount(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.selectServiceAccount(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - IPCC 연동 설정 정보 조회", notes = "기업고객관리 - IPCC 연동 설정 정보를 조회한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/getIpccSetting")
    public Object selectIpccSetting(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.selectIpccSetting(mjsonParams);

        return objRetParams;
    }

    /**
     * 메서드 설명 : 기업고객 정보 등록
     */
    @ApiOperation(value = "기업고객관리 - 기업고객 정보 등록", notes = "기업고객관리 - 기업고객 정보를 등록한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/registCertCustcoInfo")
    public Object insertCertCustco(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.insertCertCustcoInfo(mjsonParams);
        return objRetParams;
    }

    /**
     * 메서드 설명 : 기업고객 정보 수정
     */
    @ApiOperation(value = "기업고객관리 - 기업고객 정보 수정", notes = "기업고객관리 - 기업고객 정보를 수정한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/modifyCertCustcoInfo")
    public Object updateCertCustco(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.updateCertCustcoInfo(mjsonParams);

        //서비스 생성(테넌트 스키마 생성)을 한 인증_고객사일 경우,
        //custco 스키마에 반영된 기업고객 정보를 테넌트 스키마의 plt_custco 테이블에 반영
        TelewebJSON serviceAccountJSON = certCustcoManageService.selectServiceAccount(mjsonParams);
        if (StringUtils.isNotEmpty(serviceAccountJSON.getString("SRVC_CRT_DT"))) {
            TelewebJSON certCustcoJSON = certCustcoManageService.selectCertCustcoInfo(mjsonParams);
            TelewebJSON serviceSettingJSON = certCustcoManageService.selectServiceSetting(mjsonParams);
            TenantContext.setCurrentTenant(serviceAccountJSON.getString("SCHEMA_ID"));

            certCustcoManageService.syncTenantCustco(serviceAccountJSON, certCustcoJSON, serviceSettingJSON);

            //다시 custco tenant로 변경
            TenantContext.setCurrentTenant("custco");
        }

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - 서비스 설정 저장", notes = "기업고객관리 - 서비스 설정을 저장한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/modifyServiceSetting")
    public Object updateServiceSetting(
        @TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, TelewebAppException, ParseException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.updateServiceSetting(mjsonParams);

        //서비스 생성(테넌트 스키마 생성)을 한 인증_고객사일 경우,
        //custco 스키마에 반영된 기업고객 정보를 테넌트 스키마의 plt_custco, plt_custco_srvc 테이블에 반영
        TelewebJSON serviceAccountJSON = certCustcoManageService.selectServiceAccount(mjsonParams);
        if (StringUtils.isNotEmpty(serviceAccountJSON.getString("SRVC_CRT_DT"))) {
            TelewebJSON certCustcoJSON = certCustcoManageService.selectCertCustcoInfo(mjsonParams);
            TelewebJSON serviceSettingJSON = certCustcoManageService.selectServiceSetting(mjsonParams);
            TelewebJSON srvcGdsDtlJSON = certCustcoManageService.selectSrvcGdsDtl(mjsonParams);
            TenantContext.setCurrentTenant(serviceAccountJSON.getString("SCHEMA_ID"));
            //plt_custco 업데이트
            certCustcoManageService.syncTenantCustco(serviceAccountJSON, certCustcoJSON, serviceSettingJSON);

            //plt_custco_srvc 업데이트
            certCustcoManageService.syncTenantCustcoSrvc(serviceAccountJSON, srvcGdsDtlJSON);

            //다시 custco tenant로 변경
            TenantContext.setCurrentTenant("custco");
        }

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - 서비스 계정 정보 저장", notes = "기업고객관리 - 서비스 계정 정보를 저장한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/modifyServiceAccount")
    public Object updateServiceAccount(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.updateServiceAccount(mjsonParams);

        //서비스 생성(테넌트 스키마 생성)을 한 인증_고객사일 경우,
        //custco 스키마에 반영된 기업고객 정보를 테넌트 스키마의 plt_custco 테이블에 반영
        TelewebJSON serviceAccountJSON = certCustcoManageService.selectServiceAccount(mjsonParams);
        if (StringUtils.isNotEmpty(serviceAccountJSON.getString("SRVC_CRT_DT"))) {
            TelewebJSON certCustcoJSON = certCustcoManageService.selectCertCustcoInfo(mjsonParams);
            TelewebJSON serviceSettingJSON = certCustcoManageService.selectServiceSetting(mjsonParams);
            TenantContext.setCurrentTenant(serviceAccountJSON.getString("SCHEMA_ID"));

            certCustcoManageService.syncTenantCustco(serviceAccountJSON, certCustcoJSON, serviceSettingJSON);

            //다시 custco tenant로 변경
            TenantContext.setCurrentTenant("custco");
        }

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - IPCC 연동 설정 저장", notes = "기업고객관리 - IPCC 연동 설정을 저장한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/modifyIpccSetting")
    public Object updateIpccSetting(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.updateIpccSetting(mjsonParams);

        //서비스 생성(테넌트 스키마 생성)을 한 인증_고객사일 경우,
        //ipcc api_uri를 테넌트 고객사 테이블(plt_custco)에 반영
        //custco 스키마에 반영된 IPCC 연동 설정 - 전화 번호 정보를 테넌트 스키마의 plt_custco_dsptch_no 테이블에 반영
        TelewebJSON serviceAccountJSON = certCustcoManageService.selectServiceAccount(mjsonParams);
        if (StringUtils.isNotEmpty(serviceAccountJSON.getString("SRVC_CRT_DT"))) {
            TelewebJSON ipccSrvrJSON = certCustcoManageService.selectIpccSrvr(mjsonParams);
            TelewebJSON certCustcoDsptchNoList = certCustcoManageService.selectCertCustcoDsptchNo(mjsonParams);
            TenantContext.setCurrentTenant(serviceAccountJSON.getString("SCHEMA_ID"));

            certCustcoManageService.syncTenantCustcoApiUri(serviceAccountJSON, ipccSrvrJSON);
            certCustcoManageService.syncTenantCustcoDsptchNo(serviceAccountJSON, certCustcoDsptchNoList);

            //다시 custco tenant로 변경
            TenantContext.setCurrentTenant("custco");
        }

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - 언어 설정 저장", notes = "기업고객관리 - 언어 설정을 저장한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/updateLocaleInfoSetting")
    public Object updateLocaleInfoSetting(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.updateLocaleInfoSetting(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - 서비스 계정 정보 - 테넌트 생성", notes = "기업고객관리 - 서비스 계정 정보 - 테넌트를 생성한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/createTenant")
    public Object createTenant(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        if("ORACLE".equals(dbVendor.toUpperCase())) {
            //ORACLE - DB 유저 생성.
            certCustcoManageService.createDbUser4Oracle(mjsonParams);
        }
        //테넌트 스키마 생성
        certCustcoManageService.createTenant(mjsonParams);

        TelewebJSON custcoInfoRetParams = certCustcoManageService.selectCertCustco4TenantSave(mjsonParams);

        String schemaId = mjsonParams.getString("SCHEMA_ID");
        if (schemaId != null && !"".equals(schemaId)) {
            String LGN_ID = custcoInfoRetParams.getString("LGN_ID");
            //TenantContext를 schemaId로 변경한 뒤 호출.
            TenantContext.setCurrentTenant(schemaId);

            //고객사 superUser 계정 생성(플랫폼개발본부 전용 계정)
            TelewebJSON userParam = new TelewebJSON();
            userParam.setString("LGN_ID", "hkc#" + custcoInfoRetParams.getString("ASP_CUST_KEY"));
            userParam.setString("USER_NM", "*시스템 관리자*");
            userParam.setString("USER_ID", "1");
            certCustcoManageService.createCustcoUser(userParam);

            //고객사 시스템 관리자 계정 생성
            userParam.setString("LGN_ID", LGN_ID);
            custcoInfoRetParams.setString("USER_NM", "시스템 관리자");
            certCustcoManageService.createCustcoUser(custcoInfoRetParams);

            //고객사 설문 전용 계정 생성
            custcoInfoRetParams.setString("LGN_ID", custcoInfoRetParams.getString("SVY_LGN_ID"));
            custcoInfoRetParams.setString("USER_NM", "설문전용");
            certCustcoManageService.createCustcoUser(custcoInfoRetParams);

            //custco 스키마에서 프로시저 실행 및 서비스_생성_일시 업데이트를 위해 'custco'로 tenant 변경
            //Oracle은 해당 유저 스키마에서 실행.
            if("POSTGRESQL".equals(dbVendor.toUpperCase())) {
                TenantContext.setCurrentTenant("custco");
            }

            //custco의 기업고객관리 정보로 고객사 및 관련 데이터 생성
            certCustcoManageService.createCustcoData(custcoInfoRetParams);

            //인증_고객사 - 서비스_생성_일시 업데이트
            certCustcoManageService.updateCustcoSrvcCrtDt(custcoInfoRetParams);

        }
        return objRetParams;
    }

    @ApiOperation(value = "설정시스템 인증고객사관리 - FLYWAY 업데이트", notes = "설정시스템 인증고객사관리 - FLYWAY를 업데이트한다.")
    @PostMapping("/admin-api/custco/cert-custco-manage/migrationFlyway")
    public Object migrationFlyway(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.migrationFlyway(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "설정시스템 인증고객사관리 - ASP_CUST_KEY(TENANT)를 조회", notes = "설정시스템 인증고객사관리 - 로그인 ID로 ASP_CUST_KEY(TENANT)를 조회한다.")
    @PostMapping("/admin-api/custco/cert-custco-manage/tenant")
    public Object selectTenantOfLgnId(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.selectTenantOfLgnId(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - 스키마_ID 중복체크", notes = "기업고객관리 - 스키마_ID 중복체크 여부를 조회한다.")
    @PostMapping("/admin-api/custco/cert-custco-manage/dpcn-chk/schemaId")
    public Object dpcnChkSchemaId(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.dpcnChkSchemaId(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - 시스템 관리자 계정 중복체크", notes = "기업고객관리 - 시스템 관리자 계정 중복체크 여부를 조회한다.")
    @PostMapping("/admin-api/custco/cert-custco-manage/dpcn-chk/lgnId")
    public Object dpcnChkLgnId(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.dpcnChkLgnId(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - ASP_고객_키 중복체크", notes = "기업고객관리 - ASP_고객_키 중복체크 여부를 조회한다.")
    @PostMapping("/admin-api/custco/cert-custco-manage/dpcn-chk/aspCustKey")
    public Object dpcnChkAspCustKey(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.dpcnChkAspCustKey(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - 서비스 설정 - 서비스 상품 목록 조회", notes = "기업고객관리 - 서비스 설정 - 서비스 상품 목록을 조회한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/srvcGds/list")
    public Object selectSrvcGds(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.selectSrvcGds(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - 서비스 설정 - 서비스 상품 목록 조회", notes = "기업고객관리 - 서비스 설정 - 서비스 상품 목록을 조회한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/srvcGdsDtl/list")
    public Object selectSrvcGdsDtl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = certCustcoManageService.selectSrvcGdsDtl(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - IPCC 연동 설정 - IPCC 서버 목록 조회", notes = "기업고객관리 - IPCC 연동 설정 - IPCC 서버 목록을 조회한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/ipccSrvr/list")
    public Object selectIpccSrvr(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = certCustcoManageService.selectIpccSrvr(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - IPCC 연동 설정 - 전화 번호 정보 목록 조회", notes = "기업고객관리 - IPCC 연동 설정 - 전화 번호 정보 목록을 조회한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/certCustcoDsptchNo/list")
    public Object selectCustcoDsptchNo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = certCustcoManageService.selectCertCustcoDsptchNo(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - 언어 설정 - 언어 정보 목록 조회", notes = "기업고객관리 - 언어 설정 - 언어 정보 목록을 조회한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/certCustcoLocaleInfo/list")
    public Object selectCertCustcoLocaleInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = certCustcoManageService.selectCertCustcoLocaleInfo(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "해당 IPCC 서버와 연동된 인증_고객사 목록 조회", notes = "해당 IPCC 서버와 연동된 인증_고객사 목록을 조회한다.")
    @PostMapping("/admin-api/custco/cert-custco-manage/ipccSrvrCertCustco/list")
    public Object selectIpccSrvrCertCustco(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = certCustcoManageService.selectIpccSrvrCertCustco(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "IPCC 연동 설정 - 발신_번호 중복체크", notes = "기업고객관리 - 발신_번호 중복체크 여부를 조회한다.")
    @PostMapping("/admin-api/custco/cert-custco-manage/dpcn-chk/dsptchNo")
    public Object dpcnChkDsptchNo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = certCustcoManageService.dpcnChkDsptchNo(mjsonParams);

        return objRetParams;
    }

    @ApiOperation(value = "IPCC 연동 설정 - 발신_번호 삭제 가능 여부 조회", notes = "기업고객관리 - 발신_번호 삭제 가능 여부를 조회한다.")
    @PostMapping("/admin-api/custco/cert-custco-manage/chk-delete/dsptchNo")
    public Object chkDeleteDsptchNo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        //삭제 가능한 번호인지 체크 - 테넌트 전화_상담(plt_cutt_type)에 릴레이션 걸려있음.
        //테넌트 전화_상담에서 해당 발신 번호를 사용하는 설정이 있는지 체크하여 사용중이면 삭제 불가.
        TelewebJSON serviceAccountJSON = certCustcoManageService.selectServiceAccount(mjsonParams);
        TenantContext.setCurrentTenant(serviceAccountJSON.getString("SCHEMA_ID"));

        objRetParams = certCustcoManageService.chkDeleteDsptchNo(serviceAccountJSON, mjsonParams);

        //다시 custco tenant로 변경
        TenantContext.setCurrentTenant("custco");

        return objRetParams;
    }

    /**
     * Flyway 강제 실행
     */
    @ApiOperation(value = "Flyway 강제 실행", notes = "Flyway 강제 실행한다.")
    @PostMapping("/admin-api/custco/cert-custco-manage/flywalForceProc")
    public Object flywalForceProc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = certCustcoManageService.migrationFlyway(objRetParams);
        return objRetParams;
    }

    @ApiOperation(value = "기업고객관리 - PUSH 설정 - 고객사 접속 사용자 목록", notes = "기업고객관리 - PUSH 설정 - 고객사 접속 사용자 목록을 조회한다")
    @PostMapping("/admin-api/custco/cert-custco-manage/getCustcoAccessUserList")
    public Object getCustcoAccessUserList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String roomId = mjsonParams.getString("roomId");
        String schemaId = roomId.split("_")[0];

        List<String> userList = hashOpsEnterInfo.values(ENTER_INFO + "_" + roomId);

        if (userList.size() > 0) {
            if (schemaId != null && !"".equals(schemaId)) {
                //TenantContext를 schemaId로 변경한 뒤 호출.
                TenantContext.setCurrentTenant(schemaId);
                mjsonParams.setString("userList", userList.toString());
                objRetParams = certCustcoManageService.selectUserList(mjsonParams);

                //custco 스키마에서 프로시저 실행 및 서비스_생성_일시 업데이트를 위해 'custco'로 tenant 변경
                TenantContext.setCurrentTenant("custco");
            }
        }

        return objRetParams;
    }
}
