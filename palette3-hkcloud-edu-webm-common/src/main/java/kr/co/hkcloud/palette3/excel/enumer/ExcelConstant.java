package kr.co.hkcloud.palette3.excel.enumer;


/**
 * 엑셀관련상수
 * 
 * @author leeiy
 *
 */
public enum ExcelConstant
{
    FILE_NAME("fileName"),

    HEAD("head"),

    BODY("body"),

    XLS("xls"),

    XLSX("xlsx"),

    XLSX_STAREAM("xlsx-stream");


    private String name;


    private ExcelConstant(String name) {
        this.name = name;
    }


    public String getName()
    {
        return name;
    }
}
