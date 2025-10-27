package kr.co.hkcloud.palette3.phone.transfer.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.transfer.app.PhoneTransferManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneTransferManageRestController", description = "이관처리현황 REST 컨트롤러")
public class PhoneTransferManageRestController {

    private final PhoneTransferManageService phoneTransferManageService;
    private final PaletteCmmnService paletteCmmnService;

    /**
     * 이관 배분 현황 count 조회
     * @param mjsonParams 요청 파라미터 (시분초는 고정)
     *                    - DRWI_DT_START 조회시작기간 ex) 20250120000000
     *                    - DRWI_DT_END   조회종료기간 ex) 20250121235959
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "이관 배분 현황 count 조회", notes = "이관 배분 현황 count 조회한다.")
    @PostMapping("/phone-api/transfer/manage/transfer-dstb/inqire")
    public Object selectTransferDstbCount(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //DAO 검색 메소드 호출
        objRetParams = phoneTransferManageService.selectTransferDstbCount(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 상담사 별 이관 배분 현황 COUNT 조회
     * @param mjsonParams 요청 파라미터
     *                    - USER_NM 사용자명
     *                    - LGN_ID  로그인아이디
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "상담사 별 이관 배분 현황 COUNT 조회", notes = "상담사 별 이관 배분 현황 COUNT 조회한다.")
    @PostMapping("/phone-api/transfer/manage/agent-dstb/inqire")
    public Object selectTransferAgentDstbCount(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //DAO 검색 메소드 호출
        objRetParams = phoneTransferManageService.selectTransferAgentDstbCount(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 이관 상담사들에게 배분
     * @param mjsonParams 요청 파라미터 (시분초는 고정)
     *                    - ALTMNT_INFO   상담사 및 배분 건수     ex) 'user_id':'cnt'/'user_id':'cnt'
     *                    - START_DATE    시작일자               ex) 20250120000000
     *                    - DRWI_DT_START 시작일자               ex) 20250120000000
     *                    - DRWI_DT_END   종료일자               ex) 20250120235959
     *                    - NOT_AMT_LIST  배분할 이관대상 id list
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "이관 상담사들에게 배분", notes = "미배분 이관을 상담사들에게 배분한다.")
    @PostMapping("/phone-api/transfer/manage/regist")
    public Object insertTransferManageDstb(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //DAO 검색 메소드 호출
        objRetParams = phoneTransferManageService.insertTransferManageDstb(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 이관 상담사 회수
     * @param mjsonParams 요청 파라미터 (시분초는 고정)
     *                    - CUTT_TRNSF_ID   상담이관ID
     *                    - TARGET_OPT_CD   담당자옵션코드
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "이관 상담사들에게 배분", notes = "미배분 이관을 상담사들에게 배분한다.")
    @PostMapping("/phone-api/transfer/manage/agent-dstb/modify")
    public Object deleteTransferManageDstb(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //DAO 검색 메소드 호출
        objRetParams = phoneTransferManageService.deleteTransferManageDstb(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }
}
