package kr.co.hkcloud.palette3.phone.outbound.app;


import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneOutboundRegistAgentChangePopupService")
public class PhoneOutboundRegistAgentChangePopupServiceImpl implements PhoneOutboundRegistAgentChangePopupService
{
    private final TwbComDAO mobjDao;


    /**
     * 배분변경 상담원 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectObndDivChangeList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        // 배분변경 상담원 조회
        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentChangePopupMapper", "selectObndDivChangeList", mjsonParams);

        return objRetParams;

    }


    /**
     * 배분 상담원 변경
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateDivChange(TelewebJSON jsonParams) throws TelewebAppException
    {

        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON objParams = new TelewebJSON();
        TelewebJSON objRetDivSel = new TelewebJSON();
        TelewebJSON objDivParams = new TelewebJSON(); // 수정 시 파라미터

        // 상담원 변경 건 정보
        String dataStr = jsonParams.getString("DSTR_INFO"); // 아웃바운드 수정할 정보
        String totChgCnt = jsonParams.getString("TOT_CHG_CNT"); // 변경 건 총합계
        String[] dataArr = dataStr.split("/"); // 그리드 행 정보

        // 다중 아웃바운드 캠페인ID 정보
        String[] camIdInfo = jsonParams.getString("CAM_ID_ARR").split("/"); // 그리드 행 정보
        // CAM_ID 조건 추가
        List<String> CAM_VALUE_ARR = new LinkedList<String>();
        for(int x = 0; x < camIdInfo.length; x++) {
            String[] camIdInfoArr = camIdInfo[x].split(":"); // CAM_ID : 미배분건수
            CAM_VALUE_ARR.add(camIdInfoArr[0]);
        }
        jsonParams.setObject("CAM_VALUE_ARR", 0, CAM_VALUE_ARR);

        for(int i = 0; i < dataArr.length; i++) {

            // 수정할 아웃바운드 유무 확인.
            if(!dataArr[i].equals("") && dataArr[i] != null) {
                String[] dataDtlArr = dataArr[i].split(":"); // 각 상담원사번 : 건수

                String beUserId = dataDtlArr[0]; // 이전상담원
                String userId = dataDtlArr[1]; //  변경상담원
                String chgCnt = dataDtlArr[2]; // 건수

                //jsonParams.setString("CSLT_ID", userId); // 상담원사번
                jsonParams.setString("CSLT_ID", beUserId); // 이전상담원사번
                jsonParams.setString("DIV_CNT", chgCnt);        // 배분 횟수

                // 아웃바운드 배분 조회
                objParams = mobjDao
                    .select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "selectObndDivList", jsonParams);

                // 기존 상담원에 배분이 되어있는 경우만
                if(objParams.getSize() > 0) {
                    objDivParams.setString("CSLT_ID", userId);
                    objDivParams.setString("CHNG_MAN", jsonParams.getString("CHNG_MAN"));
                    for(int z = 0; z < objParams.getSize(); z++) {

                        objDivParams.setString("CAM_ID", objParams.getString("CAM_ID", z));
                        objDivParams.setString("CUST_NO", objParams.getString("CUST_NO", z));

                        // 배분 상담원 변경 
                        objRetParams = mobjDao
                            .update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentChangePopupMapper", "updateDivChange", objDivParams);

                    }
                }

            }
        }

        //최종결과값 반환
        return objRetParams;

    }

}
