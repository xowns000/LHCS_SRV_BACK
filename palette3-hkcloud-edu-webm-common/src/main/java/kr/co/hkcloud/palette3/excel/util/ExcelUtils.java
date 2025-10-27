package kr.co.hkcloud.palette3.excel.util;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@Component
public class ExcelUtils
{

    // 전역변수 : sheet, keys[], xlsKind,
    // 1. 생성자에서 파일명을 받아서 전역변수 sheet을 만들어놓고
    //    keys[], xlsKind에 세팅한다.
    // 2. 메소드는 카운트 가지고 오는것과 로우의 데이터를 json으로 리턴하는것 만들기

    public HSSFWorkbook workXls   = null;
    public HSSFSheet    sheetXls  = null;
    public XSSFWorkbook workXlsx  = null;
    public XSSFSheet    sheetXlsx = null;
    public String[]     keys      = null;
    public String       xlsKind   = "";

    public SXSSFWorkbook workbook = null;
    public SXSSFCell  cell = null;


    /**
     * 엑셀파일을 로드하여 각 Object를 생성한다.
     * 
     * @param  fileName
     * @throws Exception
     */
    public void loadExcel(String strFullPath) throws Exception
    {
        String strExt = strFullPath.substring(strFullPath.lastIndexOf(".") + 1).toUpperCase();

        // 파일명을 받아서 workbook을 생성한다.
        if(strExt.equals("XLSX")) {
            this.workXlsx = new XSSFWorkbook(new FileInputStream(new File(strFullPath)));
            this.xlsKind = "xlsx";
        }
        else {
            POIFSFileSystem fileSystem = new POIFSFileSystem(new FileInputStream(new File(strFullPath)));
            this.workXls = new HSSFWorkbook(fileSystem);
            this.xlsKind = "xls";
        }
    }


    /**
     * key를 세팅한다.
     * 
     * @param  keys
     * @throws Exception
     */
    public void setKeys(String[] keys)
    {
        this.keys = keys;
    }


    /**
     * sheet를 읽는다
     * 
     * @param  sheetIdx
     * @throws Exception
     */
    public void setSheet(int sheetIdx)
    {
        if(this.xlsKind.equals("xlsx")) {
            this.sheetXlsx = this.workXlsx.getSheetAt(sheetIdx);
        }
        else {
            this.sheetXls = this.workXls.getSheetAt(sheetIdx);
        }
    }


    /**
     * 엑셀 row 갯수를 리턴한다.
     * 
     * @return
     * @throws Exception
     */
    public int getRowCount()
    {
        int rtnInt = 0;

        if(this.xlsKind.equals("xls")) {
            rtnInt = this.sheetXls.getPhysicalNumberOfRows();
        }
        else {
            rtnInt = this.sheetXlsx.getPhysicalNumberOfRows();
        }

        return rtnInt;
    }


    /**
     * 입력받은 로우의 데이터를 json으로 리턴한다.
     * 
     * @param  rowIdx
     * @return
     * @throws Exception
     */
    public TelewebJSON getRowData(int rowIdx, int cellCnt)
    {
        TelewebJSON rtnJson = new TelewebJSON();
        if(this.xlsKind.equals("xls")) {
            HSSFRow rowObj = this.sheetXls.getRow(rowIdx);

            if(rowObj != null) {
                int cells = rowObj.getPhysicalNumberOfCells();
                if(cellCnt > 0) {
                    cells = cellCnt;
                }

                for(int j = 0; j < cells; j++) {
                    HSSFCell cellObj = rowObj.getCell(j);
                    if(cellObj != null) {
                        switch(cellObj.getCellType())
                        {
                            case NUMERIC:
                                //cellObj.setCellType(XSSFCell.CELL_TYPE_STRING);
                                //rtnJson.setString(this.keys[j], 0, cellObj.getStringCellValue().toString());
                                rtnJson.setString(this.keys[j], 0, Long.toString((long) cellObj.getNumericCellValue()));
                                break;
                            case STRING:
                                rtnJson.setString(this.keys[j], 0, cellObj.getStringCellValue().toString());
                                break;
                            default:
                                rtnJson.setString(this.keys[j], 0, cellObj.getStringCellValue().toString());
                                break;
                        }
                    }
                    else
                        rtnJson.setString(this.keys[j], 0, "");
                }
            }
        }
        else {
            XSSFRow rowObj = this.sheetXlsx.getRow(rowIdx);
            if(rowObj != null) {
                int cells = rowObj.getPhysicalNumberOfCells();
                if(cellCnt > 0) {
                    cells = cellCnt;
                }
                for(int j = 0; j < cells; j++) {
                    XSSFCell cellObj = rowObj.getCell(j);

                    if(cellObj != null) {
                        switch(cellObj.getCellType())
                        {
                            case NUMERIC:
                                //cellObj.setCellType(XSSFCell.CELL_TYPE_STRING);
                                //rtnJson.setString(this.keys[j], 0, cellObj.getStringCellValue().toString());
                                rtnJson.setString(this.keys[j], 0, Long.toString((long) cellObj.getNumericCellValue()));
                                break;
                            case STRING:
                                rtnJson.setString(this.keys[j], 0, cellObj.getStringCellValue().toString());
                                break;
                            default:
                                rtnJson.setString(this.keys[j], 0, cellObj.getStringCellValue().toString());
                                break;
                        }
                    }
                    else
                        rtnJson.setString(this.keys[j], 0, "");
                }
            }
        }
        return rtnJson;
    }


