package kr.co.hkcloud.palette3.phone.outbound.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.outbound.app.PhoneOutboundManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneOutboundManageRestController",
     description = "아웃바운드접수조회 REST 컨트롤러")
public class PhoneOutboundManageRestController
{
    private final PhoneOutboundManageService phoneOutboundManageService;


    /**
     * 아웃바운드 조회한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "아웃바운드 조회",
                  notes = "아웃바운드 조회")
    @PostMapping("/phone-api/outbound/manage/list")
    public Object selectObndList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 Key 조회
        objRetParams = phoneOutboundManageService.selectObndList(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 완전판매 아웃바운드조회한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "완전판매 아웃바운드조회",
                  notes = "완전판매 아웃바운드조회")
    @PostMapping("/phone-api/outbound/manage/sle-inquiry")
    public Object selectObndEndFcntList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //완전판매 아웃바운드조회
        objRetParams = phoneOutboundManageService.selectObndEndFcntList(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 진행조회한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "아웃바운드 진행조회",
                  notes = "아웃바운드 진행조회")
    @PostMapping("/phone-api/outbound/manage/inquire")
    public Object selectObndPrceList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 진행조회
        objRetParams = phoneOutboundManageService.selectObndPrceList(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 상세조회한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "아웃바운드 상세조회",
                  notes = "아웃바운드 상세조회")
    @PostMapping("/phone-api/outbound/manage/detail")
    public Object selectObndDtail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 Key 조회
        objRetParams = phoneOutboundManageService.selectObndDtail(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 배분정보 조회한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "아웃바운드 배분정보",
                  notes = "아웃바운드 배분정보")
    @PostMapping("/phone-api/outbound/manage/outbnd-list")
    public Object selectObndDivInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 Key 조회
        objRetParams = phoneOutboundManageService.selectObndDivInfo(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드마감 한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "아웃바운드마감",
                  notes = "아웃바운드마감")
    @PostMapping("/phone-api/outbound/manage/clos/process")
    public Object updateObndEnd(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 Key 조회
        objRetParams = phoneOutboundManageService.updateObndEnd(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 정보 수정 한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "아웃바운드정보 수정",
                  notes = "아웃바운드정보 수정")
    @PostMapping("/phone-api/outbound/manage/modify")
    public Object updateObndInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 Key 조회
        objRetParams = phoneOutboundManageService.updateObndInfo(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /*
     * /** 아웃바운드 사용여부 수정 한다.
     * 
     * @param inHashMap
     * 
     * @return TelewebJSON 형식의 처리 결과 데이터
     * 
     * @ApiOperation(value = "아웃바운드 사용여부 수정", notes = "아웃바운드 사용여부 수정")
     * 
     * @PostMapping("/phone-api/outbound/manage/process-at") public Object updateObndUseYn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
     * 
     * TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성 TelewebJSON jsonParams = new TelewebJSON(mjsonParams); //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA)); String
     * strTransFlag = mjsonParams.getString("DATA_FLAG"); //화면에서 전송된 플래그 설정
     * 
     * //아웃바운드 Key 조회 objRetParams = phoneOutboundManageService.updateObndUseYn(mjsonParams);
     * 
     * //최종결과값 반환 return objRetParams; }
     */
    /**
     * 아웃바운드 삭제 한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "아웃바운드 삭제",
                  notes = "아웃바운드 삭제")
    @PostMapping("/phone-api/outbound/manage/delete")
    public Object deleteObndCust(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 Key 조회
        objRetParams = phoneOutboundManageService.deleteObndCust(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 상담사별 아웃바운드 조회한다.(우측 인터페이스)
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "상담사별 아웃바운드 조회",
                  notes = "상담사별 아웃바운드 조회")
    @PostMapping("/phone-api/outbound/manage/listByUser")
    public Object selectUserObndList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 Key 조회
        objRetParams = phoneOutboundManageService.selectUserObndList(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

}
