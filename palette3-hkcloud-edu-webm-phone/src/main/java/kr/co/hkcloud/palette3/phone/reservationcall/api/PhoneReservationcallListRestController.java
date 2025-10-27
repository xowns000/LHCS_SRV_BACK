package kr.co.hkcloud.palette3.phone.reservationcall.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.reservationcall.app.PhoneReservationcallListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneReservationcallListRestController",
     description = "예약콜조회 REST 컨트롤러")
public class PhoneReservationcallListRestController
{
    private final PhoneReservationcallListService phoneReservationcallListService;


    /**
     * 예약콜정보 조회
     * 
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "예약콜정보 조회",
                  notes = "예약콜정보 조회")
    @PostMapping("/phone-api/reservationcall/list")
    public Object selectRtnResvCallInqInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneReservationcallListService.selectRtnResvCallInqInfo(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 예약콜 완료처리
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "예약콜 완료처리",
                  notes = "예약콜 완료처리")
    @PostMapping("/phone-api/reservationcall/list/process")
    public Object updateRtnResvCallInqInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objParams = new TelewebJSON(mjsonParams);
        JSONArray objArry = mjsonParams.getDataObject();
        JSONObject objJson = new JSONObject();

        if(!objArry.isEmpty()) {
            for(int i = 0; i < objArry.size(); i++) {
                objJson = objArry.getJSONObject(i);
                if(!objJson.isEmpty() && !objJson.isNullObject()) {
                    TelewebJSON jsonParams = new TelewebJSON(objRetParams);
                    jsonParams.setDataObject(objParams.getDataObject(TwbCmmnConst.G_DATA));
                    objParams.setDataObject(JSONArray.fromObject(objJson));

                    objRetParams = phoneReservationcallListService.updateRtnResvCallInqInfo(objParams);
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }
    
    /**
     * 예약콜 삭제
     * 
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "예약콜 삭제",
                  notes = "예약콜 삭제")
    @PostMapping("/phone-api/reservationcall/delete")
    public Object deleteRtnResvCallInqInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneReservationcallListService.deleteRtnResvCallInqInfo(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }
}