    /**
     * TelewebJSON 포맷 데이터를 엑셀로 변환하여 파일스트림객체에 Write 한다.
     * 
     * @param  objExcelArrayList 엑셀로 치환할 멀티데이터
     * @param  objFileOutStream  파일스트림객체
     * @return                   파일스트림객체에 엑셀파일을 Write해서 반환한다.
     * @throws Exception
     * @author                   MPC R&D Team
     */
    public void cnvtJsonToExcel(ArrayList<HashMap<String, TelewebJSON>> objExcelArrayList, OutputStream objFileOutStream) throws Exception
    {
        XSSFWorkbook objWorkBook = new XSSFWorkbook();                                  //엑셀워크북생성
        Iterator<HashMap<String, TelewebJSON>> objIterator = objExcelArrayList.iterator();
        int intNo = 1;
        int intHeaderStartRow = 1;
        int intBodyStartRow = 2;
        while(objIterator.hasNext()) {
            HashMap<String, TelewebJSON> objHashData = objIterator.next();
            //시트내용을 디자인할 해더파라메터와 데이터를 추출한다.
            TelewebJSON jsonHeader = objHashData.get("HEADER_PRAMS");
            TelewebJSON jsonData = objHashData.get("BODY_DATA");

            //시트객체 생성
            XSSFSheet objSheet = objWorkBook.createSheet("Sheet" + intNo);                  //워크북에 시트생성

            //해더생성
            createHeader(objWorkBook, objSheet, jsonHeader, intHeaderStartRow);

            //데이터바디생성
            createBodyData(objWorkBook, objSheet, jsonHeader, jsonData, intBodyStartRow);

            //시트관련 기본적 설정
            objSheet.setDisplayGridlines(true);
            objSheet.setPrintGridlines(true);
            objSheet.setFitToPage(true);
            objSheet.setHorizontallyCenter(true);
            objSheet.setAutobreaks(true);

            intNo++;
        }

        //엑셀파일을 스트림정보에 Write 한다.
        objWorkBook.write(objFileOutStream);
    }

