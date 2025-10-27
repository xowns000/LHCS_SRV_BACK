package kr.co.hkcloud.palette3.config.aspect.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.log.app.SystemEventLogService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.core.support.PaletteServletRequestSupport;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * packageName    : kr.co.hkcloud.palette3.config.aspect.util
 * fileName       : AspectSystemEventLoggerUtils
 * author         : KJD
 * date           : 2023-12-26
 * description    : 주요 시스템설정정보 변경 로깅처리
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-26        KJD       최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AspectSystemEventLoggerUtils {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final SystemEventLogService systemEventLogService;

    /**
     * 로깅처리 AOP
     *
     * @throws TelewebUtilException
     */
    public void processingLog(Object result, SystemEventLogAspectAnotation eventAnnotation) throws TelewebUtilException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Optional.ofNullable(requestAttributes).ifPresent(ra -> {

            HttpServletRequest objRequest = ra.getRequest();

            String userId = StringUtils.defaultIfEmpty(objRequest.getParameter("DATA[0][USER_ID]"), "");
            String custcoId = StringUtils.defaultIfEmpty(objRequest.getParameter("DATA[0][CUSTCO_ID]"), "");
            String menuId = StringUtils.defaultIfEmpty(objRequest.getParameter("HEADER[MENU_ID]"), ""); // menu id
            String menuPath = StringUtils.defaultIfEmpty(objRequest.getParameter("HEADER[MENU_PATH]"), ""); // menu id

            TelewebJSON jsonParams = new TelewebJSON();
            jsonParams = jsonParams.cnvtTelewebJson(objRequest, true);  // Param 정보
            //암호화 관련 key는 삭제 - 시스템_로그 PARAM 컬럼에 저장 방지.
            jsonParams.remove("PP_KEY_PP");
            jsonParams.remove("PP_ALG_PP");

            try {
                //주요 시스템설정정보 변경 로깅 용도.
                String taskSeCd = eventAnnotation.value();
                String taskSeNote = eventAnnotation.note();
                boolean taskIsSavePameter = eventAnnotation.isSaveParameter();

                if (!"none".equals(taskSeCd)) {
                    JSONObject resultObj = JSONObject.fromObject((((ResponseEntity) result).getBody()).toString());

                    log.debug("[" + taskSeCd + "=>" + taskSeNote + " ] 이후 로깅 Param 저장여부==> {}", taskIsSavePameter);
                    log.debug("[" + taskSeCd + "=>" + taskSeNote + " ] 이후 로깅 Param(원본) ==> {}", jsonParams.toString());
                    log.debug("[" + taskSeCd + "=>" + taskSeNote + " ] 이후 로깅 Param ==> {}", jsonParams.getDataJSON());
                    log.debug("[" + taskSeCd + "=>" + taskSeNote + " ] 이후 로깅 응답 ==> {}", resultObj.toString());
                    String paramStr = jsonParams.getDataJSON();
                    String responseStr = resultObj.toString();

                    objectMapper.writeValueAsString(jsonParams.getDataJSON());

                    TelewebJSON insertParams = new TelewebJSON();
                    insertParams.setString("CUSTCO_ID", custcoId);
                    insertParams.setString("SE_CD", taskSeCd);
                    insertParams.setString("SE_NM", taskSeNote);
                    insertParams.setString("PARAM", objectMapper.writeValueAsString(paramStr));
                    insertParams.setString("RSLT", objectMapper.writeValueAsString(responseStr));
                    insertParams.setString("CNTN_MENU_CD", menuId);
                    insertParams.setString("CNTN_MENU_PATH", menuPath);
                    insertParams.setString("CNTN_IP", PaletteServletRequestSupport.getClientIp(objRequest));
                    insertParams.setString("RGTR_ID", userId);
                    systemEventLogService.insertSystemEventLog(insertParams);
                }
            } catch (JSONException je) {
                log.debug("JSONException ========================");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
