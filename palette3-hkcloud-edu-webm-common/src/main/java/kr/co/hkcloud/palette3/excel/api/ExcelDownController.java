package kr.co.hkcloud.palette3.excel.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteUtils;
import kr.co.hkcloud.palette3.excel.app.ExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ExceDownController", description = "엑셀 다운로드 컨트롤러")
public class ExcelDownController {

    private final ExcelService excelService;

    public CellStyle TITLE_STYLE = null;          //제목 스타일
    public CellStyle SUB_TITLE_STYLE = null;      //서브 제목 스타일
    public CellStyle TABLE_HEADER_STYLE = null;   //부제목 스타일
    public CellStyle BODY_STYLE_LEFT = null;           //내용 스타일
    public CellStyle BODY_STYLE_RIGHT = null;           //내용 스타일
    public CellStyle BODY_STYLE_CENTER = null;           //내용 스타일
    public CellStyle INFO_STYLE = null;           //정보 스타일

    public CellStyle TITLE_STYLE2 = null;          //제목 스타일
    public CellStyle SUB_TITLE_STYLE2 = null;      //서브 제목 스타일
    public CellStyle TABLE_HEADER_STYLE2 = null;   //부제목 스타일
    public CellStyle BODY_STYLE_LEFT2 = null;           //내용 스타일
    public CellStyle BODY_STYLE_RIGHT2 = null;           //내용 스타일
    public CellStyle BODY_STYLE_CENTER2 = null;           //내용 스타일
    public CellStyle INFO_STYLE2 = null;           //정보 스타일
    public CellStyle NUMBER_STYLE_CENTER2 = null; //숫자용 스타일

    public CellStyle XSS_TITLE_STYLE = null;          //제목 스타일
    public CellStyle XSS_SUB_TITLE_STYLE = null;      //서브 제목 스타일
    public CellStyle XSS_TABLE_HEADER_STYLE = null;   //부제목 스타일
    public CellStyle XSS_BODY_STYLE_LEFT = null;           //내용 스타일
    public CellStyle XSS_BODY_STYLE_RIGHT = null;           //내용 스타일
    public CellStyle XSS_BODY_STYLE_CENTER = null;           //내용 스타일
    public CellStyle XSS_INFO_STYLE = null;           //정보 스타일