    public static String filePath = "D:\\PALETTE3\\hkcloud\\project\\deploy\\production\\repository\\web\\file";
    public static String fileNm = "poi_making_file_test.xlsx";
    /**
     * TelewebJSON 포맷 데이터를 엑셀로 변환하여 파일스트림객체에 Write 한다.
     *
     * @param  mJsonParams       엑셀로 치환할 멀티데이터
     * @param  objFileOutStream  파일스트림객체
     * @return                   파일스트림객체에 엑셀파일을 Write해서 반환한다.
     * @throws Exception
     * @author                   njy
     */
    public void cnvtJsonToExcelSXSSF(TelewebJSON mJsonParams, OutputStream objFileOutStream) throws Exception {

        log.debug("start make excel======================" + mJsonParams.getDataObject("DATA"));
        SXSSFWorkbook objWorkBook = new SXSSFWorkbook();                                            //엑셀워크북생성

        JSONArray ArrData = new JSONArray();

        ArrData = mJsonParams.getDataObject("DATA");                        // TelewebJSON에서 DATA만 추출해서 배열생성
        log.debug("start make excel======================" + mJsonParams.getDataObject("DATA"));
        log.debug("objData======================" + ArrData);
        log.debug("objData======================" + ArrData.getClass().getName());
        log.debug("sobjData=====================" + mJsonParams.getDataObject("DATA"));

        ArrayList<String> headerKeyList = new ArrayList<>();

        JSONObject objData = new JSONObject();
        objData = ArrData.getJSONObject(0);
        Iterator iterator = objData.keys();
        while(iterator.hasNext()){
            String header = iterator.next().toString();
            headerKeyList.add(header);
        }
//        for(int k = 0; k < ArrData.size(); k++){
//            for(int j = 0; j < headerKeyList.size(); j++){
//                String[] valueItem = new String[headerKeyList.size()];
//                valueItem[k] = ArrData.getJSONObject(k).getString(headerKeyList.get(j));
//                bodyValueList.add(valueItem[k]);
//        log.debug("valueItem[+"+k+"] ===========\n"+  valueItem[k]);
//            }
//        log.debug("bodyValueList===========\n"+ bodyValueList);
//        }
        log.debug("headerKeyList==========="+ headerKeyList);

        SXSSFSheet objSheet = objWorkBook.createSheet("Sheet");                  //워크북에 시트생성

        SXSSFRow row= objSheet.createRow(0);

        for(int i=0; i<headerKeyList.size(); i++) {		//헤더 구성
            cell = row.createCell(i);
            log.debug("cell"+headerKeyList.get(i));
            cell.setCellValue(headerKeyList.get(i));
//            cell.setCellStyle(headerStyle);
        }

        for(int i=0; i<ArrData.size(); i++) {	//데이터 구성
            row = objSheet.createRow(i + 1);
            int cellIdx = 0;
            for(int j = 0; j < headerKeyList.size(); j++) {
                cell = row.createCell(j);
                log.debug("ArrData.getJSONObject(i).getString(headerKeyList.get(j))"+ArrData.getJSONObject(i).getString(headerKeyList.get(j)));
                cell.setCellValue(ArrData.getJSONObject(i).getString(headerKeyList.get(j)));
            }
////            cell.setCellStyle(dataStyle);
        }
//
//        for (int i=0; i<headerKeyList.size(); i++) {
//            objSheet.autoSizeColumn(i);
////            objSheet.setColumnWidth(i, objSheet.getColumnWidth(i));
//        }
//
//        for(int i=0; i < columnTitle.length; i++) {
//            String header = columnTitle[i].toString();
//            cell=row.createCell(i);
//            cell.setCellValue( header );
//            cell.setCellStyle(styleTitle);
//        }

//        HashMap<String, TelewebJSON> HEADER_PRAMS = new HashMap<String, TelewebJSON>();
//        HEADER_PRAMS.put("DATA",mJsonParams);
//
//
//        ArrayList<HashMap<String, TelewebJSON>> objExcelArrayList = new ArrayList<HashMap<String, TelewebJSON>>();
//        objExcelArrayList.add(HEADER_PRAMS);
//
//        Iterator<HashMap<String, TelewebJSON>> objIterator = objExcelArrayList.iterator();
//
//        int intNo = 1;
//        int intHeaderStartRow = 1;
//        int intBodyStartRow = 2;
//        while(objIterator.hasNext()) {
//            HashMap<String, TelewebJSON> objHashData = objIterator.next();
//            //시트내용을 디자인할 해더파라메터와 데이터를 추출한다.
//            TelewebJSON jsonHeader = objHashData.get("HEADER_PRAMS");
//            TelewebJSON jsonData = objHashData.get("BODY_DATA");
//
//            //시트객체 생성
//            XSSFSheet objSheet = objWorkBook.createSheet("Sheet" + intNo);                  //워크북에 시트생성
//
//            //해더생성
//            createHeader(objWorkBook, objSheet, jsonHeader, intHeaderStartRow);
//
//            //데이터바디생성
//            createBodyData(objWorkBook, objSheet, jsonHeader, jsonData, intBodyStartRow);
//
//            //시트관련 기본적 설정
//            objSheet.setDisplayGridlines(true);
//            objSheet.setPrintGridlines(true);
//            objSheet.setFitToPage(true);
//            objSheet.setHorizontallyCenter(true);
//            objSheet.setAutobreaks(true);
//
//            intNo++;
//            }
//        }

/*
        for (int i = 0; i < objData.size(); i++) {
            Row row = objSheet.createRow(i);
            JSONObject rowData = objData.getJSONObject(i);
            for (int j = 0; j < rowData.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(rowData.get(j));
//            }
        }*/

        //엑셀파일을 스트림정보에 Write 한다.
//        objWorkBook.write(objFileOutStream);

        try (FileOutputStream out = new FileOutputStream(new File(filePath, fileNm))) {
//            workbook.write(out);
            objWorkBook.write(out);
            objWorkBook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 엑셀 해더를 디자인 한다.
     * 
     * @param  objWorkBook       엑셀워크북객체
     * @param  objSheet          엑셀시트객체
     * @param  jsonHeader        해더를 디자인하기 위한 정보
     * @param  intHeaderStartRow 해더가 그려지는 로우번호
     * @return                   없음
     * @throws Exception
     * @author                   MPC R&D Team
     */
    private void createHeader(XSSFWorkbook objWorkBook, XSSFSheet objSheet, TelewebJSON jsonHeader, int intHeaderStartRow)
    {
        Font objBoldFont = objWorkBook.createFont();                                //폰트별 스타일 객체 생성(두꺼운폰트)
//        objBoldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        objBoldFont.setBold(true);

        //Header 스타일 셀 정의
        CellStyle objHeaderStyle = objWorkBook.createCellStyle();                   //Cell별 스타일 객체 생성(Header 스타일 셀)
        setCellBoxStyle(objHeaderStyle);
//        objHeaderStyle.setFillForegroundColor(Color.ColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex());   //셀안에 밝은 블루톤 색
        objHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);                  //셀안에 색을 백그라운드로 색칠
        objHeaderStyle.setFont(objBoldFont);                                            //셀안에 글자는 굵은 색

        //해더정보 설정
        JSONArray objColModel = jsonHeader.getDataObject("COL_MODEL");
        JSONArray objColNames = jsonHeader.getDataObject("COL_NAMES");
        JSONArray objCnvtColNames = cnvtColNames(objColNames);                          //모델리스트와 인덱스 숫자를 맞춘다

        //해더 첫번째 셀은 번호(NO)로 고정한다.
        int intCellPoint = 0;
        XSSFRow objHeaderRow = objSheet.createRow(intHeaderStartRow);
        XSSFCell objHaderCell = objHeaderRow.createCell(intCellPoint);
        objHaderCell.setCellStyle(objHeaderStyle);
        objHaderCell.setCellType(CellType.STRING);
        objHaderCell.setCellValue("NO");
        objSheet.setColumnWidth(intCellPoint, 3000);

        intCellPoint++;

        //해더정보를 이용하여 컬럼수만큼 엑셀 해더를 디자인 한다.
        for(int i = 0; i < objColModel.size(); i++) {
            boolean blnViewExcel = checkExcelColumn(jsonHeader, i);

            //엑셀에 컬럼을 표시해야 할 경우 디자인을 한다.
            if(blnViewExcel) {
                //한글명과 병합한 개수를 추출한다.
                String strKorName = objCnvtColNames.getJSONObject(i).getString("name");
                int intColspan = 1;
                if(objCnvtColNames.getJSONObject(i).containsKey("colspan")) {
                    intColspan = Integer.parseInt(objCnvtColNames.getJSONObject(i).getString("colspan"));
                }

                //모델정보를 이용하여 셀의 넓이와 한글명을 디자인 한다.
                objHaderCell = objHeaderRow.createCell(intCellPoint);
                objHaderCell.setCellValue(strKorName);
                objHaderCell.setCellStyle(objHeaderStyle);
                objHaderCell.setCellType(CellType.STRING);
                if(jsonHeader.getString("COL_MODEL", "width", i).equals("")) {
                    objSheet.setColumnWidth((i + 1), 100 * 50);
                }
                else {
                    objSheet.setColumnWidth((i + 1), jsonHeader.getInt("COL_MODEL", "width", i) * 50);
                }

                //컬럼 병합이 있을 경우 해당 컬럼 수만 큼 해더를 병합한다.
                if(intColspan > 1) {
                    objHaderCell = objHeaderRow.createCell(intCellPoint);
                    objHaderCell.setCellValue(strKorName);
                    objHaderCell.setCellStyle(objHeaderStyle);
                    objHaderCell.setCellType(CellType.STRING);
                    if(jsonHeader.getString("COL_MODEL", "width", i).equals("")) {
                        objSheet.setColumnWidth((i + 1), 100 * 50);
                    }
                    else {
                        objSheet.setColumnWidth((i + 1), jsonHeader.getInt("COL_MODEL", "width", i) * 50);
                    }
                    objSheet.addMergedRegion(new CellRangeAddress(intHeaderStartRow, (short) (intCellPoint), intHeaderStartRow, (short) (intCellPoint + intColspan - 1)));
                }
                intCellPoint++;
            }
        }
    }

    /**
     * 엑셀 해더를 디자인 한다.
     *
     * @param  objWorkBook       엑셀워크북객체
     * @param  objSheet          엑셀시트객체
     * @param  jsonHeader        해더를 디자인하기 위한 정보
     * @param  intHeaderStartRow 해더가 그려지는 로우번호
     * @return                   없음
     * @throws Exception
     * @author                   MPC R&D Team
     */
    private void createHeaderXssf(XSSFWorkbook objWorkBook, XSSFSheet objSheet, TelewebJSON jsonHeader, int intHeaderStartRow)
    {
        Font objBoldFont = objWorkBook.createFont();                                //폰트별 스타일 객체 생성(두꺼운폰트)
//        objBoldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        objBoldFont.setBold(true);

        //Header 스타일 셀 정의
        CellStyle objHeaderStyle = objWorkBook.createCellStyle();                   //Cell별 스타일 객체 생성(Header 스타일 셀)
        setCellBoxStyle(objHeaderStyle);
//        objHeaderStyle.setFillForegroundColor(Color.ColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex());   //셀안에 밝은 블루톤 색
        objHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);                  //셀안에 색을 백그라운드로 색칠
        objHeaderStyle.setFont(objBoldFont);                                            //셀안에 글자는 굵은 색

        //해더정보 설정
        JSONArray objColModel = jsonHeader.getDataObject("COL_MODEL");
        JSONArray objColNames = jsonHeader.getDataObject("COL_NAMES");
        JSONArray objCnvtColNames = cnvtColNames(objColNames);                          //모델리스트와 인덱스 숫자를 맞춘다

        //해더 첫번째 셀은 번호(NO)로 고정한다.
        int intCellPoint = 0;
        XSSFRow objHeaderRow = objSheet.createRow(intHeaderStartRow);
        XSSFCell objHaderCell = objHeaderRow.createCell(intCellPoint);
        objHaderCell.setCellStyle(objHeaderStyle);
        objHaderCell.setCellType(CellType.STRING);
        objHaderCell.setCellValue("NO");
        objSheet.setColumnWidth(intCellPoint, 3000);

        intCellPoint++;

        //해더정보를 이용하여 컬럼수만큼 엑셀 해더를 디자인 한다.
        for(int i = 0; i < objColModel.size(); i++) {
            boolean blnViewExcel = checkExcelColumn(jsonHeader, i);

            //엑셀에 컬럼을 표시해야 할 경우 디자인을 한다.
            if(blnViewExcel) {
                //한글명과 병합한 개수를 추출한다.
                String strKorName = objCnvtColNames.getJSONObject(i).getString("name");
                int intColspan = 1;
                if(objCnvtColNames.getJSONObject(i).containsKey("colspan")) {
                    intColspan = Integer.parseInt(objCnvtColNames.getJSONObject(i).getString("colspan"));
                }

                //모델정보를 이용하여 셀의 넓이와 한글명을 디자인 한다.
                objHaderCell = objHeaderRow.createCell(intCellPoint);
                objHaderCell.setCellValue(strKorName);
                objHaderCell.setCellStyle(objHeaderStyle);
                objHaderCell.setCellType(CellType.STRING);
                if(jsonHeader.getString("COL_MODEL", "width", i).equals("")) {
                    objSheet.setColumnWidth((i + 1), 100 * 50);
                }
                else {
                    objSheet.setColumnWidth((i + 1), jsonHeader.getInt("COL_MODEL", "width", i) * 50);
                }

                //컬럼 병합이 있을 경우 해당 컬럼 수만 큼 해더를 병합한다.
                if(intColspan > 1) {
                    objHaderCell = objHeaderRow.createCell(intCellPoint);
                    objHaderCell.setCellValue(strKorName);
                    objHaderCell.setCellStyle(objHeaderStyle);
                    objHaderCell.setCellType(CellType.STRING);
                    if(jsonHeader.getString("COL_MODEL", "width", i).equals("")) {
                        objSheet.setColumnWidth((i + 1), 100 * 50);
                    }
                    else {
                        objSheet.setColumnWidth((i + 1), jsonHeader.getInt("COL_MODEL", "width", i) * 50);
                    }
                    objSheet.addMergedRegion(new CellRangeAddress(intHeaderStartRow, (short) (intCellPoint), intHeaderStartRow, (short) (intCellPoint + intColspan - 1)));
                }
                intCellPoint++;
            }
        }
    }


