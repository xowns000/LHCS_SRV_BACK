package kr.co.hkcloud.palette3.km.conts.app;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.shortcut.app.ShortcutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("kmContsService")
public class KmContsServiceImpl implements KmContsService {

    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO mobjDao;
    private final ShortcutService shortcutService;
    private final String namespace = "kr.co.hkcloud.palette3.km.conts.dao.KmContsMapper";

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectContentList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectContentList", mjsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectContent(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectContent", mjsonParams);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON mergeContent(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        String HIST_YN = "N";

        // 검토이력 등록 여부
        if (!StringUtils.isBlank(mjsonParams.getString("HIST_YN"))) {
            HIST_YN = mjsonParams.getString("HIST_YN");
        }

        //  등록  --  등록시 검토이력은 INSERT 하지 않는다. 20230704 

        if (StringUtils.isBlank(mjsonParams.getString("KMS_CONTS_ID"))) {
            // 1. 지식컨텐츠 등록
            int KMS_CONTS_ID = innbCreatCmmnService.createSeqNo("KMS_CONTS_ID");
            mjsonParams.setInt("KMS_CONTS_ID", KMS_CONTS_ID);
            //mjsonParams.setString("STTS_CD", "WRTG"); -- 개인 스크립트 등록 시 상태 : 공개(PBIC)
            objRetParams = mobjDao.insert(namespace, "insertContent", mjsonParams);
        }
        //  수정
        else {
            // 1. 지식컨텐츠 수정
            objRetParams = mobjDao.update(namespace, "updateContent", mjsonParams);

            if ("Y".equalsIgnoreCase(HIST_YN)) {
                int KMS_CONTS_RVW_HSTRY_ID = innbCreatCmmnService.createSeqNo("KMS_CONTS_RVW_HSTRY_ID");

                TelewebJSON histParam = new TelewebJSON();
                histParam.setInt("KMS_CONTS_RVW_HSTRY_ID", KMS_CONTS_RVW_HSTRY_ID);
                histParam.setString("KMS_CONTS_ID", mjsonParams.getString("KMS_CONTS_ID"));
                histParam.setString("STTS_CD", mjsonParams.getString("STTS_CD"));
                histParam.setString("USER_ID", mjsonParams.getString("USER_ID"));
                if (!StringUtils.isBlank(mjsonParams.getString("CN"))) {
                    histParam.setString("CN", mjsonParams.getString("CN"));
                }

                objRetParams = mobjDao.insert(namespace, "insertContentReviewHistory", histParam);
            }
        }
        //KMS_CONTS_ID 값 리턴
        objRetParams.setString("KMS_CONTS_ID", mjsonParams.getString("KMS_CONTS_ID"));
        //KMS_콘텐츠_내용 등록/수정
        mergeContentCn(mjsonParams);

        //KMS_콘텐츠_키워드 등록/삭제
        mergeKeywordRtn(mjsonParams, mjsonParams.getString("KMS_CONTS_ID"));
        
        
        //표시 순서(SORD_ORD) 업데이트 - 해당 지식분류의 지식 콘텐츠의 표시 순서와 값이 같거나 큰 수의 콘텐츠 목록을 조회하여 표시 순서 값을 업데이트 한다.
        TelewebJSON sortOrdParams = new TelewebJSON();
        if("Y".equals(mjsonParams.getString("COMM_YN"))) {
            //지식 콘텐츠 관리의 공통 콘텐츠 등록/수정
            sortOrdParams.setString("KMS_CLSF_ID",  mjsonParams.getString("KMS_CLSF_ID"));
            sortOrdParams.setString("BASE_KMS_CONTS_ID",  mjsonParams.getString("KMS_CONTS_ID"));
            sortOrdParams.setString("BASE_SORT_ORD",  mjsonParams.getString("SORT_ORD"));
        } else {
            //우측 지식 정보의 개인 스크립트 등록/수정
            sortOrdParams.setString("KMS_CLSF_ID",  mjsonParams.getString("KMS_CLSF_ID"));
            sortOrdParams.setString("SE_CD", "SCRIPT");
            sortOrdParams.setString("COMM_YN", "N");
            sortOrdParams.setString("BASE_KMS_CONTS_ID",  mjsonParams.getString("KMS_CONTS_ID"));
            sortOrdParams.setString("BASE_SORT_ORD",  mjsonParams.getString("SORT_ORD"));
            sortOrdParams.setString("IS_PRIVATE",  mjsonParams.getString("Y"));
        }
        updateContentSortOrd(sortOrdParams);
        
        
        

        return objRetParams;
    }
    
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON updateContent(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        
        String HIST_YN = "N";

        // 검토이력 등록 여부
        if (!StringUtils.isBlank(mjsonParams.getString("HIST_YN"))) {
            HIST_YN = mjsonParams.getString("HIST_YN");
        }
        
        // 1. 지식컨텐츠 수정
        objRetParams = mobjDao.update(namespace, "updateContent", mjsonParams);
        
        if ("Y".equalsIgnoreCase(HIST_YN)) {
            int KMS_CONTS_RVW_HSTRY_ID = innbCreatCmmnService.createSeqNo("KMS_CONTS_RVW_HSTRY_ID");

            TelewebJSON histParam = new TelewebJSON();
            histParam.setInt("KMS_CONTS_RVW_HSTRY_ID", KMS_CONTS_RVW_HSTRY_ID);
            histParam.setString("KMS_CONTS_ID", mjsonParams.getString("KMS_CONTS_ID"));
            histParam.setString("STTS_CD", mjsonParams.getString("STTS_CD"));
            histParam.setString("USER_ID", mjsonParams.getString("USER_ID"));
            if (!StringUtils.isBlank(mjsonParams.getString("CN"))) {
                histParam.setString("CN", mjsonParams.getString("CN"));
            }

            objRetParams = mobjDao.insert(namespace, "insertContentReviewHistory", histParam);
        }

        //KMS_CONTS_ID 값 리턴
        objRetParams.setString("KMS_CONTS_ID", mjsonParams.getString("KMS_CONTS_ID"));
        return objRetParams;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON contsProcStts(TelewebJSON mjsonParams) throws TelewebAppException {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성

        String sHistYn = mjsonParams.getString("HIST_YN");
        String sUserId = mjsonParams.getString("USER_ID");
        String sSttsCd = mjsonParams.getString("STTS_CD");
        String sCn = mjsonParams.getString("CN");
        String sSortOrd = mjsonParams.getString("SORT_ORD");

        JSONArray jContsList = mjsonParams.getDataObject();

        if (!jContsList.isEmpty()) {
            for (int i = 0; i < jContsList.size(); i++) {
                JSONObject tmpConts = jContsList.getJSONObject(i);

                TelewebJSON contsJson = new TelewebJSON();
                contsJson.setString("HIST_YN", sHistYn);
                contsJson.setString("USER_ID", sUserId);
                contsJson.setString("KMS_CONTS_ID", tmpConts.getString("KMS_CONTS_ID"));
                contsJson.setString("STTS_CD", sSttsCd);
                if (!StringUtils.isBlank(sCn)) {
                    contsJson.setString("CN", sCn);
                }
                if (!StringUtils.isBlank(sSortOrd)) {
                    contsJson.setString("SORT_ORD", sSortOrd);
                }
                objRetParams = this.updateContent(contsJson);
                
                //삭제 시 단축키 삭제 - 해당 KMS_CONTS_ID 와 링크된 단축키 전체 삭제(모든 사용자 대상)
                if("DELT".equals(sSttsCd)) {
                    TelewebJSON shortcutParamsJson = new TelewebJSON();
                    shortcutParamsJson.setString("CUSTCO_ID", mjsonParams.getString("CUSTCO_ID"));
                    shortcutParamsJson.setString("LNK_ID", tmpConts.getString("KMS_CONTS_ID"));
                    shortcutService.deleteShortcut(shortcutParamsJson);
                }
            }
        }

        return objRetParams;
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON kmContsApprManageList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "kmContsApprManageList", mjsonParams);
    }
    
    
    /**
     * 표시 순서(SORD_ORD) 업데이트 - 해당 지식분류의 지식 콘텐츠의 표시 순서와 값이 같거나 큰 수의 콘텐츠 목록을 조회하여 표시 순서 값을 업데이트 한다.
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateContentSortOrd(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON contentListParams = selectContentList(mjsonParams);
        
        JSONArray contentList = contentListParams.getDataObject();
        int baseSortOrd = mjsonParams.getInt("BASE_SORT_ORD");
        for( int i=0; i < contentList.size(); i++) {
            JSONObject content = contentList.getJSONObject(i);
            //삭제된 건은 SORT_ORD가 999 여서 제외.
            if(!mjsonParams.getString("BASE_KMS_CONTS_ID").equals(content.getString("KMS_CONTS_ID")) 
                && !"DELT".equals(content.getString("STTS_CD"))) {
                TelewebJSON updateParams = new TelewebJSON();
                baseSortOrd ++;
                updateParams.setString("KMS_CONTS_ID", content.getString("KMS_CONTS_ID"));
                updateParams.setInt("SORT_ORD", baseSortOrd);
                
                mobjDao.update(namespace, "updateContentSortOrd", updateParams);
            }
            
        }
        
        return mjsonParams;
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectReviewHistoryList(TelewebJSON mjsonParams) throws TelewebAppException {
        return  mobjDao.select(namespace, "selectReviewHistoryList", mjsonParams);
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectNewSortOrd(TelewebJSON mjsonParams) throws TelewebAppException {
        return  mobjDao.select(namespace, "selectNewSortOrd", mjsonParams);
    }

    /**
     * 콘텐츠 키워드 저장.
     * 
     * @param jsonParams
     * @param KMS_CONTS_ID
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    private void mergeKeywordRtn(TelewebJSON jsonParams, String KMS_CONTS_ID) throws TelewebAppException {

        try {
            String list = jsonParams.getString("LIST").toString();
            list = list.replace("&#91;", "[").replace("&#93;", "]");
            List<String> listKW = ((List<String>) new ObjectMapper().readValue(list, List.class));

            mobjDao.delete(namespace, "deleteContentKeywd", jsonParams);

            for (String keyword : listKW) {
                TelewebJSON param = new TelewebJSON(jsonParams);
                param.setString("KMS_KEYWD_ID", innbCreatCmmnService.createSeqNo("KMS_KEYWD_ID") + "");
                param.setString("KMS_CONTS_ID", KMS_CONTS_ID);
                param.setString("KMS_KEYWD_NM", keyword);

                mobjDao.insert(namespace, "insertContentKeywd", param);
            }
        } catch (Exception e) {
            throw new TelewebAppException(e.getMessage());
        }
    }

    /**
     * 콘텐츠 내용 저장/수정
     * 
     * @param jsonParams
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    private void mergeContentCn(TelewebJSON jsonParams) throws TelewebAppException {
        try {
            mobjDao.insert(namespace, "mergeContentCn", jsonParams);
        } catch (Exception e) {
            throw new TelewebAppException(e.getMessage());
        }
    }

}
