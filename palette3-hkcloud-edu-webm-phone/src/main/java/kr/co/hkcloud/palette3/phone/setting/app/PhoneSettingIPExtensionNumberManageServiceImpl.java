package kr.co.hkcloud.palette3.phone.setting.app;


import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("PhoneSettingIPExtensionNumberManageService")
public class PhoneSettingIPExtensionNumberManageServiceImpl implements PhoneSettingIPExtensionNumberManageService
{
    private final TwbComDAO mobjDao;


    /**
     * ip내선번호관리 목록 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON selectRtnPhoneSettingIPExtensionNumberManage(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.setting.dao.PhoneSettingIPExtensionNumberManageMapper", "selectRtnPhoneSettingIPExtensionNumberManage", mjsonParams);
    }


    /**
     * ip내선번호관리 등록
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertCheckRtnPhoneSettingIPExtensionNumberManage(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.setting.dao.PhoneSettingIPExtensionNumberManageMapper", "insertCheckRtnPhoneSettingIPExtensionNumberManage", mjsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnPhoneSettingIPExtensionNumberManage(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.setting.dao.PhoneSettingIPExtensionNumberManageMapper", "insertRtnPhoneSettingIPExtensionNumberManage", mjsonParams);
    }


    /**
     * ip내선번호관리 수정
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnPhoneSettingIPExtensionNumberManage(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	//update구문 사용시 where절에 user_id가 들어가기 때문에 user_id를 변경할 수 없음
        //return mobjDao.update("kr.co.hkcloud.palette3.phone.setting.dao.PhoneSettingIPExtensionNumberManageMapper", "updateRtnPhoneSettingIPExtensionNumberManage", mjsonParams);
    	
    	//해당 ip delete후 insert
    	mobjDao.delete("kr.co.hkcloud.palette3.phone.setting.dao.PhoneSettingIPExtensionNumberManageMapper", "deleteRtnPhoneSettingIPExtensionNumberManage", mjsonParams);
    	return mobjDao.insert("kr.co.hkcloud.palette3.phone.setting.dao.PhoneSettingIPExtensionNumberManageMapper", "insertRtnPhoneSettingIPExtensionNumberManage", mjsonParams);
    }


    /**
     * ip내선번호관리 삭제
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnPhoneSettingIPExtensionNumberManage(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.phone.setting.dao.PhoneSettingIPExtensionNumberManageMapper", "deleteRtnPhoneSettingIPExtensionNumberManage", mjsonParams);
    }


    /**
     * ip내선번호관리 호분배
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */

    @Override
    public TelewebJSON UserBatchInterface(TelewebJSON mjsonParams) throws TelewebAppException
    {
        try {
            //String custcoId = mjsonParams.getString("ASP_NEWCUST_KEY_IN").toString();
            //String custcoIdArray[] = custcoId.split(",");

            TelewebJSON objRetParams = new TelewebJSON(); // 조회 파라미터
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.setting.dao.PhoneSettingIPExtensionNumberManageMapper", "selectQueueNum", mjsonParams);
            System.out.println("objRetParams====>" + objRetParams);
            System.out.println("objRetParams.getString(\"DATA\")====>" + objRetParams.getDataObject("DATA"));
            System.out.println("objRetParams.size====>" + objRetParams.getDataObject("DATA").size());
            JSONArray data_list = (JSONArray) objRetParams.getDataObject("DATA");
            System.out.println("data_list====>" + data_list);

            String[] custcoIdArray = new String[data_list.size()];
            for(int i = 0; i < data_list.size(); i++) {
                JSONObject imsi = (JSONObject) data_list.get(i);
                String queue = (String) imsi.get("QUEUE_NUM");
                System.out.println("queue==>" + queue);
                custcoIdArray[i] = queue;
            }

            String aspUserId = mjsonParams.getString("USER_ID");
            String inLineNo = mjsonParams.getString("INLNE_NO");
            //String custcoId = mjsonParams.getString("ASP_NEWCUST_KEY");
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); //타임아웃 설정 5초
            factory.setReadTimeout(5000);//타임아웃 설정 5초

            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            String url = "http://121.67.187.236:60080/API/";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("REQ", "agent_forwarding");
            jsonObject.put("AGENT_ID", aspUserId);
            jsonObject.put("EXTENSION_NO", inLineNo);
            jsonObject.put("FKEY", "QUEUE");

            JSONArray req_array = new JSONArray();
            for(int i = 0; i < custcoIdArray.length; i++) {
                //data.put(null, custcoIdArray[i]);
                req_array.add(custcoIdArray[i]);
            }

            jsonObject.put("FDATA", req_array);

            log.debug("jsonObject =====================" + jsonObject);

            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();

            parameters.add("tplexParam", jsonObject);
            log.debug("parameters ====" + parameters.toString());
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, header);