    /**
     * 엑셀 바디(데이터)를 디자인 한다.
     * 
     * @param  objWorkBook     엑셀워크북객체
     * @param  objSheet        엑셀시트객체
     * @param  jsonHeader      해더를 디자인하기 위한 정보
     * @param  intBodyStartRow 바디가 디자인되는 로우번호 위치
     * @return                 없음
     * @throws Exception
     * @author                 MPC R&D Team
     */
    private void createBodyData(XSSFWorkbook objWorkBook, XSSFSheet objSheet, TelewebJSON jsonHeader, TelewebJSON jsonData, int intBodyStartRow)
    {
        Font objBoldFont = objWorkBook.createFont();                                //폰트별 스타일 객체 생성(두꺼운폰트)
//        objBoldFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        objBoldFont.setBold(true);

        //Header 스타일 셀 정의
        CellStyle objBodyStyle = objWorkBook.createCellStyle();                     //Cell별 스타일 객체 생성(Body 스타일 셀)
        setCellBoxStyle(objBodyStyle);
        objBodyStyle.setFont(objBoldFont);

        //해더정보 설정
        JSONArray objColModel = jsonHeader.getDataObject("COL_MODEL");

        //데이터로우수만큼 디자인 한다.
        for(int i = 0; i < jsonData.getSize(); i++) {
            //로우번호 디자인
            int intCellPoint = 0;
            XSSFRow objHeaderRow = objSheet.createRow(intBodyStartRow);
            XSSFCell objHaderCell = objHeaderRow.createCell(intCellPoint);
            objBodyStyle.setAlignment(HorizontalAlignment.RIGHT);
            objHaderCell.setCellStyle(objBodyStyle);
            objHaderCell.setCellValue(i + 1);

            intCellPoint++;

            //해더정보를 이용하여 컬럼수만큼 엑셀 해더를 디자인 한다.
            for(int j = 0; j < objColModel.size(); j++) {
                //엑셀에 컬럼을 표시할지 여부를 판단한다.
                boolean blnViewExcel = checkExcelColumn(jsonHeader, j);

                //엑셀에 컬럼을 표시해야 할 경우 디자인을 한다.
                if(blnViewExcel) {
                    //모델정보를 이용하여 셀의 정렬을 정의한다.
                    String strKeyName = jsonHeader.getString("COL_MODEL", "name", j);
                    String strAlign = jsonHeader.getString("COL_MODEL", "align", j).toLowerCase();
                    objHaderCell = objHeaderRow.createCell(intCellPoint);
                    //정렬설정
                    objBodyStyle.setAlignment(HorizontalAlignment.CENTER);
                    if(strAlign.equals("left")) {
                        objBodyStyle.setAlignment(HorizontalAlignment.LEFT);
                    }
                    else if(strAlign.equals("right")) {
                        objBodyStyle.setAlignment(HorizontalAlignment.RIGHT);
                    }
                    objHaderCell.setCellStyle(objBodyStyle);
                    objHaderCell.setCellType(CellType.STRING);
                    objHaderCell.setCellValue(jsonData.getString(strKeyName, i));

                    intCellPoint++; //컬럼증가
                }
            }
            intBodyStartRow++;  //로우증가
        }
    }


