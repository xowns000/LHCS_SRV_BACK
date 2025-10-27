package kr.co.hkcloud.palette3.infra.palette.util;

public class RestTemplateUtils
{
// Sample임
// private final ObjectMapper objectMapper;
//
// public void moveKiccEasypayOrder() throws TelewebUtilException
// {
// EasypayOrderReqDto dto = EasypayOrderReqDto.builder()
// .EP_mall_id("T5102001")
// .EP_mall_nm("이지페이8.0 모바일")
// .EP_order_no("ORDER_20200714130400")
// .EP_pay_type("11")
// .EP_currency("00")
// .EP_product_nm("테스트상품")
// .EP_product_amt("51004")
// .EP_return_url("http://210.112.73.25:15018/infra/kicc/api/easypay-order-res")
// .EP_lang_flag("KOR")
// .EP_charset("UTF-8")
// .EP_user_id("psj1988")
// .EP_memb_user_no("15123485756")
// .EP_user_nm("홍길동")
// .EP_user_mail("kildong@kicc.co.kr")
// .EP_user_phone1("0221471111")
// .EP_user_phone2("01012345679")
// .EP_user_addr("서울시 금천구 가산동")
// .EP_product_type("0")
// .EP_product_expr("20201231")
// .EP_usedcard_code("")
// .EP_quota("")
// .EP_noinst_term("029-02:03")
// .EP_point_card("029-40")
// .EP_vacct_bank("")
// .EP_vacct_end_date("20200714")
// .EP_vacct_end_time("153025")
// .build();
//
//
// MultiValueMap<String, String> parameters = TalkMultiValueMapConverter.convert(objectMapper, dto);
//
// //MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
// //parameters.add("send_phone", "12341234");
//
// HttpHeaders headers = new HttpHeaders();
// //headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"); //전부다 String형일 때. RestTemplate 때문에 생략가능
//
// HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
//
// RestTemplate rest = new RestTemplate();
// String result = rest.postForObject(new URI("http://testpg.easypay.co.kr/webpay/MainAction.do"), request, String.class);
// //결과 {“result_code”:”200”,”cmid”:”20130314163439459”}
//
// log.debug("result={}", result);
//
// // 결과값에 따른 처리
//
// }
}
