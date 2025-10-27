package kr.co.hkcloud.palette3.common.code.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.code.app.LkagCodeCmmnService;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "LkagCodeCmmnRestController", description = "연동 코드공통 REST 컨트롤러")
public class LkagCodeCmmnRestController {

    private final LkagCodeCmmnService lkagCodeCmmnService;
    private final PaletteCmmnService paletteCmmnService;


    /**
     * 연동  공통코드의 코드,코드명를 검색한다.(콤보박스로드용) 컬럼명 통일을 위해 컬럼명을 변경함 CODE -> GROUP_CD, CODE_NM -> CD_NM, CODE_SE -> CD_TYPE
     *
     * @return 코드정보(CODE, CODE_NM)
     *
     */
    @NoBizLog
    @ApiOperation(value = "연동 공통코드 검색", notes = "연동 공통코드의 코드,코드명를 검색한다.(콤보박스로드용)")
    @PostMapping("/api/lkag-code/common/code-book/inqry")
    public Object selectRtnCodeBook(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        for(int i = 0; i < mjsonParams.getSize(); i++) {
            //코드타입에 ETC 코드값 설정 시 분리
            String[] objCodeType = mjsonParams.getString("LKAG_GROUP_CD_ID", i).split("\\|");
            String[] objCodeGubun = objCodeType[0].split("\\:");
            jsonParams.setString("LKAG_GROUP_CD_ID", objCodeGubun[0]);              //코드타입에 대한 키값 설정
            objRetParams.setDataObject(objCodeType[0], lkagCodeCmmnService.selectRtnCodeBook(jsonParams));
        }
        //반환정보 세팅
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "정상조회되었습니다.");

        //최종결과값 반환
        return objRetParams;

    }
}
