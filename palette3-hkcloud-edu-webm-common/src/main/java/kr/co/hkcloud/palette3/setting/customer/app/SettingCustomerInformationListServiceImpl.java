package kr.co.hkcloud.palette3.setting.customer.app;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Iterator;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.config.security.properties.PaletteSecurityProperties;
import kr.co.hkcloud.palette3.core.security.crypto.AES128Cipher;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper;
import kr.co.hkcloud.palette3.setting.customer.dto.CustomerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("settingCustomerInformationListService")
public class SettingCustomerInformationListServiceImpl implements SettingCustomerInformationListService {

    private final TwbComDAO mobjDao;
    private final PaletteProperties paletteProperties;
    private final PaletteSecurityProperties paletteSecurityProperties;
    private final SettingCustomerInformationListMapper settingCustomerInformationListMapper;
    private final InnbCreatCmmnService innbCreatCmmnService;

    /**
     * 설정고객정보목록 목록을 조회한다
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnPageCustInfo(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "selectRtnPageCustInfo",
            jsonParams);
    }

    /**
     * 고객정보 조회(중복된 데이터 체크)
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCustInfo(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "selectRtnCustInfo",
            jsonParams);
    }

    /**
     * 고객정보 삽입 (머지)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON insertUserInfo(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "insertUserInfo",
            jsonParams);
    }

    /**
     * 챗봇 고객정보 삽입 (머지)
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON insertCustInfo(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "insertCustInfo",
            jsonParams);
    }

    /**
     * 고객기본정보 DB에 삽입
     */
    @Override
    @Transactional(readOnly = false)
    public void mergeCustomerBaseInfo(CustomerVO customerVO) throws TelewebAppException {
        // insert / update 수정 
        if (settingCustomerInformationListMapper.updateCustomerBaseInfo(customerVO) == 0) { // 동일한 값이 없다면 update
            TelewebJSON customerParam = new TelewebJSON();
            TelewebJSON mjsonParams = new TelewebJSON();
            mjsonParams.setString("talkUserKey", customerVO.getTalkUserKey());
            customerParam = mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
                "selectCustomerBaseInfoCount", mjsonParams);
            log.info("customer_cnt" + customerParam.getString("CNT"));
            if (customerParam.getString("CNT").equals("0")) {   // /message와 /reference 동시에 들어왔을때 처리
                customerVO.setCustomerId(Integer.toString(innbCreatCmmnService.createSeqNo("CUST_ID")));
                customerVO.setCustChgHstryId(Integer.toString(innbCreatCmmnService.createSeqNo("CUST_CHG_HSTRY_ID")));

                settingCustomerInformationListMapper.insertCustomerBaseInfo(customerVO);
                settingCustomerInformationListMapper.custExpsnAttrForceRegVO(customerVO);
                settingCustomerInformationListMapper.insertChatCustomer(customerVO);
                settingCustomerInformationListMapper.insertCustomerInfoChg(customerVO);
                settingCustomerInformationListMapper.insertCustomerInfoChgDtl(customerVO);
                //            settingCustomerInformationListMapper.updateCustIdSeq(customerVO);//CUST_ID 채번 후 시퀀스 +1
                //            settingCustomerInformationListMapper.updateInfoChgIdSeq(customerVO);//고객정보변경ID 채번 후 시퀀스 +1
            }
        }
    }

    /**
     * 고객기본정보 DB에 삽입(sony)
     */
    @Override
    public void mergeCustomerBaseInfoBySony(CustomerVO customerVOBySony) throws TelewebAppException {
        settingCustomerInformationListMapper.mergeCustomerBaseInfoBySony(customerVOBySony);

    }

    /**
     * 고객기본정보 DB에 삽입(ssg)
     */
    @Override
    public void mergeCustomerBaseInfoBySsg(CustomerVO customerVOBySsg) throws TelewebAppException {
        settingCustomerInformationListMapper.mergeCustomerBaseInfoBySsg(customerVOBySsg);

    }

    /**
     * 고객기본정보 DB에 삽입(kaom)
     */
    @Override
    public void mergeCustomerBaseInfoByKaom(CustomerVO customerVoByKaom) throws TelewebAppException {
        if (settingCustomerInformationListMapper.updateCustomerBaseInfoByKaom(customerVoByKaom) == 0)   // 동일한 값이 없다면 update
            settingCustomerInformationListMapper.insertCustomerBaseInfoByKaom(customerVoByKaom);
    }

    /**
     * 고객기본정보 암호화 해제
     */
    @Override
    public String decryptCustBaseInfo(String encryptString) throws TelewebAppException {
        String decryptKey = "";
        switch (paletteProperties.getMode()) {
            case REAL: {
                decryptKey = paletteSecurityProperties.getRealEncryptKey();
                break;
            }
            default: {
                decryptKey = paletteSecurityProperties.getDevEncryptKey();
                break;
            }
        }
        String decryptString = "";
        try {
            if (encryptString != null && !"".equals(encryptString)) {
                decryptString = AES128Cipher.decryptString(encryptString, decryptKey);
            }
        } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }
        return decryptString;
    }

    /**
     * 고객정보 삭제
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON deleteRtnCustInfo(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "deleteRtnCustInfo",
            jsonParams);
    }

    /*
     * 통합고객마스터 테이블(PLT_CUS) 고객정보조회팝업 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCustInfoPop(TelewebJSON jsonParams) throws TelewebAppException {
        log.debug("jsonParams ====================   " + jsonParams);

        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성
        int iRowCnt = jsonParams.getHeaderInt("ROW_CNT");
        int iPagesCnt = jsonParams.getHeaderInt("PAGES_CNT");

        TelewebJSON custcoExpsnInfo = this.custcoExpsnInfo(jsonParams);

        JSONArray jsonObj = new JSONArray();
        if (custcoExpsnInfo.getDataObject(TwbCmmnConst.G_DATA).size() > 0) {
            jsonObj = custcoExpsnInfo.getDataObject(TwbCmmnConst.G_DATA);
        }

        if (jsonObj.size() > 0) {
            jsonParams.setString("EXPSN_ATTR_LIST", jsonObj.toString());

            //고객 확장 속성 검색
            String SCH_CUST_EXPSN_ATTR = jsonParams.getString("SCH_CUST_EXPSN_ATTR").toString();
            SCH_CUST_EXPSN_ATTR = SCH_CUST_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
            jsonParams.setString("SCH_CUST_EXPSN_ATTR", SCH_CUST_EXPSN_ATTR);

            //상담 확장 속성 검색
            String SCH_CUTT_EXPSN_ATTR = jsonParams.getString("SCH_CUTT_EXPSN_ATTR").toString();
            SCH_CUTT_EXPSN_ATTR = SCH_CUTT_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
            jsonParams.setString("SCH_CUTT_EXPSN_ATTR", SCH_CUTT_EXPSN_ATTR);

            jsonParams.setHeader("ROW_CNT", iRowCnt);
            jsonParams.setHeader("PAGES_CNT", iPagesCnt);

            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
                "selectRtnCustInfoPop", jsonParams);
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "확장항목 정보 없음");
        }
        objRetParams.setDataObject("EXPSN_ATTR", custcoExpsnInfo);
        return objRetParams;
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON custcoExpsnInfo(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성
        jsonParams.setHeader("ROW_CNT", 0);
        jsonParams.setHeader("PAGES_CNT", 0);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "custcoExpsnInfo",
            jsonParams);

        return objRetParams;
    }

    /*
     * 통합고객마스터 테이블(PLT_CUS) 고객정보조회팝업 조회 Artrhur.Kim 2021.10.14
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnComCustInqire(TelewebJSON jsonParams) throws TelewebAppException {
        log.debug("jsonParams ====================   " + jsonParams);
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "selectRtnComCustInqire",
            jsonParams);
    }

    /*
     * 캠페인고객마스터 테이블(PLT_PHN_OBD_CUS) 고객정보조회팝업 조회 Artrhur.Kim 2021.10.15
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnComObdCustInqire(TelewebJSON jsonParams) throws TelewebAppException {
        log.debug("jsonParams ====================   " + jsonParams);
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "selectRtnComObdCustInqire", jsonParams);
    }

    /*
     * 통합고객마스터 테이블(PLT_CUS) 채팅/전화메인 고객검색 버튼 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCust(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "selectRtnCust",
            jsonParams);
    }

    /*
     * 기업 정보 전광판 QUEUE 정보 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectQUEUEInqire(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "selectQUEUEInqire",
            jsonParams);
    }

    /**
     * 고객정보 저장, 수정
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON custProc(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();
        
        String sExpsnAttrList = jsonParams.getString("EXPSN_ATTR");
        sExpsnAttrList = sExpsnAttrList.replace("&#91;", "[").replace("&#93;", "]");

        if (StringUtils.isBlank(jsonParams.getString("CUST_ID")) || jsonParams.getInt("CUST_ID") == -1) { //등록
            jsonParams.setString("STAT", "REG");
            jsonParams.setString("CUST_STTS_CD", "NOML"); //등록인 경우 고객 상태가 정상인 고객만 조회

            TelewebJSON custAgreHstryExistInfo = this.custDefaultInoSelect(jsonParams);

            if (custAgreHstryExistInfo.getSize() > 0) {
                jsonParams.setString("STAT", "MOD");
                jsonParams.setString("CUST_ID", custAgreHstryExistInfo.getString("CUST_ID"));
                //1.고객 기본 정보 수정
                objRetParams = this.custMod(jsonParams);
            } else {
                //고객 전화번호 ID
                int CUST_TELNO_ID = innbCreatCmmnService.createSeqNo("CUST_TELNO_ID");

                TelewebJSON custTelNoParams = new TelewebJSON();

                custTelNoParams.setInt("CUST_TELNO_ID", CUST_TELNO_ID);
                custTelNoParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                custTelNoParams.setString("CUST_PHN_NO", jsonParams.getString("CUST_PHN_NO"));
                custTelNoParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                objRetParams = this.custTelNoReg(custTelNoParams); //1. 고객 전화번호 저장

                //고객 ID
                int CUST_ID = innbCreatCmmnService.createSeqNo("CUST_ID");
                jsonParams.setInt("CUST_ID", CUST_ID);
                objRetParams = this.custReg(jsonParams); //2.고객 기본정보 저장
                objRetParams.setHeader("GEN_CUST_ID", jsonParams.getInt("CUST_ID")); //개인정보 로깅용도.

                TelewebJSON expsnAttrParams = new TelewebJSON();

                expsnAttrParams.setInt("CUST_ID", jsonParams.getInt("CUST_ID"));
                expsnAttrParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));

                //4.고객 전화번호 통합 저장
                TelewebJSON custTelNoItgrtParams = new TelewebJSON();

                custTelNoItgrtParams.setInt("CUST_TELNO_ID", CUST_TELNO_ID);
                custTelNoItgrtParams.setInt("CUST_ID", jsonParams.getInt("CUST_ID"));
                custTelNoItgrtParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                objRetParams = this.custTelNoItgrtReg(custTelNoItgrtParams);

                //5.고객 통합 이력 저장
                //고객 통합 이력 ID
                int CUST_ITGRT_HSTRY_ID = innbCreatCmmnService.createSeqNo("CUST_ITGRT_HSTRY_ID");
                TelewebJSON custItgrtHstryParams = new TelewebJSON();

                custItgrtHstryParams.setInt("CUST_ITGRT_HSTRY_ID", CUST_ITGRT_HSTRY_ID);
                custItgrtHstryParams.setInt("CUST_TELNO_ID", CUST_TELNO_ID);
                custItgrtHstryParams.setInt("CUST_ID", jsonParams.getInt("CUST_ID"));
                custItgrtHstryParams.setString("CHG_SE_CD", "NEW"); //변경이력 최초
                custItgrtHstryParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                objRetParams = this.custItgrtHstryReg(custItgrtHstryParams);
            }

        } else { //수정
            TelewebJSON custAgreHstryExistInfo = this.custDefaultInoSelect(jsonParams);
            //채팅으로 처음 인입된 후 전화번호를 입력받았을 때(수정) 전화번호가 중복이 아닐때
            if (custAgreHstryExistInfo.getString("CUST_PHN_NO").equals("") || custAgreHstryExistInfo.getString("CUST_PHN_NO") == null) {
                //고객 전화번호 ID
                int CUST_TELNO_ID = innbCreatCmmnService.createSeqNo("CUST_TELNO_ID");

                TelewebJSON custTelNoParams = new TelewebJSON();

                custTelNoParams.setInt("CUST_TELNO_ID", CUST_TELNO_ID);
                custTelNoParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                custTelNoParams.setString("CUST_PHN_NO", jsonParams.getString("CUST_PHN_NO"));
                custTelNoParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                objRetParams = this.custTelNoReg(custTelNoParams); //1. 고객 전화번호 저장

                //2.고객 전화번호 통합 저장
                TelewebJSON custTelNoItgrtParams = new TelewebJSON();

                custTelNoItgrtParams.setInt("CUST_TELNO_ID", CUST_TELNO_ID);
                custTelNoItgrtParams.setInt("CUST_ID", jsonParams.getInt("CUST_ID"));
                custTelNoItgrtParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                objRetParams = this.custTelNoItgrtReg(custTelNoItgrtParams);

                //3.고객 통합 이력 저장
                //고객 통합 이력 ID
                int CUST_ITGRT_HSTRY_ID = innbCreatCmmnService.createSeqNo("CUST_ITGRT_HSTRY_ID");
                TelewebJSON custItgrtHstryParams = new TelewebJSON();

                custItgrtHstryParams.setInt("CUST_ITGRT_HSTRY_ID", CUST_ITGRT_HSTRY_ID);
                custItgrtHstryParams.setInt("CUST_TELNO_ID", CUST_TELNO_ID);
                custItgrtHstryParams.setInt("CUST_ID", jsonParams.getInt("CUST_ID"));
                custItgrtHstryParams.setString("CHG_SE_CD", "NEW"); //변경이력 최초
                custItgrtHstryParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                objRetParams = this.custItgrtHstryReg(custItgrtHstryParams);
            }

            jsonParams.setString("STAT", "MOD");
            //1.고객 기본 정보 수정
            objRetParams = this.custMod(jsonParams);
        }

        //고객 정보 변경 이력 ID
        int CUST_CHG_HSTRY_ID = innbCreatCmmnService.createSeqNo("CUST_CHG_HSTRY_ID");
        jsonParams.setInt("CUST_CHG_HSTRY_ID", CUST_CHG_HSTRY_ID);
        objRetParams = this.custInfoChgHistReg(jsonParams); //고객 정보 변경 이력 저장

        //2.고객 확장 정보 저장
//        JSONArray jsonObj = jsonParams.getDataObject(TwbCmmnConst.G_DATA);
//        for (int i = 0; i < jsonObj.size(); i++) {
//            JSONObject objData = jsonObj.getJSONObject(i);

//            @SuppressWarnings("rawtypes") Iterator it = objData.keys();
//            while (it.hasNext()) {
//                String strKey = (String) it.next();
//                String strValue = objData.getString(strKey);

//                if (strKey.indexOf("EXPSN_ATTR") > -1 && StringUtils.isNotEmpty(strValue)) {
//                    strValue = strValue.replace("&#91;", "[").replace("&#93;", "]");
        			if(!StringUtils.isEmpty(sExpsnAttrList)) {
	                    JSONArray arryExpsnAttr = JSONArray.fromObject(sExpsnAttrList);
	
	                    if (arryExpsnAttr.size() > 0) {
	                        for (Object expsnAttr : arryExpsnAttr) {
	                            TelewebJSON expsnAttrParams = new TelewebJSON();
	
	                            expsnAttrParams.setString("CUST_ID", jsonParams.getString("CUST_ID"));
	                            expsnAttrParams.setString("USER_ID", jsonParams.getString("USER_ID"));
	                            expsnAttrParams.setString("PP_ALG_PP", jsonParams.getString("PP_ALG_PP"));
	                            expsnAttrParams.setString("PP_KEY_PP", jsonParams.getString("PP_KEY_PP"));
	                            expsnAttrParams.setString("EXPSN_ATTR_COL_ID", (String) ((JSONObject) expsnAttr).get("EXPSN_ATTR_COL_ID"));
	                            expsnAttrParams.setString("ATTR_ID", (String) ((JSONObject) expsnAttr).get("ATTR_ID"));
	                            expsnAttrParams.setString("ATTR_VL", (String) ((JSONObject) expsnAttr).get("V_POST_PARAM"));
	                            expsnAttrParams.setString("INDI_INFO_ENCPT_YN", (String) ((JSONObject) expsnAttr).get("INDI_INFO_ENCPT_YN"));
	                            expsnAttrParams.setInt("CUST_CHG_HSTRY_ID", CUST_CHG_HSTRY_ID);
	
	                            objRetParams = this.custExpsnAttrMerge(expsnAttrParams); //고객 확장 정보 저장, 수정
	
	                            objRetParams = this.custInfoExpsnAttrReg(expsnAttrParams); //고객 정보 확장 속성 저장
	                        }
	                    }
        			}
//                }
//            }
//        }

        objRetParams.setDataObject(TwbCmmnConst.G_DATA, jsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 고객 전화번호 체크
     */
    @Override
    public TelewebJSON custTelNoCheck(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "custTelNoCheck",
            jsonParams);
    }

    /**
     * 동일 전화번호 존재 시 채팅 키를 통해 통합 가능 고객 판단
     */
    @Override
    public TelewebJSON chtCustDuplChk(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "chtCustDuplChk",
            jsonParams);
    }

    /**
     * 고객 전화번호 저장
     */
    public TelewebJSON custTelNoReg(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "custTelNoReg",
            jsonParams);
    }

    /**
     * 고객 기본 정보 저장
     */
    public TelewebJSON custReg(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "custReg", jsonParams);
    }

    /**
     * 고객 전화번호 통합 저장
     */
    public TelewebJSON custTelNoItgrtReg(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "custTelNoItgrtReg",
            jsonParams);
    }

    /**
     * 고객 통합 이력 저장
     */
    public TelewebJSON custItgrtHstryReg(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "custItgrtHstryReg",
            jsonParams);
    }

    /**
     * 고객 기본 정보 수정 및 주의 고객 정보 저장
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON custMod(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "custMod", jsonParams);
    }

    /**
     * 고객 확장 정보 저장, 수정
     */
    @Transactional(readOnly = false)
    public TelewebJSON custExpsnAttrMerge(TelewebJSON jsonParams) throws TelewebAppException {

        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "custExpsnAttrMerge", jsonParams);

        if ("CUST_STAT".equals(jsonParams.getString("EXPSN_ATTR_COL_ID"))) {
            jsonParams.setString("CUST_STTS_CD", jsonParams.getString("ATTR_VL"));
            //            if("DELT".equals(jsonParams.getString("ATTR_VL"))){ //삭제인 경우
            //                jsonParams.setString("DEL_YN", "Y");
            //            }else{ //삭제가 아닌 경우
            //                jsonParams.setString("DEL_YN", "N");
            //            }
            this.custMod(jsonParams);
        }

        return objRetParams;
    }

    /**
     * 
     * 고객 정보 변경 이력 저장
     * 
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON custInfoChgHistReg(TelewebJSON jsonParams) throws TelewebAppException {

        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "custInfoChgHistReg", jsonParams);

        return objRetParams;
    }

    /**
     * 
     * 고객 정보 변경 확장 속성 저장
     * 
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON custInfoExpsnAttrReg(TelewebJSON jsonParams) throws TelewebAppException {

        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "custInfoExpsnAttrReg", jsonParams);

        return objRetParams;
    }

    /**
     * 고객정보 저장, 수정
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON custAgreHstryProc(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();

        TelewebJSON custAgreHstryExistInfo = mobjDao.select(
            "kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "custAgreHstryExistInfo", jsonParams);

        if (custAgreHstryExistInfo.getDataObject("DATA").size() == 0) { //등록
            objRetParams = this.custAgreHstryReg(jsonParams);
        } else { //수정
            objRetParams = this.custAgreHstryMod(jsonParams);
        }

        return objRetParams;
    }

    /**
     * 고객 동의 이력 저장
     */
    public TelewebJSON custAgreHstryReg(TelewebJSON jsonParams) throws TelewebAppException {

        TelewebJSON objRetParams = new TelewebJSON();
        //고객 ID
        jsonParams.setInt("CUST_AGRE_HSTRY_ID", innbCreatCmmnService.createSeqNo("CUST_AGRE_HSTRY_ID"));

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "custAgreHstryReg", jsonParams);

        return objRetParams;
    }

    /**
     * 고객 동의 이력 수정
     */
    public TelewebJSON custAgreHstryMod(TelewebJSON jsonParams) throws TelewebAppException {

        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "custAgreHstryMod", jsonParams);

        return objRetParams;
    }

    /**
     * 고객 기본 정보 조회
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON custDefaultInoSelect(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "custSelect",
            jsonParams);

        return objRetParams;
    }

    /**
     * 고객 기본 정보 조회 및 신규 고객 생성
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON custSelect(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = this.custDefaultInoSelect(jsonParams);

        if (objRetParams.getDataObject("DATA").size() == 0) { //등록
            if (!"-1".equals(jsonParams.getString("CUST_ID"))) {
                TelewebJSON result = this.custProc(jsonParams);

                //고객 확장 정보 고객상태 정상 강제 적용
                objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
                    "custExpsnAttrForceReg", result);

                objRetParams = this.custDefaultInoSelect(result);
            }
        }

        return objRetParams;
    }

    /**
     * 고객 확장 정보 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON custExpsnInfoSelect(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "custExpsnInfoSelect",
            jsonParams);
    }

    /**
     * 고객 동의 정보 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON custAgreeInfoSelect(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "custAgreeInfoSelect",
            jsonParams);
    }

    /**
     * PALETTE3 상담 이력 목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON integCuttHistList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "integCuttHistList",
            mjsonParams);
    }

    /**
     * PALETTE3 VOC 목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON vocList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "vocList", mjsonParams);
    }

    /**
     * PALETTE3 예약 콜 목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON rsvtCallList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "rsvtCallList",
            mjsonParams);
    }

    /**
     * PALETTE3 콜백 목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON callBackList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "callBackList",
            mjsonParams);
    }

    /**
     * PALETTE3 캠페인 목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON cpiStatHistList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "cpiStatHistList",
            mjsonParams);
    }

    /**
     * 고객 통합
     */
    @Transactional(readOnly = false)
    public TelewebJSON mergeCust(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON resultParams = new TelewebJSON(mjsonParams);

        mjsonParams.setString("CUST_ID", mjsonParams.getString("AF_CUST_ID"));
        //통합할 고객 확장정보 조회
        resultParams = mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "custExpsnInfoSelect", mjsonParams);
        if (resultParams.getDataObject("DATA").size() != 0) { //조회값이 있는지 확인
            //고객 확장 정보 통합
            JSONArray jsonObj = resultParams.getDataObject("DATA");
            for (int i = 0; i < jsonObj.size(); i++) {
                JSONObject objData = jsonObj.getJSONObject(i);
                TelewebJSON expsnAttrParams = new TelewebJSON();

                expsnAttrParams.setString("CUST_ID", mjsonParams.getString("BF_CUST_ID"));
                expsnAttrParams.setString("ATTR_ID", objData.getString("ATTR_ID"));
                expsnAttrParams.setString("ATTR_VL", objData.getString("ATTR_VL"));

                log.info("expsnAttrParams" + expsnAttrParams);

                this.custExpsnAttrMerge(expsnAttrParams); //고객 확장 정보 저장, 수정
            }
        }
        //고객명 선택한 고객명으로 통합
        resultParams = mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "updateCustNm",
            mjsonParams);

        //전화고객인지 확인 -> 통합 이후 전화고객 확인이 안됨
        //        resultParams = mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "selectCustType",
        //            mjsonParams);
        //        if (resultParams.getString("CNT").equals("0")) {
        //            //채팅고객 테이블에 없으므로 해당 고객은 전화고객임
        //            mjsonParams.setString("CUST_ID", mjsonParams.getString("BF_CUST_ID"));        //통합할 CUST_ID
        //            mjsonParams.setString("ORG_CUST_ID", mjsonParams.getString("AF_CUST_ID"));    //통합될 CUST_ID
        //        } else {
        //채팅고객 테이블에 있으므로 해당 고객은 채팅고객임
        //무조건 먼저 들어온 cust_id로 고객 통합 (cust_id가 작은쪽으로 통합)
        if (Integer.parseInt(mjsonParams.getString("BF_CUST_ID")) > Integer.parseInt(mjsonParams.getString("AF_CUST_ID"))) {
            mjsonParams.setString("ORG_CUST_ID", mjsonParams.getString("BF_CUST_ID"));  //통합될 CUST_ID
        } else {
            mjsonParams.setString("CUST_ID", mjsonParams.getString("BF_CUST_ID"));      //통합할 CUST_ID
            mjsonParams.setString("ORG_CUST_ID", mjsonParams.getString("AF_CUST_ID"));  //통합될 CUST_ID
        }
        //        }
        //고객 통합 진행
        //plt_cht_user_rdy_hstry
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "updateChtUserRdyHstry", mjsonParams);

        //고객 동의이력은 한 고객이라도 있으면 있는걸로 통합
        //plt_cust_agre_hstry
        resultParams = mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "updateCustAgre",
            mjsonParams);
        resultParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "deleteCustAgreDupl", mjsonParams);

        //plt_cht_rdy
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "updateChtRdy",
            mjsonParams);

        //plt_voc_rcpt
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "updateVocRcpt",
            mjsonParams);

        //plt_cutt_itgrt_hstry
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "updateCuttItgrtHstry", mjsonParams);

        //plt_cht_cutt
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "updateChtCutt",
            mjsonParams);

        //plt_cht_cutt_hstry
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "updateChtCuttHstry", mjsonParams);

        //plt_cht_cust
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "updateChtCust",
            mjsonParams);

        //plt_srvy_trgt
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "updateSrvyTrgt",
            mjsonParams);

        //plt_stats_cht_cutt_dtl
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "updateStatsChtCuttDtl", mjsonParams);

        //plt_obd_cpi 아웃바운드 캠페인은 기존 cust_id조회 후 통합 cust_id로 insert,delete
        resultParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "insertObdCpiCust", mjsonParams);
        resultParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "deleteObdCpiCust", mjsonParams);

        resultParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "insertObdCpiCustAltmnt", mjsonParams);
        resultParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "deleteObdCpiCustAltmnt", mjsonParams);

        resultParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "insertObdCpiCustDtl", mjsonParams);
        resultParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "deleteObdCpiCustDtl", mjsonParams);

        //        //plt_obd_cpi_cust
        //        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
        //            "updateObdCpiCust", mjsonParams);
        //
        //        //plt_obd_cpi_cust_altmnt
        //        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
        //            "updateObdCpiCustAltmnt", mjsonParams);
        //
        //        //plt_obd_cpi_cust_dtl
        //        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
        //            "updateObdCpiCustDtl", mjsonParams);

        //plt_phn_clbk
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "updatePhnClbk",
            mjsonParams);

        //plt_phn_cutt
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "updatePhnCutt",
            mjsonParams);

        //plt_phn_dsptch_hstry
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "updatePhnDsptchHstry", mjsonParams);

        //plt_prvc_inq_log
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "updatePrvcInqLog", mjsonParams);

        //plt_stats_phn_cutt_dtl
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "updateStatsPhnCuttDtl", mjsonParams);

        //고객확장정보삭제 -> 고객정보 삭제
        //고객 확장정보 완전삭제가 아닌 소프트삭제 -> 고객정보는 삭제할 필요 없음
        //고객 확장항목에서 고객상태 - 삭제 는 빠짐 -> 고객정보에 삭제여부 추가
        //        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
        //            "deleteCustExpsnInfo", mjsonParams);

        //고객 통합 표시 (이름 뒤에 통합id 표시)
        resultParams = mobjDao.update("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "updateCustNmCustId", mjsonParams);

        //전화번호 통합 삭제
        //plt_cust_telno테이블에 cust_id가 없기 때문에 삭제할 CUST_TELNO_ID를 조회해와야 함
        resultParams = mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
            "selectTelnoId", mjsonParams);
        if(!resultParams.getHeaderString("COUNT").equals("0")){
        	mjsonParams.setString("CUST_TELNO_ID", resultParams.getString("CUST_TELNO_ID"));
            //plt_cust_itgrt_hstry
            resultParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
                "deleteCustItgrtHstry", mjsonParams);
            //plt_cust_telno_itgrt
            resultParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
                "deleteCustTelnoItgrt", mjsonParams);
            //plt_cust_telno
            resultParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "deleteCustTelno",
                mjsonParams);
        }
        //resultParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "deleteCustAgreDupl", mjsonParams);

        resultParams.setHeader("CUST_ID", mjsonParams.getString("CUST_ID"));
        return resultParams;
    }

}
