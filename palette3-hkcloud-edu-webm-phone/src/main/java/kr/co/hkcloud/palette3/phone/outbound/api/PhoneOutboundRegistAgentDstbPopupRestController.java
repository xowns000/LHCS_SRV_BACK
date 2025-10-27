package kr.co.hkcloud.palette3.phone.outbound.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.outbound.app.PhoneOutboundRegistAgentDstbPopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneOutboundRegistAgentDstbRestController",
     description = "아웃바운드접수조회 REST 컨트롤러")
public class PhoneOutboundRegistAgentDstbPopupRestController
{
    private final PhoneOutboundRegistAgentDstbPopupService phoneOutboundRegistAgentDstbPopupService;


    /**
     * 상담원 배분 팝업 에서 아웃바운드 조회한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "상담원 배분 아웃바운드 조회",
                  notes = "상담원 배분 아웃바운드 조회")
    @PostMapping("/phone-api/outbound/manage/agent-dstb-process-popup/inqire")
    public Object selectCsltDivObndList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 조회
        objRetParams = phoneOutboundRegistAgentDstbPopupService.selectCsltDivObndList(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 상담원 배분 팝업 에서 아웃바운드 상세조회 한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "아웃바운드 상세조회",
                  notes = "아웃바운드 상세조회")
    @PostMapping("/phone-api/outbound/manage/agent-dstb-process-popup/detail")
    public Object selectCsltDivObndDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 조회
        objRetParams = phoneOutboundRegistAgentDstbPopupService.selectCsltDivObndDetail(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 상담원 배분정보 조회 한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "상담원 배분정보",
                  notes = "상담원 배분정보")
    @PostMapping("/phone-api/outbound/manage/agent-dstb-popup/agent-dstb/inqire")
    public Object selectCsltDivInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 조회
        objRetParams = phoneOutboundRegistAgentDstbPopupService.selectCsltDivInfo(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 상담원배분_아웃바운드 고객 조회 한다.
     * 
     * @param              inHashMap
     * @return                       TelewebJSON 형식의 처리 결과 데이터
     * 
     * @ApiOperation(value           = "아웃바운드 고객 조회", notes = "아웃바운드 고객 조회") @PostMapping("/api/ObndDiv/selectObndDivCustList") public Object selectObndDivCustList(@TelewebJsonParam TelewebJSON mjsonParams) throws
     *                                   Exception {
     * 
     *                                   TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성 TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
     *                                   //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA)); String strTransFlag = mjsonParams.getString("DATA_FLAG"); //화면에서 전송된 플래그 설정
     * 
     *                                   //아웃바운드 조회 objRetParams = phoneOutboundRegistAgentDstbPopupService.selectObndDivCustList(mjsonParams);
     * 
     *                                   //최종결과값 반환 return objRetParams; }
     * 
     * 
     *                                   /** 아웃바운드 배분 조회 한다.
     * 
     * @param              inHashMap
     * @return                       TelewebJSON 형식의 처리 결과 데이터
     * 
     * @ApiOperation(value           = "아웃바운드 배분 조회", notes = "아웃바운드 배분 조회") @PostMapping("/api/ObndDiv/selectObndDivList") public Object selectObndDivList(@TelewebJsonParam TelewebJSON mjsonParams) throws Exception {
     * 
     *                                   TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성 TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
     *                                   //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA)); String strTransFlag = mjsonParams.getString("DATA_FLAG"); //화면에서 전송된 플래그 설정
     * 
     *                                   //아웃바운드 조회 objRetParams = phoneOutboundRegistAgentDstbPopupService.selectObndDivList(mjsonParams);
     * 
     *                                   //최종결과값 반환 return objRetParams; }
     */
    /**
     * 아웃바운드 배분 수정 한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "아웃바운드배분수정",
                  notes = "아웃바운드배분수정")
    @PostMapping("/phone-api/outbound/manage/agent-dstb-process-popup/modify")
    public Object updateObndDiv(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 조회
        objRetParams = phoneOutboundRegistAgentDstbPopupService.updateObndDiv(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 배분 등록 한다.
     * 
     * @param              inHashMap
     * @return                       TelewebJSON 형식의 처리 결과 데이터
     * 
     * @ApiOperation(value           = "아웃바운드배분등록", notes = "아웃바운드배분등록") @PostMapping("/api/ObndDiv/insertObndDiv") public Object insertObndDiv(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
     * 
     *                                   TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성 TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
     *                                   //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA)); String strTransFlag = mjsonParams.getString("DATA_FLAG"); //화면에서 전송된 플래그 설정
     * 
     *                                   //아웃바운드 조회 objRetParams = phoneOutboundRegistAgentDstbPopupService.insertObndDiv(mjsonParams);
     * 
     *                                   //최종결과값 반환 return objRetParams; }
     */
    /**
     * 아웃바운드고객 배분여부 수정 한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "아웃바운드고객배분여부수정",
                  notes = "아웃바운드고객배분여부수정")
    @PostMapping("/phone-api/outbound/manage/agent-dstb-popup/modify-at/inqire")
    public Object updateObndDivYn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 조회
        objRetParams = phoneOutboundRegistAgentDstbPopupService.updateObndDivYn(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 아웃바운드 상담원배분미완료 조회 한다.
     * 
     * @param              inHashMap
     * @return                       TelewebJSON 형식의 처리 결과 데이터
     * 
     * @ApiOperation(value           = "아웃바운드상담원배분미완료조회", notes = "아웃바운드상담원배분미완료조회") @PostMapping("/api/ObndDiv/selectCsltDivNotEnd") public Object selectCsltDivNotEnd(@TelewebJsonParam TelewebJSON mjsonParams) throws
     *                                   Exception {
     * 
     *                                   TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성 TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
     *                                   //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA)); String strTransFlag = mjsonParams.getString("DATA_FLAG"); //화면에서 전송된 플래그 설정
     * 
     *                                   //아웃바운드 조회 objRetParams = phoneOutboundRegistAgentDstbPopupService.selectCsltDivNotEnd(mjsonParams);
     * 
     *                                   //최종결과값 반환 return objRetParams; }
     * 
     *                                   /** 아웃바운드 배분삭제 한다.
     * 
     * @param              inHashMap
     * @return                       TelewebJSON 형식의 처리 결과 데이터
     * 
     * @ApiOperation(value           = "아웃바운드 배분삭제", notes = "아웃바운드 배분삭제") @PostMapping("/api/ObndDiv/deleteObndDiv") public Object deleteObndDiv(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
     * 
     *                                   TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성 TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
     *                                   //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA)); String strTransFlag = mjsonParams.getString("DATA_FLAG"); //화면에서 전송된 플래그 설정
     * 
     *                                   //아웃바운드 조회 objRetParams = phoneOutboundRegistAgentDstbPopupService.deleteObndDiv(mjsonParams);
     * 
     *                                   //최종결과값 반환 return objRetParams; }
     * 
     *                                   /** 고객을가지고있는 사원 조회 한다.
     * 
     * @param              inHashMap
     * @return                       TelewebJSON 형식의 처리 결과 데이터
     * 
     * @ApiOperation(value           = "고객을가지고있는 사원조회", notes = "고객을가지고있는 사원조회") @PostMapping("/api/ObndDiv/selectObndHavingCust") public Object selectObndHavingCust(@TelewebJsonParam TelewebJSON mjsonParams) throws
     *                                   Exception {
     * 
     *                                   TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성 TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
     *                                   //jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA)); String strTransFlag = mjsonParams.getString("DATA_FLAG"); //화면에서 전송된 플래그 설정
     * 
     *                                   //아웃바운드 조회 objRetParams = phoneOutboundRegistAgentDstbPopupService.selectObndHavingCust(mjsonParams);
     * 
     *                                   //최종결과값 반환 return objRetParams; }
     * 
     */
}
