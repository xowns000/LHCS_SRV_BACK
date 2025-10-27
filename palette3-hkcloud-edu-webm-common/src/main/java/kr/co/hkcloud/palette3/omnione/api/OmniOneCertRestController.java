package kr.co.hkcloud.palette3.omnione.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Iterator;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raonwhitehat.cert.cx.CertResult;
import com.raonwhitehat.cert.cx.CertSecuManager;
import com.raonwhitehat.cert.cx.CertToken;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.omnione.app.OmniOneCertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "OmniOneCertRestController", description = "OmniOneCX 통합인증 서비스")
public class OmniOneCertRestController {

    @Value("${cert.co.app.omnionecx.oacxUrl}")
    private String oacxUrl;

    private final OmniOneCertService omniOneCertService;

    /**
     * 
     * 암복호화 토큰 생성
     * 
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "암복호화 토큰 생성", notes = "암복호화 토큰을 생성 한다.")
    @PostMapping("/api/omnione/makeAuthToken")
    public Object makeAuthToken(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        JSONArray omniOneGetInfo = omniOneCertService.omniGetInfo(mjsonParams).getDataObject(TwbCmmnConst.G_DATA);

        String cpCode = "";
        String siteKey = "";
        JSONObject resultObj = new JSONObject();
        if (omniOneGetInfo.size() > 0) {
            cpCode = omniOneGetInfo.getJSONObject(0).getString("CUSTCO_CD");
            siteKey = omniOneGetInfo.getJSONObject(0).getString("SRVC_KEY");

            String callbackUrl = "/api/omnione/makeToken";

            /** CertToken 발급 시작 */
            CertSecuManager csm = new CertSecuManager(siteKey);
            CertToken ct = csm.reqCert(cpCode, callbackUrl);

            resultObj.put("oacxUrl", oacxUrl);
            resultObj.put("status", ct.getStatus());

