package kr.co.hkcloud.palette3.excel.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ExcelService
{
    /**
     * 
     * Excel 데이터 조회
     * @Method Name  	: excelDataList
     * @date   			: 2023. 11. 16.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON excelDataList(TelewebJSON mjsonParams) throws TelewebAppException;
}
