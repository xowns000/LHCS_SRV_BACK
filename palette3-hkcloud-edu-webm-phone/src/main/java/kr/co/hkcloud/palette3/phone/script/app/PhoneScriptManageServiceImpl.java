package kr.co.hkcloud.palette3.phone.script.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneScriptManageService")
public class PhoneScriptManageServiceImpl implements PhoneScriptManageService
{
    private final TwbComDAO            twbComDao;
    private final InnbCreatCmmnService innbCreatCmmnService;


    /**
     * 스크립트 저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnScriptMng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON objRetKey = new TelewebJSON();
        TelewebJSON objRetDate = new TelewebJSON();
//        TelewebJSON jsonFileParams = new TelewebJSON();
//        String strRegID = mjsonParams.getString("USER_ID");
//        String strRegDeptCode = mjsonParams.getString("WRTR_DRPT_CD");
//        String strFileGroupKey = "";
        String LVL_NO = mjsonParams.getString("LVL_NO");                        // 레벨
        int EOT_DATE = Integer.parseInt(mjsonParams.getString("EOT_DATE"));     // 종료일자
        int MAX_EOT_DATE;                                                       // 상위 노드들의 가장 큰 종료일자

        objRetKey = twbComDao.select("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptManageMapper", "selectScriptKey", mjsonParams);

        // key 생성
        if(objRetKey != null) {

            // key 매핑
            mjsonParams.setString("SCRIPT_ID", objRetKey.getString("MAX_SCRIPT_ID"));
            // 상위 노드들의 가장 큰 종료일자
            // objRetDate = twbComDao.select("kr.co.hkcloud.palette.phone.script.dao.PhoneScriptManageMapper", "selectRtnMaxEotDate", mjsonParams);
            // MAX_EOT_DATE = Integer.parseInt(objRetDate.getString("EOT_DATE"));
//            jsonFileParams.setDataObject(mjsonParams.getDataObject("ATTACH"));
//
//            //첨부파일이 존재할 경우 공통을 이용하여 첨부된 파일을 저장한다.
//            if(mjsonParams.getDataObject("ATTACH") != null && jsonFileParams.getDataObject("DATA").size() > 0) {
//
//                strFileGroupKey = innbCreatCmmnService.getSeqNo(jsonFileParams, "TWB");
//
//                // [파일] objComFileBiz.saveFile: 전화-스크립트저장
//                objComFileBiz.saveFile(jsonFileParams, strFileGroupKey, strRegID, strRegDeptCode, "TWB");
//                //objComFileBiz.saveFile(jsonFileParams, strRegID, strRegDeptCode);
//
//                mjsonParams.setString("FILE_GROUP_KEY", strFileGroupKey);
//            }

            // 중, 소분류를 추가할 때
            if(!LVL_NO.equals("1")) {
                //  if(EOT_DATE > MAX_EOT_DATE) {
                objRetParams = twbComDao.insert("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptManageMapper", "insertRtnScriptMng", mjsonParams);
                //      twbComDao.update("kr.co.hkcloud.palette.phone.script.dao.PhoneScriptManageMapper", "updateRtnEotDate", mjsonParams);
                //   }
            }
            else {
                objRetParams = twbComDao.insert("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptManageMapper", "insertRtnScriptMng", mjsonParams);
            }
        }
        return objRetParams;
    }


    /**
     * 스크립트 업데이트
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnScriptMng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        log.debug("11mjsonParams ==========" + mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON();
//        TelewebJSON jsonFileParams = new TelewebJSON();
//
//        String strRegID = mjsonParams.getString("USER_ID");
//        String strRegDeptCode = mjsonParams.getString("WRTR_DRPT_CD");
//        String strOldFileGroupKey = mjsonParams.getString("OLD_FILE_GROUP_KEY");
//
//        String strFileGroupKey = "";
//
//        jsonFileParams.setDataObject(mjsonParams.getDataObject("ATTACH"));
//        log.debug("getDataObject ==========" + mjsonParams.getDataObject("ATTACH"));
//        log.debug("jsonFileParams ==========" + jsonFileParams);
//        if(mjsonParams.getDataObject("ATTACH") != null && jsonFileParams.getDataObject("DATA").size() > 0) {
//
//            // [파일] objComFileBiz.saveFile: 전화-스크립트 업데이트
//            strFileGroupKey = innbCreatCmmnService.getSeqNo(jsonFileParams, "TWB");
////            objComFileBiz.saveFile(jsonFileParams, strFileGroupKey, strRegID, strRegDeptCode, "TWB");
//
//            mjsonParams.setString("FILE_GROUP_KEY", strFileGroupKey);
//
//        }

        // 스크립트 변경 이력 등록
        twbComDao.insert("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptChangeManageMapper", "insertScrtChng", mjsonParams);

//        //기존첨부파일있으면서 신규파일이 존재하면 그룹키변경
//        if(!strOldFileGroupKey.equals("") && !strFileGroupKey.equals("")) {
//            twbComDao.update("kr.co.hkcloud.palette.phone.script.dao.PhoneScriptManageMapper", "updateRtnScriptFileGroupKey", mjsonParams);
//        }

        objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptManageMapper", "updateRtnScriptMng", mjsonParams);

        return objRetParams;
    }


    /**
     * 스크립트정보 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnScriptMng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//        TelewebJSON objRetFileParams = new TelewebJSON(mjsonParams);

//        //파일존재하는지 체크
//        objRetFileParams = twbComDao.select("kr.co.hkcloud.palette.phone.script.dao.PhoneScriptInqireMapper", "selectRtnScriptFile", mjsonParams);
//        //파일삭제
//        if(objRetFileParams.getDataObject().size() > 0) {
//            // [파일] objComFileBiz.deleteFile: 전화-스크립트삭제
//            objComFileBiz.deleteFile(objRetFileParams);
//        }

        objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptManageMapper", "deleteRtnScriptMng", mjsonParams);

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON checkAttachFilCnt(TelewebJSON jsonParams) throws TelewebAppException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON();

        jsonParams = twbComDao.select("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptManageMapper", "SELECT_ATTACH_FILE_CNT", jsonParams);

        //테이블의 그룹파일키 널 업데이터 처리
        if("0".equals(objRetParams.getString("ATTACH_CNT"))) {
            objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptManageMapper", "UPDATE_NULL_TABLE_FILE_GROUP_KEY", jsonParams);
        }

        return objRetParams;
    }


    /**
     * 스크립트 변경요청
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON requestRtnScriptMng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        log.debug("11mjsonParams ==========" + mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = twbComDao.insert("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptChangeManageMapper", "insertScrtChng", mjsonParams);

        return objRetParams;
    }


    /**
     * 스크립트 변경승인
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON finRequestRtnScriptMng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        log.debug("11mjsonParams ==========" + mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON();
        if("SUCCESS".equals(objRetParams.getString("CHNG_REQ"))) {
            twbComDao.update("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptManageMapper", "updateRtnScriptMng", mjsonParams);
        }
        objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptManageMapper", "updateScrtChng", mjsonParams);

        return objRetParams;
    }
}
