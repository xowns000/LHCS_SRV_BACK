package kr.co.hkcloud.palette3.config.view.excel;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import kr.co.hkcloud.palette3.excel.util.ExcelWriterUtils;


/**
 * 엑셀 이전 버전용 View
 * 
 * @author leeiy
 *
 */
@Component
public class ExcelXlsView extends AbstractXlsView
{

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        new ExcelWriterUtils(workbook, model, request, response).create();
    }

}
