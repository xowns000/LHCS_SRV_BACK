package kr.co.hkcloud.palette3.chat.history.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.history.app.ChatHistoryManageService;
import kr.co.hkcloud.palette3.chat.history.util.ChatHistoryManageValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteDataFormatUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatHistoryManageRestController", description = "상담이력관리 REST 컨트롤러")
public class ChatHistoryManageRestController {

    private final ChatHistoryManageService chatHistoryManageService;
    private final PaletteDataFormatUtils paletteDataFormatUtils;
    private final ChatHistoryManageValidator chatHistoryManageValidator;

    /**
     *
     */
    @ApiOperation(value = "채팅이력관리-수정", notes = "상담이력 수정(고객상담이력, 고객상담이력상세, 고객마스터 테이블)")
    @PostMapping("/chat-api/history/manage/modify")
    public Object updateRtnConsHist(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");    //client에서 전송받은 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                //최종 반환할 객체 생성, mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setString("ASP_NEWCUST_KEY", mjsonParams.getString("ASP_NEWCUST_KEY"));
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatHistoryManageService.updateRtnTwbTalkContact(jsonParams);

        return objRetParams;    //최종결과값 반환
    }


    /**
     * @return TeleWebJson 형식의 처리 결과 데이터
     * @since 2018. 05. 08
     */
    @ApiOperation(value = "채팅이력관리/목록", notes = "채팅이력관리/목록")
    @PostMapping("/chat-api/history/manage/list")
    public Object selectRtnPageConsHist(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        objRetParams.setHeader("ERROR_FLAG", false);

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatHistoryManageService.selectRtnPageConsHist(jsonParams);

        return objRetParams;
    }

    /**
     * 상담이력 수정(EAI통해 NGNS 상담이력 업데이트)
     *
     * @Transactional 트랜잭션 사용
     * @param         inHashMap 전송된 파라메터
     * @return TelewebJSON 형식의 처리 결과 데이터
     *
     */
    //    @ApiOperation(value = "상담이력 수정(EAI통해 NGNS 상담이력 업데이트)", notes = "상담이력 수정(EAI통해 NGNS 상담이력 업데이트)")
    //    @PostMapping("/api/TwbTalkConsHistMgmt/processRtn")
    //    public Object processRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    //    {
    //        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //최종 반환할 객체 생성, mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
    //        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
    //        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
    //
    //        //Validation 체크
    //        chatHistoryManageValidator.validate(mjsonParams, result);
    //        if(result.hasErrors()) {
    //        	throw new ValidationException(result.getAllErrors());
    //        }
    //
    //        if (!chatHistoryManageService.checkAuthProcess(mjsonParams))
    //        {
    //        	throw new ChatHistoryManageException("수정 권한이 없습니다.");
    //        }
    //
    //        objRetParams = chatHistoryManageService.updateRtnTwbTalkContact(jsonParams);	//고객상담이력 수정
    //
    //        return objRetParams;    //최종결과값 반환
    //    }


    /**
     * 상담 내용조회
     *
     * @return TeleWebJson 형식의 처리 결과 데이터
     * @since 2019. 06. 04
     */
    private String selectRtnContent(String talkContactId, TelewebJSON mjsonParams) throws TelewebApiException {

        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                                //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        objRetParams.setHeader("ERROR_FLAG", false);

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        jsonParams.setString("TALK_CONTACT_ID", talkContactId);
        jsonParams.setString("ASP_NEWCUST_KEY", mjsonParams.getString("ASP_NEWCUST_KEY"));

        objRetParams = chatHistoryManageService.selectRtnContent(jsonParams);
        JSONArray objArry = objRetParams.getDataObject();
        int objArrSize = objArry.size();

        //배열 사이즈만큼 처리
        String value = "";
        if(!objArry.isEmpty()) {
            for(int i = 0; i < objArrSize; i++) {
                String target = objArry.getJSONObject(i).getString("TARGET");
                String content = objArry.getJSONObject(i).getString("CONTENT");
                value += target + content + "\n\n";
            }
        }

        return value;
    }

}
