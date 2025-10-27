package kr.co.hkcloud.palette3.phone.outbound.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneOutboundRegistCstmrPopupService")
public class PhoneOutboundRegistCstmrPopupServiceImpl implements PhoneOutboundRegistCstmrPopupService
{
    private final TwbComDAO mobjDao;


    /**
     * 아웃바운드 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectObndCam(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistCstmrPopupMapper", "selectObndCam", mjsonParams);
    }


    /**
     * 아웃바운드 단건 등록 팝업
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertObndSnglReg(TelewebJSON mjsonParams) throws TelewebAppException
    {

        TelewebJSON objRetParams = new TelewebJSON();

        String centTyStr = mjsonParams.getString("CENT_TY");

        //if( centTyStr.equals("G") ) {
        // 공제단말

        // 아웃바운드 고객 등록
        mobjDao
            .insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCustReg", mjsonParams);

        // 아웃바운드 고객 부가 정보 등록
        mobjDao
            .insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCustRegAdd", mjsonParams);

        // 캠페인 고객 정보 등록
        objRetParams = mobjDao
            .insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCamCustReg", mjsonParams);

        // 배분여부 Y인 경우
        if(mjsonParams.getString("DIV_YN").equals("Y")) {

            // 파라미터
            mjsonParams.setString("DSTR_YN", "Y");  // 배분여부
            mjsonParams.setString("CSLT_ID", mjsonParams.getString("REG_MAN"));  // 배분담당자 (로그인한 상담원)

            // 캠페인 고객 정보 등록
            mobjDao
                .insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "insertObndDiv", mjsonParams);

            // 아웃바운드고객 배분여부 수정
            mobjDao
                .update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "updateObndDivYn", mjsonParams);

        }

        //}

        return objRetParams;

    }


    /**
     * 아웃바운드 고객 단건 등록 중복 체크
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectObndSnglRegDubChk(TelewebJSON mjsonParams) throws TelewebAppException
    {

        TelewebJSON objRetParams = new TelewebJSON();

        // 아웃바운드 고객 단건 등록 중복 체크
        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "selectObndSnglRegDubChk", mjsonParams);

        return objRetParams;

    }

}
