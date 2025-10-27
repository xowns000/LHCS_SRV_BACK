package kr.co.hkcloud.palette3.servlet.dhtmlx.web;


import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.co.hkcloud.palette3.common.twb.app.TwbBasicServiceImpl;
import kr.co.hkcloud.palette3.common.twb.app.TwbBizServiceImpl;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.security.util.PaletteSecurityUtils;
import kr.co.hkcloud.palette3.core.util.PaletteUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;


@Slf4j
@RequiredArgsConstructor
@WebServlet(urlPatterns = {"/servlet/ExcelExportServlet"})
public class ExcelExportServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = -1661784219515992017L;
    private final TwbBasicServiceImpl telewebBasicService;
    private final TwbBizServiceImpl telewebBizService;
    private final PaletteUtils paletteUtil;
    private final PaletteSecurityUtils paletteSecurityUtils;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }


    /**
     * POST방식의 Servlet 호출을 받는다.
     *
     * @param req : request
     * @param resp : response
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
        String telIdentifier = DateFormatUtils.format((new Date()), "HH:mm:ss") + "-" + UUID.randomUUID().toString().substring(0, 8);

        //CLIENT로부터 넘겨받은 파라메터객체
        //        String excelInfo = req.getParameter("excelInfo");
        String excelInfo = req.getParameter("PARAMS");
        TelewebJSON jsonReq = new TelewebJSON(excelInfo);

        // 개인정보 영향 평가 결과에 따라 처리.
        //개인정보영향평가 : 업무처리 가능(VDI IP)여부 체크. 상담내역이 포함일 경우 VDI망 체크
        boolean bVDI = paletteSecurityUtils.checkVDI(req);
        if (!bVDI) {
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter out = resp.getWriter();
            out.println("<script>");
            out.println("alert('[엑셀출력 실패] 접근제한지역에서만 해당내용을 처리할 수 있습니다.');");
            out.println("</script>");
            return;
        }
        jsonReq.setHeader("TELEWEB_IDENTIFIER", telIdentifier);

        TelewebJSON rtnJson = new TelewebJSON(jsonReq);
        TelewebJSON colInfo = new TelewebJSON(jsonReq);

        //변환 대상 그리드 정보 파라메터 데이터셋에서 헤더정보와 컬럼정보 문자열 데이터를 추출하여 JSONArray화 시킨다.
        colInfo.setDataObject(jsonReq.getDataObject("COLINFO"));
        String strColName = colInfo.getString("COL_NAME");
        String strColModel = colInfo.getString("COL_MODEL");
        JSONArray objJsonArryColName = JSONArray.fromObject(strColName);
        JSONArray objJsonArryColModel = JSONArray.fromObject(strColModel);
        JSONObject objJsonColName = new JSONObject();
        JSONObject objJsonColModel = new JSONObject();

        int intHeadRowCnt = objJsonArryColName.size();                                                  //HEADER 행 갯수
        int intColLen = 0;                                                                              //그리드 정의 컬럼갯수
        int intVisibleColLen = 0;                                                                       //실제 표기되는 컬럼갯수
        int intPageRowCnt = colInfo.getInt("DATA", "ROW_CNT", 0, 9999999);    //1페이지당 표출할 행 갯수

        //엑셀 변환할 데이터 조회 서비스 호출 파라메터 정의
        String serviceType = jsonReq.getHeaderString("TYPE");                                                                        //호출 타입
        String dataGroup = jsonReq.getHeaderString("EXCELGROUP");                                                                //결과 데이터셋 명
        jsonReq = new TelewebJSON(
            jsonReq.toString().substring(0, jsonReq.toString().lastIndexOf("COLINFO") - 2) + "}");    //변환할 그리드 헤더정보를 제외한 조회서비스호출 파라메터정보
        jsonReq.setHeader("TELEWEB_IDENTIFIER", telIdentifier);

        String fileName = paletteUtil.getTimeStampMilis() + "_" + jsonReq.getHeaderString("FILENAME") + ".xlsx";            //변환할 파일명
        String excelTitle = jsonReq.getHeaderString("EXCELTITLE");                                                                //엑셀타이틀

        //엑셀 변환할 데이터 조회 서비스 호출
        try {
            rtnJson = callService(req, resp, jsonReq, dataGroup, serviceType);

            //데이터 건수
            int dataLen = 0;
            if (!("").equals(dataGroup) && !("DATA").equals(dataGroup)) {
                dataLen = rtnJson.getHeaderInt(dataGroup + "_COUNT");
            } else {
                dataLen = rtnJson.getHeaderInt("COUNT");
            }

            //데이터 건수가 5,000건 이상일 시 메모리 증폭 및 점유 문제로 클라이언트 pc 멈춤현상을 고려해 엑셀 다운로드 불가.
            if (dataLen > 5000) {
                resp.setContentType("text/html;charset=utf-8");
                PrintWriter out = resp.getWriter();
                out.println("<script>");
                out.println("alert('[엑셀출력 실패] 5,000건 이상은 엑셀 다운로드가 불가합니다.');");
                out.println("</script>");
            } else {
                //데이터 조회 정상처리여부 확인
                if (rtnJson.getHeaderBoolean("ERROR_FLAG") == true) {
                    //오류발생 시 해당 오류메시지 표출
                    resp.setContentType("text/html;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.println("<script>");
                    out.println("alert('[엑셀출력 실패] " + rtnJson.getHeaderString("ERROR_MSG") + "');");
                    out.println("</script>");
                } else {
                    //엑셀로 변환한다.
                    Row rowh = null;                //타겟 행 객체
                    Cell cell = null;                //타겟 셀 객체

                    SXSSFWorkbook wb = new SXSSFWorkbook();        //워크북을 만들고
                    SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("sheet");    //시트이름은 한글도 가능하다.
                    Map<?, ?> cellStyles = createCellStyles(wb);        //셀스타일 정의

                    sheet.setAutobreaks(true);

                    int rowN = intHeadRowCnt;
                    String columnName = "";

                    // 제목 헤더를 만든다.
                    rowh = sheet.createRow(rowN);

                    int HcellCursor = 1;
                    String strWidth = "";
                    boolean blnExcel = true;
                    ArrayList<Integer> exceptIdxList = new ArrayList<Integer>();

                    cell = rowh.createCell(0);
                    cell.setCellValue("순번");
                    cell.setCellStyle((CellStyle) cellStyles.get("header"));
                    cell.setCellType(CellType.STRING);
                    sheet.setColumnWidth(0, 1500);

                    //헤더부
                    for (int i = intHeadRowCnt - 1; i >= 0; i--) {
                        rowh = sheet.createRow(rowN);
                        cell = rowh.createCell(0);
                        cell.setCellValue("순번");
                        cell.setCellStyle((CellStyle) cellStyles.get("header"));
                        cell.setCellType(CellType.STRING);
                        sheet.setColumnWidth(0, 1500);

                        HcellCursor = 1;

                        objJsonColName = objJsonArryColName.getJSONObject(i);

                        if (!objJsonColName.isEmpty()) {
                            String[] colName = objJsonColName.getString("HEAD").split(",");
                            intColLen = colName.length;
                            intVisibleColLen = colName.length;
                            for (int j = 0; j < colName.length; j++) {
                                objJsonColModel = objJsonArryColModel.getJSONObject(j);
                                if (!objJsonColModel.isEmpty()) {
                                    blnExcel = Boolean.parseBoolean(objJsonColModel.getString("excel"));
                                    if (blnExcel == true) {
                                        cell = rowh.createCell(HcellCursor);
                                        cell.setCellValue(colName[j]);
                                        //헤더부분 셀 스타일 설정한다.
                                        cell.setCellStyle((CellStyle) cellStyles.get("header"));
                                        cell.setCellType(CellType.STRING);
                                        strWidth = objJsonColModel.getString("width");
                                        if (strWidth != null && !"".equals(strWidth)) {
                                            if (strWidth.indexOf(".") >= 0) {
                                                sheet.setColumnWidth(HcellCursor,
                                                    Integer.parseInt(strWidth.substring(0, strWidth.indexOf("."))) * 45);
                                            } else {
                                                sheet.setColumnWidth(HcellCursor, Integer.parseInt(strWidth) * 45);
                                            }
                                        } else {
                                            sheet.setColumnWidth(HcellCursor, 100 * 45);
                                        }
                                        HcellCursor++;
                                    } else {
                                        if (i == 0) {
                                            intVisibleColLen--;
                                            exceptIdxList.add(j);
                                        }
                                    }
                                }
                            }
                            rowN--;
                        }
                    }

                    //헤더부 ROW가 1ROW 이상일 시 merge작업.
                    if (intHeadRowCnt > 1) {
                        Row _rowh = sheet.getRow(1);
                        Row _rowhSub = null;
                        Cell _cell = null;
                        Cell _cellSub = null;
                        int intColSpan = 0;
                        int intRowSpan = 0;
                        String strVal = "";
                        String strValSub = "";
                        String strNextVal = "";
                        String strNextValSub = "";
                        int intRowMergeStrtNum = -1;

                        //colspan 병합
                        for (int y = 1; y <= intHeadRowCnt; y++) {
                            //행
                            _rowh = sheet.getRow(y);

                            for (int z = 0; z <= intColLen - exceptIdxList.size(); z++) {
                                _cell = _rowh.getCell(z);
                                strVal = _cell.getStringCellValue();
                                strValSub = _cell.getStringCellValue();
                                intColSpan = 0;
                                intRowSpan = 0;

                                if (!isMergedCell(sheet, y, z)) {
                                    for (int zz = (z + 1); zz <= intColLen - exceptIdxList.size(); zz++) {
                                        _cell = _rowh.getCell(zz);
                                        strNextVal = _cell.getStringCellValue();
                                        if (strVal.equals(strNextVal)) {
                                            intColSpan++;
                                            strVal = strNextVal;
                                        } else {
                                            break;
                                        }
                                    }

                                    for (int zzz = y + 1; zzz <= intHeadRowCnt; zzz++) {
                                        _rowhSub = sheet.getRow(zzz);
                                        _cellSub = _rowhSub.getCell(z);
                                        strNextValSub = _cellSub.getStringCellValue();
                                        if (strValSub.equals(strNextValSub)) {
                                            intRowSpan++;
                                            strValSub = strNextValSub;
                                        } else {
                                            break;
                                        }
                                    }
                                }
                                if (intColSpan > 0) {
                                    sheet.addMergedRegion(new CellRangeAddress(y, (y + intRowSpan), z, (z + intColSpan)));
                                }
                            }
                        }

                        //rowspan 병합
                        for (int x = 0; x <= intColLen - exceptIdxList.size(); x++) {
                            _rowh = sheet.getRow(1);
                            _cell = _rowh.getCell(x);
                            strVal = _cell.getStringCellValue();
                            intRowSpan = 0;
                            intRowMergeStrtNum = 0;
                            for (int n = 2; n <= intHeadRowCnt; n++) {
                                _rowh = sheet.getRow(n);
                                _cell = _rowh.getCell(x);
                                strNextVal = _cell.getStringCellValue();
                                if (strVal.equals(strNextVal)) {
                                    if (intRowMergeStrtNum == 0) {
                                        intRowMergeStrtNum = n - 1;
                                    }
                                    intRowSpan++;
                                }
                                strVal = strNextVal;
                            }
                            if (intRowSpan > 0) {
                                if (intRowMergeStrtNum == 0) {
                                    intRowMergeStrtNum = 1;
                                }
                                if (!isMergedCell(sheet, intRowMergeStrtNum, x)) {
                                    sheet.addMergedRegion(
                                        new CellRangeAddress(intRowMergeStrtNum, (intRowMergeStrtNum + intRowSpan), x, x));
                                }
                            }
                        }
                    }

                    //엑셀 타이틀 존재할 시 1번째 줄에 표기.
                    if (!"".equals(excelTitle)) {
                        rowh = sheet.createRow(0);
                        cell = rowh.createCell(0);
                        cell.setCellValue(excelTitle);
                        cell.setCellStyle((CellStyle) cellStyles.get("subtitle"));
                        cell.setCellType(CellType.STRING);
                        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, intVisibleColLen));
                    }

                    rowN = intHeadRowCnt + 1;

                    //데이터부
                    if (!"[{}]".equals(rtnJson.getDataObject(dataGroup).toString())) {
                        String strValue = "";
                        String strFormat = "";
                        String strAlign = "";
                        int BcellCursor = 0;
                        int intPageCnt = rtnJson.getHeaderInt("PAGES_CNT");
                        if (!"DATA".equals(dataGroup)) {
                            intPageCnt = rtnJson.getHeaderInt(dataGroup + "_PAGES_CNT");
                        }
                        if (intPageCnt == 0) {
                            intPageCnt = 1;
                        }

                        for (int k = 0; k < rtnJson.getSize(dataGroup); k++) {

                            rowh = sheet.createRow(rowN);
                            BcellCursor = 0;

                            cell = rowh.createCell(BcellCursor);

                            cell.setCellValue(((intPageCnt - 1) * intPageRowCnt) + (k + 1));
                            cell.setCellStyle((CellStyle) cellStyles.get("rownumbers"));
                            cell.setCellType(CellType.NUMERIC);
                            BcellCursor++;

                            for (int x = 0; x < intColLen; x++) {
                                strFormat = "";
                                strAlign = "center";
                                cell = rowh.createCell(BcellCursor);

                                objJsonColModel = objJsonArryColModel.getJSONObject(x);
                                if (!objJsonColModel.isEmpty()) {
                                    blnExcel = Boolean.parseBoolean(objJsonColModel.getString("excel"));

                                    if (blnExcel == true) {
                                        columnName = objJsonColModel.getString("name");

                                        //rtnJson에 KEY가 없을 경우에 다운로드 실패가 일어나, KEY여부 로직 추가 2018.10.11
                                        if (rtnJson.containsKey(columnName)) {
                                            strValue = paletteUtil.chkXSSConstraintsDec(rtnJson.getString(dataGroup, columnName,
                                                k));//.replaceAll("&lt;", "<").replaceAll("&gt;", ">");;
                                        } else {
                                            strValue = "";
                                        }
                                        //strValue = rtnJson.getString(dataGroup, columnName, k);
                                        strFormat = objJsonColModel.getString("format");
                                        strAlign = objJsonColModel.getString("align");

                                        if (!"".equals(strFormat)) {
                                            strValue = getFormatData(strFormat, strValue);
                                        }
                                        cell.setCellValue(strValue);

                                        //바디(데이터)부분 셀 스타일을 설정한다.
                                        if ("left".equals(strAlign)) {
                                            cell.setCellStyle((CellStyle) cellStyles.get("body_left"));
                                        } else if ("right".equals(strAlign)) {
                                            cell.setCellStyle((CellStyle) cellStyles.get("body_right"));
                                        } else {
                                            cell.setCellStyle((CellStyle) cellStyles.get("body_center"));
                                        }

                                        cell.setCellType(CellType.STRING);
                                        BcellCursor++;
                                    }
                                }

                            }
                            sheet.flushRows(100);    //메모리 flush
                            rowN++;
                        }
                    }

                    sheet.setDisplayGridlines(true);// 기본적인 설정들이다.
                    sheet.setPrintGridlines(true);
                    sheet.setFitToPage(true);
                    sheet.setHorizontallyCenter(true);
                    sheet.setAutobreaks(true);

                    resp.setContentType("application/octet-stream");
                    resp.setHeader("Content-Disposition",
                        "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()) + ";");
                    OutputStream xlsOut = resp.getOutputStream(); //OutputStream으로 엑셀을 저장한다.
                    wb.write(xlsOut);

                    xlsOut.close();
                }
            }
        } catch (Exception e) {
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter out = resp.getWriter();
            out.println("<script>");
            out.println("alert('[엑셀출력 실패] " + e.getMessage() + "');");
            out.println("</script>");
        }
    }


    /**
     * 셀스타일을 정의한다.
     */
    private static Map<String, CellStyle> createCellStyles(SXSSFWorkbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

        CellStyle style1 = wb.createCellStyle();
        style1.setVerticalAlignment(VerticalAlignment.CENTER);
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style1.setBorderRight(BorderStyle.THIN);
        style1.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style1.setBorderTop(BorderStyle.THIN);
        style1.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style1.setAlignment(HorizontalAlignment.LEFT);
        styles.put("body_left", style1);

        CellStyle style2 = wb.createCellStyle();
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style2.setBorderRight(BorderStyle.THIN);
        style2.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style2.setBorderTop(BorderStyle.THIN);
        style2.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style2.setAlignment(HorizontalAlignment.CENTER);
        styles.put("body_center", style2);

        CellStyle style3 = wb.createCellStyle();
        style3.setVerticalAlignment(VerticalAlignment.CENTER);
        style3.setBorderBottom(BorderStyle.THIN);
        style3.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style3.setBorderLeft(BorderStyle.THIN);
        style3.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style3.setBorderRight(BorderStyle.THIN);
        style3.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style3.setBorderTop(BorderStyle.THIN);
        style3.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style3.setAlignment(HorizontalAlignment.RIGHT);
        styles.put("body_right", style3);

        CellStyle style4 = wb.createCellStyle();
        Font style4Font = wb.createFont();
        //        style4Font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style4Font.setBold(true);
        style4Font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        style4.setAlignment(HorizontalAlignment.CENTER);
        style4.setVerticalAlignment(VerticalAlignment.CENTER);
        style4.setBorderBottom(BorderStyle.THIN);
        style4.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style4.setBorderLeft(BorderStyle.THIN);
        style4.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style4.setBorderRight(BorderStyle.THIN);
        style4.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style4.setBorderTop(BorderStyle.THIN);
        style4.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style4.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        style4.setFont(style4Font);
        style4.setWrapText(true);
        styles.put("header", style4);

        CellStyle style5 = wb.createCellStyle();
        style5.setVerticalAlignment(VerticalAlignment.CENTER);
        style5.setBorderBottom(BorderStyle.THIN);
        style5.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style5.setBorderLeft(BorderStyle.THIN);
        style5.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style5.setBorderRight(BorderStyle.THIN);
        style5.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style5.setBorderTop(BorderStyle.THIN);
        style5.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style5.setAlignment(HorizontalAlignment.CENTER);
        styles.put("rownumbers", style5);

        CellStyle style6 = wb.createCellStyle();
        Font style6Font = wb.createFont();
        //        style6Font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style6Font.setBold(true);
        style6Font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        style6.setVerticalAlignment(VerticalAlignment.CENTER);
        style6.setBorderBottom(BorderStyle.THIN);
        style6.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style6.setBorderLeft(BorderStyle.THIN);
        style6.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style6.setBorderRight(BorderStyle.THIN);
        style6.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style6.setBorderTop(BorderStyle.THIN);
        style6.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style6.setAlignment(HorizontalAlignment.RIGHT);
        style6.setFont(style6Font);
        style6.setWrapText(false);
        styles.put("subtitle", style6);

        return styles;
    }


    /**
     * 서비스를 호출한다.
     */
    protected TelewebJSON callService(HttpServletRequest req, HttpServletResponse resp, TelewebJSON jsonReq, String dataGroup,
        String serviceType) throws Exception {

        TelewebJSON rtnJSON = new TelewebJSON();

        if ("XML_SERVICE".equals(serviceType)) {
            rtnJSON = telewebBasicService.excuteCom(jsonReq, req);
        } else if ("BIZ_SERVICE".equals(serviceType)) {
            rtnJSON = telewebBizService.callService(req, resp, req.getSession(), jsonReq);
        }

        //크로스사이트 스크립팅 공격에 대비해 치환된 특수기호 코드를 재변환.
        cnvtRestoreXSS(rtnJSON, dataGroup);

        return rtnJSON;
    }


    /**
     * 데이터의 포맷팅을 위한 메소드
     */
    public String getFormatData(String gubun, String value) {
        String strReturn = "";
        String workStr = "";

        if (gubun.indexOf("class_biz") >= 0) {                //사업자 번호
            workStr = exceptMask(value);
            if (workStr.length() != 10) {
                strReturn = value;
            } else {
                strReturn = workStr.substring(0, 3) + "-" + workStr.substring(3, 5) + "-" + workStr.substring(5);
            }
        } else if (gubun.indexOf("class_post") >= 0) {        //우편번호
            workStr = exceptMask(value);
            if (workStr.length() != 6) {
                strReturn = value;
            } else {
                strReturn = workStr.substring(0, 3) + "-" + workStr.substring(3);
            }
        } else if (gubun.indexOf("class_rrno") >= 0) {        //주민번호
            workStr = exceptMask(value);
            if (workStr.length() != 13) {
                strReturn = value;
            } else {
                strReturn = workStr.substring(0, 6) + "-" + workStr.substring(6);
            }
        } else if (gubun.indexOf("class_tlnoor") >= 0) {    //전화번호
            int nCnt = 0;
            workStr = exceptMask(value);
            if (workStr.length() == 4) {
                strReturn = value;
            } else if (workStr.length() < 7 || workStr.length() > 11) {
                strReturn = value;
            } else {
                String sTel3 = workStr.substring(workStr.length() - 4, workStr.length());
                String sTemp = workStr.substring(0, workStr.length() - 4);

                if (!sTemp.substring(0, 1).equals("0")) {
                    if (workStr.length() < 9) {
                        return sTemp + "-" + sTel3;
                    }
                } else {
                    if (sTemp.substring(0, 2).equals("02")) {
                        nCnt = 2;
                    } else {
                        if (sTemp.substring(0, 3).equals("011") || sTemp.substring(0, 3).equals("013") || sTemp.substring(0, 3)
                            .equals("016") || sTemp.substring(0, 3).equals("017") || sTemp.substring(0, 3).equals("018") || sTemp.substring(
                            0, 3).equals("019") || sTemp.substring(0, 3).equals("010") || sTemp.substring(0, 3).equals("070")
                            || sTemp.substring(0, 3).equals("080") || sTemp.substring(0, 3).equals("031") || sTemp.substring(0, 3)
                            .equals("032") || sTemp.substring(0, 3).equals("033") || sTemp.substring(0, 3).equals("041") || sTemp.substring(
                            0, 3).equals("042") || sTemp.substring(0, 3).equals("043") || sTemp.substring(0, 3).equals("051")
                            || sTemp.substring(0, 3).equals("052") || sTemp.substring(0, 3).equals("053") || sTemp.substring(0, 3)
                            .equals("054") || sTemp.substring(0, 3).equals("055") || sTemp.substring(0, 3).equals("061") || sTemp.substring(
                            0, 3).equals("062") || sTemp.substring(0, 3).equals("063") || sTemp.substring(0, 3).equals("064")
                            || sTemp.substring(0, 3).equals("050")) {
                            nCnt = 3;
                        }
                    }
                    strReturn = sTemp.substring(0, nCnt) + "-" + sTemp.substring(nCnt, sTemp.length()) + "-" + sTel3;
                }
            }
        } else if (gubun.indexOf("class_number") >= 0) {    //숫자
            String minus = "";
            String strInt = "";
            workStr = value;

            if (value.equals("")) {
                strReturn = "";
            } else {
                if (workStr.substring(0, 1).equals("-")) {
                    minus = "-";
                }

                if (workStr.lastIndexOf(".") == -1) {

                    if ("0".equals(workStr.substring(0, 1))) {
                        while (workStr.length() > 0 && "0".equals(workStr.substring(0, 1))) {
                            workStr = workStr.substring(1, workStr.length());
                        }
                        if ("".equals(workStr)) {
                            workStr = "0";
                        }
                    }

                    if (minus.equals("-")) {
                        strInt = workStr.substring(1);
                    } else {
                        strInt = workStr;
                    }

                    int index = strInt.length();
                    String sRet = "";
                    for (int i = 0; i < index; i += 3) {
                        if (index > i + 3) {
                            sRet = "," + strInt.substring(index - i - 3, index - i) + sRet;
                        } else {
                            sRet = strInt.substring(0, index - i) + sRet;
                        }
                    }
                    strReturn = minus + sRet;
                } else {
                    if (minus.equals("-")) {
                        strInt = workStr.substring(1, workStr.lastIndexOf("."));
                    } else {
                        strInt = workStr.substring(0, workStr.lastIndexOf("."));
                    }

                    int index = strInt.length();
                    String sRet = "";
                    for (int i = 0; i < index; i += 3) {
                        if (index > i + 3) {
                            sRet = "," + strInt.substring(index - i - 3, index - i) + sRet;
                        } else {
                            sRet = strInt.substring(0, index - i) + sRet;
                        }
                    }
                    strReturn = minus + sRet + "." + workStr.substring(workStr.lastIndexOf(".") + 1, workStr.length());
                }
            }
        } else if (gubun.indexOf("class_date") >= 0 || gubun.indexOf("class_month") >= 0) {        //날짜
            workStr = exceptMask(value);

            if (gubun.indexOf("time") >= 0) {
                workStr = exceptMask(value);
                if (workStr.length() != 12 && workStr.length() != 14) {
                    strReturn = workStr;
                } else {
                    if (workStr.length() == 12) {
                        strReturn = workStr.substring(0, 4) + "/" + workStr.substring(4, 6) + "/" + workStr.substring(6, 8) + " "
                            + workStr.substring(8, 10) + ":" + workStr.substring(10);
                    } else {
                        strReturn = workStr.substring(0, 4) + "/" + workStr.substring(4, 6) + "/" + workStr.substring(6, 8) + " "
                            + workStr.substring(8, 10) + ":" + workStr.substring(10, 12) + ":" + workStr.substring(12);
                    }
                }
            } else {
                if (workStr.equals("")) {
                    strReturn = value;
                } else {
                    if (workStr.length() != 6 && workStr.length() != 8) {
                        strReturn = workStr;
                    } else {
                        if (workStr.length() == 6) {
                            strReturn = workStr.substring(0, 4) + "/" + workStr.substring(4);
                        } else {
                            strReturn = workStr.substring(0, 4) + "/" + workStr.substring(4, 6) + "/" + workStr.substring(6);
                        }
                    }
                }
            }

        } else if (gubun.indexOf("class_time") >= 0) {        //시간
            workStr = exceptMask(value);
            if (workStr.length() != 4 && workStr.length() != 6) {
                strReturn = workStr;
            } else {
                if (workStr.length() == 4) {
                    strReturn = workStr.substring(0, 2) + ":" + workStr.substring(2);
                } else {
                    strReturn = workStr.substring(0, 2) + ":" + workStr.substring(2, 4) + ":" + workStr.substring(4);
                }
            }
        } else if (gubun.indexOf("class_birth") >= 0) {        //생년월일
            workStr = exceptMask(value);

            if (workStr.equals("")) {
                strReturn = value;
            } else {
                if (workStr.length() != 8) {
                    strReturn = value;
                } else {
                    strReturn = workStr.substring(0, 4) + "/**/" + workStr.substring(6, 8);
                }
            }
        } else if (gubun.indexOf("class_rrn") >= 0) {        //생년월일
            workStr = exceptMask(value);

            if (workStr.equals("")) {
                strReturn = value;
            } else {
                if (workStr.length() != 6 && workStr.length() != 8) {
                    strReturn = workStr;
                } else {
                    if (workStr.length() == 6) {
                        strReturn = workStr.substring(0, 2) + "/**/" + workStr.substring(4);
                    } else {
                        strReturn = workStr.substring(0, 4) + "/**/" + workStr.substring(6);
                    }
                }
            }
        } else if (gubun.indexOf("class_card") >= 0) {        //카드
            workStr = exceptMask(value);
            if (workStr.length() != 16) {
                strReturn = workStr;
            } else {
                strReturn =
                    workStr.substring(0, 4) + "-" + workStr.substring(4, 8) + "-" + workStr.substring(8, 12) + "-" + workStr.substring(12);
            }
        } else if (gubun.indexOf("class_cardEnc") >= 0) {    //카드마스킹
            workStr = exceptMask(value);
            if (!"".equals(workStr)) {
                if (workStr.length() != 16) {
                    strReturn = workStr.substring(0, 4);
                    for (int i = 0; i < workStr.length() - 4; i++) {
                        strReturn += "*";
                    }
                } else {
                    strReturn = workStr.substring(0, 4) + "-****-****-" + workStr.substring(12);
                }
            }
        } else if (gubun.indexOf("class_acct") >= 0) {        //계좌번호
            workStr = exceptMask(value);
            if (workStr.length() > 8) {
                strReturn = workStr.substring(0, workStr.length() - 4) + "****";
            } else {
                strReturn = workStr;
            }
        } else if (gubun.indexOf("class_eml") >= 0) {        //이메일
            workStr = value;
            int eIdx = workStr.indexOf("@");
            int intLen = 0;
            String strMask = "";
            if (eIdx != -1) {
                intLen = workStr.substring(0, eIdx).length();
                if (intLen > 4) {
                    for (int i = 3; i < eIdx; i++) {
                        strMask += "*";
                    }
                    strReturn = workStr.substring(0, 3) + strMask + workStr.substring(eIdx);
                } else if (intLen == 4) {
                    strReturn = workStr.substring(0, 2) + "**" + workStr.substring(eIdx);
                } else if (intLen == 3) {
                    strReturn = workStr.substring(0, 1) + "**" + workStr.substring(eIdx);
                } else {
                    strReturn = "**" + workStr.substring(eIdx);
                }
            } else {
                strReturn = workStr;
            }
        } else if (gubun.indexOf("class_nameEnc") >= 0) {    //이름
            workStr = value;
            strReturn = workStr;
            if (workStr.length() > 2) {
                String tvar1 = workStr.substring(0, 1);
                String tvar2 = workStr.substring(workStr.length() - 1);
                String tmp = "";
                for (int i = 1; i < workStr.length() - 1; i++) {
                    tmp += "*";
                }
                strReturn = tvar1 + tmp + tvar2;
            } else if (workStr.length() == 2) {
                strReturn = workStr.substring(0, 1) + "*";
            }
        } else if (gubun.indexOf("class_addr") >= 0) {        //주소
            workStr = value;
            strReturn = workStr;
            if (workStr.length() > 0) {
                int sIdx = -1;
                int sIdx2 = -1;
                int eIdx = -1;
                int eIdx2 = -1;
                String strMask = "";
                String strMask2 = "";
                if (workStr.indexOf("읍 ") >= 0) {
                    eIdx = workStr.indexOf("읍 ");
                } else if (workStr.indexOf("면 ") >= 0) {
                    eIdx = workStr.indexOf("면 ");
                } else if (workStr.indexOf("동 ") >= 0) {
                    eIdx = workStr.indexOf("동 ");
                } else if (workStr.indexOf("로 ") >= 0) {
                    eIdx = workStr.indexOf("로 ");
                    if (workStr.indexOf("동) ") >= 0) {
                        eIdx2 = workStr.indexOf("동) ");
                    } else if (workStr.indexOf("동&#41; ") >= 0) {
                        eIdx2 = workStr.indexOf("동&#41; ");
                    } else if (workStr.indexOf("읍) ") >= 0) {
                        eIdx2 = workStr.indexOf("읍) ");
                    } else if (workStr.indexOf("읍&#41; ") >= 0) {
                        eIdx2 = workStr.indexOf("읍&#41; ");
                    } else if (workStr.indexOf("면) ") >= 0) {
                        eIdx2 = workStr.indexOf("면) ");
                    } else if (workStr.indexOf("면&#41; ") >= 0) {
                        eIdx2 = workStr.indexOf("면&#41; ");
                    }
                } else if (workStr.indexOf("길 ") >= 0) {
                    eIdx = workStr.indexOf("길 ");
                    if (workStr.indexOf("동) ") >= 0) {
                        eIdx2 = workStr.indexOf("동) ");
                    } else if (workStr.indexOf("동&#41; ") >= 0) {
                        eIdx2 = workStr.indexOf("동&#41; ");
                    } else if (workStr.indexOf("읍) ") >= 0) {
                        eIdx2 = workStr.indexOf("읍) ");
                    } else if (workStr.indexOf("읍&#41; ") >= 0) {
                        eIdx2 = workStr.indexOf("읍&#41; ");
                    } else if (workStr.indexOf("면) ") >= 0) {
                        eIdx2 = workStr.indexOf("면) ");
                    } else if (workStr.indexOf("면&#41; ") >= 0) {
                        eIdx2 = workStr.indexOf("면&#41; ");
                    }

                }
                for (int i = eIdx; i >= 0; i--) {
                    if (" ".equals(workStr.substring(i - 1, i))) {
                        sIdx = i;
                        break;
                    } else if (i - 1 < 0) {
                        sIdx = i;
                        break;
                    }
                    strMask += "*";
                }
                for (int j = eIdx2; j >= 0; j--) {
                    if ("(".equals(workStr.substring(j - 1, j))) {
                        sIdx2 = j;
                        break;
                    } else if ("&#40;".equals(workStr.substring(j - 5, j))) {
                        sIdx2 = j;
                        break;
                    } else if (j - 1 < 0) {
                        sIdx2 = j;
                        break;
                    }
                    strMask2 += "*";
                }
                if (sIdx >= 0 && eIdx >= 0) {
                    if (sIdx2 >= 0 && eIdx2 >= 0) {
                        strReturn =
                            workStr.substring(0, sIdx) + strMask + workStr.substring(eIdx, sIdx2) + strMask2 + workStr.substring(eIdx2);
                    } else {
                        strReturn = workStr.substring(0, sIdx) + strMask + workStr.substring(eIdx);
                    }
                }
            }
        } else if (gubun.indexOf("class_tlno") >= 0) {        //전화번호
            int nCnt = 0;
            workStr = exceptMask(value);
            if (workStr.length() == 4) {
                strReturn = value;
            } else if (workStr.length() < 7 || workStr.length() > 11) {
                strReturn = value;
            } else {
                String sTel3 = workStr.substring(workStr.length() - 4, workStr.length());
                String sTemp = workStr.substring(0, workStr.length() - 4);

                if (!sTemp.substring(0, 1).equals("0")) {
                    if (workStr.length() < 9) {
                        return sTemp + "-" + sTel3;
                    }
                } else {
                    if (sTemp.substring(0, 2).equals("02")) {
                        nCnt = 2;
                    } else {
                        if (sTemp.substring(0, 3).equals("011") || sTemp.substring(0, 3).equals("013") || sTemp.substring(0, 3)
                            .equals("016") || sTemp.substring(0, 3).equals("017") || sTemp.substring(0, 3).equals("018") || sTemp.substring(
                            0, 3).equals("019") || sTemp.substring(0, 3).equals("010") || sTemp.substring(0, 3).equals("070")
                            || sTemp.substring(0, 3).equals("080") || sTemp.substring(0, 3).equals("031") || sTemp.substring(0, 3)
                            .equals("032") || sTemp.substring(0, 3).equals("033") || sTemp.substring(0, 3).equals("041") || sTemp.substring(
                            0, 3).equals("042") || sTemp.substring(0, 3).equals("043") || sTemp.substring(0, 3).equals("051")
                            || sTemp.substring(0, 3).equals("052") || sTemp.substring(0, 3).equals("053") || sTemp.substring(0, 3)
                            .equals("054") || sTemp.substring(0, 3).equals("055") || sTemp.substring(0, 3).equals("061") || sTemp.substring(
                            0, 3).equals("062") || sTemp.substring(0, 3).equals("063") || sTemp.substring(0, 3).equals("064")
                            || sTemp.substring(0, 3).equals("050")) {
                            nCnt = 3;
                        }
                    }
                    strReturn = sTemp.substring(0, nCnt) + "-****-" + sTel3;
                }
            }
        } else if (gubun.indexOf("class_tmtime") >= 0) {
            strReturn = value;
            workStr = exceptMask(value);
            if (!"".equals(workStr)) {
                int intHour;
                int intMinute;
                int intSecond;
                int workVal = Integer.parseInt(workStr);
                intHour = workVal / 3600;
                intMinute = (workVal % 3600) / 60;
                intSecond = workVal % 60;
                strReturn = paletteUtil.characterPad(String.valueOf(intHour), 2, "0", true) + ":" + paletteUtil.characterPad(
                    String.valueOf(intMinute), 2, "0", true) + ":" + paletteUtil.characterPad(String.valueOf(intSecond), 2, "0", true);
            }
        }

        return strReturn;
    }


    /**
     * 숫자만 가지고온다.
     */
    public String exceptMask(String data) {
        String lsReturn = "", lsTemp = data.trim();
        char lcChar;

        for (int i = 0; i < lsTemp.length(); i++) {
            lcChar = lsTemp.charAt(i);
            if (Character.isDigit(lcChar)) {
                lsReturn += lcChar;
            }
        }
        return lsReturn;
    }


    /**
     * 파라메터의 데이터그룹에 있는 모든 데이터 중 <, > 문자가 있을 경우 특수문자로 변환한다. 모든 데이터를 대상으로 해당 문자열 치환을 적용할 경우 업무관점에서 문제가 발생할 수 있으므로 DATA 그룹 파라메터만 대상으로 처리한다.
     *
     * @param objJsonParams 파라메터
     */
    public void cnvtRestoreXSS(TelewebJSON objJsonParams, String strDataGroupName) throws Exception {
        try {
            JSONArray jsonArrayData = objJsonParams.getDataObject(strDataGroupName);
            for (int i = 0; i < jsonArrayData.size(); i++) {
                JSONObject objJson = jsonArrayData.getJSONObject(i);
                Iterator<String> objIterator = objJson.keys();
                while (objIterator.hasNext()) {
                    String strKeyName = objIterator.next();
                    String strValue = objJson.getString(strKeyName);
                    objJsonParams.setString(strKeyName, i, paletteUtil.chkXSSConstraints2(strValue), strDataGroupName);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 셀병합 여부 판단
     */
    private boolean isMergedCell(org.apache.poi.ss.usermodel.Sheet sheet, int rowIdx, int colIdx) {
        for (int i = 0; i < sheet.getNumMergedRegions(); ++i) {
            org.apache.poi.ss.util.CellRangeAddress range = sheet.getMergedRegion(i);
            if (rowIdx >= range.getFirstRow() && rowIdx <= range.getLastRow() && colIdx >= range.getFirstColumn()
                && colIdx <= range.getLastColumn()) {
                return true;
            }
        }
        return false;
    }
}
