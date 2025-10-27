package kr.co.hkcloud.palette3.excel.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("excelService")
public class ExcelServiceImpl implements ExcelService
{
    private final TwbComDAO mobjDao;


    /**
     * Excel 데이터 조회
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON excelDataList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams); // 반환 파라미터 생성
        
        objRetParams = mobjDao.select(jsonParams.getString("serviceNm"), jsonParams.getString("sqlId"), jsonParams);

        return objRetParams;
    }
}
