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
@Service("phoneOutboundRegistAgentDstbPopupService")
public class PhoneOutboundRegistAgentDstbPopupServiceImpl implements PhoneOutboundRegistAgentDstbPopupService
{
    private final TwbComDAO mobjDao;


    /**
     * 상담원 배분 팝업 에서 아웃바운드 조회한다.
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectCsltDivObndList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        String dataStr = jsonParams.getString("CAM_ID_ARR"); // 아웃바운드 수정할 정보
        String[] dataArr = dataStr.split("/"); // 그리드 행 정보

        // CAM_ID 조건 추가
        List<String> CAM_VALUE_ARR = new LinkedList<String>();
        for(int i = 0; i < dataArr.length; i++) {
            CAM_VALUE_ARR.add(dataArr[i]);
        }
        jsonParams.setObject("CAM_VALUE_ARR", 0, CAM_VALUE_ARR);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "selectCsltDivObndList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 상담원 배분 팝업 에서 아웃바운드 상세조회 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectCsltDivObndDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        String dataStr = jsonParams.getString("CAM_ID_ARR"); // 아웃바운드 수정할 정보
        String[] dataArr = dataStr.split("/"); // 그리드 행 정보

        // CAM_ID 조건 추가
        List<String> CAM_VALUE_ARR = new LinkedList<String>();
        for(int i = 0; i < dataArr.length; i++) {
            CAM_VALUE_ARR.add(dataArr[i]);
        }
        jsonParams.setObject("CAM_VALUE_ARR", 0, CAM_VALUE_ARR);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "selectCsltDivObndDetail", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 상담원 배분정보 조회 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectCsltDivInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        String dataStr = jsonParams.getString("CAM_ID_ARR"); // 아웃바운드 수정할 정보
        String[] dataArr = dataStr.split("/"); // 그리드 행 정보

        // CAM_ID 조건 추가
        List<String> CAM_VALUE_ARR = new LinkedList<String>();
        for(int i = 0; i < dataArr.length; i++) {
            CAM_VALUE_ARR.add(dataArr[i]);
        }
        jsonParams.setObject("CAM_VALUE_ARR", 0, CAM_VALUE_ARR);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "selectCsltDivInfo", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 상담원배분_아웃바운드 고객 조회 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectObndDivCustList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "selectObndDivCustList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 배분 조회 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectObndDivList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "selectObndDivList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 배분 수정 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateObndDiv(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(); // RETURN REUST
        TelewebJSON objRetDivCustParams = new TelewebJSON(); // RETURN REUST
        TelewebJSON objParams = new TelewebJSON();
        TelewebJSON objDivParams = new TelewebJSON();       // 수정 시 파라미터

        /******************** 기본 정보 선언 S ********************/
        // 아웃바운드 추가.회수 및 배분 정보
        String dataStr = jsonParams.getString("DSTR_INFO"); // 아웃바운드 수정할 정보
        String totChgCnt = jsonParams.getString("TOT_CHG_CNT"); // 변경 건 총합계
        String[] dataArr = dataStr.split("/"); // 그리드 행 정보 (상담원 배분정보) - 각 상담원사번 : 건수
        String ASP_NEWCUST_KEY = jsonParams.getString("ASP_NEWCUST_KEY"); //회사명
        // 다중 아웃바운드 배분 수정 정보
        String[] camIdInfo = jsonParams.getString("CAM_ID_INFO").split("/"); // 그리드 행 정보
        List<String> CAM_VALUE_ARR = new LinkedList<String>();
        // 캠페인 ID (캠페인기준) 
        for(int x = 0; x < camIdInfo.length; x++) {
            String[] camIdInfoArr = camIdInfo[x].split(":");        // CAM_ID : 미배분건수
            CAM_VALUE_ARR.add(camIdInfoArr[0]);                     // CAM_ID
        }
        jsonParams.setObject("CAM_VALUE_ARR", 0, CAM_VALUE_ARR);    // CAM_ID

