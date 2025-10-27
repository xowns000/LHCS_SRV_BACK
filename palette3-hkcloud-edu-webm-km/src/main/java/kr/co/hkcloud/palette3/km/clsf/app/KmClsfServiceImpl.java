package kr.co.hkcloud.palette3.km.clsf.app;


import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("kmClsfService")
public class KmClsfServiceImpl implements KmClsfService
{
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO            mobjDao;

    /*
     * 지식분류 Tree 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON clsfTreeList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "clsfTreeList", mjsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON clsfInfo(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "clsfInfo", mjsonParams);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON clsfProc(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        
        //  등록
        if(StringUtils.isBlank(mjsonParams.getString("KMS_CLSF_ID"))) {
            
            // 1. 지식분류 등록
            int KMS_CLSF_ID = innbCreatCmmnService.createSeqNo("KMS_CLSF_ID");
            mjsonParams.setInt("KMS_CLSF_ID", KMS_CLSF_ID);
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "INSERT_KMS_CLSF", mjsonParams);
            
            // 2. 지식분류 권한 등록 -- 관리자 선택시만 진행
            String PIC_ID = mjsonParams.getString("PIC_ID");
            if( !StringUtils.isBlank(PIC_ID) ) {
                int KMS_CLSF_AUTHRT_ID = innbCreatCmmnService.createSeqNo("KMS_CLSF_AUTHRT_ID");
                mjsonParams.setInt("KMS_CLSF_AUTHRT_ID", KMS_CLSF_AUTHRT_ID);
                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "INSERT_KMS_CLSF_AUTHRT", mjsonParams);
            }
            
        }
        //  수정
        else {
            // 1. 지식분류 수정
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "UPDATE_KMS_CLSF", mjsonParams);
            
            /*      이력관리 할 경우 사용.
             * 기존 Row 전체 DEL_YN = 'Y' 후 새로운 Row Insert 방식 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~! Start..
             * */
            
            // 2. 해당 분류의 권한 모두 DEL_YN 값 Y 로 수정
//            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "UPDATE_KMS_CLSF_AUTHRT_ALL_DEL", mjsonParams);
            
            // 3. 해당 분류의 새로운 권한 INSERT
//            String PIC_ID = mjsonParams.getString("PIC_ID");
//            if( !StringUtils.isBlank(PIC_ID) ) {
//                int KMS_CLSF_AUTHRT_ID = innbCreatCmmnService.createSeqNo("KMS_CLSF_AUTHRT_ID");
//                mjsonParams.setInt("KMS_CLSF_AUTHRT_ID", KMS_CLSF_AUTHRT_ID);
//                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "INSERT_KMS_CLSF_AUTHRT", mjsonParams);
//            }
            
            /*
             * 기존 Row 전체 DEL_YN = 'Y' 후 새로운 Row Insert 방식 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~! End..
             * */
            
            /*       이력관리 없이 사용 - 삭제시 삭제 Flag Update 수정시 업데이트, 삭제 후 등록시 새로  Insert
             * 2. 해당 분류가 있다면 업데이트 처리 방식 Start ~~~~~~~~~~~~~~~~~~~~ 
             * */
            String sKMS_CLSF_AUTHRT_ID = mjsonParams.getString("KMS_CLSF_AUTHRT_ID");
            if( !StringUtils.isBlank( sKMS_CLSF_AUTHRT_ID ) ) {
                String PIC_ID = mjsonParams.getString("PIC_ID");
                if( !StringUtils.isBlank(PIC_ID) ) {
                    mjsonParams.setString("DEL_YN", "N");
                } else {
                    mjsonParams.setString("DEL_YN", "Y");
                }
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "UPDATE_KMS_CLSF_AUTHRT", mjsonParams);
            } else {
                String PIC_ID = mjsonParams.getString("PIC_ID");
                if( !StringUtils.isBlank(PIC_ID) ) {
                    int KMS_CLSF_AUTHRT_ID = innbCreatCmmnService.createSeqNo("KMS_CLSF_AUTHRT_ID");
                    mjsonParams.setInt("KMS_CLSF_AUTHRT_ID", KMS_CLSF_AUTHRT_ID);
                    objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "INSERT_KMS_CLSF_AUTHRT", mjsonParams);
                }
            }
            /*
             * 2. 해당 분류가 있다면 업데이트 처리 방식 End ~~~~~~~~~~~~~~~~~~~~ 
             * */
            
        }

        return objRetParams;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON clsfOrderUpdate(TelewebJSON mjsonParams) throws TelewebAppException
    {
        // 바꾸려는 Key 값 조회
        TelewebJSON selectInfoParam = new TelewebJSON();
        selectInfoParam.setString("UP_KMS_CLSF_ID", mjsonParams.getString("UP_KMS_CLSF_ID"));
        selectInfoParam.setString("SORT_ORD", mjsonParams.getString("CHG_ORD"));
        
        selectInfoParam = mobjDao.select("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "clsfInfo", selectInfoParam);
        String otherKmsClsfId = selectInfoParam.getString("KMS_CLSF_ID");
        
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        
        // 1. 바꾸려는 Key 값의 SortOrd 변경
        TelewebJSON udtParam = new TelewebJSON();
        udtParam.setString("KMS_CLSF_ID", otherKmsClsfId);
        udtParam.setString("SORT_ORD", mjsonParams.getString("SORT_ORD"));
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "UPDATE_KMS_CLSF", udtParam);
        
        // 선택한 분류의 순서변경
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "UPDATE_KMS_CLSF_SORT_ORDER", mjsonParams);
        
        return objRetParams;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON clsfDel(TelewebJSON mjsonParams) throws TelewebAppException
    {
        // 삭제되는 분류의 권한 모두 삭제여부 Update
        mobjDao.update("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "UPDATE_KMS_CLSF_AUTHRT_ALL_DEL", mjsonParams);
        mjsonParams.setString("DEL_YN", "Y");
        // 지식분류 삭제여부 Update
        return mobjDao.update("kr.co.hkcloud.palette3.km.clsf.dao.kmClsfMapper", "UPDATE_KMS_CLSF", mjsonParams);
    }

}
