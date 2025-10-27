package kr.co.hkcloud.palette3.phone.outbound.api;


import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.model.PaletteApiResponse;
import kr.co.hkcloud.palette3.excel.args.ExcelRuleProperties;
import kr.co.hkcloud.palette3.excel.domain.ExcelRequest.ExcelUploadRequest;
import kr.co.hkcloud.palette3.excel.domain.ExcelResponse.ExcelPropertiesResponse;
import kr.co.hkcloud.palette3.excel.util.ExcelReaderUtils;
import kr.co.hkcloud.palette3.exception.PaletteApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.outbound.domain.PhoneOutboundResponse.OuboundRegistExcelResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneOutboundExceUploadRestController",
     description = "전화 아웃바운드 엑셀 업로드 REST 컨트롤러")
public class PhoneOutboundExceUploadRestController
{
    private final ExcelReaderUtils excelReaderUtils;


    @ApiOperation(value = "전화 아웃바운드 등록 엑셀 업로드",
                  notes = "전화 아웃바운드 등록에서 엑셀 파일을 업로드한다")
    @PostMapping("/api/excel/{taskTypeCd}/{pathTypeCd}/regist/upload")
    public ResponseEntity<PaletteApiResponse<?>> uploadExcel(@ExcelRuleProperties @Valid final ExcelPropertiesResponse excelPropertiesResponse, @Valid final ExcelUploadRequest excelUploadRequest, BindingResult bindingResult) throws PaletteApiException
    {
        log.debug("excelPropertiesResponse: {}", excelPropertiesResponse);
        log.debug("excelUploadRequest: {}", excelUploadRequest);

        List<OuboundRegistExcelResponse> uploadResponseList;
        try {
            uploadResponseList = excelReaderUtils.readFileToList(excelUploadRequest, OuboundRegistExcelResponse::new);
            log.debug("uploadResponseList::: {}", uploadResponseList);
        }
        catch(InvalidFormatException | IOException e) {
            throw new PaletteApiException(e.getLocalizedMessage(), e);
        }

        //응답
        return new ResponseEntity<PaletteApiResponse<?>>(PaletteApiResponse.res(uploadResponseList), HttpStatus.OK);
    }


    /* 엑셀양식 다운로드 */
    @ApiOperation(value = "전화 아웃바운드 등록 엑셀 양식 다운로드",
                  notes = "전화 아웃바운드 등록에서 엑셀 양식 파일을 다운로드한다")
    @PostMapping("/api/excel/phone/outbound/regist/header/inqire")
    public Object selectExcelHeader(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        mjsonParams.setHeader("TWB_SQL_NAME_SPACE", "kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper");
        mjsonParams.setHeader("TWB_SQL_ID", "selectObndCustList");
        mjsonParams.setString("OBND_SRCH_DATE", "9999/99/99");

        //최종결과값 반환
        return mjsonParams;
    }
}
