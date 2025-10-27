package kr.co.hkcloud.palette3.infra.tplex.cti.api;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.infra.tplex.cti.app.IvrCallbackDataService;
import kr.co.hkcloud.palette3.infra.tplex.cti.util.IvrRestTemplateUtils;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCustomerInformationListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * IVR콜백데이터 REST 컨트롤러 클래스
 * 
 * @author dckim
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "IvrCallbackDataRestController", description = "IVR콜백데이터 REST 컨트롤러")
public class IvrCallbackDataRestController {

    private final IvrCallbackDataService ivrCallbackDataService;
    private final IvrRestTemplateUtils ivrRestTemplateUtils;

    /**
     * 
     * 콜백 등록
     * 
     * @Method Name : callbackRegistProcess
     * @date : 2024. 3. 6.
     * @author : NJY
     * @version : 1.1
     * ----------------------------------------
     * @param request IVR 전송 리소스
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "콜백등록", notes = "콜백등록")
    @PostMapping("/api/infra/tplex/cti/callback-regist/process")
    public String callbackRegistProcess(HttpServletRequest request) throws TelewebApiException{
        return ivrCallbackDataService.callbackRegistProcess(request);
    }

    @ApiOperation(value = "기업전광판조회", notes = "기업전광판조회")

    @PostMapping("/api/infra/tplex/cti/cnsl-info/inqire")
    public Object selectCnslInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //String QUEUE = "";
        //int queueNumber = 3;
        //Queue[] queue = new Queue[queueNumber];
        //String[] Queue = ["91000"];
        //QUEUE = mjsonParams.getString("QUEUE");
        String queueData = "";

        queueData = mjsonParams.getString("queueData");
        String[] QueueStr = queueData.split(",");
        System.out.println("queueData====>" + queueData);
        //queueData.split(",")
        ArrayList<String> Queue = new ArrayList<String>();
        Queue.add(queueData);
        //String QueueStr = StringUtils.join(Queue, ",");
        // http parameters 값 세팅
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("REQ", "queue_call_today");
        jsonObject.put("QUEUE", Arrays.toString(QueueStr));
        //jsonObject.put("QUEUE", ["91000"]);

        //String textValue = String.valueOf(jsonObject);
        System.out.println("jsonObject====>" + jsonObject);
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("tplexParam", jsonObject);
        System.out.println("parameters====>" + parameters);
        // http 통신을 위한 RestTemplate 헤더값은 util에 설정 값만 보내준다.
        String result = ivrRestTemplateUtils.sendRestObjectTemplate(parameters);

        System.out.println("result====>" + result);
        //  받아온 문자열을 파싱하는 부분
        String[] tempStringArray = result.split("\\:\\{");
        int size = tempStringArray.length;
        // 배열 처음부분이 문자열을 자를때 +1이 되므로 -1을 해준다.
        size = size - 1;
        // 클라이언트로 넘어 갈때 int->String로 변경한다.
        String length = Integer.toString(size);
        System.out.println("length====>" + length);
        // TelewebJSON 으로 변경한다.
        objRetParams.setString("DATA", result);
        objRetParams.setString("LENGTH", length);
        return objRetParams;
    }

    /**
     * 미니전광판 전체조회_수정 상단 전광판 조회
     * 
     * @param mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "미니전광판전체 조회", notes = "미니전광판전체 조회")
    @PostMapping("/api/infra/tplex/cti/queue-info/inqire")
    public Object PhoneMainOutboundQueueInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String AGENT_ID = "";

        AGENT_ID = mjsonParams.getString("AGENT_ID");

        // http parameters 값 세팅
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("REQ", "agent_call_today");
        jsonObject.put("AGENT_ID", AGENT_ID);

        //String textValue = String.valueOf(jsonObject);
        System.out.println("jsonObject====>" + jsonObject);
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("tplexParam", jsonObject);
        System.out.println("parameters====>" + parameters);
        // http 통신을 위한 RestTemplate 헤더값은 util에 설정 값만 보내준다.
        String result = ivrRestTemplateUtils.sendRestObjectTemplate(parameters);

        System.out.println("result====>" + result);
        //  받아온 문자열을 파싱하는 부분
        String[] tempStringArray = result.split("\\:\\{");
        int size = tempStringArray.length;
        // 배열 처음부분이 문자열을 자를때 +1이 되므로 -1을 해준다.
        size = size - 1;
        // 클라이언트로 넘어 갈때 int->String로 변경한다.
        String length = Integer.toString(size);
        System.out.println("length====>" + length);
        // TelewebJSON 으로 변경한다.
        objRetParams.setString("DATA", result);
        objRetParams.setString("LENGTH", length);
        return objRetParams;
    }

    /**
     * Arthur.Kim 2021.10.29
     * 
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "녹취플레이", notes = "녹취플레이")
    @PostMapping("/api/infra/tplex/cti/rec-info/inqire")
    public Object selectRecInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //반환 파라메터 생성
        String channel = "";
        String search_date = "";

        channel = mjsonParams.getString("CHANNEL");
        search_date = mjsonParams.getString("SEARCH_DATE");

        JSONObject jsonObj = new JSONObject();

        jsonObj.put("REQ", "record_url");
        jsonObj.put("CHANNEL", channel);
        jsonObj.put("SEARCH_DATE", search_date);

        // http parameters 값 세팅
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();

        parameters.add("tplexParam", jsonObj);
        //parameters.add("sp_code", "2000");
        //parameters.add("req", "record_url");
        //parameters.add("channel", channel);
        //parameters.add("search_date", search_date);

        log.debug("==================== 녹취데이터 전달 ====================");
        log.debug("req: " + "record_url");
        log.debug("channel: " + channel);
        log.debug("search_date: " + search_date);
        log.debug("jsonObj: " + jsonObj.toString());
        log.debug("parameters1: " + String.valueOf(parameters));

        // http 통신을 위한 RestTemplate 헤더값은 util에 설정 값만 보내준다.
        //String result = ivrRestTemplateUtils.sendRestTemplate(parameters);
        String result = ivrRestTemplateUtils.sendRestObjectTemplate(parameters);

        //POST 방식으로 cti 서버로 리퀘스트를 전송한다. 인자 (URL, HttpEntity request, 받을 클래스 형식);
        log.debug("==================== 녹취데이터 결과 ====================");
        log.debug("result: " + result);

        // URL 디코더로 암호화를 해재한다.
        try {

            result = URLDecoder.decode(result, "UTF-8");

            //  받아온 문자열을 파싱하는 부분
            String[] tempStringArray = result.split("\\:\\{");
            int size = tempStringArray.length;

            // 배열 처음부분이 문자열을 자를때 +1이 되므로 -1을 해준다.
            size = size - 1;

            // 클라이언트로 넘어 갈때 int->String로 변경한다.
            String length = Integer.toString(size);

            // TelewebJSON 으로 변경한다.
            objRetParams.setString("DATA", result);
            objRetParams.setString("LENGTH", length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return objRetParams;
    }

    /**
     * 호전환 팝업 상담원 조회
     * 
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "호전환 팝업 상담원 조회", notes = "호전환 팝업 상담원 조회")
    @PostMapping("/api/infra/tplex/cti/cnvrs-detail/list")
    public Object cnvrsDetailList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        // http 헤더 값 세팅
        // http parameters 값 세팅
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("sp_code", "2000");
        parameters.add("req", "display_agent_info");

        // http 통신을 위한 RestTemplate 헤더값은 util에 설정 값만 보내준다.
        String result = ivrRestTemplateUtils.sendRestTemplate(parameters);

        //  받아온 문자열을 파싱하는 부분
        String[] tempStringArray = result.split("\\:\\{");
        int size = tempStringArray.length;
        // 배열 처음부분이 문자열을 자를때 +1이 되므로 -1을 해준다.
        size = size - 1;
        // 클라이언트로 넘어 갈때 int->String로 변경한다.
        String length = Integer.toString(size);
        // TelewebJSON 으로 변경한다.
        objRetParams.setString("DATA", result);
        objRetParams.setString("LENGTH", length);
        return objRetParams;
    }

    /**
     * 기업 전화번호 조회
     * 
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "기업 전화번호 조회", notes = "전화걸기-기업 전화번호 검색")
    @PostMapping("/api/infra/tplex/cti/sendnum/inqire")
    public Object selectSendNum(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        log.debug("mjsonParams" + mjsonParams);

        objRetParams = ivrCallbackDataService.selectSendNum(mjsonParams);

        log.debug("objRetParams" + objRetParams);

        return objRetParams;
    }

    /**
     * 개인 전광판 조회
     * 
     * @param mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "개인 전광판 조회", notes = "개인 전광판 조회")
    @PostMapping("/api/infra/tplex/cti/monitoring/inqire")
    public Object getMonitor(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = ivrCallbackDataService.getMonitor(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }

}
