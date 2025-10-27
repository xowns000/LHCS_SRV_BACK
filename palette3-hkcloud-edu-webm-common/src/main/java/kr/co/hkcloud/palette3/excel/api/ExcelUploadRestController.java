package kr.co.hkcloud.palette3.excel.api;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;


@Slf4j
//@RequiredArgsConstructor
//@RestController
@Api(value = "ExceUploadRestController",
     description = "엑셀 업로드 REST 컨트롤러 샘플")
public class ExcelUploadRestController
{
    //PhoneOutboundExceUploadRestController 참조
    //- 엑셀 업로드는 해당 업무마다 개별적으로 처리함 
//    private final ExcelReaderUtils excelReaderUtils;
//
//
//    @ApiOperation(value = "엑셀 업로드",
//                  notes = "엑셀 파일을 업로드한다")
//    @PostMapping("/api/excel/{taskTypeCd}/{pathTypeCd}/upload")
//    public ResponseEntity<Resource> uploadExcel(@ExcelRuleProperties @Valid final ExcelPropertiesResponse excelPropertiesResponse, @Valid final ExcelUploadRequest excelUploadRequest, BindingResult bindingResult) throws TelewebApiException
//    {
//        log.debug("excelPropertiesResponse: {}", excelPropertiesResponse);
//        log.debug("excelUploadRequest: {}", excelUploadRequest);
//
////        return excelReaderUtils.readFileToList(multipartFile, dProduct::from);
//        return null;
//    }
}
