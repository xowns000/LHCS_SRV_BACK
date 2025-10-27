package kr.co.hkcloud.palette3.excel.web;


import io.swagger.annotations.Api;


// @Controller
@Api(value = "ExcelDownloadController",
     description = "엑셀 다운로드 컨트롤러 샘플")
public class ExcelDownloadController
{
//    @ApiOperation(value = "Microsoft Excel 다운로드",
//                  notes = "Microsoft 엑셀 다운로드한다")
//    @GetMapping(value = "/excel/web/{taskTypeCd}/{pathTypeCd}/download-xls",
//                produces = "application/vnd.ms-excel")
//    public ModelAndView xlsView()
//    {
//        return new ModelAndView("excelXlsView", initExcelData());
//    }
//
//
//    @ApiOperation(value = "Open XML Excel 다운로드",
//                  notes = "Open XML 엑셀 다운로드한다")
//    @GetMapping(value = "/excel/web/{taskTypeCd}/{pathTypeCd}/download-xlsx",
//                produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
//    public ModelAndView xlsxView()
//    {
//        return new ModelAndView("excelXlsxView", initExcelData());
//    }
//
//
//    @ApiOperation(value = "Open XML Excel Streaming 다운로드",
//                  notes = "Open XML Excel 스트리밍 다운로드한다")
//    @GetMapping(value = "/excel/web/{taskTypeCd}/{pathTypeCd}/download-xlsx-streaming",
//                produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
//    public ModelAndView xlsxStreamingView()
//    {
//        return new ModelAndView("excelXlsxStreamingView", initExcelData());
//    }
//
//
//    private Map<String, Object> initExcelData()
//    {
//        Map<String, Object> map = new HashMap<>();
//        map.put(ExcelConstant.FILE_NAME.getName(), "default_excel");
//        map.put(ExcelConstant.HEAD.getName(), Arrays.asList("ID", "NAME", "COMMENT"));
//        map.put(ExcelConstant.BODY.getName(), Arrays.asList(Arrays.asList("A", "a", "가"), Arrays.asList("B", "b", "나"), Arrays.asList("C", "c", "다")));
//        return map;
//    }
}
