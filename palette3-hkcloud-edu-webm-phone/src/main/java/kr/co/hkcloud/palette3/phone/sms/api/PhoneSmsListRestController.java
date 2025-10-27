package kr.co.hkcloud.palette3.phone.sms.api;


import io.swagger.annotations.Api;
import kr.co.hkcloud.palette3.phone.sms.app.PhoneSmsListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneSmsListRestController",
     description = "SMS리스트 REST 컨트롤러")
public class PhoneSmsListRestController
{
    private final PhoneSmsListService phoneSmsListService;

    /**
     * SMS 리스트 조회
     *
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS 템플릿 리스트 조회",
//            notes = "SMS 템플릿 리스트 조회")
//    @PostMapping("/phone-api/sms/sendHistory/smsTmpllist")
//    public Object selectSmsTmplLists(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException
//    {
//    	return phoneSmsListService.selectSmsTmplList(mjsonParams);
//    }

    /**
     * SMS 리스트 조회
     *
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS리스트 조회",
//            notes = "SMS리스트 조회")
//    @PostMapping("/phone-api/sms/list/list")
//    public Object selectSmsSendHistory(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException {
        /*
    	log.debug("mjsonParams" + mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

//        ext=본인내선번호
//        &inout=수신발신
//        &telno=전화번호
//        &date_st=검색시작일자
//        &date_ed=검색종료일자
//        &msg=문자내용
//        &ext2=검색할내선번호

        String sndTelNo = jsonParams.getString("SND_TEL_NO");
        String sndMsg = jsonParams.getString("SND_MSG");
        String inlneNo = jsonParams.getString("INLNE_NO");
        String searchInlneNo = jsonParams.getString("SEARCH_INLNE_NO");
        String inOutDiv = jsonParams.getString("IN_OUT_DIV");
        String startDate = jsonParams.getString("START_DATE");
        String endDate = jsonParams.getString("END_DATE");
        log.info("sndTelNo:{}, sndMsg:{}, inlneNo:{}, searchInlneNo:{}, inOutDiv:{}, startDate:{}, endDate:{}", sndTelNo, sndMsg, inlneNo, searchInlneNo, inOutDiv, startDate, endDate);

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://meta.hkipcc.co.kr/api/usim_sms_list.php";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        if(!sndTelNo.isEmpty()) {
            builder = builder.queryParam("telno", sndTelNo);
        }
        if(!sndMsg.isEmpty()) {
//            builder = builder.queryParam("msg", URLEncoder.encode(sndMsg, "EUC-KR"));
            builder = builder.queryParam("msg", sndMsg);
        }
        if(!inlneNo.isEmpty()) {
            builder = builder.queryParam("ext", inlneNo);
        }
        if(!searchInlneNo.isEmpty()) {
            builder = builder.queryParam("ext2", searchInlneNo);
        }
        if(!inOutDiv.isEmpty()) {
            builder = builder.queryParam("inout", inOutDiv);
        }
        if(!startDate.isEmpty()) {
            builder = builder.queryParam("date_st", startDate);
        }
        if(!endDate.isEmpty()) {
            builder = builder.queryParam("date_ed", endDate);
        }

        URI endUri =  builder.build().encode(Charsets.toCharset("EUC-KR")).toUri();
        log.info("getHost:{}", endUri.getHost());
        log.info("getPath:{}", endUri.getPath());
        log.info("getQuery:{}", endUri.getQuery());

        ResponseEntity<String> response = restTemplate.getForEntity(endUri, String.class);
        String body = response.getBody();
        log.info("body:{}", body);

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        if(body != null && body.startsWith("[")) {
            jsonArray = JSONArray.fromObject(body);
            jsonObject = jsonArray.getJSONObject(0);
            log.info("msg:{}", jsonObject.getString("msg"));
            objRetParams.setDataObject(jsonArray);
        }
		*/
//        return phoneSmsListService.selectSmsSendHistory(mjsonParams);
//    }



    /**
     * SMS 발송
     *
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS 발송",
//            notes = "SMS 발송")
//    @PostMapping("/phone-api/sms/list/send")
//    public Object sendSms(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException {
//        log.debug("mjsonParams" + mjsonParams);
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
//        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
//        String sndTelNo = jsonParams.getString("SND_TEL_NO");
//        String smsDesc = jsonParams.getString("SMS_DESC");
//        String inlneNo = jsonParams.getString("INLNE_NO");
//
//        log.info("sndTelNo:{}, smsDesc:{}, inlneNo:{}", sndTelNo, smsDesc, inlneNo);
//
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity entity = new HttpEntity(headers);
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://meta.hkipcc.co.kr/api/usim_sms_send.php";
//
//        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
//
////        inlneNo= "1099"; // TEST
//        builder = builder.queryParam("ext", inlneNo);
//        builder = builder.queryParam("telno", sndTelNo);
//        builder = builder.queryParam("msg", URLEncoder.encode(smsDesc, "EUC-KR"));
//
//        URI endUri = builder.build().toUri();
//
//        log.info("endUri.getPath():{}, endUri.getPath():{}", endUri.getPath(), endUri.getQuery());
//        ResponseEntity<String> response = restTemplate.exchange(endUri, HttpMethod.GET, entity, String.class);
//        String body = response.getBody();
//        log.info("body:{}", body);
//
//        JSONArray jsonArray = new JSONArray();
//        JSONObject jsonObject = new JSONObject();
//
//        if(body != null && body.startsWith("[")) {
//            jsonArray = JSONArray.fromObject(body);
//            jsonObject = jsonArray.getJSONObject(0);
//            objRetParams.setDataObject(jsonArray);
//        }
//
//        return objRetParams;
//    }
}