    public void excel_tmpl(HttpServletRequest request, HttpServletResponse response,
        @TelewebJsonParam TelewebJSON mjsonParams) throws Exception {

        try {

            String downFileName = this.doGetFileName(mjsonParams.getString("sFileName"));

            if (mjsonParams.getInt("iExcelLimitRowCnt") > 0) {

                int iExcelLimitRowCnt = mjsonParams.getInt("iExcelLimitRowCnt");

                SXSSFWorkbook workbook = new SXSSFWorkbook();

                try {

                    TelewebJSON resultList = excelService.excelDataList(mjsonParams);

                    int iTotalResultCnt = resultList.getSize();

                    int sheetCount = (int) (1 + Math.ceil(iTotalResultCnt / iExcelLimitRowCnt));

                    ArrayList<ArrayList> totalList = new ArrayList<ArrayList>();

                    ArrayList<JSONObject> itemList = new ArrayList<JSONObject>();

                    int x = 1;
                    for (int i = 0; i < iTotalResultCnt; i++) {
                        itemList.add((JSONObject) resultList.getDataObject().get(i));

                        if (i == (iExcelLimitRowCnt * x) - 1) {
                            totalList.add((ArrayList) itemList);
                            x = x + 1;
                            itemList = new ArrayList<JSONObject>();
                        } else if (i == iTotalResultCnt - 1) {
                            totalList.add((ArrayList) itemList);
                        }
                    }

                    for (int y = 0; y < sheetCount; y++) {
                        int rowCnt = 0;

                        String sheetName = "sheet" + (y + 1);
                        SXSSFSheet sheet = workbook.createSheet(sheetName);
                        this.initStyle(workbook);
                        this.initStyle2(workbook);

                        //타이틀.
                        Row mainTitle = sheet.createRow(rowCnt++);
                        Cell titleCell = mainTitle.createCell(0);
                        titleCell.setCellValue(mjsonParams.getString("sTitleName") + (sheetCount > 1 ? (y + 1) : ""));
                        titleCell.setCellStyle(TITLE_STYLE2);

                        int headerCnt = PaletteUtils.stringToArray(mjsonParams.getString("sHeaderData"), "||").size();
                        ArrayList arrHeader = PaletteUtils.stringToArray(mjsonParams.getString("sHeaderData"), "||");

                        int columnCnt = PaletteUtils.stringToArray(headerCnt == 1 ? mjsonParams.getString("sHeaderData")
                            : (String) arrHeader.get(headerCnt - 1), ",").size();
                        ArrayList arrHeaderData = PaletteUtils.stringToArray(headerCnt == 1 ? mjsonParams.getString("sHeaderData")
                            : (String) arrHeader.get(headerCnt - 1), ",");

                        sheet.addMergedRegion(new CellRangeAddress(rowCnt - 1, rowCnt - 1, 0, columnCnt - 1));

                        //빈줄 하나.
                        Row emptyRow = sheet.createRow(rowCnt++);

                        if (headerCnt > 1) {
                            for (int h = 0; h < headerCnt; h++) {
                                Row headerRow = sheet.createRow(rowCnt++);
                                if (h < headerCnt - 1) {

                                    for (int i = 0, j = columnCnt; i < j; i++) {
                                        Cell headerCell = headerRow.createCell(i);
                                        headerCell.setCellStyle(TABLE_HEADER_STYLE2);

                                        int upHeaderCnt = PaletteUtils.stringToArray((String) arrHeader.get(h), ",").size();
                                        ArrayList arrUpHeaderData = PaletteUtils.stringToArray((String) arrHeader.get(h), ",");

                                        for (int k = 0, l = upHeaderCnt; k < l; k++) {
                                            String _HeaderStr = (String) arrUpHeaderData.get(k);
                                            if (i == Integer.parseInt(_HeaderStr.split("\\^")[0])) {
                                                headerCell.setCellValue(_HeaderStr.split("\\^")[2]);

                                                if (_HeaderStr.split("\\^")[0].equals(_HeaderStr.split("\\^")[1])) {
                                                    sheet.addMergedRegion(new CellRangeAddress(rowCnt - 1, (rowCnt - 1) + (headerCnt - 1),
                                                        Integer.parseInt(_HeaderStr.split("\\^")[0]), Integer.parseInt(_HeaderStr.split(
                                                            "\\^")[1])));
                                                } else {
                                                    sheet.addMergedRegion(new CellRangeAddress(rowCnt - 1, rowCnt - 1, Integer.parseInt(
                                                        _HeaderStr.split("\\^")[0]), Integer.parseInt(_HeaderStr.split("\\^")[1])));
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    for (int i = 0, j = columnCnt; i < j; i++) {
                                        Cell headerCell = headerRow.createCell(i);
                                        headerCell.setCellStyle(TABLE_HEADER_STYLE2);

                                        String _HeaderStr = (String) arrHeaderData.get(i);
                                        headerCell.setCellValue(_HeaderStr.split("\\^")[1]);
                                    }
                                }
                            }
                        } else {
                            Row headerRow = sheet.createRow(rowCnt++);
                            for (int i = 0, j = columnCnt; i < j; i++) {
                                Cell headerCell = headerRow.createCell(i);
                                headerCell.setCellStyle(TABLE_HEADER_STYLE2);

                                String _HeaderStr = (String) arrHeaderData.get(i);
                                headerCell.setCellValue(_HeaderStr.split("\\^")[1]);
                            }
                        }

                        sheet.createFreezePane(0, 3 + (headerCnt - 1)); //3번째 row까지 틀 고정

                        ArrayList<JSONObject> itemListTemp = (ArrayList) totalList.get(y);

                        for (int k = 0, l = itemListTemp.size(); k < l; k++) {
                            Row bodyRow = sheet.createRow(rowCnt++);
                            JSONObject _map = (JSONObject) itemListTemp.get(k);

                            for (int i = 0, j = columnCnt; i < j; i++) {
                                Cell cell = bodyRow.createCell(i);

                                String _HeaderStr = (String) arrHeaderData.get(i);

                                String sContent = "RNUM".equals(_HeaderStr.split("\\^")[0].toUpperCase()) ? "" + (k + 1) : _map.getString(
                                    _HeaderStr.split("\\^")[0].toUpperCase());
                                sContent = PaletteUtils.convertHtmlCharacter(sContent).replaceAll("<br>", "\n");
                                sContent = PaletteUtils.removeTag(sContent);

                                if ("CUSL_TP_CL".equals(_HeaderStr.split("\\^")[0]) && sContent.indexOf("_##_") > -1)
                                    sContent = sContent.split("_##_")[1];

                                cell.setCellValue(sContent);
                                if (_HeaderStr.split("\\^").length > 2) {
                                    if (_HeaderStr.split("\\^")[2].toUpperCase().equals("LEFT")) {
                                        cell.setCellStyle(BODY_STYLE_LEFT);
                                    } else if (_HeaderStr.split("\\^")[2].toUpperCase().equals("RIGHT")) {
                                        cell.setCellStyle(BODY_STYLE_RIGHT);
                                    } else {
                                        cell.setCellStyle(BODY_STYLE_CENTER);
                                    }
                                } else {
                                    cell.setCellStyle(BODY_STYLE_CENTER);
                                }
                            }
                        }
                        this.autoSizeColumn(sheet, columnCnt);
                    }

                    response.setHeader("Content-Disposition", "attachment; filename=\"" + downFileName + "\"");
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    ServletOutputStream out = response.getOutputStream();

                    workbook.write(out);
                    workbook.close();
                    out.close();

                } catch (NullPointerException npe) {
                    throw npe;
                } catch (Exception ex) {
                    throw ex;
                }
            } else {

                byte[] excel = doCreateExcel(mjsonParams);
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + downFileName + "\"");
                response.setContentLength(excel.length);
                response.getOutputStream().write(excel);

            }

        } catch (NullPointerException npe) {
            throw npe;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public byte[] doCreateExcel(@TelewebJsonParam TelewebJSON mjsonParams) throws Exception {

        try {

            TelewebJSON list = excelService.excelDataList(mjsonParams);

            SXSSFWorkbook workbook = new SXSSFWorkbook();             //워크북 생성
            SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet("Sheet1");   //시트생성

            this.initStyle(workbook);
            int rowNum = 0;

            //페이지 푸터
            Footer footer = sheet.getFooter();
            footer.setRight("Page " + HeaderFooter.page() + " of " + HeaderFooter.numPages());

            //타이틀.
            SXSSFRow mainTitle = sheet.createRow(rowNum++);
            SXSSFCell titleCell = mainTitle.createCell(0);
            titleCell.setCellValue(mjsonParams.getString("sTitleName"));
            titleCell.setCellStyle(TITLE_STYLE);

            int headerCnt = PaletteUtils.stringToArray(mjsonParams.getString("sHeaderData"), "||").size();
            ArrayList arrHeader = PaletteUtils.stringToArray(mjsonParams.getString("sHeaderData"), "||");

            int columnCnt = PaletteUtils.stringToArray(headerCnt == 1 ? mjsonParams.getString("sHeaderData") : (String) arrHeader.get(
                headerCnt - 1), ",").size();
            ArrayList arrHeaderData = PaletteUtils.stringToArray(headerCnt == 1 ? mjsonParams.getString("sHeaderData") : (String) arrHeader
                .get(headerCnt - 1), ",");

            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, columnCnt - 1));

            //빈줄 하나.
            SXSSFRow emptyRow = sheet.createRow(rowNum++);

            if (headerCnt > 1) {
                for (int h = 0; h < headerCnt; h++) {
                    SXSSFRow tableHeader = sheet.createRow(rowNum++);
                    if (h < headerCnt - 1) {

                        for (int i = 0, j = columnCnt; i < j; i++) {
                            Cell headerCell = tableHeader.createCell(i);
                            headerCell.setCellStyle(TABLE_HEADER_STYLE2);

                            int upHeaderCnt = PaletteUtils.stringToArray((String) arrHeader.get(h), ",").size();
                            ArrayList arrUpHeaderData = PaletteUtils.stringToArray((String) arrHeader.get(h), ",");

                            for (int k = 0, l = upHeaderCnt; k < l; k++) {
                                String _HeaderStr = (String) arrUpHeaderData.get(k);
                                if (i == Integer.parseInt(_HeaderStr.split("\\^")[0])) {
                                    headerCell.setCellValue(_HeaderStr.split("\\^")[2]);

                                    if (_HeaderStr.split("\\^")[0].equals(_HeaderStr.split("\\^")[1])) {
                                        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, (rowNum - 1) + (headerCnt - 1), Integer
                                            .parseInt(_HeaderStr.split("\\^")[0]), Integer.parseInt(_HeaderStr.split("\\^")[1])));
                                    } else {
                                        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, Integer.parseInt(_HeaderStr
                                            .split("\\^")[0]), Integer.parseInt(_HeaderStr.split("\\^")[1])));
                                    }
                                }
                            }
                        }
                    } else {
                        for (int i = 0, j = columnCnt; i < j; i++) {
                            SXSSFCell thc = tableHeader.createCell(i);
                            thc.setCellStyle(TABLE_HEADER_STYLE);

                            String _HeaderStr = (String) arrHeaderData.get(i);

                            thc.setCellValue(_HeaderStr.split("\\^")[1]);
                        }
                    }
                }
            } else {
                SXSSFRow tableHeader = sheet.createRow(rowNum++);
                for (int i = 0, j = columnCnt; i < j; i++) {
                    SXSSFCell thc = tableHeader.createCell(i);
                    thc.setCellStyle(TABLE_HEADER_STYLE);

                    String _HeaderStr = (String) arrHeaderData.get(i);

                    thc.setCellValue(_HeaderStr.split("\\^")[1]);
                }
            }

            sheet.createFreezePane(0, 3); //3번째 row까지 틀 고정

            TelewebJSON _progress = new TelewebJSON();
            //그리드 내용 지정.
            for (int k = 0, l = list.getSize(); k < l; k++) {

                SXSSFRow row = sheet.createRow(rowNum++);

                JSONObject _map = (JSONObject) list.getDataObject().get(k);

                for (int i = 0, j = columnCnt; i < j; i++) {

                    SXSSFCell tbc = row.createCell(i);

                    String _HeaderStr = (String) arrHeaderData.get(i);

                    String sContent = "RNUM".equals(_HeaderStr.split("\\^")[0].toUpperCase()) ? "" + (k + 1) : _map.getString(_HeaderStr
                        .split("\\^")[0].toUpperCase());
                    sContent = PaletteUtils.convertHtmlCharacter(sContent).replaceAll("<br>", "\n");
                    sContent = PaletteUtils.removeTag(sContent);

                    if ("CUSL_TP_CL".equals(_HeaderStr.split("\\^")[0]) && sContent.indexOf("_##_") > -1)
                        sContent = sContent.split("_##_")[1];

                    tbc.setCellValue(sContent);

                    if (_HeaderStr.split("\\^").length > 2) {
                        if (_HeaderStr.split("\\^")[2].toUpperCase().equals("LEFT")) {
                            tbc.setCellStyle(BODY_STYLE_LEFT);
                        } else {
                            tbc.setCellStyle(BODY_STYLE_CENTER);
                        }
                    } else {
                        tbc.setCellStyle(BODY_STYLE_CENTER);
                    }
                }
            }

            this.autoSizeColumn(sheet, columnCnt); /* 컬럼 넓이 자동지정 */
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                workbook.write(out);
            } catch (IOException ioe) {
                throw ioe;
            } catch (Exception e) {
                throw e;
            } finally {
                if (out != null)
                    out.close();
            }

