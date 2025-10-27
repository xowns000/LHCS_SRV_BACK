package kr.co.hkcloud.palette3.palette.main.app;


import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.util.PaletteMailUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.app.FileDbMngService;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse;
import kr.co.hkcloud.palette3.file.util.FileDownloadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service("paletteMainService")
public class PaletteMainServiceImpl implements PaletteMainService
{
    @Autowired
    Environment environment;

    private final TwbComDAO mobjDao;
    private final PaletteMailUtils paletteMailUtils;
    private final FileDownloadUtils fileDownloadUtils;
    private final FileDbMngService fileDbMngService;

    /**
     * 우측메뉴 채팅스크립트 상세조회한다.
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnChatScriptDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectRtnChatScriptDetail", jsonParams);
    }


    /**
     * 우측메뉴 채팅스크립트 조회한다.
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnChatScript(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectRtnChatScript", jsonParams);
    }


    /**
     * 우측메뉴 스크립트 상세조회한다.
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnScriptDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectRtnScriptDetail", jsonParams);
    }


    /**
     * 우측메뉴 스크립트 조회한다.
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnScript(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectRtnScript", jsonParams);
    }


    /**
     * 프레임상단 메뉴목록을 조회한다
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnMenu(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retJson = new TelewebJSON(jsonParams);
        JSONArray jsonArray;
        JSONArray jsonArrayMenu1 = new JSONArray();
        JSONArray jsonArrayMenu2 = new JSONArray();
        JSONArray jsonArrayMenu3 = new JSONArray();
        JSONArray jsonArrayDevMenu1 = new JSONArray();
        JSONArray jsonArrayDevMenu2 = new JSONArray();
        JSONArray jsonArrayDevMenu3 = new JSONArray();

        retJson = mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectAllMenu", jsonParams);

        jsonArray = retJson.getDataObject(TwbCmmnConst.G_DATA);
        int arrSize = jsonArray.size();

        for (int i = 0; i < arrSize; i++)
        {
            if ("MAIN".equals(jsonArray.getJSONObject(i).getString("VIEW_TRGT")))
            {
                JSONObject jsonTemp = new JSONObject();
                jsonTemp = jsonArray.getJSONObject(i);
                jsonTemp.put("title", jsonArray.getJSONObject(i).getString("NODE_TITLE"));

                switch (jsonArray.getJSONObject(i).getInt("NODE_LVL"))
                {
                    case 1:
                        if (jsonArray.getJSONObject(i).containsKey("ICON_CLASS_NM"))
                        {
                            jsonTemp.put("icon", jsonArray.getJSONObject(i).getString("ICON_CLASS_NM"));
                        }

                        jsonArrayMenu1.add(jsonTemp);
                        jsonArrayMenu2 = new JSONArray();    //초기화
                        break;

                    case 2:
                        jsonArrayMenu2.add(jsonTemp);
                        jsonArrayMenu1.getJSONObject(jsonArrayMenu1.size() - 1).put("child", jsonArrayMenu2);
                        jsonArrayMenu3 = new JSONArray();    //초기화
                        break;

                    case 3:
                        jsonArrayMenu3.add(jsonTemp);
                        jsonArrayMenu2.getJSONObject(jsonArrayMenu2.size() - 1).put("child", jsonArrayMenu3);
                        jsonArrayMenu1.getJSONObject(jsonArrayMenu1.size() - 1).put("child", jsonArrayMenu2);
                        break;

                    case 99:
                        log.info("this is just POP : {}", jsonArray.getJSONObject(i).getString("NODE_TITLE"));
                        break;

                    default:
                        log.info("menu do not imported : {}", jsonArray.getJSONObject(i).getString("NODE_TITLE"));
                        break;
                }
            } else if ("DEV".equals(jsonArray.getJSONObject(i).getString("VIEW_TRGT")))
            {
                JSONObject jsonTemp = new JSONObject();
                jsonTemp = jsonArray.getJSONObject(i);
                jsonTemp.put("title", jsonArray.getJSONObject(i).getString("NODE_TITLE"));

                switch (jsonArray.getJSONObject(i).getInt("NODE_LVL"))
                {
                    case 1:
                        if (jsonArray.getJSONObject(i).containsKey("ICON_CLASS_NM"))
                        {
                            jsonTemp.put("icon", jsonArray.getJSONObject(i).getString("ICON_CLASS_NM"));
                        }

                        jsonArrayDevMenu1.add(jsonTemp);
                        jsonArrayDevMenu2 = new JSONArray();    //초기화
                        break;

                    case 2:
                        jsonArrayDevMenu2.add(jsonTemp);
                        jsonArrayDevMenu1.getJSONObject(jsonArrayDevMenu1.size() - 1).put("child", jsonArrayDevMenu2);
                        jsonArrayDevMenu3 = new JSONArray();    //초기화
                        break;

                    case 3:
                        jsonArrayDevMenu3.add(jsonTemp);
                        jsonArrayDevMenu2.getJSONObject(jsonArrayDevMenu2.size() - 1).put("child", jsonArrayDevMenu3);
                        jsonArrayDevMenu1.getJSONObject(jsonArrayDevMenu1.size() - 1).put("child", jsonArrayDevMenu2);
                        break;

                    case 99:
                        log.info("this is just POP : {}", jsonArray.getJSONObject(i).getString("NODE_TITLE"));
                        break;

                    default:
                        log.info("menu do not imported : {}", jsonArray.getJSONObject(i).getString("NODE_TITLE"));
                        break;
                }
            }
        }

        retJson.setDataObject("MENU_ITEMS", jsonArrayMenu1);
        retJson.setDataObject("DEV_MENU_ITEMS", jsonArrayDevMenu1);

        return retJson;
    }


    /**
     * 프레임상단 권한에 따른 전체 메뉴목록을 조회한다
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON getAllMenuListWithAuth(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectAllMenuInfoWithAuth", jsonParams);
    }


    /**
     * 프레임상단 실시간공지목록을 조회한다
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON getRtNotice(TelewebJSON jsonParams) throws TelewebAppException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        //DAO검색 메소드 호출
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectRtNotice", jsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        return objRetParams;
    }


    /**
     * 프레임상단 실시간공지 팝업을 조회한다
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON getPopRtNotice(TelewebJSON jsonParams) throws TelewebAppException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        //DAO검색 메소드 호출
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectPopRtNotice", jsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 프레임상단 메뉴정보를 조회한다
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectGetMenuBaseInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectGetMenuBaseInfo", jsonParams);
    }


    /**
     * 알림톡템플릿을 조회한다.
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectAlimtalkTmplat(TelewebJSON mjsonParams) throws TelewebAppException
    {

        return mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectAlimtalkTmplat", mjsonParams);

    }


    /**
     * 알림톡전송이력을 저장한다.
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertAlimtalkHist(TelewebJSON mjsonParams) throws TelewebAppException
    {

        return mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "insertAlimtalkHist", mjsonParams);

    }


    /**
     * 휴대폰번호로 고객id를 조회 한다.
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectCstmrId(TelewebJSON mjsonParams) throws TelewebAppException
    {

        return mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectCstmrId", mjsonParams);

    }


    /**
     * sms템플릿를 조회한다
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectSmsTmplat(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectSmsMngList", jsonParams);
    }


    /**
     * 이메일전송이력을 저장한다.
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertEmailHist(TelewebJSON mjsonParams) throws TelewebAppException
    {

        return mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "insertEmailHist", mjsonParams);

    }


    /**
     * 회사구분 조회
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectCustcoId(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retJson = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        retJson = mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectCustcoId", jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectAspCustNm", retJson);
        return objRetParams;
    }


    /**
     * 사용자 권한 조회
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectAuth(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectAuth", jsonParams);
        return objRetParams;
    }


    /**
     * 홈화면 상담이력.
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectCountCNSL(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectCountCNSL", jsonParams);
    }


    /**
     * 사용자 권한 조회
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON getUserAuth(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retJson = new TelewebJSON(jsonParams);
        retJson = mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "getUserAuth", jsonParams);
        return retJson;
    }

    /**
     * 간편인증 전 고객 업데이트
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON setBFOmniAuthUser(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "setBFOmniAuthUser", jsonParams);
    }

    /**
     * 간편인증 후 고객 업데이트
     *
     * @param TelewebJSON
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @Transactional(readOnly = true)
    public TelewebJSON setAFOmniAuthUser(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "setAFOmniAuthUser", jsonParams);
    }
}
