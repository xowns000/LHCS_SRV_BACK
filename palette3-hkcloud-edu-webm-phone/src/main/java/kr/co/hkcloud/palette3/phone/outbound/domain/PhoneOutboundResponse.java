package kr.co.hkcloud.palette3.phone.outbound.domain;


import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


/**
 * 전화 아웃바운드 도메인
 * 
 * @author leeiy
 *
 */
public class PhoneOutboundResponse
{
    /**
     * 아웃바운드 엑셀 업로드
     * 
     * @author leeiy
     *
     */
    @Data
    @AllArgsConstructor
    @ToString
    public static final class OuboundRegistExcelResponse
    {
        @JsonProperty("NO")
        private String NO;         //순번

        @JsonProperty("CUST_NO")
        private String CUST_NO;  //주민번호

        @JsonProperty("CUST_NM")
        private String CUST_NM;  //계약자명

        @JsonProperty("IFLW_TY")
        private String IFLW_TY;  //유입구분

        @JsonProperty("MOBIL_NO")
        private String MOBIL_NO;  //핸드폰번호

        @JsonProperty("CO_TEL_NO")
        private String CO_TEL_NO;  //직장전화번호

        @JsonProperty("HOUSE_TEL_NO")
        private String HOUSE_TEL_NO;  //자택전화번호

        @JsonProperty("SECU_NO")
        private String SECU_NO;  //증권번호

        @JsonProperty("JOB_DTL_NM")
        private String JOB_DTL_NM;  //직업상세명

        @JsonProperty("SALE_START_DATE")
        private String SALE_START_DATE;  //판매개시일자

        @JsonProperty("UPDT_DATE")
        private String UPDT_DATE;  //갱신일자

        @JsonProperty("REM")
        private String REM;  //비고


        public OuboundRegistExcelResponse(Row row) {
            this(row, new DataFormatter());
        }


        public OuboundRegistExcelResponse(Row row, DataFormatter formatter) {
            // @formatter:off
            this(formatter.formatCellValue(row.getCell(0))
               , formatter.formatCellValue(row.getCell(1))
               , formatter.formatCellValue(row.getCell(2))
               , formatter.formatCellValue(row.getCell(3))
               , formatter.formatCellValue(row.getCell(4))
               , formatter.formatCellValue(row.getCell(5))
               , formatter.formatCellValue(row.getCell(6))
               , formatter.formatCellValue(row.getCell(7))
               , formatter.formatCellValue(row.getCell(8))
               , formatter.formatCellValue(row.getCell(9))
               
               , formatter.formatCellValue(row.getCell(10))
               , formatter.formatCellValue(row.getCell(11))
            );
            // @formatter:on
        }
    }
}