    /**
     * 엑셀박스스타일 지정
     * 
     * @param  objCellStyle 셀스타일객체
     * @return              없음
     * @throws Exception
     * @author              MPC R&D Team
     */
    private void setCellBoxStyle(CellStyle objCellStyle)
    {
        objCellStyle.setAlignment(HorizontalAlignment.CENTER);                              //가운데정렬(수평)
        objCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);                   //가운데정렬(수직)
        objCellStyle.setBorderBottom(BorderStyle.THIN);                            //셀 하단에 선
        objCellStyle.setBorderLeft(BorderStyle.THIN);                              //셀 왼쪽에 선
        objCellStyle.setBorderRight(BorderStyle.THIN);                             //셀 오른쪽에 선
        objCellStyle.setBorderTop(BorderStyle.THIN);                               //셀 상단에 선
        objCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());                  //셀 하단에 검은색 선
        objCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());                    //셀 왼쪽에 검은색 선
        objCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());                   //셀 오른쪽에 검은색 선
        objCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());                     //셀 상단에 검은색 선
    }


    /**
     * 엑셀에 컬럼을 표시할지 여부 판단 조건: 모델정보에 excel 속성이 true로 정의되어 있을 경우, excel 속성이 없는 상태에서 hidden속성이 false 또는 미정의되어 있을 경우 엑셀에 해당 컬럼을 표시한다.
     * 
     * @param  jsonHeader 모델정보
     * @param  intIndex   인덱스
     * @return            true:엑셀에표시, false:엑셀에 미표시
     * @throws Exception
     * @author            MPC R&D Team
     */
    private boolean checkExcelColumn(TelewebJSON jsonHeader, int intIndex)
    {
        boolean blnView = false;

        //엑셀에 컬럼을 표시할지 여부를 판단한다.
        String strExcelHeader = jsonHeader.getString("COL_MODEL", "excel", intIndex).toLowerCase();
        String strHidden = jsonHeader.getString("COL_MODEL", "hidden", intIndex).toLowerCase();
        if(strExcelHeader.equals("")) {
            strExcelHeader = "true";
            //엑셀표시여부 속성이 없을 경우 Hidden 속성으로 판단한다.
            if(strHidden.equals("false")) {
                strExcelHeader = "true";
            }
            else if(strHidden.equals("true")) {
                strExcelHeader = "false";
            }
        }

        if(strExcelHeader.equals("true")) {
            blnView = true;
        }

        return blnView;
    }


    /**
     * 엑셀해더 이름 리스트 정보와 모델 리스트정보의 인덱스가 colspan속성으로 인해 맞지 않기 때문에 colspan 정보를 이용하여 해더이름리스트 인덱스를 모델 리스트와 맞춘다. colspan 값이 2 이상일 경우 1번째는 해당 정보를 입력하고 그 다음 정보는 name="", colspan=-1 값으로 인덱스를 생성한다.
     * 
     * @param  objColNames 해더이름리스트
     * @return             모델리스트와 인덱스가 맞는 해더이름리스트
     * @throws Exception
     * @author             MPC R&D Team
     */
    private JSONArray cnvtColNames(JSONArray objColNames)
    {
        JSONArray objCnvtColNames = new JSONArray();
        Iterator<JSONObject> objIterator = objColNames.iterator();
        while(objIterator.hasNext()) {
            JSONObject objColItem = objIterator.next();
            int intColspan = 1;
            if(objColItem.containsKey("colspan")) {
                intColspan = objColItem.getInt("colspan");
            }
            if(intColspan == 1) {
                objCnvtColNames.add(objColItem);
            }
            else {
                //Model과 인덱스 수를 맞추기 위해 머지개수만큼 추가로 동일하게 생성하되 두번째부터는 이름을 빈문자로 치환한다.
                for(int i = 0; i < intColspan; i++) {
                    if(i == 0) {
                        objCnvtColNames.add(objColItem);
                    }
                    else {
                        objColItem.put("name", "");
                        objColItem.put("colspan", -1);
                        objCnvtColNames.add(objColItem);
                    }
                }
            }
        }
        return objCnvtColNames;
    }


    /**
     * TelewebJSON 포맷 데이터를 엑셀로 변환하여 파일스트림객체에 Write 한다.
     * 
     * @param  objExcelArrayList 엑셀로 치환할 멀티데이터
     * @param  objFileOutStream  파일스트림객체
     * @return                   파일스트림객체에 엑셀파일을 Write해서 반환한다.
     * @throws Exception
     * @author                   MPC R&D Team
     */
    public void cnvtJsonToExcelThread(ArrayList<HashMap<String, TelewebJSON>> objExcelArrayList, OutputStream objFileOutStream) throws Exception
    {
        XSSFWorkbook objWorkBook = new XSSFWorkbook();                                  //엑셀워크북생성
        Iterator<HashMap<String, TelewebJSON>> objIterator = objExcelArrayList.iterator();
        int intNo = 1;
        int intHeaderStartRow = 1;
        int intBodyStartRow = 2;
        while(objIterator.hasNext()) {
            HashMap<String, TelewebJSON> objHashData = objIterator.next();
            //시트내용을 디자인할 해더파라메터와 데이터를 추출한다.
            TelewebJSON jsonHeader = objHashData.get("HEADER_PRAMS");
            TelewebJSON jsonData = objHashData.get("BODY_DATA");

            //데이터그룹에 해더정보인 COL_MODEL, COL_NAMES가 존재할 경우 해더정보에 동적으로 재설정한다.
            if(jsonData.getDataObject("COL_MODEL") != null) {
                jsonHeader.setDataObject("COL_MODEL", jsonData.getDataObject("COL_MODEL"));
            }
            if(jsonData.getDataObject("COL_NAMES") != null) {
                jsonHeader.setDataObject("COL_NAMES", jsonData.getDataObject("COL_NAMES"));
            }

            //시트객체 생성
            XSSFSheet objSheet = objWorkBook.createSheet("Sheet" + intNo);                  //워크북에 시트생성

            //해더생성
            intBodyStartRow = createHeaderThread(objWorkBook, objSheet, jsonHeader, intHeaderStartRow);

            //데이터바디생성
            createBodyData(objWorkBook, objSheet, jsonHeader, jsonData, intBodyStartRow);

            //시트관련 기본적 설정
            objSheet.setDisplayGridlines(true);
            objSheet.setPrintGridlines(true);
            objSheet.setFitToPage(true);
            objSheet.setHorizontallyCenter(true);
            objSheet.setAutobreaks(true);

            intNo++;
        }

        //엑셀파일을 스트림정보에 Write 한다.
        objWorkBook.write(objFileOutStream);
    }


    /**
     * 엑셀 해더를 디자인 한다.
     * 
     * @param  objWorkBook       엑셀워크북객체
     * @param  objSheet          엑셀시트객체
     * @param  jsonHeader        해더를 디자인하기 위한 정보
     * @param  intHeaderStartRow 해더가 그려지는 로우번호
     * @return                   없음
     * @throws Exception
     * @author                   MPC R&D Team
     */
    private int createHeaderThread(XSSFWorkbook objWorkBook, XSSFSheet objSheet, TelewebJSON jsonHeader, int intHeaderStartRow)
    {
        Font objBoldFont = objWorkBook.createFont();                                //폰트별 스타일 객체 생성(두꺼운폰트)
//        objBoldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        objBoldFont.setBold(true);

        //Header 스타일 셀 정의
        CellStyle objHeaderStyle = objWorkBook.createCellStyle();                   //Cell별 스타일 객체 생성(Header 스타일 셀)
        setCellBoxStyle(objHeaderStyle);
//        objHeaderStyle.setFillForegroundColor(Color.ColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex());   //셀안에 밝은 블루톤 색
        objHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);                  //셀안에 색을 백그라운드로 색칠
        objHeaderStyle.setFont(objBoldFont);                                            //셀안에 글자는 굵은 색

        //해더정보 설정
        JSONArray objColModel = jsonHeader.getDataObject("COL_MODEL");
        JSONArray objColNames = jsonHeader.getDataObject("COL_NAMES");

        for(int cnt = 0; cnt < objColNames.size(); cnt++) {

            //해더 첫번째 셀은 번호(NO)로 고정한다.
            int intCellPoint = 0;
            XSSFRow objHeaderRow = objSheet.createRow(intHeaderStartRow);
            XSSFCell objHaderCell = objHeaderRow.createCell(intCellPoint);
            objHaderCell.setCellStyle(objHeaderStyle);
            objHaderCell.setCellType(CellType.STRING);
            objHaderCell.setCellValue("NO");
            objSheet.setColumnWidth(intCellPoint, 3000);

            intCellPoint++;

            //한글명을 추출한다.
            String[] strKorName = objColNames.getJSONObject(cnt).getString("HEADER_NM").split(",");
            strKorName = cnvtColNamesThread(objColModel, strKorName);                                   //모델리스트와 인덱스 숫자를 맞춘다
            //해더정보를 이용하여 컬럼수만큼 엑셀 해더를 디자인 한다.
            for(int i = 0; i < objColModel.size(); i++) {
                boolean blnViewExcel = checkExcelColumn(jsonHeader, i);

                //엑셀에 컬럼을 표시해야 할 경우 디자인을 한다.
                if(blnViewExcel) {

                    //모델정보를 이용하여 셀의 넓이와 한글명을 디자인 한다.
                    objHaderCell = objHeaderRow.createCell(intCellPoint);
                    objHaderCell.setCellValue(strKorName[i].toString());
                    objHaderCell.setCellStyle(objHeaderStyle);
                    objHaderCell.setCellType(CellType.STRING);
                    if(jsonHeader.getString("COL_MODEL", "width", i).equals("")) {
                        objSheet.setColumnWidth((i + 1), 100 * 50);
                    }
                    else {
                        objSheet.setColumnWidth((i + 1), jsonHeader.getInt("COL_MODEL", "width", i) * 50);
                    }
                    intCellPoint++;
                }
            }
            intHeaderStartRow++;  //로우증가
            objHeaderRow = objSheet.createRow(intHeaderStartRow);
        }

        return intHeaderStartRow;
    }


    /**
     * 엑셀해더 이름 리스트 정보와 모델 리스트정보의 인덱스의 갯수가 맞지 않은 경우에 빈값으로 맞춘다
     * 
     * @param  objColNames 해더이름리스트
     * @return             모델리스트와 인덱스가 맞는 해더이름리스트
     * @throws Exception
     * @author             MPC R&D Team
     */
    private String[] cnvtColNamesThread(JSONArray objColModels, String[] arrColNames)
    {
        String[] arrTemp = new String[objColModels.size()];

        for(int i = 0; i < arrColNames.length; i++) {
            arrTemp[i] = arrColNames[i];
        }

        if(arrColNames.length < objColModels.size()) {
            int intDiff = objColModels.size() - arrColNames.length;
            for(int i = 0; i < intDiff; i++) {
                arrTemp[arrColNames.length + i] = "";
            }
        }

        return arrTemp;
    }

}
