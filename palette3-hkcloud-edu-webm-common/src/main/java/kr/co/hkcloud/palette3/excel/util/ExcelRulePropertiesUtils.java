package kr.co.hkcloud.palette3.excel.util;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import kr.co.hkcloud.palette3.config.properties.excel.ExcelProperties;
import kr.co.hkcloud.palette3.config.properties.excel.ExcelProperties.Repository;
import kr.co.hkcloud.palette3.config.properties.excel.ExcelProperties.Repository.Phone;
import kr.co.hkcloud.palette3.excel.domain.ExcelResponse.ExcelPropertiesResponse;
import kr.co.hkcloud.palette3.excel.enumer.ExcelBusiType;
import kr.co.hkcloud.palette3.excel.enumer.ExcelPathType;
import kr.co.hkcloud.palette3.exception.PaletteUtilException;
import kr.co.hkcloud.palette3.file.enumer.FileUploadLib;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 엑셀 규칙 유틸
 * 
 * @author RND
 *
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@Component
public class ExcelRulePropertiesUtils
{
    private final ExcelProperties excelProperties;


    /**
     * 파일 업무구분에 따른 경로구분 프로퍼티
     * 
     * @param  taskTypeCd
     * @param  pathTypeCd
     * @return                   RulePathTypePropertiesResponse
     * @throws FileRuleException
     */
    @Valid
    public ExcelPropertiesResponse getProperties(@NotNull final ExcelBusiType taskTypeCd, @NotNull final ExcelPathType pathTypeCd) throws PaletteUtilException
    {
        ExcelPropertiesResponse excelPropertiesResponse = null;
        final Repository repository = excelProperties.getRepository();
        switch(taskTypeCd)
        {
            //전화
            case phone:
            {
                final Phone phone = repository.getPhone();
                switch(pathTypeCd)
                {
                    //아웃바운드
                    case outbound:
                    {
                        final Phone.Outbound files = phone.getOutbound();
                        excelPropertiesResponse = new ExcelPropertiesResponse();
                        BeanUtils.copyProperties(files, excelPropertiesResponse);
                        log.debug("taskTypeCd={}, pathTypeCd={}, excelPropertiesResponse={}", taskTypeCd, pathTypeCd, excelPropertiesResponse);
                        break;
                    }
                    default:
                    {
                        throw new PaletteUtilException(String.format("엑셀 레포지토리 경로 구분이 유효하지 않습니다. %1$s>%2$s", taskTypeCd.name(), pathTypeCd.name()));
                    }
                }
                break;
            }
            default:
            {
                throw new PaletteUtilException(String.format("엑셀 레포지토리 업무 구분이 유효하지 않습니다. %1$s", taskTypeCd.name()));
            }
        }
        if(excelPropertiesResponse != null) {
            //파일 업로드 라이브러리
            final FileUploadLib excelUploadLib = excelProperties.getExcelUploadLib();

            // @formatter:off
            //업로드 경로 .append("/").append(taskTypeCd.name()).append("/").append(pathTypeCd.name())
            final StringBuffer sbUploadUri = new StringBuffer("/api/excel").append("/")
                                                      .append(taskTypeCd.name()).append("/")
                                                      .append(pathTypeCd.name());

            //다운로드 경로
            final StringBuffer sbDownloadUri = new StringBuffer("/excel/web").append("/")
                                                        .append(taskTypeCd.name()).append("/")
                                                        .append(pathTypeCd.name());

            excelPropertiesResponse.setUploadUri(sbUploadUri.toString());
            excelPropertiesResponse.setDownloadUri(sbDownloadUri.toString());
            excelPropertiesResponse.setTaskTypeCd(taskTypeCd);
            excelPropertiesResponse.setPathTypeCd(pathTypeCd);
            excelPropertiesResponse.setExcelUploadLib(excelUploadLib);
           // @formatter:on
        }
        return excelPropertiesResponse;
    }

}