            String result = restTemplate.postForObject(new URI(url), request, String.class);

            JSONObject jsonObj = JSONObject.fromObject(result);

            String ResultCode = jsonObj.getString("result_code");
            String ErrorMsg = "";

            switch(ResultCode)
            {
                case "1":
                    ErrorMsg = "등록되지 않은 상담사 ID 입니다.";
                    break;
                case "2":
                    ErrorMsg = "중복된 큐가 있습니다.";
                    break;
                case "3":
                    ErrorMsg = "등록되지 않은 큐 입니다.";
                    break;
                case "4":
                    ErrorMsg = "상담사가 로그인 상태입니다.";
                    break;
                case "5":
                    ErrorMsg = "FDATA 값이 없습니다.";
                    break;
                case "6":
                    ErrorMsg = "FKEY 값이 없습니다.";
                    break;
                case "7":
                    ErrorMsg = "중복된 업체코드가 있습니다.";
                    break;
                case "8":
                    ErrorMsg = "등록되지 않은 업체코드가 있습니다.";
                    break;

            }

            mjsonParams.setString("userVaild", jsonObj.getString("result_code"));
            mjsonParams.setString("Error", ErrorMsg);

            log.debug("jsonObj=======" + jsonObj);

        }
        catch(HttpClientErrorException | HttpServerErrorException e) {

            System.out.println(e.toString());

        }
        catch(Exception e) {

            System.out.println(e.toString());
        }

        return mjsonParams;
    }


    /**
     * ip내선번호관리 내선번호 존재여부
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertInlneNoCheckRtnPhoneSettingIPExtensionNumberManage(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.setting.dao.PhoneSettingIPExtensionNumberManageMapper", "insertInlneNoCheckRtnPhoneSettingIPExtensionNumberManage", mjsonParams);
    }


    /**
     * ip내선번호관리 해당 사용자 id에 따른 내선번호관리 자동채우기
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertInlneNoFillRtnPhoneSettingIPExtensionNumberManage(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.setting.dao.PhoneSettingIPExtensionNumberManageMapper", "insertInlneNoFillRtnPhoneSettingIPExtensionNumberManage", mjsonParams);
    }


    /*
     * ip내선번호관리 삭제 전 CUSTCO_ID에 2개 이상의 회사명이 들어가있는지 확인
     * 
     * @param mjsonParams
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON selectAspkeyCheckPhoneSettingIPExtensionNumberManage(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.setting.dao.PhoneSettingIPExtensionNumberManageMapper", "selectAspkeyCheckPhoneSettingIPExtensionNumberManage", mjsonParams);
    }


    /*
     * ip내선번호관리 CUSTCO_ID에 2개 이상의 회사명 업데이트
     * 
     * @param mjsonParams
     * 
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateAspkeyCheckPhoneSettingIPExtensionNumberManage(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.phone.setting.dao.PhoneSettingIPExtensionNumberManageMapper", "updateAspkeyCheckPhoneSettingIPExtensionNumberManage", mjsonParams);
    }
}