            if ("C001".equals(ct.getStatus())) {
                objRetParams.setHeader("ERROR_FLAG", false);

                resultObj.put("authToken", ct.getAuthToken());
                resultObj.put("certInfo", ct.getCertInfo());
            } else {
                objRetParams.setHeader("ERROR_FLAG", true);

                resultObj.put("clientMessage", ct.getMessage());
            }

        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            resultObj.put("clientMessage", "관리자 화면에서 OmniOne 키 등록 후 이용해 주시기 바랍니다.");
        }

        objRetParams.setDataObject("DATA", resultObj); //요청 이력 저장
        //최종결과값 반환
        return objRetParams;
    }

    @ApiOperation(value = "토큰 생성", notes = "토큰을 생성 한다.")
    @PostMapping("/api/omnione/makeToken")
    public Object makeToken(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    @ApiOperation(value = "인증요청 파라미터 암호화", notes = "인증요청 파라미터를 암호화 처리 한다.")
    @PostMapping("/api/omnione/enCryptInfo")
    public Object enCryptInfo(
        @TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        //반환 파라메터 생성
        JSONArray enCryptResult = new JSONArray();
        JSONObject enCryptParam = new JSONObject();
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        String authToken = mjsonParams.getString("authToken");

        SecretKeySpec skey_spec = new SecretKeySpec(authToken.getBytes("UTF-8"), "AES");
        Cipher encrypter = Cipher.getInstance("AES/ECB/PKCS5Padding");
        encrypter.init(Cipher.ENCRYPT_MODE, skey_spec);

        JSONArray jsonArray = mjsonParams.getDataObject();
        JSONObject objJson = jsonArray.getJSONObject(0);
        Iterator<String> objIterator = objJson.keys();
        while (objIterator.hasNext()) {
            String strKeyName = objIterator.next();
            String strValue = objJson.getString(strKeyName);

            if (!strKeyName.equals("CUSTCO_ID") && !strKeyName.equals("authToken") && !strKeyName.equals("USER_ID") && !strKeyName.equals(
                "PP_KEY_PP") && !strKeyName.equals("PP_ALG_PP") && !strKeyName.equals("LOCALE") && !strKeyName.equals("CUST_ID")) {

                byte[] encryptedData = encrypter.doFinal(strValue.getBytes("UTF-8"));
                String encrypted = new String(Base64.getEncoder().encode(encryptedData), "UTF-8");
                enCryptParam.put(strKeyName, encrypted);
            }
        }

        enCryptResult.add(enCryptParam);

        objRetParams.setDataObject("DATA", enCryptResult);

        omniOneCertService.omniOneRequestReg(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    @ApiOperation(value = "복호화", notes = "복호화를 처리 한다.")
    @PostMapping("/api/omnione/deCryptInfo")
    public Object deCryptInfo(
        @TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        JSONArray omniOneGetInfo = omniOneCertService.omniGetInfo(mjsonParams).getDataObject(TwbCmmnConst.G_DATA);

        String cpCode = "";
        String siteKey = "";

        if (omniOneGetInfo.size() > 0) {
            cpCode = omniOneGetInfo.getJSONObject(0).getString("CUSTCO_CD");
            siteKey = omniOneGetInfo.getJSONObject(0).getString("SRVC_KEY");

            String authToken = mjsonParams.getString("authToken");
            String token = mjsonParams.getString("token");

            CertSecuManager csm = new CertSecuManager(siteKey);
            CertResult result = csm.resCert(cpCode, authToken, token);

            objRetParams.setString("RESULT", result.toString());

            objRetParams.setString("rsltCd", result.getRsltCd());
            objRetParams.setString("message", result.getMessage());

            if ("C000".equals(result.getRsltCd())) {

                objRetParams.setString("name", result.getName());
                objRetParams.setString("birth", result.getBirth());
                objRetParams.setString("ci", result.getCi());
                objRetParams.setString("di", result.getDi());
                objRetParams.setString("ci2", result.getCi2());
                objRetParams.setString("ciUpdCnt", result.getCiUpdCnt());
                objRetParams.setString("txid", result.get("txid"));
                objRetParams.setString("cxid", result.get("cxid"));
                objRetParams.setString("phone", result.get("phone"));
                objRetParams.setString("provider", result.get("provider"));
                objRetParams.setString("resultCode", result.get("resultCode"));
                objRetParams.setString("clientMessage", result.get("clientMessage"));
                objRetParams.setString("signedData", result.get("signedData"));
                objRetParams.setString("signType", result.get("signType"));
                objRetParams.setString("nationality", result.get("nationality"));
                objRetParams.setString("gender", result.get("gender"));

                objRetParams.setString("address", result.get("address")); //면허증 주소
                objRetParams.setString("asort", result.get("asort")); // 면허증 종류
                objRetParams.setString("inspctbegend", result.get("inspctbegend")); // 적성검삭기간
                objRetParams.setString("issude", result.get("issude")); // 발행일
                objRetParams.setString("issude", result.get("lcnscndcdnm")); //면허조건명
                objRetParams.setString("locpanm", result.get("locpanm")); //발행 지방경찰청명의
                objRetParams.setString("passwordsn", result.get("passwordsn")); // 암호일련번호

                objRetParams.setString("bctypecode", result.get("bctypecode")); //증명서코드
                objRetParams.setString("bjd_addr", result.get("bjd_addr")); //주소
                objRetParams.setString("yongdo", result.get("yongdo")); //용도
                objRetParams.setString("gbgjsincheong_yn", result.get("gbgjsincheong_yn")); //군별 기재 / 미기재
                objRetParams.setString("gggjsincheong_yn", result.get("gggjsincheong_yn")); //계급 기재 / 미기재
                objRetParams.setString("bgtggjsincheong_yn", result.get("bgtggjsincheong_yn")); //병과(특기) 기재 / 미기재
                objRetParams.setString("jygbgjsincheong_yn", result.get("jygbgjsincheong_yn")); //전역구분(사유) 기재 / 미기재
                objRetParams.setString("yjgjsincheong_yn", result.get("yjgjsincheong_yn")); //역종 기재 / 미기재
                objRetParams.setString("bmbygjsincheong_yn", result.get("bmbygjsincheong_yn")); //복무분야 기재 / 미기재
                objRetParams.setString("bmgggjsincheong_yn", result.get("bmgggjsincheong_yn")); //복무계급 기재 / 미기재
                objRetParams.setString("ggrgjsincheong_yn", result.get("ggrgjsincheong_yn")); //군경력 및 기타 포함 / 미포함
                objRetParams.setString("ggrgjsincheong_cn", result.get("ggrgjsincheong_cn")); //군격력 및 기타
                objRetParams.setString("bgjssangtaekd", result.get("bgjssangtaekd")); //발급 접수상태 코드
                objRetParams.setString("damdangjajbc", result.get("damdangjajbc")); //담당자지방청
                objRetParams.setString("brcibgjeopsubh", result.get("brcibgjeopsubh")); //블록체인발급접수번호
                objRetParams.setString("ggcd", result.get("ggcd")); //결과코드
                objRetParams.setString("damdangjasm", result.get("damdangjasm"));//담당자 성명
                objRetParams.setString("ddjjibangcheongkd", result.get("ddjjibangcheongkd")); //담당자지방청코드
                objRetParams.setString("damdangjayrc", result.get("damdangjayrc")); //담당자연락처
                objRetParams.setString("ggmsg", result.get("ggmsg")); //결과 메시지
                objRetParams.setString("gunbokmuyb", result.get("gunbokmuyb")); //군복무여부
                objRetParams.setString("bjjmsbalgeupny", result.get("bjjmsbalgeupny")); //병적증명서 발급 내용
                objRetParams.setString("bjjmsbalgeupnyReplace", result.get("bjjmsbalgeupny") != null ? result.get("bjjmsbalgeupny").replace(
                    "\n", "<br/>") : "");
                objRetParams.setString("jmsyuhyogg", result.get("jmsyuhyogg")); //증명서유효기간
                objRetParams.setString("jmssayongyd", result.get("jmssayongyd")); //증명서사용용도
                objRetParams.setString("balhaengbh", result.get("balhaengbh")); //발행번호
                objRetParams.setString("balhaengij", result.get("balhaengij")); //발행일자
            }

            TelewebJSON histParams = new TelewebJSON(mjsonParams);

            histParams.setString("USER_ID", mjsonParams.getString("USER_ID"));
            histParams.setString("PROV_OG", mjsonParams.getString("PROV_OG"));
            histParams.setString("CUST_ID", mjsonParams.getString("CUST_ID"));
            histParams.setString("name", objRetParams.getString("name"));
            histParams.setString("phone", objRetParams.getString("phone"));
            histParams.setString("birth", objRetParams.getString("birth"));
            histParams.setString("authToken", authToken);
            histParams.setString("RSLT_CD", objRetParams.getString("rsltCd"));
            histParams.setString("RESULT", token);
            histParams.setString("DECRYPT_RESULT", objRetParams.toString());
            omniOneCertService.omniOneResultReg(histParams); //결과 이력 저장
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            JSONObject resultObj = new JSONObject();
            resultObj.put("clientMessage", "관리자 화면에서 OmniOne 키 등록 후 이용해 주시기 바랍니다.");
            objRetParams.setDataObject("DATA", resultObj);
        }

        //최종결과값 반환
        return objRetParams;
    }
}
