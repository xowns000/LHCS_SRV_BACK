package kr.co.hkcloud.palette3.config.view.excel;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import kr.co.hkcloud.palette3.excel.util.ExcelWriterUtils;


/**
 * 엑셀 view
 * 
 * @author leeiy
 *
 */
@Component
public class ExcelXlsxView extends AbstractXlsxView
{

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        new ExcelWriterUtils(workbook, model, request, response).create();
    }

}
