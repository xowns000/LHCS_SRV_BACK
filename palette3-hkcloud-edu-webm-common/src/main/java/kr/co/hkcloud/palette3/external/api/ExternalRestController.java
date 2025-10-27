package kr.co.hkcloud.palette3.external.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.board.util.BoardValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.external.app.ExternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ExternalRestController", description = "external test 컨트롤러")
public class ExternalRestController {

    private final ExternalService externalService;

    private final BoardValidator boardValidator;

    /**
     *
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "게시판-목록-조회", notes = "게시판 목록을 조회한다")
    @PostMapping("/api/test/select")
    public Object selectRtn1(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        //
        //        //필수객체정의
        //        TelewebJSON objRetData1 = new TelewebJSON(mjsonParams);     //부분반환파라미터생성
        //
        //        //DAO검색 메서드 호출
        //
        ////        objRetParams = boardService.selectRtnBrdSystem(mjsonParams);
        //        objRetData1 = externalService.sampleSelect(mjsonParams);
        ////log.info("objRetData1 >>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+objRetData1.toString());
        //        //objRetParams.setHeader("MASTER_CNT", objRetData1.getHeaderInt("COUNT"));
        //        objRetParams.setDataObject("DATA_MASTER", objRetData1);
        //
        //        //최종결과값 반환
        //        return objRetParams;

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성

        //필수객체정의
        TelewebJSON objRetData1 = new TelewebJSON(mjsonParams);     //부분반환파라미터생성

        //DAO검색 메서드 호출
        String brdId = mjsonParams.getString("BRD_ID");

        if ("1".equals(brdId) || "7".equals(brdId)) { // 시스템 공지 사항 또는 챗봇 공지사항

            //Validation 체크
            boardValidator.validate(mjsonParams, result);
            if (result.hasErrors()) {
                throw new TelewebValidationException(result.getAllErrors());
            }

            objRetParams = externalService.selectRtnBrdSystem1(mjsonParams);
        } else {
            objRetParams = externalService.selectRtnBrdList1(mjsonParams);
        }
        objRetData1 = externalService.selectRtnBrdCheck1(mjsonParams);

        objRetParams.setHeader("MASTER_CNT", objRetData1.getHeaderInt("COUNT"));
        objRetParams.setDataObject("DATA_MASTER", objRetData1.getDataObject(TwbCmmnConst.G_DATA));

        //최종결과값 반환
        return objRetParams;


    }


}
