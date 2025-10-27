package kr.co.hkcloud.palette3.core.cron.app;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.messenger.domain.ChatOnMessageEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.integration.service.CommerceApiServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service("cronChatBoardService")
public class CronChatBoardServiceImpl implements CronChatBoardService {

    private final CommerceApiServiceFactory commerceApiServiceFactory;
    private final ApplicationEventPublisher eventPublisher;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO mobjDao;
    private String namespace = "kr.co.hkcloud.palette3.core.cron.dao.CronChatBoardMapper";

    @Override
    public TelewebJSON collectChatCuttBbs(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON retObj = new TelewebJSON(jsonParams);
        try {

            //이전배치에서 CHT_CUTT_ID 적용안된 경우 다시 처리하기 위함
            this.retryChatOnMessageEvent();

            int settingCount = jsonParams.getHeaderInt("TOT_COUNT");
            if (settingCount > 0) {
                int insertCnt = 0;
                int modifyCnt = 0;
                JSONObject settingObj = (JSONObject) jsonParams.getDataObject().get(0);
                String custcoId = settingObj.getString("CUSTCO_ID");
                log.info("+=+=+=+=+=+=+=+=+=+=+=+=___BBS " + jsonParams.getString("BEAN_ID") + "-" + jsonParams.getString("BBS_INQRY_TYPE_CD")
                    + " 수집 ___ ::: Start ___CUSTCO_ID___" + custcoId);

                jsonParams.setString(settingObj.getString("FROM_DATE_COL_NM"), jsonParams.getString("LAST_SRCH_DT"));
                jsonParams.setString(settingObj.getString("TO_DATE_COL_NM"), jsonParams.getString("currentDateTime"));

                TelewebJSON retJson = commerceApiServiceFactory.getCommerceApiService(jsonParams.getString("BEAN_ID")).call_batch_api(jsonParams);
                //JSONObject pageable = (JSONObject)retJson.getDataObject("pageable").get(0);
                JSONArray retArray = retJson.getDataObject(TwbCmmnConst.G_DATA);
                if (retArray.size() > 0) {

                    for (int j = 0; j < retArray.size(); j++) {
                        TelewebJSON returnJson = new TelewebJSON();
                        TelewebJSON paramJson = new TelewebJSON();
                        JSONArray dataArray = new JSONArray();
                        dataArray.add(retArray.getJSONObject(j));
                        paramJson.setDataObject(dataArray);

                        if (StringUtils.isNotEmpty(paramJson.getString("INQRY_TTL"))) {
                            paramJson.setString("DSPTCH_MSG", paramJson.getString("INQRY_TTL"));
                        } else {
                            paramJson.setString("DSPTCH_MSG", paramJson.getString("INQRY_CN"));
                        }

                        returnJson = mobjDao.select(namespace, "selectChatCuttBbsInfo", paramJson);
                        if (returnJson.getHeaderInt("COUNT") == 0) {
                            log.info("신규 :: after {}", paramJson.toString());

                            paramJson.setInt("CHT_CUTT_BBS_ID", innbCreatCmmnService.createSeqNo("CHT_CUTT_BBS_ID"));
                            mobjDao.insert(namespace, "insertChatCuttBbs", paramJson);

                            String userKey = paramJson.getString("BEAN_ID") + "_" + paramJson.getString("BEAN_TP") + "_" + paramJson.getString("INQRY_NO");

                            JSONObject messageJson = new JSONObject();
                            messageJson.put("call_typ_cd", jsonParams.getString("CHN_CLSF_CD"));
                            messageJson.put("type", "BBS");
                            messageJson.put("asp_sndrKey", jsonParams.getString("SNDR_KEY"));
                            messageJson.put("sndrKey", jsonParams.getString("SNDR_KEY"));
                            messageJson.put("custco_id", custcoId);
                            messageJson.put("biz_service_cd", jsonParams.getString("CHN_CLSF_CD"));
                            messageJson.put("user_key", userKey);
                            messageJson.put("org_user_key", userKey);
                            messageJson.put("content", paramJson.getString("DSPTCH_MSG"));
                            messageJson.put("CHT_CUTT_BBS_ID", paramJson.getString("CHT_CUTT_BBS_ID"));

                            //이메일 상담 등록 및 상담원 배정 대기 이벤트 발행
                            eventPublisher.publishEvent(ChatOnMessageEvent.builder().messageJson(messageJson).build());
                            insertCnt++;

                        } else {
                            log.info("수정 :: before {}", returnJson.toString());
                            log.info("수정 :: after {}", paramJson.toString());

                            paramJson.setString("CHT_CUTT_BBS_ID", returnJson.getString("CHT_CUTT_BBS_ID"));

                            if (!paramJson.getString("INQRY_CN").equals(returnJson.getString("INQRY_CN")) || !paramJson.getString("INQRY_TTL")
                                .equals(returnJson.getString("INQRY_TTL"))) {
                                // 내용이 변경인경우 이력등록
                                mobjDao.insert(namespace, "insertChatCuttBbsHstry", returnJson);    //채팅_문의_게시판 이력저장 (제목 내용 _##_ 구분 하여 저장)
                                mobjDao.update(namespace, "updateChatCuttBbs", paramJson);  //채팅_문의_게시판 업데이트
                                mobjDao.update(namespace, "updateChtCuttDtlDsptchMsg", paramJson);  //채팅_상담_상세 메시지 업데이트
                                modifyCnt++;
                            }
                        }
                    }

                    //                    JSONObject jsonObj = new JSONObject();
                    //                    jsonObj.put("ITEM_LIST", jsonParams.getDataObject(TwbCmmnConst.G_DATA));
                    //                    JSONArray arrParam = new JSONArray();
                    //                    arrParam.add(jsonObj);
                    //jsonParams.setDataObject("ITEM_LIST", retArray );
                }

                //수집 결과 저장
                retObj.setString("JOB_SCS_YN", "Y");
                retObj.setString("JOB_RSLT_MSG",
                    jsonParams.getString("BEAN_ID") + "-" + jsonParams.getString("BBS_INQRY_TYPE_CD") + " 수집 완료 - " + retArray.size() + " 건 ( 신규:" + insertCnt
                        + ", 수정:" + modifyCnt + ") ");
                retObj.setHeader("ERROR_FLAG", false);
                retObj.setHeader("ERROR_MSG", "");
                log.info(
                    "+=+=+=+=+=+=+=+=+=+=+=+=___BBS " + jsonParams.getString("BEAN_ID") + "-" + jsonParams.getString("BBS_INQRY_TYPE_CD") + " 수집 ___ ::: End"
                        + retArray.size() + " 건  ( 신규:\"+insertCnt+\", 수정:\"+modifyCnt+\")");
            } else {
                retObj.setString("JOB_SCS_YN", "Y");
                retObj.setString("JOB_RSLT_MSG", jsonParams.getString("BEAN_ID") + "-" + jsonParams.getString("BBS_INQRY_TYPE_CD") + " 수집 완료 - 0 건");
                retObj.setHeader("ERROR_FLAG", false);
                retObj.setHeader("ERROR_MSG", "");
            }
        } catch (Exception e) {
            retObj.setHeader("ERROR_FLAG", true);
            retObj.setHeader("ERROR_MSG", e.getMessage());
            retObj.setString("JOB_SCS_YN", "N");
            retObj.setString("JOB_RSLT_MSG", e.toString());
        }

        return retObj;

    }

