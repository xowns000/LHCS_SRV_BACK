package kr.co.hkcloud.palette3.v3.gwm.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.transaction.Transactional;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.security.crypto.AES256Cipher;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.v3.gwm.service.V3GwmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * packageName    : kr.co.hkcloud.palette3.v3.gwm.service.impl
 * fileName       : V3GwmServiceImpl
 * author         : njy
 * date           : 2024-03-13
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-13           njy            최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("V3GwmService")
public class V3GwmServiceImpl implements V3GwmService {

    private final TwbComDAO twbComDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
    @Value("${api.gwm.encryptKey}")
    private String KEY;

    @Override
    @Transactional
    public TelewebJSON mergeCustInfo(
        TelewebJSON jsonParam) throws TelewebAppException, InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams.setHeader("ERROR_FLAG", false);
        JSONObject objBody = jsonParam.getDataObject("body").getJSONObject(0);
        // 0. 고객정보
        jsonParam.setString("CUSTCO_ID", objBody.getString("CUSTCO_ID"));
        jsonParam.setString("USER_ID", objBody.getString("USER_ID"));

        jsonParam.setString("CUST_PHN_NO", (AES256Cipher.decryptString(objBody.getString("customerTelNo"), KEY).replaceAll("-", "")));
        jsonParam.setString("CUST_NM", AES256Cipher.decryptString(objBody.getString("customerNm"), KEY));
        jsonParam.setString("MDFCN_DT", AES256Cipher.decryptString(objBody.getString("customerStatUdt"), KEY));
        jsonParam.setString("MEMBER_SEQ_NO", AES256Cipher.decryptString(objBody.getString("memberNo"), KEY));
        String customerStat = (AES256Cipher.decryptString(objBody.getString("customerStat"), KEY));
        if ("ACT".equals(customerStat)) {
            jsonParam.setString("CUST_STAT", "NOML");   //정상
        } else if ("DRMNCY".equals(customerStat)) {
            jsonParam.setString("CUST_STAT", "SELP");   //휴면
        } else if ("SECSN".equals(customerStat)) {
            jsonParam.setString("CUST_STAT", "DELT");   //탈퇴,삭제
        } else {
            jsonParam.setString("CUST_STAT", customerStat);
        }
        // 1. 고객 전화번호 검색
        //고객정보 검색을 위한 TelewebJSON 파라미터
        JSONArray arrCustInfo = this.selectCustByPhnNo(jsonParam).getDataObject("DATA");
        // 2. 존재하는 고객 전화번호 update
        if (!arrCustInfo.isEmpty()) {
            jsonParam.setString("CUST_ID", arrCustInfo.getJSONObject(0).getString("CUST_ID"));
            String custSttsCd = arrCustInfo.getJSONObject(0).getString("CUST_STTS_CD");
            /* 1. 고객 기본정보 update */
            this.updateCust(jsonParam);
            // 2. 회원번호, 고객상태 확장속성 등록
            this.deleteInsertCustExpsnAttr(jsonParam);
        } else {
            // 3. 존재하지 않는 고객 전화번호 insert
            // 고객 기본정보 등록
            int custId = innbCreatCmmnService.createSeqNo("CUST_ID");
            jsonParam.setInt("CUST_ID", custId);
            this.insertCust(jsonParam); //1.고객 기본정보 저장

            //고객 전화번호 ID
            int custTelNoId = innbCreatCmmnService.createSeqNo("CUST_TELNO_ID");
            jsonParam.setInt("CUST_TELNO_ID", custTelNoId);
            this.insertCustTelNo(jsonParam); // 2. 고객 전화번호 저장

            //3.고객 전화번호 통합 저장
            jsonParam.setInt("CUST_TELNO_ID", custTelNoId);
            this.custTelNoItgrtReg(jsonParam);

            // 확장속성 저장
            this.deleteInsertCustExpsnAttr(jsonParam);

            //5.고객 통합 이력 저장
            //고객 통합 이력 ID
            int custItgrtHstryId = innbCreatCmmnService.createSeqNo("CUST_ITGRT_HSTRY_ID");
            TelewebJSON custItgrtHstryParams = new TelewebJSON();

            custItgrtHstryParams.setInt("CUST_ITGRT_HSTRY_ID", custItgrtHstryId);
            custItgrtHstryParams.setInt("CUST_TELNO_ID", custTelNoId);
            custItgrtHstryParams.setInt("CUST_ID", jsonParam.getInt("CUST_ID"));
            custItgrtHstryParams.setString("CHG_SE_CD", "NEW-BO"); //변경이력 최초
            custItgrtHstryParams.setString("USER_ID", jsonParam.getString("USER_ID"));
            this.custItgrtHstryReg(custItgrtHstryParams);
        }

        //고객 정보 변경 이력 저장
        this.insertCustInfoChgHist(jsonParam);

