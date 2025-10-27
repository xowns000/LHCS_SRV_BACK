package kr.co.hkcloud.palette3.chat.setting.app;


import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.properties.chat.ChatProperties;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.util.FileMngUtils;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Slf4j
@RequiredArgsConstructor
@Service("ChatSettingInquiryTypeManageService")
public class ChatSettingInquiryTypeManageServiceImpl implements ChatSettingInquiryTypeManageService
{
    private final FileRulePropertiesUtils         fileRulePropertiesUtils;
    private final InnbCreatCmmnService            innbCreatCmmnService;
    private final ChatSettingMessageManageService chatSettingMessageManageService;
    private final ChatProperties                  chatProperties;
    private final TwbComDAO                       mobjDao;
    private final FileMngUtils                    fileMngUtils;


    /**
     * 채널별 문의유형트리 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnTalkMngInqryTypeTreeViewByChannel(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "selectRtnTalkMngInqryTypeTreeViewByChannel", jsonParams);
    }


    /**
     * 문의유형 채널 노드 상세내역 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnChannelNodeDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "selectRtnChannelNodeDetail", jsonParams);
    }


    /**
     * 문의유형 노드 상세내역 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnNodeDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "selectRtnNodeDetail", jsonParams);
    }


    /**
     * 하위 문의유형 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnChildrenNode(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "selectRtnChildrenNode", jsonParams);
    }


    /**
     * 문의유형 하위 노드 중 가장 마지막 정렬순서 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnInqryCdLastSortOrd(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "selectRtnInqryCdLastSortOrd", jsonParams);
    }


    /**
     * 문의유형 MERGE (insert duplicate update)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateRtnTwbTalkInqryTyp(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "updateRtnTwbTalkInqryTyp", jsonParams);
    }


    /**
     * 엑셀에 매핑된 ChatSettingInquiryTypeManageRestController에서 호출하지 않는 함수 문의유형 삭제 ( recursive )
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void updateRecursiveInqryTypUseChannel(String InqryCode, int curTalkInqryLvl, String inqryUseChannel) throws TelewebAppException
    {
        TelewebJSON childJson = selectChildInqryCode(InqryCode, curTalkInqryLvl);

        // 1. 현재 선택된 문의유형의 하위레벨 문의유형이 있는지 확인 (없을 경우, 현재 문의유형이 마지막 문의유형)
        if(childJson.getHeaderInt("TOT_COUNT") > 0) {
            for(int i = 0; i < childJson.getHeaderInt("TOT_COUNT"); i++) {
                JSONArray objArry = childJson.getDataObject();

                String pInqryCode = objArry.getJSONObject(i).getString("TALK_INQRY_CD");
                int pTalkInqryLvl = objArry.getJSONObject(i).getInt("INQRY_TYP_DIV_CD");

                updateRecursiveInqryTypUseChannel(pInqryCode, pTalkInqryLvl, inqryUseChannel);
            }
        }

        updateRtnInqryTypUseChannel(InqryCode, inqryUseChannel);
    }


    /**
     * 엑셀에 매핑된 ChatSettingInquiryTypeManageRestController에서 호출하지 않는 함수 문의유형을 삭제 처리
     * 
     * @author        kmg
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2018.11.08
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateRtnInqryTypUseChannel(String InqryCode, String inqryUseChannel) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("TALK_INQRY_CD", InqryCode);
        jsonParams.setString("INQRY_USE_CHANNEL", inqryUseChannel);

        //DAO검색 메서드 호출
        TelewebJSON objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "updateRtnInqryTypUseChannel", jsonParams);
        return objRetParams;
    }


    /**
     * 문의유형 삭제 ( recursive )
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void deleteRecursiveInqryTyp(String InqryCode, int curTalkInqryLvl, String custcoId) throws TelewebAppException
    {
        TelewebJSON childJson = new TelewebJSON();
        childJson = selectDeletingChildInqryCode(InqryCode, curTalkInqryLvl, custcoId);

        // 1. 현재 선택된 문의유형의 하위레벨 문의유형이 있는지 확인 (없을 경우, 현재 문의유형이 마지막 문의유형)
        if(childJson.getHeaderInt("TOT_COUNT") > 0) {
            for(int i = 0; i < childJson.getHeaderInt("TOT_COUNT"); i++) {
                JSONArray objArry = childJson.getDataObject();

                String pInqryCode = objArry.getJSONObject(i).getString("INQRY_TYP_CD");
                int pTalkInqryLvl = objArry.getJSONObject(i).getInt("INQRY_TYP_DIV_CD");

                deleteRecursiveInqryTyp(pInqryCode, pTalkInqryLvl, custcoId);
            }
        }
        deleteInqryTyp(InqryCode, custcoId);
    }


    /**
     * 문의유형 채널 삭제 처리
     * 
     * @author        kmg
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2018.11.08
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteRtnInqryTypChannel(TelewebJSON jsonParams) throws TelewebAppException
    {
        jsonParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
        jsonParams.setString("SNDR_KEY", jsonParams.getString("SNDR_KEY"));

        //DAO검색 메서드 호출
        TelewebJSON objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "deleteRtnInqryTypChannel", jsonParams);
        return objRetParams;
    }


    /**
     * 문의유형을 삭제 처리
     * 
     * @author        kmg
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2018.11.08
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteInqryTyp(String InqryCode, String custcoId) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("TALK_INQRY_CD", InqryCode);
        jsonParams.setString("ASP_NEWCUST_KEY", custcoId);

        //DAO검색 메서드 호출
        TelewebJSON objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "deleteRtnInqryTyp", jsonParams);
        return objRetParams;
    }
    
    /**
     * 문의유형 삭제
     * 
     * @author KTJ
     * @since  2023.06.09
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
				    rollbackFor = {Exception.class, SQLException.class},
				    readOnly = false)
    public TelewebJSON deleteQstnType(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "DELETE_QSTN_TYPE", jsonParams);
    }


    /**
     * 트리 정렬을 위해 정렬순서값 중복 방지
     * 
     * @author HYG
     * @since  2019.09.05
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnDupSortOrd(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "selectRtnDupSortOrd", jsonParams);
    }


    /**
     * 트리 정렬을 위해 정렬순서값 중복 방지
     * 
     * @author HYG
     * @since  2019.09.05
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnDupInqryTyp(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "selectRtnDupInqryTyp", jsonParams);
    }


    /**
     * 문의유형 이미지 파일키 업데이트
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2020.08.19
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateRtnInqryTypImageFileKey(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "updateRtnInqryTypImageFileKey", jsonParams);
    }


    /**
     * 문의유형 이미지 파일업로드 처리
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2020.08.19
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON uploadInqryImage(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat; //채팅팅
        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images; //문의유형 이미지지
        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        log.debug("fileProperties>>>{}", fileProperties);

        //파일키 업데이트
        updateRtnInqryTypImageFileKey(jsonParams);

        return objRetParams;
    }


    /**
     * 문의유형 관리화면 저장
     * 
     * @param  inHashMap
     * @return           objRetParams
     * @since            2020.09.07
     * @author           R&D
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON processRtnTwbTalkInqryTyp(TelewebJSON mjsonParams) throws TelewebAppException
    {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다..

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //HYG 20190905 :: 트리 정렬을 위해 정렬순서값 중복 방지
        TelewebJSON obj = this.selectRtnDupSortOrd(jsonParams);
        if(obj.getHeaderInt("TOT_COUNT") > 0) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "같은 위치의 문의유형은 정렬순서가 중복될 수 없습니다.");
        }
        else {
            if(jsonParams.getString("TRANS_STATUS").equals(TwbCmmnConst.TRANS_INS)) {
                TelewebJSON objTempParams = this.selectRtnDupInqryTyp(jsonParams);
                if(objTempParams.getHeaderInt("TOT_COUNT") > 0) {
                    objRetParams.setHeader("ERROR_FLAG", true);
                    objRetParams.setHeader("ERROR_MSG", "해당 상담유형코드가 이미 존재합니다.");
                }
                else {
                    //문의유형 신규코드 생성
                    String strNewInqryTypCd = innbCreatCmmnService.getSeqNo(jsonParams, "INQRY");
                    jsonParams.setString("INQRY_TYP_CD", strNewInqryTypCd);

                    //문의유형 저장
                    this.updateRtnTwbTalkInqryTyp(jsonParams);
                    objRetParams.setString("INQRY_TYP_CD", strNewInqryTypCd);

                    //파일처리
                    objRetParams.setDataObject("FILE_INFO", this.uploadInqryImage(mjsonParams).getDataObject("FILE_INFO"));
                }
            }
            else {
                //문의유형 저장
                this.updateRtnTwbTalkInqryTyp(jsonParams);
                objRetParams.setString("INQRY_TYP_CD", jsonParams.getString("INQRY_TYP_CD"));

                //파일처리
                objRetParams.setDataObject("FILE_INFO", this.uploadInqryImage(mjsonParams).getDataObject("FILE_INFO"));
            }
        }

        return objRetParams;
    }


    /**
     * 하위 문의유형 저장
     * 
     * @param  inHashMap
     * @return           objRetParams
     * @since            2020.09.07
     * @author           R&D
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON processRtnTwbTalkInqryTypChild(TelewebJSON mjsonParams) throws TelewebAppException
    {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다..

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        if(jsonParams.getString("INQRY_TYP_CD") == null || jsonParams.getString("INQRY_TYP_CD").isEmpty()) {
            //문의유형 신규코드 생성
            String strNewInqryTypCd = innbCreatCmmnService.getSeqNo(jsonParams, "INQRY");
            jsonParams.setString("INQRY_TYP_CD", strNewInqryTypCd);
        }

        //문의유형 저장
        this.updateRtnTwbTalkInqryTyp(jsonParams);
        objRetParams.setString("INQRY_TYP_CD", jsonParams.getString("INQRY_TYP_CD"));

        return objRetParams;
    }


    /**
     * 엑셀에 매핑된 ChatSettingInquiryTypeManageRestController에서 호출하지 않는 함수 하위레벨의 문의유형을 가지고온다.
     * 
     * @author        kmg
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2018.11.08
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectChildInqryCode(String InqryCode, int curTalkInqryLvl) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("TALK_INQRY_CD", InqryCode);
        jsonParams.setInt("INQRY_TYP_DIV_CD", curTalkInqryLvl);

        //DAO검색 메서드 호출
        TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectChildInqryCode", jsonParams);
        return objRetParams;
    }


    /**
     * 하위레벨의 문의유형을 가지고온다. (삭제할 대상)
     * 
     * @author        kmg
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2018.11.08
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectDeletingChildInqryCode(String InqryCode, int curTalkInqryLvl, String custcoId) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("TALK_INQRY_CD", InqryCode);
        jsonParams.setInt("INQRY_TYP_DIV_CD", curTalkInqryLvl + 1);
        jsonParams.setString("CUSTCO_ID", custcoId);

        //DAO검색 메서드 호출
        TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectDeletingChildInqryCode", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTwbTalkInqryTypTreeClear(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "selectRtnTwbTalkInqryTypTreeClear", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnInqryCd(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "selectRtnInqryCd", jsonParams);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON processRtnInqryType(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);      //DB Access 파라메터 생성
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //하위 문의유형 저장
        JSONArray jsonArray = mjsonParams.getDataObject("QSTN_INFO");
        JSONArray returnJsonArray = new JSONArray();
        if(jsonArray != null) {
            int intQstnSize = jsonArray.size();
            for(int i = 0; i < intQstnSize; i++) {
                TelewebJSON jsonQstnParams = new TelewebJSON(mjsonParams);
                jsonQstnParams.setString("CUSTCO_ID", mjsonParams.getString("CUSTCO_ID"));					//고객사ID
                jsonQstnParams.setString("USER_ID", mjsonParams.getString("USER_ID"));						//사용자명
                jsonQstnParams.setString("SORT_ORD", Integer.toString(i+1));								//정렬순서

                jsonQstnParams.setString("QSTN_TYPE_ID", mjsonParams.getString("QSTN_TYPE_ID"));			//문의유형ID
                jsonQstnParams.setString("QSTN_TYPE_SE_CD", mjsonParams.getString("QSTN_TYPE_SE_CD"));		//문의유형 레벨
                jsonQstnParams.setString("SNDR_KEY", mjsonParams.getString("SNDR_KEY"));					//문의유형 사용 채널
                jsonQstnParams.setString("FILE_GROUP_KEY", mjsonParams.getString("FILE_GROUP_KEY"));		//문의유형 이미지파일
                jsonQstnParams.setString("USE_YN", mjsonParams.getString("USE_YN"));			//문의유형ID
                jsonQstnParams.setString("UNDER_QSTN_TYPE_ID", jsonArray.getJSONObject(i).getString("QSTN_TYPE_ID"));	//하위문의유형ID
                jsonQstnParams.setString("UNDER_QSTN_TYPE_NM", jsonArray.getJSONObject(i).getString("QSTN_TYPE_NM"));	//하위문의유형명
                jsonQstnParams.setString("UNDER_QSTN_TYPE_CD", jsonArray.getJSONObject(i).getString("QSTN_TYPE_CD"));	//하위문의유형타입
                jsonQstnParams.setString("UNDER_QSTN_TYPE_EXPLN", jsonArray.getJSONObject(i).getString("QSTN_TYPE_EXPLN"));	//하위문의유형메시지

                String qstnTypeId = jsonQstnParams.getString("UNDER_QSTN_TYPE_ID");
                if(qstnTypeId.contains("NEW")) {
                	qstnTypeId = Integer.toString(innbCreatCmmnService.createSeqNo("QSTN_TYPE_ID"));
                	jsonQstnParams.setString("UNDER_QSTN_TYPE_ID", qstnTypeId);
                    //추가
                	returnJsonArray.add(i, mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "INSERT_QSTN_TYPE_ID", jsonQstnParams));
                }
                else {
                    //수정
                	returnJsonArray.add(i, mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "UPDATE_QSTN_TYPE_ID", jsonQstnParams));
                }
            }
            objRetParams.setDataObject("QSTN_INFO", returnJsonArray);
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnNodeDetailMst(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);      //DB Access 파라메터 생성
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //시스템메시지 정보 조회 (메시지 없을 경우 자동 세팅할 메시지)
        objRetParams.setDataObject("INQRY_DEFAULT_MSG", chatSettingMessageManageService.selectRtnSystemMsgByMsgID(jsonParams));

        //문의유형 상세정보 조회
        objRetParams.setDataObject("INQRY_DETAIL", selectRtnNodeDetail(jsonParams));

        //하위 문의유형 정보 조회
        objRetParams.setDataObject("INQRY_CHILDREN", selectRtnChildrenNode(jsonParams));

        return objRetParams;    //최종결과값 반환
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnChannelNodeDetailMst(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);      //DB Access 파라메터 생성
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //시스템메시지 정보 조회 (메시지 없을 경우 자동 세팅할 메시지)
        objRetParams.setDataObject("INQRY_DEFAULT_MSG", chatSettingMessageManageService.selectRtnSystemMsgByMsgID(jsonParams));

        //문의유형 상세정보 조회
        objRetParams.setDataObject("INQRY_DETAIL", selectRtnChannelNodeDetail(jsonParams));

        //하위 문의유형 정보 조회
        objRetParams.setDataObject("INQRY_CHILDREN", selectRtnChildrenNode(jsonParams));

        return objRetParams;    //최종결과값 반환
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectInfoMsg(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "selectInfoMsg", mjsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON infoMsgRegist(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        
        String qstnTypeId = mjsonParams.getString("QSTN_TYPE_ID");
        if(qstnTypeId.equals("")) {
        	mjsonParams.setString("QSTN_TYPE_ID", Integer.toString(innbCreatCmmnService.createSeqNo("QSTN_TYPE_ID")));
            //추가
        	objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "infoMsgRegist", mjsonParams);
        }
        else {
            //수정
        	objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingInquiryTypeManageMapper", "infoMsgUpdate", mjsonParams);
        }
        return objRetParams;
    }
}
