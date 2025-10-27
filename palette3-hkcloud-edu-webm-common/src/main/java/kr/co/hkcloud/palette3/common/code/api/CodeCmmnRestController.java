package kr.co.hkcloud.palette3.common.code.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.code.app.CodeCmmnService;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "CodeCmmnRestController",
     description = "코드공통 REST 컨트롤러")
public class CodeCmmnRestController
{
    private final CodeCmmnService    codeCmmnService;
    private final PaletteCmmnService paletteCmmnService;


    /**
     * 프레임워크에서 기본적으로 제공하는 공통코드의 코드,코드명를 검색한다.(콤보박스로드용) 컬럼명 통일을 위해 컬럼명을 변경함 CODE -> GROUP_CD, CODE_NM -> CD_NM, CODE_SE -> CD_TYPE
     * 
     * @return 코드정보(CODE,CODE_NM)
     * 
     */
    @NoBizLog
    @ApiOperation(value = "공통코드 검색",
                  notes = "프레임워크에서 기본적으로 제공하는 공통코드의 코드,코드명를 검색한다.(콤보박스로드용)")
    @PostMapping("/api/code/common/code-book/inqry")
    public Object selectRtnCodeBook(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        for(int i = 0; i < mjsonParams.getSize(); i++) {
            //코드타입에 ETC 코드값 설정 시 분리
            String[] objCodeType = mjsonParams.getString("GROUP_CD_ID", i).split("\\|");
            String[] objCodeGubun = objCodeType[0].split("\\:");
            jsonParams.setString("GROUP_CD_ID", objCodeGubun[0]);              //코드타입에 대한 키값 설정

            if(objCodeGubun.length > 1) {
                jsonParams.setString("CD_TYPE", objCodeGubun[1]);           //코드구분값 설정
            }

            for(int j = 1; j < objCodeType.length; j++) {
                //코드타입에 ETC코드값에 추가조건(CODE_TY|ETC_CODE1|ETC_CODE2|ETC_CODE3)설정시 해당 등록조건만큼 키 정보 생성
                jsonParams.setString("ETC_INFO0" + String.valueOf(j), objCodeType[j]);
            }
            if("ASPSENDERKEY".equals(objCodeType[0]) || "SNDR_KEY".equals(objCodeType[0])) {
                objRetParams.setDataObject(objCodeType[0], paletteCmmnService.selectRtnCachingAspSenderKey(jsonParams));
            }
            else if("CHN_CLSF_CD".equals(objCodeType[0])) {
                objRetParams.setDataObject(objCodeType[0], paletteCmmnService.selectRtnCachingBizServicesCd(jsonParams));
            }
            else if("TALK030_NOCACHE".equals(objCodeType[0])) {
                jsonParams.setString("GROUP_CD", objCodeType[0].replaceAll("_NOCACHE", ""));
                objRetParams.setDataObject(objCodeType[0], codeCmmnService.selectRtnCodeBook(jsonParams));
            }
            else {
                objRetParams.setDataObject(objCodeType[0], codeCmmnService.selectRtnCachingCodeBook(jsonParams));
            }
        }

        //반환정보 세팅
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "정상조회되었습니다.");

        //최종결과값 반환
        return objRetParams;

    }


    /**
     * 회사의 코드를 검색한다.(콤보박스로드용) ASP_USER_ID 를 통해 값을 전달.
     * 
     * @return 코드정보(CODE,CODE_NM)
     * @author 정성현
     */

    @NoBizLog
    @ApiOperation(value = "공통코드 검색",
                  notes = "로그인한 유저 ID에 따라 회사코드,코드명를 검색한다.(콤보박스로드용)")
    @PostMapping("/api/code/common/code-book/aspinqry")
    public Object selectRtnCachingAspBook(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

//        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        for(int i = 0; i < mjsonParams.getSize(); i++) {
            //코드타입에 ETC 코드값 설정 시 분리
            String[] objCodeType = mjsonParams.getString("USER_ID", i).split("\\|");
            String[] objCodeGubun = objCodeType[0].split("\\:");
            jsonParams.setInt("USER_ID", Integer.parseInt(objCodeGubun[0]));              //코드타입에 대한 키값 설정

            objRetParams.setDataObject(objCodeType[0], codeCmmnService.selectRtnCachingAspBook(jsonParams));

        }

        //반환정보 세팅
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "정상조회되었습니다.");

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 회사의 코드를 검색한다.(콤보박스로드용) ASP_USER_ID 를 통해 값을 전달.
     * 
     * @return 코드정보(CODE,CODE_NM)
     * @author 정성현
     */

    @NoBizLog
    @ApiOperation(value = "공통코드 검색",
                  notes = "로그인한 유저 ID에 따라 채팅채널이 있는 회사코드,코드명를 검색한다.(콤보박스로드용)")
    @PostMapping("/api/code/common/code-book/sender/aspinqry")
    public Object selectRtnCachingSenderAspBook(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

//        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        for(int i = 0; i < mjsonParams.getSize(); i++) {
            //코드타입에 ETC 코드값 설정 시 분리
            String[] objCodeType = mjsonParams.getString("ASP_USER_ID", i).split("\\|");
            String[] objCodeGubun = objCodeType[0].split("\\:");
            jsonParams.setString("ASP_USER_ID", objCodeGubun[0]);              //코드타입에 대한 키값 설정

            objRetParams.setDataObject(objCodeType[0], codeCmmnService.selectRtnCachingSenderAspBook(jsonParams));

        }

        //반환정보 세팅
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "정상조회되었습니다.");

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 모든 회사의 코드를 검색한다.(콤보박스로드용) 사용자의 권한 전달.
     * 
     * @return 코드정보(CODE,CODE_NM)
     */

    @NoBizLog
    @ApiOperation(value = "공통코드 검색",
                  notes = "모든 회사의 코드를 검색한다.(콤보박스로드용)")
    @PostMapping("/api/code/common/code-book/allaspinqry")
    public Object selectRtnCachingAllAspBook(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

//        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = codeCmmnService.selectRtnCachingAllAspBook(mjsonParams);

        //반환정보 세팅
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "정상조회되었습니다.");

        //최종결과값 반환
        return objRetParams;
    }

}