        // 상담원 정보
        String[] dataCsltIdArr = new String[dataArr.length];
        String[] dataDivCntArr = new String[dataArr.length];
        for(int x = 0; x < dataArr.length; x++) {
            String[] dataDtlArr = dataArr[x].split(":");                // 각 상담원사번 : 건수
            dataCsltIdArr[x] = dataDtlArr[0];                       // 상담원
            dataDivCntArr[x] = dataDtlArr[1];                       // 추가.회수건수
        }
        /******************** 기본 정보 선언 E ********************/

        // 캠페인 기준 배분 할 아웃바운드 고객조회  // 캠페인에 따른 미배분된 건수(정보 포함)
        jsonParams.setString("DSTR_YN", "N");                       // 배분여부
        jsonParams.setString("DIV_CNT", totChgCnt);                 // 배분할 총 건수
        objRetDivCustParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "selectObndDivCustList", jsonParams);

        // 캠페인, 고객 기준
        int csltIdCnt = 0;                                          // 진행 번호
        // 캠페인, 고객 기준에 따른 상담원 배분 매핑
        String CSLT_ID = "";                                        // 상담원 ( 선택된 상담원 )
        int DIV_CNT = 0;                                            // 각 상담원의 배분 된 건수
        boolean flug = false;

        for(int y = 0; y < objRetDivCustParams.getSize(); y++) {

            // 선택한 상담원 인원 보다 카운트가 많을 경우 다시 처음 상담원으로 세팅
            if(csltIdCnt >= dataCsltIdArr.length) {
                csltIdCnt = 0;
            }

            CSLT_ID = dataCsltIdArr[csltIdCnt];                     // 상담원 매핑
            DIV_CNT = Integer.parseInt(dataDivCntArr[csltIdCnt]);   // 각 상담원의 배분 된 건수
            //objRetDivCustParams.setString("CSLT_ID", y, CSLT_ID);

            // 배분 분배 건수가 아직 남아있는 상담원 검색
            if(DIV_CNT == 0) {

                // 남은 배분할 건수
                int sumDivCnt = 0;
                for(int x = 0; x < dataDivCntArr.length; x++) {
                    sumDivCnt += Integer.parseInt(dataDivCntArr[x]);
                }

                // 배분할 건수가 없는 경우.
                if(sumDivCnt == 0) {
                    flug = true;
                }
                else {
                    // 배분할 건수가 남아 있는 경우
                    // 선택한 상담원 인원 보다 카운트가 많을 경우 다시 처음 상담원으로 세팅
                    csltIdCnt++;
                    if(csltIdCnt >= dataCsltIdArr.length) {
                        csltIdCnt = 0;
                    }
                    for(int i = csltIdCnt; i < dataDivCntArr.length; i++) {
                        // 배분이 남아있는 상황
                        if(Integer.parseInt(dataDivCntArr[i]) > 0) {
                            flug = false;
                            CSLT_ID = dataCsltIdArr[i];                                     // 배분 분배 건수가 아직 남아있는 상담원 매핑
                            DIV_CNT = Integer.parseInt(dataDivCntArr[i]);                   // 남아있는 배분건수
                            csltIdCnt = i;                                                  // 진행 번호
                            break;
                        }
                    }

                    // 그다음 상담원들도 배분 건수가 없을 경우 다시 처음 상담원을 비교
                    if(DIV_CNT == 0) {
                        for(int i = 0; i < dataDivCntArr.length; i++) {
                            // 배분이 남아있는 상황
                            if(Integer.parseInt(dataDivCntArr[i]) > 0) {
                                flug = false;
                                CSLT_ID = dataCsltIdArr[i];                                     // 배분 분배 건수가 아직 남아있는 상담원 매핑
                                DIV_CNT = Integer.parseInt(dataDivCntArr[i]);                   // 남아있는 배분건수
                                csltIdCnt = i;                                                  // 진행 번호
                                break;
                            }
                        }
                    }

                }
            }

            // 배분이 완료 될때까지 실행
            if(!flug) {

                // 아웃바운드 배분
                String userId = CSLT_ID;                                                         // 상담원
                String chgCnt = "1";                                                             // 추가.회수건수
                jsonParams.setString("CSLT_ID", objRetDivCustParams.getString("CSLT_ID", y));    // 이미 매핑되어 있는 상담원
                jsonParams.setString("DIV_CNT", chgCnt);                                         // 배분건수는 한건씩
                List<String> CAM_ID_LIST = new LinkedList<String>();                             // 캠페인ID는 한개씩 
                CAM_ID_LIST.add(objRetDivCustParams.getString("CAM_ID", y));
                jsonParams.setObject("CAM_VALUE_ARR", 0, CAM_ID_LIST);
                // 아웃바운드 배분 조회 (처리중, 진행중인)
                objParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "selectObndDivList", jsonParams);

                // 기존 상담원에 배분이 되어있는 경우 (update)
                if(objParams.getSize() > 0) {
                    // 기존에 배분 되어있는 상담원 매핑
                    objDivParams.setString("CSLT_ID", userId);
                    objDivParams.setString("CHNG_MAN", jsonParams.getString("CHNG_MAN"));
                    objDivParams.setString("CAM_ID", objRetDivCustParams.getString("CAM_ID", y));
                    objDivParams.setString("CUST_NO", objRetDivCustParams.getString("CUST_NO", y));
                    objDivParams.setString("ASP_NEWCUST_KEY", objRetDivCustParams.getString("CUSTCO_ID", y));

                    //아웃바운드 배분 수정.
                    objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "updateObndDiv", objDivParams);

                    // 기존 상담원에 배분이 안 되어 있는 경우 (insert)
                }
                else {
                    // 고객을 가지고 있는 상담원 조회
                    TelewebJSON objRetDivSngl = new TelewebJSON();
                    objRetDivSngl.setString("CAM_ID", objRetDivCustParams.getString("CAM_ID", y));
                    objRetDivSngl.setString("CUST_NO", objRetDivCustParams.getString("CUST_NO", y));
                    objRetDivSngl.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                    TelewebJSON objObndHavingCust = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "selectObndHavingCust", objRetDivSngl);
                    // 아웃바운드 등록 및 수정 조건
                    if(objObndHavingCust.getSize() > 0) {
                        // 고객을 가지고 있는 상담원 세팅 (기존 상담원)
                        jsonParams.setString("CSLT_ID", objObndHavingCust.getString("CSLT_ID"));       // 기존상담원 매핑
                    }
                    else {
                        jsonParams.setString("CSLT_ID", userId);       // 상담원 매핑
                    }
                    // 회사명 배분11111

                    //if(objRetDivSngl.getString("ASP_NEWCUST_KEY", y) != null) { 
                    //jsonParams.setString("ASP_NEWCUST_KEY", objRetDivSngl.getString("ASP_NEWCUST_KEY", y));
                    jsonParams.setString("ASP_NEWCUST_KEY", ASP_NEWCUST_KEY);
                    //}
                    /*
                     * else { jsonParams.setString("ASP_NEWCUST_KEY", objRetDivCustParams.getString("CUSTCO_ID", y)); System.out.println("다중 아웃바운드 실행중====>5555555"); }
                     */

                    jsonParams.setString("CAM_ID", objRetDivCustParams.getString("CAM_ID", y));        // 캠페인ID
                    jsonParams.setString("CUST_NO", objRetDivCustParams.getString("CUST_NO", y));      // 고객번호
                    jsonParams.setString("DSTR_YN", "Y");                                              // 배분여부 Y
                    // 아웃바운드 상담원 배분 등록.
                    objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "insertObndDiv", jsonParams);

                    // 아웃바운드 고객 배분여부 수정.
                    objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "updateObndDivYn", jsonParams);

                }

                DIV_CNT--;                                                                             // 건수 하나씩 빼기
                dataDivCntArr[csltIdCnt] = DIV_CNT + "";
                csltIdCnt++;

            }

        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 배분 등록 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertObndDiv(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "insertObndDiv", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드고객 배분여부 수정 한다.(회수처리)
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateObndDivYn(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(); // 조회 파라미터
        TelewebJSON objUpdtDelParams = new TelewebJSON(); // 수정삭제 파리미터
        TelewebJSON objUpdtDelRetParams = new TelewebJSON(); // 수정삭제 결과 파리미터

        String dataStr = jsonParams.getString("DSTR_INFO"); // 아웃바운드 수정할 정보
        String totChgCnt = jsonParams.getString("TOT_CHG_CNT"); // 변경 건 총합계
        String centTy = jsonParams.getString("CENT_TY"); // 센터구분

        // 나중에 센터 구분 (공제지원 / 업무지원)
        // if( centTy.equals("G") ) {}

        // 다중 아웃바운드 배분 수정 정보
        String[] camIdInfo = jsonParams.getString("CAM_ID_INFO").split("/"); // 그리드 행 정보
        // CAM_ID 조건 추가
        List<String> CAM_VALUE_ARR = new LinkedList<String>();
        for(int x = 0; x < camIdInfo.length; x++) {
            String[] camIdInfoArr = camIdInfo[x].split(":"); // CAM_ID : 미배분건수
            CAM_VALUE_ARR.add(camIdInfoArr[0]);
        }
        jsonParams.setObject("CAM_VALUE_ARR", 0, CAM_VALUE_ARR);

        String[] dataArr = dataStr.split("/"); // 그리드 행 정보
        for(int i = 0; i < dataArr.length; i++) {
            // 수정할 아웃바운드 정보 확인.
            if(!dataArr[i].equals("") && dataArr[i] != null) {
                String[] dataDtlArr = dataArr[i].split(":"); // 각 상담원사번 : 건수

                String userId = dataDtlArr[0];
                String chgCnt = dataDtlArr[1];

                jsonParams.setString("CSLT_ID", userId); // 상담원사번
                jsonParams.setString("CHG_CNT", chgCnt);        // 배분 횟수

                // 아웃바운드 상담원배분미완료 조회
                objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "selectCsltDivNotEnd", jsonParams);

                // 결과값 확인. 
                if(objRetParams.getSize() > 0) {
                    for(int x = 0; x < objRetParams.getSize(); x++) {

                        objUpdtDelParams.setString("CAM_ID", objRetParams.getString("CAM_ID", x));
                        objUpdtDelParams.setString("CUST_NO", objRetParams.getString("CUST_NO", x));
                        objUpdtDelParams.setString("CSLT_ID", jsonParams.getString("CSLT_ID"));

                        //아웃바운드 배분 삭제.(캠페인 배분정보 삭제)
                        mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "deleteObndDiv", objUpdtDelParams);

                        objUpdtDelParams.setString("DSTR_YN", "N"); // 배분여부
                        objUpdtDelParams.setString("CHNG_MAN", jsonParams.getString("CHNG_MAN"));
                        //아웃바운드 배분 여부 수정. (대상고객 배분 수정)
                        objUpdtDelRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "updateObndDivYn", objUpdtDelParams);
                    }
                }

            }
        }

        //최종결과값 반환
        return objUpdtDelRetParams;
    }


    /**
     * 아웃바운드 상담원배분미완료 조회 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectCsltDivNotEnd(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "selectCsltDivNotEnd", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 배분삭제 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteObndDiv(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.delete("kr.co.cu.cuics.mng.ics.obm.obnd.dao.deleteObndDiv", "deleteObndDiv", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 고객을가지고있는 사원조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectObndHavingCust(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistAgentDstbPopupMapper", "selectObndHavingCust", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }

}
