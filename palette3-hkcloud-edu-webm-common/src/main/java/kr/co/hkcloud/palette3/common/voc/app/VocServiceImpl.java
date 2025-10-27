package kr.co.hkcloud.palette3.common.voc.app;


import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * Description : VOC Impl
 * package  : kr.co.hkcloud.palette3.setting.voc.app
 * filename : VocServiceImpl.java
 * Date : 2023. 6. 9.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 6. 9., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service("vocService")
public class VocServiceImpl implements VocService
{
	private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO mobjDao;


    /**
     * VOC-목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON vocList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.common.voc.dao.VocMapper", "vocList", mjsonParams);
    }


    /**
     * VOC-등록, 수정
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON vocProc(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성

        //등록
        if(StringUtils.isBlank(mjsonParams.getString("VOC_RCPT_ID")) || "-1".equals(mjsonParams.getString("VOC_RCPT_ID"))) {
        	int VOC_RCPT_ID = innbCreatCmmnService.createSeqNo("VOC_RCPT_ID");
        	mjsonParams.setInt("VOC_RCPT_ID", VOC_RCPT_ID);
        	
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.common.voc.dao.VocMapper", "INSERT_VOC", mjsonParams);
        }else{ //수정
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.common.voc.dao.VocMapper", "UPDATE_VOC", mjsonParams);
        }

        return objRetParams;
    }
    
}