            return out.toByteArray();

        } catch (NullPointerException npe) {
            throw npe;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void initStyle(SXSSFWorkbook wb) {

        TITLE_STYLE = wb.createCellStyle();    //제목 스타일
        Font tf = wb.createFont();
        tf.setFontHeightInPoints((short) 24);
        tf.setBold(true);
        TITLE_STYLE.setFont(tf);
        TITLE_STYLE.setAlignment(HorizontalAlignment.CENTER);

        SUB_TITLE_STYLE = wb.createCellStyle();    //서브 제목 스타일
        Font stf = wb.createFont();
        stf.setFontHeightInPoints((short) 9);
        stf.setBold(true);
        SUB_TITLE_STYLE.setFont(stf);

        TABLE_HEADER_STYLE = wb.createCellStyle();   //부제목 스타일
        Font hf = wb.createFont();
        hf.setFontHeightInPoints((short) 9);
        hf.setBold(true);
        TABLE_HEADER_STYLE.setFont(hf);
        TABLE_HEADER_STYLE.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        TABLE_HEADER_STYLE.setAlignment(HorizontalAlignment.CENTER);
        TABLE_HEADER_STYLE.setVerticalAlignment(VerticalAlignment.CENTER);
        TABLE_HEADER_STYLE.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        TABLE_HEADER_STYLE.setBorderBottom(BorderStyle.THIN);
        TABLE_HEADER_STYLE.setBorderLeft(BorderStyle.THIN);
        TABLE_HEADER_STYLE.setBorderRight(BorderStyle.THIN);
        TABLE_HEADER_STYLE.setBorderTop(BorderStyle.THIN);

        BODY_STYLE_LEFT = wb.createCellStyle();   //내용 스타일     
        Font lbf = wb.createFont();
        lbf.setFontHeightInPoints((short) 9);
        lbf.setBold(false);
        BODY_STYLE_LEFT.setWrapText(true);
        BODY_STYLE_LEFT.setFont(lbf);
        BODY_STYLE_LEFT.setAlignment(HorizontalAlignment.LEFT);
        BODY_STYLE_LEFT.setVerticalAlignment(VerticalAlignment.CENTER);
        BODY_STYLE_LEFT.setBorderBottom(BorderStyle.THIN);
        BODY_STYLE_LEFT.setBorderLeft(BorderStyle.THIN);
        BODY_STYLE_LEFT.setBorderRight(BorderStyle.THIN);
        BODY_STYLE_LEFT.setBorderTop(BorderStyle.THIN);

        BODY_STYLE_RIGHT = wb.createCellStyle();   //내용 스타일     
        Font rbf = wb.createFont();
        rbf.setFontHeightInPoints((short) 9);
        rbf.setBold(false);
        BODY_STYLE_RIGHT.setWrapText(true);
        BODY_STYLE_RIGHT.setFont(rbf);
        BODY_STYLE_RIGHT.setAlignment(HorizontalAlignment.RIGHT);
        BODY_STYLE_RIGHT.setVerticalAlignment(VerticalAlignment.CENTER);
        BODY_STYLE_RIGHT.setBorderBottom(BorderStyle.THIN);
        BODY_STYLE_RIGHT.setBorderLeft(BorderStyle.THIN);
        BODY_STYLE_RIGHT.setBorderRight(BorderStyle.THIN);
        BODY_STYLE_RIGHT.setBorderTop(BorderStyle.THIN);

        BODY_STYLE_CENTER = wb.createCellStyle();   //내용 스타일     
        Font cbf = wb.createFont();
        cbf.setFontHeightInPoints((short) 9);
        cbf.setBold(false);
        BODY_STYLE_CENTER.setWrapText(true);
        BODY_STYLE_CENTER.setFont(cbf);
        BODY_STYLE_CENTER.setAlignment(HorizontalAlignment.CENTER);
        BODY_STYLE_CENTER.setVerticalAlignment(VerticalAlignment.CENTER);
        BODY_STYLE_CENTER.setBorderBottom(BorderStyle.THIN);
        BODY_STYLE_CENTER.setBorderLeft(BorderStyle.THIN);
        BODY_STYLE_CENTER.setBorderRight(BorderStyle.THIN);
        BODY_STYLE_CENTER.setBorderTop(BorderStyle.THIN);

        INFO_STYLE = wb.createCellStyle();   //내용 스타일     
        Font inf = wb.createFont();
        inf.setFontHeightInPoints((short) 10);
        inf.setBold(true);
        INFO_STYLE.setWrapText(true);
        INFO_STYLE.setFont(inf);
        INFO_STYLE.setBorderBottom(BorderStyle.NONE);
        INFO_STYLE.setBorderLeft(BorderStyle.NONE);
        INFO_STYLE.setBorderRight(BorderStyle.NONE);
        INFO_STYLE.setBorderTop(BorderStyle.NONE);

    }

    public void initStyle2(SXSSFWorkbook wb) {

        TITLE_STYLE2 = wb.createCellStyle();    //제목 스타일
        Font tf = wb.createFont();
        tf.setFontHeightInPoints((short) 24);
        tf.setBold(true);
        TITLE_STYLE2.setFont(tf);
        TITLE_STYLE2.setAlignment(HorizontalAlignment.CENTER);

        SUB_TITLE_STYLE2 = wb.createCellStyle();    //서브 제목 스타일
        Font stf = wb.createFont();
        stf.setFontHeightInPoints((short) 9);
        stf.setBold(true);
        SUB_TITLE_STYLE2.setFont(stf);

        TABLE_HEADER_STYLE2 = wb.createCellStyle();   //부제목 스타일
        Font hf = wb.createFont();
        hf.setFontHeightInPoints((short) 9);
        hf.setBold(true);
        TABLE_HEADER_STYLE2.setFont(hf);
        TABLE_HEADER_STYLE2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        TABLE_HEADER_STYLE2.setAlignment(HorizontalAlignment.CENTER);
        TABLE_HEADER_STYLE2.setVerticalAlignment(VerticalAlignment.CENTER);
        TABLE_HEADER_STYLE2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        TABLE_HEADER_STYLE2.setBorderBottom(BorderStyle.THIN);
        TABLE_HEADER_STYLE2.setBorderLeft(BorderStyle.THIN);
        TABLE_HEADER_STYLE2.setBorderRight(BorderStyle.THIN);
        TABLE_HEADER_STYLE2.setBorderTop(BorderStyle.THIN);

        BODY_STYLE_LEFT2 = wb.createCellStyle();   //내용 스타일     
        Font lbf = wb.createFont();
        lbf.setFontHeightInPoints((short) 9);
        lbf.setBold(false);
        BODY_STYLE_LEFT2.setWrapText(true);
        BODY_STYLE_LEFT2.setFont(lbf);
        BODY_STYLE_LEFT2.setAlignment(HorizontalAlignment.LEFT);
        BODY_STYLE_LEFT2.setVerticalAlignment(VerticalAlignment.CENTER);
        BODY_STYLE_LEFT2.setBorderBottom(BorderStyle.THIN);
        BODY_STYLE_LEFT2.setBorderLeft(BorderStyle.THIN);
        BODY_STYLE_LEFT2.setBorderRight(BorderStyle.THIN);
        BODY_STYLE_LEFT2.setBorderTop(BorderStyle.THIN);

        BODY_STYLE_CENTER2 = wb.createCellStyle();   //내용 스타일     
        Font cbf = wb.createFont();
        cbf.setFontHeightInPoints((short) 9);
        cbf.setBold(false);
        BODY_STYLE_CENTER2.setWrapText(true);
        BODY_STYLE_CENTER2.setFont(cbf);
        BODY_STYLE_CENTER2.setAlignment(HorizontalAlignment.CENTER);
        BODY_STYLE_CENTER2.setVerticalAlignment(VerticalAlignment.CENTER);
        BODY_STYLE_CENTER2.setBorderBottom(BorderStyle.THIN);
        BODY_STYLE_CENTER2.setBorderLeft(BorderStyle.THIN);
        BODY_STYLE_CENTER2.setBorderRight(BorderStyle.THIN);
        BODY_STYLE_CENTER2.setBorderTop(BorderStyle.THIN);

        INFO_STYLE2 = wb.createCellStyle();   //내용 스타일     
        Font inf = wb.createFont();
        inf.setFontHeightInPoints((short) 10);
        inf.setBold(true);
        INFO_STYLE2.setWrapText(true);
        INFO_STYLE2.setFont(inf);
        INFO_STYLE2.setBorderBottom(BorderStyle.NONE);
        INFO_STYLE2.setBorderLeft(BorderStyle.NONE);
        INFO_STYLE2.setBorderRight(BorderStyle.NONE);
        INFO_STYLE2.setBorderTop(BorderStyle.NONE);

    }

    public void initStyleXss(XSSFWorkbook wb) {

        System.setProperty("java.awt.headless", "true");

        XSS_TITLE_STYLE = wb.createCellStyle();    //제목 스타일
        Font tf = wb.createFont();
        tf.setFontHeightInPoints((short) 24);
        tf.setBold(true);
        XSS_TITLE_STYLE.setFont(tf);
        XSS_TITLE_STYLE.setAlignment(HorizontalAlignment.CENTER);

        XSS_SUB_TITLE_STYLE = wb.createCellStyle();    //서브 제목 스타일
        Font stf = wb.createFont();
        stf.setFontHeightInPoints((short) 9);
        stf.setBold(true);
        XSS_SUB_TITLE_STYLE.setFont(stf);

        XSS_TABLE_HEADER_STYLE = wb.createCellStyle();   //부제목 스타일
        Font hf = wb.createFont();
        hf.setFontHeightInPoints((short) 9);
        hf.setBold(true);
        XSS_TABLE_HEADER_STYLE.setFont(hf);
        XSS_TABLE_HEADER_STYLE.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        XSS_TABLE_HEADER_STYLE.setAlignment(HorizontalAlignment.CENTER);
        XSS_TABLE_HEADER_STYLE.setVerticalAlignment(VerticalAlignment.CENTER);
        XSS_TABLE_HEADER_STYLE.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSS_TABLE_HEADER_STYLE.setBorderBottom(BorderStyle.THIN);
        XSS_TABLE_HEADER_STYLE.setBorderLeft(BorderStyle.THIN);
        XSS_TABLE_HEADER_STYLE.setBorderRight(BorderStyle.THIN);
        XSS_TABLE_HEADER_STYLE.setBorderTop(BorderStyle.THIN);

        XSS_BODY_STYLE_LEFT = wb.createCellStyle();   //내용 스타일     
        Font lbf = wb.createFont();
        lbf.setFontHeightInPoints((short) 9);
        lbf.setBold(false);
        XSS_BODY_STYLE_LEFT.setWrapText(true);
        XSS_BODY_STYLE_LEFT.setFont(lbf);
        XSS_BODY_STYLE_LEFT.setAlignment(HorizontalAlignment.LEFT);
        XSS_BODY_STYLE_LEFT.setVerticalAlignment(VerticalAlignment.CENTER);
        XSS_BODY_STYLE_LEFT.setBorderBottom(BorderStyle.THIN);
        XSS_BODY_STYLE_LEFT.setBorderLeft(BorderStyle.THIN);
        XSS_BODY_STYLE_LEFT.setBorderRight(BorderStyle.THIN);
        XSS_BODY_STYLE_LEFT.setBorderTop(BorderStyle.THIN);

        XSS_BODY_STYLE_RIGHT = wb.createCellStyle();   //내용 스타일     
        Font rbf = wb.createFont();
        rbf.setFontHeightInPoints((short) 9);
        rbf.setBold(false);
        XSS_BODY_STYLE_RIGHT.setWrapText(true);
        XSS_BODY_STYLE_RIGHT.setFont(rbf);
        XSS_BODY_STYLE_RIGHT.setAlignment(HorizontalAlignment.RIGHT);
        XSS_BODY_STYLE_RIGHT.setVerticalAlignment(VerticalAlignment.CENTER);
        XSS_BODY_STYLE_RIGHT.setBorderBottom(BorderStyle.THIN);
        XSS_BODY_STYLE_RIGHT.setBorderLeft(BorderStyle.THIN);
        XSS_BODY_STYLE_RIGHT.setBorderRight(BorderStyle.THIN);
        XSS_BODY_STYLE_RIGHT.setBorderTop(BorderStyle.THIN);

        XSS_BODY_STYLE_CENTER = wb.createCellStyle();   //내용 스타일     
        Font cbf = wb.createFont();
        cbf.setFontHeightInPoints((short) 9);
        cbf.setBold(false);
        XSS_BODY_STYLE_CENTER.setWrapText(true);
        XSS_BODY_STYLE_CENTER.setFont(cbf);
        XSS_BODY_STYLE_CENTER.setAlignment(HorizontalAlignment.CENTER);
        XSS_BODY_STYLE_CENTER.setVerticalAlignment(VerticalAlignment.CENTER);
        XSS_BODY_STYLE_CENTER.setBorderBottom(BorderStyle.THIN);
        XSS_BODY_STYLE_CENTER.setBorderLeft(BorderStyle.THIN);
        XSS_BODY_STYLE_CENTER.setBorderRight(BorderStyle.THIN);
        XSS_BODY_STYLE_CENTER.setBorderTop(BorderStyle.THIN);

        XSS_INFO_STYLE = wb.createCellStyle();   //내용 스타일     
        Font inf = wb.createFont();
        inf.setFontHeightInPoints((short) 10);
        inf.setBold(true);
        XSS_INFO_STYLE.setWrapText(true);
        XSS_INFO_STYLE.setFont(inf);
        XSS_INFO_STYLE.setBorderBottom(BorderStyle.NONE);
        XSS_INFO_STYLE.setBorderLeft(BorderStyle.NONE);
        XSS_INFO_STYLE.setBorderRight(BorderStyle.NONE);
        XSS_INFO_STYLE.setBorderTop(BorderStyle.NONE);
    }

    private void autoSizeColumn(SXSSFSheet sheet, int columnCnt) {
        sheet.trackAllColumnsForAutoSizing();

        for (int i = 0, j = columnCnt; i < j; i++) {
            sheet.autoSizeColumn(i);
            //추가로 넓이를 넓힌다.
            int currentWidth = sheet.getColumnWidth(i);
            if (currentWidth < 10000) {
                currentWidth = (int) (currentWidth * 1.3);
            } else {
                currentWidth = 10000;
            }
            sheet.setColumnWidth(i, currentWidth);
        }
    }

    public String doGetFileName(String fileName) throws UnsupportedEncodingException {
        String result = fileName + "_" + DateFormatUtils.format((new Date()), "yyyyMMddHHmmss") + ".xlsx";

        return URLEncoder.encode(result, "UTF-8");
    }
}