        return objRetParams;
    }
    private TelewebJSON selectCustByPhnNo(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON();
        objRetParam = twbComDao.select("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "selectCustByPhnNo", jsonParams);
        return objRetParam;
    }
    private TelewebJSON updateCust(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON();
        objRetParam = twbComDao.update("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "updateCustDefaultInfo", jsonParams);
        return objRetParam;
    }
    private TelewebJSON insertCustTelNo(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDao.insert("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "insertCustTelNo", jsonParams);
    }
    private TelewebJSON insertCust(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDao.insert("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "insertCust", jsonParams);
    }
    private TelewebJSON custTelNoItgrtReg(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDao.insert("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "insertCustTelNoItgrt", jsonParams);
    }
    private TelewebJSON custItgrtHstryReg(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDao.insert("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "insertCustItgrtHstry", jsonParams);
    }
    private TelewebJSON deleteInsertCustExpsnAttr(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON udtAttrParams = new TelewebJSON();
        // memberNo 멤버 번호
        udtAttrParams.setString("CUST_ID", jsonParams.getString("CUST_ID"));
        udtAttrParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
        udtAttrParams.setString("ATTR_ID", "MEMBER_SEQ_NO");
        udtAttrParams.setString("ATTR_VL", jsonParams.getString("MEMBER_SEQ_NO"));
        twbComDao.delete("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "deleteCustExpsnAttr", udtAttrParams);
        twbComDao.insert("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "insertCustExpsnAttr", udtAttrParams);

        // customerStat 고객 상태
        udtAttrParams.setString("ATTR_ID", "CUST_STAT");
        udtAttrParams.setString("ATTR_VL", jsonParams.getString("CUST_STAT"));
        twbComDao.delete("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "deleteCustExpsnAttr", udtAttrParams);
        twbComDao.insert("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "insertCustExpsnAttr", udtAttrParams);
        return udtAttrParams;
    }
    private TelewebJSON insertCustInfoChgHist(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON custinfoChgDtlParams = new TelewebJSON();
        int CUST_CHG_HSTRY_ID = innbCreatCmmnService.createSeqNo("CUST_CHG_HSTRY_ID");
        custinfoChgDtlParams.setInt("CUST_CHG_HSTRY_ID", CUST_CHG_HSTRY_ID);
        custinfoChgDtlParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
        custinfoChgDtlParams.setString("USER_ID", jsonParams.getString("USER_ID"));
        custinfoChgDtlParams.setString("CUST_ID", jsonParams.getString("CUST_ID"));
        twbComDao.insert("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "insertCustInfoChgHist", custinfoChgDtlParams);

        // memberNo 멤버 번호
        custinfoChgDtlParams.setString("ATTR_ID", "MEMBER_SEQ_NO");
        custinfoChgDtlParams.setString("ATTR_VL", jsonParams.getString("MEMBER_SEQ_NO"));
        twbComDao.insert("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "insertCustInfoChgHistDtl", custinfoChgDtlParams);

        // customerStat 고객 상태
        custinfoChgDtlParams.setString("ATTR_ID", "CUST_STAT");
        custinfoChgDtlParams.setString("ATTR_VL", jsonParams.getString("CUST_STAT"));
        twbComDao.insert("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "insertCustInfoChgHistDtl", custinfoChgDtlParams);

        return custinfoChgDtlParams;
    }

    @Override
    @Transactional
    public TelewebJSON selectCustomer(
        TelewebJSON jsonParam) throws TelewebAppException, InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams.setHeader("ERROR_FLAG", false);
        JSONObject objBody = jsonParam.getDataObject("body").getJSONObject(0);

        // 0. 고객정보
        jsonParam.setString("SCHEMA_ID", objBody.getString("SCHEMA_ID"));
        jsonParam.setString("CUSTCO_ID", objBody.getString("CUSTCO_ID"));
        jsonParam.setString("USER_ID", objBody.getString("USER_ID"));
        if( objBody.containsKey( "customerTelNo" ) ) {
            jsonParam.setString("CUST_PHN_NO", AES256Cipher.decryptString(objBody.getString("customerTelNo"), KEY));
        }
        if(objBody.containsKey("memberNo")) {
            jsonParam.setString("MEMBER_SEQ_NO", AES256Cipher.decryptString(objBody.getString("memberNo"), KEY));
        }
        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.v3.customer.dao.V3GwmMapper", "selectCustomer", jsonParam);
        if( objRetParams.getHeaderInt("COUNT") > 0 ) {
            JSONArray returnArray = new JSONArray();
            JSONObject custInfo = (JSONObject)objRetParams.getDataObject("DATA").get(0);
            Iterator i = custInfo.keys();
            while (i.hasNext()) {
                String key = i.next().toString();
                custInfo.put(key, AES256Cipher.encryptString(custInfo.getString(key), KEY));
            }
            returnArray.add(custInfo);
            objRetParams.setDataObject( returnArray );
        }

        return objRetParams;
    }
}
