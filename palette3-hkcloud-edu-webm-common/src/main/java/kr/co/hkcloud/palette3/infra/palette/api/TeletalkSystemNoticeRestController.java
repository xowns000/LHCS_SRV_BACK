package kr.co.hkcloud.palette3.infra.palette.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoAspectAround;
import kr.co.hkcloud.palette3.core.util.PaletteUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.infra.palette.app.TwbSystemNoticeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 시스템공지 API
 **/
@Slf4j
@AllArgsConstructor
@RestController
public class TeletalkSystemNoticeRestController
{
    private final TwbSystemNoticeService twbSystemNoticeService;
    private final PaletteUtils           paletteUtils;


    /**
     * 시스템공지 API
     **/
    @ApiOperation(value = "시스템공지 API",
                  notes = "시스템공지 API.")
    @NoAspectAround
    @PostMapping(value = "/infra/hkcdv/api/getSystemNotice")
    public JSONObject getSystemNotice() throws TelewebApiException
    {
        JSONObject jsonRtn = new JSONObject();
        JSONObject data = new JSONObject();
        TelewebJSON jsonParams = new TelewebJSON();
        TelewebJSON jsonReturn = new TelewebJSON();

        jsonReturn = twbSystemNoticeService.selectRtnChatbotSkillSystemNotice(jsonParams);

        if(0 < jsonReturn.getHeaderInt("TOT_COUNT")) {
            data.put("code", "200");							//결과코드

            //게시판 내용 <br /> 파싱처리
            if(jsonReturn.getDataObject().getJSONObject(0).size() > 0 && jsonReturn.getDataObject().getJSONObject(0).containsKey("BRD_RMK")) {
                String brdMnk = jsonReturn.getDataObject().getJSONObject(0).getString("BRD_RMK");
                jsonReturn.getDataObject().getJSONObject(0).put("BRD_RMK", paletteUtils.chkXSSConstraintsDec(brdMnk).replaceAll("<br />\n", "\n").replaceAll("&nbsp;", " "));
            }

            data.put("emgList", jsonReturn.getDataObject());	//긴급공지 리스트

            jsonRtn.put("success", true);						//API 성공 여부
            jsonRtn.put("code", "200");							//결과코드
            jsonRtn.put("message", "공지가 정상적으로 조회되었습니다.");	//메시지
            jsonRtn.put("data", data);
        }
        else {
            data.put("code", "200");							//결과코드
            data.put("emgList", new JSONArray());				//긴급공지 리스트 : empty

            jsonRtn.put("success", true);						//API 성공 여부
            jsonRtn.put("code", "200");							//결과코드
            jsonRtn.put("message", "공지가 없습니다.");				//메시지
            jsonRtn.put("data", data);
        }

        log.debug("jsonRtn : {}", jsonRtn);

        return jsonRtn;
    }
}
