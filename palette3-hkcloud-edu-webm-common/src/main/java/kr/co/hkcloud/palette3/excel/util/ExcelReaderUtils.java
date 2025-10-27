package kr.co.hkcloud.palette3.excel.util;


import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.co.hkcloud.palette3.excel.domain.ExcelRequest.ExcelUploadRequest;
import kr.co.hkcloud.palette3.excel.enumer.ExcelConstant;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class ExcelReaderUtils
{
    public <T> List<T> readFileToList(final ExcelUploadRequest excelUploadRequest, final Function<Row, T> rowFunc) throws IOException, InvalidFormatException
    {

        final Workbook workbook = readWorkbook(excelUploadRequest.getExcelfile());
        final Sheet sheet = workbook.getSheetAt(0);
        final int rowCount = sheet.getPhysicalNumberOfRows();
        log.info("EXCEL rowCount::::{}", rowCount);
//        log.info("EXCEL sheet.getRow::::{}", sheet.getRow(0));
//        log.info("EXCEL sheet.getRow::::{}", sheet.getRow(1));
//        log.info("EXCEL sheet.getRow::::{}", sheet.getRow(2));
//        log.info("EXCEL sheet.getRow::::{}", sheet.getRow(3));
//        log.info("EXCEL sheet.getRow::::{}", sheet.getRow(4));

        // @formatter:off
        return IntStream.range(2, rowCount+1)
                        .mapToObj(rowIndex -> rowFunc.apply(sheet.getRow(rowIndex)))
                        .collect(Collectors.toList()); 
        // @formatter:on
    }


    private Workbook readWorkbook(MultipartFile multipartFile) throws IOException, InvalidFormatException
    {
        verifyFileExtension(multipartFile);
        return multipartFileToWorkbook(multipartFile);
    }


    private void verifyFileExtension(MultipartFile multipartFile) throws InvalidFormatException
    {
        final String actlFileNm = multipartFile.getOriginalFilename();
        log.info("EXCEL actlFileNm::::{}", actlFileNm);
        if(!isExcelExtension(actlFileNm)) { throw new InvalidFormatException("This file extension is not verify"); }
    }


    private boolean isExcelExtension(String fileName)
    {
        return fileName.endsWith(ExcelConstant.XLS.getName()) || fileName.endsWith(ExcelConstant.XLSX.getName());
    }


    private boolean isExcelXls(String fileName)
    {
        return fileName.endsWith(ExcelConstant.XLS.getName());
    }


    private boolean isExcelXlsx(String fileName)
    {
        return fileName.endsWith(ExcelConstant.XLSX.getName());
    }


    private Workbook multipartFileToWorkbook(MultipartFile multipartFile) throws IOException
    {
        if(isExcelXls(multipartFile.getOriginalFilename())) {
            return new HSSFWorkbook(multipartFile.getInputStream());
        }
        else {
            return new XSSFWorkbook(multipartFile.getInputStream());
        }
    }
}