    /**
     * 고객사_채널_BBS_설정 목록 조회
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCustcoChannelBbsSettingList(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectCustcoChannelBbsSettingList", jsonParams);
    }

    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateChtCuttId(TelewebJSON paramJson) throws TelewebAppException {
        return mobjDao.select(namespace, "updateChtCuttId", paramJson);
    }

    /* 이전배치에서 CHT_CUTT_ID 적용안된 경우 다시 처리하기 위함.*/
    public void retryChatOnMessageEvent() {
        TelewebJSON retJson = mobjDao.select(namespace, "selectNotMatchChtCuttId", new TelewebJSON());
        if (retJson.getHeaderInt("COUNT") > 0) {
            JSONArray retArray = retJson.getDataObject(TwbCmmnConst.G_DATA);
            for (int j = 0; j < retArray.size(); j++) {
                JSONObject retryObj = retArray.getJSONObject(j);
                JSONObject messageJson = new JSONObject();
                messageJson.put("call_typ_cd", retryObj.getString("CHN_CLSF_CD"));
                messageJson.put("type", "BBS");
                messageJson.put("asp_sndrKey", retryObj.getString("SNDR_KEY"));
                messageJson.put("sndrKey", retryObj.getString("SNDR_KEY"));
                messageJson.put("custco_id", retryObj.getString("CUSTCO_ID"));
                messageJson.put("biz_service_cd", retryObj.getString("CHN_CLSF_CD"));
                messageJson.put("user_key", retryObj.getString("INQRY_NO"));
                messageJson.put("org_user_key", retryObj.getString("INQRY_NO"));
                messageJson.put("content", retryObj.getString("INQRY_CN"));
                messageJson.put("CHT_CUTT_BBS_ID", retryObj.getString("CHT_CUTT_BBS_ID"));

                //이메일 상담 등록 및 상담원 배정 대기 이벤트 발행
                eventPublisher.publishEvent(ChatOnMessageEvent.builder().messageJson(messageJson).build());

            }
        }
    }
}
