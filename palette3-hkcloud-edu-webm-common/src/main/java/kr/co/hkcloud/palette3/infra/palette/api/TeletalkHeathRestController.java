package kr.co.hkcloud.palette3.infra.palette.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.hkcloud.palette3.config.aspect.NoAspectAround;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


/**
 * 헬스 체크
 **/
@Slf4j
@RestController
public class TeletalkHeathRestController
{
    /**
     * Health Check API
     **/
    @NoAspectAround
    @GetMapping(value = "/infra/hkcdv/api/health_check")
    public JSONObject healthCheck() throws TelewebApiException
    {
        JSONObject obj = new JSONObject();
        obj.put("code", 200);
        return obj;
    }
}
