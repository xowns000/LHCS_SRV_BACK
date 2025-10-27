package kr.co.hkcloud.palette3.config.aspect.util;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.co.hkcloud.palette3.common.prvc.app.PrvcService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.support.PaletteServletRequestSupport;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * 개인정보 관련 로깅처리
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AspectPrvcLoggerUtils {

    private final PrvcService prvcService;

    /**
     * 개인정보 관련 로깅처리 AOP
     * 
     * @throws TelewebUtilException
     */
    public void processingPrvcInqLog(Object result, String paramTaskSeCd) throws TelewebUtilException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Optional.ofNullable(requestAttributes).ifPresent(ra -> {

            HttpServletRequest objRequest = ra.getRequest();
            String uri = objRequest.getRequestURI();

            String userId = StringUtils.defaultIfEmpty(objRequest.getParameter("DATA[0][USER_ID]"), "");
            String custcoId = StringUtils.defaultIfEmpty(objRequest.getParameter("DATA[0][CUSTCO_ID]"), "");
            String downReason = StringUtils.defaultIfEmpty(objRequest.getParameter("DATA[0][DOWN_REASON]"), "");
            String menuId = StringUtils.defaultIfEmpty(objRequest.getParameter("HEADER[MENU_ID]"), ""); // menu id
            String menuPath = StringUtils.defaultIfEmpty(objRequest.getParameter("HEADER[MENU_PATH]"), ""); // menu id

            try {

                //개인정보 조회 로깅 용도.
                String taskSeCd = paramTaskSeCd;
                String trgtCustId = null; //대상 고객
                String trgtUserId = null; //대상 사용자
                String inqInfoSe = "";
                String cn = "";

                if ("none".equals(taskSeCd)) {
                    //------------------------------------------------------------
                    // 상세조회시 DB조회하지 않고 화면에서 처리하는 경우에 해당
                    //------------------------------------------------------------
                    taskSeCd = StringUtils.defaultIfEmpty(objRequest.getParameter("DATA[0][TASK_SE_CD]"), "");
                    if ("USER_SEL".equals(taskSeCd)) { //사용자정보 상세조회
                        trgtUserId = objRequest.getParameter("DATA[0][TRGT_ID]");
                        inqInfoSe = "사용자 정보를 조회 하였습니다.";
                    } else if ("AUDIO_PLAY".equals(taskSeCd)) {
                        //------------------------------------------------------------
                        // 녹취파일 듣기
                        //------------------------------------------------------------
                        if (!StringUtils.isEmpty(objRequest.getParameter("DATA[0][cn]"))) {
                            cn = objRequest.getParameter("DATA[0][cn]");
                        } else {
                            cn = "전화 상담 ID(PHN_CUTT_ID : " + StringUtils.defaultIfEmpty(objRequest.getParameter("DATA[0][PHN_CUTT_ID]"), "")
                                + ")";
                        }
                        trgtCustId = objRequest.getParameter("DATA[0][TRGT_ID]");
                        inqInfoSe = cn + "의 녹취 파일을 재생 하였습니다.";
                    } else if ("AUDIO_DOWN".equals(taskSeCd)) {
                        //------------------------------------------------------------
                        // 녹취파일 다운로드
                        //------------------------------------------------------------
                        if (!StringUtils.isEmpty(objRequest.getParameter("DATA[0][cn]"))) {
                            cn = objRequest.getParameter("DATA[0][cn]");
                        } else {
                            cn = "전화 상담 ID(PHN_CUTT_ID : " + StringUtils.defaultIfEmpty(objRequest.getParameter("DATA[0][PHN_CUTT_ID]"), "")
                                + ")";
                        }
                        trgtCustId = objRequest.getParameter("DATA[0][TRGT_ID]");
                        inqInfoSe = cn + "의 녹취 파일을 다운로드 하였습니다.";
                    } else if ("LIST_EXCEL_DOWN".equals(taskSeCd)) {
                        //------------------------------------------------------------
                        // 목록 엑셀 다운로드
                        //------------------------------------------------------------
                        inqInfoSe = menuPath + " 화면 목록의 엑셀을 다운로드 하였습니다.";
                        if (!StringUtils.isEmpty(downReason))
                            cn = downReason;
                    }

                } else if ("CUST_LIST".equals(taskSeCd)) {
                    //------------------------------------------------------------
                    // 고객정보 목록조회
                    //------------------------------------------------------------
                    inqInfoSe = "고객 정보 목록을 조회 하였습니다.";
                } else if ("CUST_SEL".equals(taskSeCd)) {
                    //------------------------------------------------------------
                    // 고객정보 상세조회
                    //------------------------------------------------------------
                    trgtCustId = StringUtils.defaultIfEmpty(objRequest.getParameter("DATA[0][CUST_ID]"), "-1");
                    if (!"-1".equals(trgtCustId)) {
                        inqInfoSe = "고객 정보를 조회 하였습니다.";
                    }
                } else if ("CUST_REG,CUST_MOD".equals(taskSeCd.trim())) {
                    trgtCustId = objRequest.getParameter("DATA[0][CUST_ID]");
                    if (StringUtils.isBlank(trgtCustId)) { //등록
                        taskSeCd = "CUST_REG";
                        JSONObject jsonObj = JSONObject.fromObject((((ResponseEntity) result).getBody()).toString());
                        trgtCustId = ((JSONObject) jsonObj.get("HEADER")).getString("GEN_CUST_ID");
                        inqInfoSe = "고객 정보를 등록 하였습니다.";
                    } else {
                        if (!"-1".equals(trgtCustId)) {
                            taskSeCd = "CUST_MOD";
                            cn = StringUtils.defaultIfEmpty(objRequest.getParameter("DATA[0][PRVC_CN]"), "");
                            String msgCn = "";
                            if (!StringUtils.isBlank(cn)) {
                                String[] _arr = cn.split(",");
                                if (_arr.length > 4) {
                                    msgCn = _arr[0] + "," + _arr[1] + "," + _arr[2] + "," + _arr[3] + "외 " + (_arr.length - 4) + "건";
                                } else {
                                    msgCn = cn;
                                }
                            } else {
                                msgCn = "변경정보없음";
                            }
                            inqInfoSe = "고객 정보 '" + msgCn + "'을(를) 수정 하였습니다.";
                        }
                    }

                } else if ("USER_LIST".equals(taskSeCd)) {
                    //------------------------------------------------------------
                    // 사용자정보 목록조회
                    //------------------------------------------------------------
                    taskSeCd = "USER_LIST";
                    inqInfoSe = "사용자 정보 목록을 조회 하였습니다.";
                } else if ("USER_REG,USER_MOD".equals(taskSeCd.trim())) {
                    //------------------------------------------------------------
                    // 사용자정보 등록/수정처리
                    //------------------------------------------------------------
                    String strTransFlag = StringUtils.defaultIfEmpty(objRequest.getParameter("DATA[0][DATA_FLAG]"), "");
                    if (strTransFlag.equals(TwbCmmnConst.TRANS_INS)) {
                        taskSeCd = "USER_REG";
                        JSONObject jsonObj = JSONObject.fromObject((((ResponseEntity) result).getBody()).toString());
                        trgtUserId = ((JSONObject) jsonObj.get("HEADER")).getString("GEN_USER_ID");
                        inqInfoSe = "사용자 정보를 등록 하였습니다.";
                    } else if (strTransFlag.equals(TwbCmmnConst.TRANS_UPD)) {
                        trgtUserId = StringUtils.defaultIfEmpty(objRequest.getParameter("DATA[0][REG_USER_ID]"), "");
                        if (!StringUtils.isEmpty(trgtUserId) && !"-1".equals(trgtUserId)) {
                            taskSeCd = "USER_MOD";
                            cn = StringUtils.defaultIfEmpty(objRequest.getParameter("DATA[0][PRVC_CN]"), "");
                            String msgCn = "";
                            if (!StringUtils.isBlank(cn)) {
                                String[] _arr = cn.split(",");
                                if (_arr.length > 4) {
                                    msgCn = _arr[0] + "," + _arr[1] + "," + _arr[2] + "," + _arr[3] + "외 " + (_arr.length - 4) + "건";
                                } else {
                                    msgCn = cn;
                                }
                            } else {
                                msgCn = "변경정보없음";
                            }
                            inqInfoSe = "사용자 정보 '" + msgCn + "'을(를) 수정 하였습니다.";
                        }

                    }
                } else if ("CUTT_HIST_DOWN".equals(taskSeCd)) {
                    //------------------------------------------------------------
                    // 전화 상담 이력 다운로드
                    //------------------------------------------------------------
                    inqInfoSe = "전화 상담 이력을 다운로드 하였습니다.";
                    cn = downReason;
                } else if ("CUST_DOWN".equals(taskSeCd)) {
                    //------------------------------------------------------------
                    // 고객 정보 관리 Excel 다운로드
                    //------------------------------------------------------------
                    inqInfoSe = "고객 정보 관리 목록을 다운로드 하였습니다.";
                    cn = downReason;
                } else if ("TRNSF_HIST_DOWN".equals(taskSeCd)) {
                	//------------------------------------------------------------
                	// 이관 처리현황 Excel 다운로드
                	//------------------------------------------------------------
                	inqInfoSe = "이관 처리현황 목록을 다운로드 하였습니다.";
                	cn = downReason;
                } else if ("CAUTION_CUST_LIST".equals(taskSeCd)) {
                    //------------------------------------------------------------
                    // 설정 - 주의고객목록 조회
                    //------------------------------------------------------------
                    inqInfoSe = "주의 고객 목록을 조회 하였습니다.";
                }

                if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(taskSeCd) && !StringUtils.isEmpty(inqInfoSe)) {    //로그업무구분이 있는경우 로깅처리.

                    TelewebJSON jsonParam = new TelewebJSON();
                    jsonParam.setString("CUSTCO_ID", custcoId);
                    jsonParam.setString("TASK_SE_CD", taskSeCd);
                    jsonParam.setString("TRGT_CUST_ID", trgtCustId);
                    jsonParam.setString("TRGT_USER_ID", trgtUserId);
                    jsonParam.setString("INQ_INFO_SE", inqInfoSe);
                    jsonParam.setString("CNTN_MENU_CD", menuId);
                    jsonParam.setString("CNTN_MENU_PATH", menuPath);
                    jsonParam.setString("CNTN_IP", PaletteServletRequestSupport.getClientIp(objRequest));
                    jsonParam.setString("RGTR_ID", userId);
                    jsonParam.setString("CN", cn);
                    prvcService.insertPrvcInqLog(jsonParam);
                }
            } catch (JSONException je) {
                log.debug("JSONException ========================");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
