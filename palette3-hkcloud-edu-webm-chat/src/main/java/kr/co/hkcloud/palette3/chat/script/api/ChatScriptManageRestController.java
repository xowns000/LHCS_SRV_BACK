package kr.co.hkcloud.palette3.chat.script.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.script.app.ChatScriptManageService;
import kr.co.hkcloud.palette3.chat.script.util.ChatScriptManageValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatScriptManageRestController",
     description = "채팅스크립트관리 REST 컨트롤러")
public class ChatScriptManageRestController
{
    private final ChatScriptManageValidator chatScriptManageValidator;
    private final ChatScriptManageService   chatScriptManageService;


    /**
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "채팅스크립트관리-목록",
                  notes = "채팅스크립트관리 목록 조회한다")
    @PostMapping("/chat-api/script/manage/list")
    public Object selectRtnScriptMngList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //validation 체크

        chatScriptManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatScriptManageService.selectRtnScriptMngList(jsonParams);

        return objRetParams;
    }


    /**
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "채팅스크립트관리-등록",
                  notes = "채팅스크립트관리 등록한다")
    @PostMapping("/chat-api/script/manage/regist")
    public Object insertRtnScriptMng(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //validation 체크 
        chatScriptManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatScriptManageService.insertRtnScriptMng(jsonParams);

        return objRetParams;
    }


    /**
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "채팅스크립트관리-갱신",
                  notes = "채팅스크립트관리 갱신한다")
    @PostMapping("/chat-api/script/manage/modify")
    public Object updateRtnScriptMng(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //validation 체크 
        chatScriptManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatScriptManageService.updateRtnScriptMng(jsonParams);

        return objRetParams;
    }


    /**
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 처리 결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅스크립트관리-삭제",
                  notes = "채팅스크립트관리 삭제한다")
    @PostMapping("/chat-api/script/manage/delete")
    public Object deleteRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {

//	        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");    //client에서 전송받은 파라메터 생성

        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        JSONArray objArry = jsonParams.getDataObject();
        int objArrSize = objArry.size();

        //validation 체크 
        chatScriptManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //배열 사이즈만큼 처리
        if(!objArry.isEmpty()) {
            for(int i = 0; i < objArrSize; i++) {

                jsonParams.setObject("SCRIPT_ID", 0, objArry.getJSONObject(i).getString("SCRIPT_ID"));

                chatScriptManageService.deleteRtnScriptMng(jsonParams);
            }
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * chat-script-manage.js와 매핑되지 않음 스크립트정보 검색
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 처리 결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "스크립트정보 검색",
                  notes = "스크립트정보 검색")
    @PostMapping("/api/TwbTalkScriptMng/searchScript")
    public Object searchScript(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
//	        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");    //client에서 전송받은 파라메터 생성

        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        if("Y".equals(jsonParams.getString("VIEW_BASE_SCRIPT"))) {
            objRetParams = chatScriptManageService.selectRtnScriptMngByBaseScript(jsonParams);
        }
        else if("N".equals(jsonParams.getString("VIEW_BASE_SCRIPT"))) {
            objRetParams = chatScriptManageService.selectRtnScriptMngByUserScript(jsonParams);
        }

        return objRetParams;
    }


    /**
     * chat-script-manage.js와 매핑되지 않음 컨설턴트스크립트정보 조회
     * 
     * @param  Object HashMap.
     * @return        TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "컨설턴트스크립트정보 조회",
                  notes = "컨설턴트스크립트정보 조회")
    @PostMapping("/api/TwbTalkScriptMng/searchCounselerScript")
    public Object searchCounselerScript(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
//	        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");    //client에서 전송받은 파라메터 생성

        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatScriptManageService.selectRtnScriptMngByCounseler(jsonParams);

        return objRetParams;
    }


    /**
     * chat-script-manage.js와 매핑되지 않음 공용스크립트정보 조회
     * 
     * @param  Object HashMap.
     * @return        TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "공용스크립트정보 조회",
                  notes = "공용스크립트정보 조회")
    @PostMapping("/api/TwbTalkScriptMng/searchAdminScript")
    public Object searchAdminScript(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
//	        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");    //client에서 전송받은 파라메터 생성

        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatScriptManageService.selectRtnScriptMngByAdmin(jsonParams);

        return objRetParams;
    }
}
