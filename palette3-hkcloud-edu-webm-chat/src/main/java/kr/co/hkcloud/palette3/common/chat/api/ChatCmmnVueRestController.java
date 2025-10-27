package kr.co.hkcloud.palette3.common.chat.api;


import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.core.support.PaletteUserContextSupport;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.file.app.FileDbMngService;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatVueCmmnRestController",
     description = "채팅뷰공통 REST 컨트롤러")
public class ChatCmmnVueRestController
{
    private final FileRulePropertiesUtils fileRulePropertiesUtils;
    private final InnbCreatCmmnService    innbCreatCmmnService;
    private final FileDbMngService        fileDbMngService;
    private final TwbComDAO               comDao;


    /**
     * 프레임워크에서 기본적으로 제공하는 공통코드의 코드,코드명를 검색한다.(콤보박스로드용)
     * 
     * @return 코드정보(CODE,CODE_NM)
     * @author MPC R&D Team 컬럼명 통일을 위해 컬럼명을 변경함 CODE -> GROUP_CD, CODE_NM -> CD_NM, CODE_SE -> CD_TYPE
     */
    @NoBizLog
    @ApiOperation(value = "공통코드 검색",
                  notes = "프레임워크에서 기본적으로 제공하는 공통코드의 코드,코드명를 검색한다.(콤보박스로드용)")
    @PostMapping("/chat-api/common-vue/code-book/inqry")
    public @ResponseBody Object selectRtnCodeBook(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();

        String groupCds = bodyobj.getString("GROUP_CD");

        if(groupCds != null && !("").equals(groupCds)) {
            //코드타입에 ETC 코드값 설정 시 분리
            String[] objCodeType = groupCds.split("\\|");

            if(objCodeType != null && objCodeType.length > 0) {
                List<String> groupCdList = new LinkedList<String>();
                for(int i = 0; i < objCodeType.length; i++) {
                    groupCdList.add(objCodeType[i]);
                }

                jsonParams.setObject("groupCds", 0, groupCdList);              //코드타입에 대한 키값 설정
                //objRetParams = comDao.select("com.hcteletalk.teletalk.common.dao.ProjectCommonMapper", "selectRtnCodeBooks", jsonParams);
                objRetParams = comDao.select("kr.co.hkcloud.palette3.common.code.dao.CodeCmmnMapper", "selectRtnCodeBooks", jsonParams);

            }

        }

        //반환정보 세팅
        JSONObject result = new JSONObject();
        result.put("code", "0");			// 정상
        result.put("error", "");			// error
        result.put("data", objRetParams.getDataObject());

        //최종결과값 반환
        return result;
    }


    /**
     * 상담유형정보 조회
     * 
     * @param  bodyobj
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "상담유형정보 조회",
                  notes = "상담유형 정보를 가져와 콤보를 생성한다.")
    @PostMapping("/chat-api/common-vue/cnslt-ty/inqry")
    public @ResponseBody Object selectRtnNodeDetail(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setString("ASP_NEWCUST_KEY", bodyobj.getString("ASP_NEWCUST_KEY"));
        jsonParams.setString("SPST_CNSL_TYP_CD", bodyobj.getString("SPST_CNSL_TYP_CD"));
        jsonParams.setString("USE_YN", bodyobj.getString("USE_YN"));

        objRetParams = comDao.select("kr.co.hkcloud.palette3.setting.consulttype.dao.SettingConsulttypeManageMapper", "selectRtnNodeDetail", jsonParams);

        //반환정보 세팅
        JSONObject result = new JSONObject();
        result.put("code", "0");			// 정상
        result.put("error", "");			// error
        result.put("data", objRetParams.getDataObject());

        return result;
    }


    /**
     * 전달할 에이전트 목록 검색
     * 
     * @param  bodyobj
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "전달할 사용자 목록 검색",
                  notes = "전달할 사용자 목록 검색")
    @PostMapping("/chat-api/common-vue/chaton-agents/list")
    public @ResponseBody Object selectRtnPageAgentDeliver(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        //String userId = (PaletteUserContextSupport.getCurrentUser() == null ? "test01" : PaletteUserContextSupport.getCurrentUser().getUserId());
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        String userId = bodyobj.getString("USER_ID");
        String custcoId = bodyobj.getString("CUSTCO_ID");


        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setString("SEND_USER_ID", userId);

        //대기중인 상담원 리스트
//        objRetParams = comDao.select("kr.co.hkcloud.palette3.common.chat.dao.ChatCmmnMapper", "selectCuttRdy", jsonParams);
        //모든 상담원을 다 보여달라 요청
        objRetParams = comDao.select("kr.co.hkcloud.palette3.common.chat.dao.ChatCmmnMapper", "selectAllCusl", jsonParams);

        //반환정보 세팅
        JSONObject result = new JSONObject();
        result.put("code", "0");			// 정상
        result.put("error", "");			// error
        result.put("data", objRetParams.getDataObject());

        return result;
    }


    /**
     * 이미지 리스트 조회
     * 
     * @param  headerobj
     * @param  bodyobj
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "이미지 리스트 조회",
                  notes = "이미지 리스트 조회")
    @PostMapping("/chat-api/common-vue/images/list")
    public @ResponseBody Object selectRtnImgMng(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setHeader("PAGES_CNT", bodyobj.getString("PAGES_CNT"));	//현재페이지
        jsonParams.setHeader("ROW_CNT", bodyobj.getString("ROW_CNT"));		//이미지개수
        jsonParams.setString("BRD_ID", "4");								//게시판 아이디 세팅(이미지 게시판)
        jsonParams.setString("START_DT", bodyobj.getString("START_DT"));	//시작일자
        jsonParams.setString("END_DT", bodyobj.getString("END_DT"));		//종료일자
        jsonParams.setString("BRD_TIT", bodyobj.getString("BRD_TIT"));		//종료일자
        jsonParams.setString("CUSTCO_ID", custcoId);		//종료일자
        jsonParams.setString("ASP_NEWCUST_KEY", bodyobj.getString("ASP_NEWCUST_KEY"));       //회사구분

        //DAO검색 메서드 호출
        objRetParams = comDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingImageManageMapper", "selectRtnImgMng", jsonParams);
        log.debug("*******************************************************" + objRetParams);

        // [파일o] 상담원-이미지관리-리스트(Vue): 채팅-이미지
        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지
        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        log.debug("fileProperties>>>{}", fileProperties);

        switch(fileProperties.getTrgtTypeCd())
        {
            case DB:
            {
                JSONArray jsonArray = new JSONArray();
                JSONArray imgMngJsonArray = new JSONArray();
                imgMngJsonArray = objRetParams.getDataObject(TwbCmmnConst.G_DATA);

                log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + imgMngJsonArray);
                for(int i = 0; i < objRetParams.getHeaderInt("COUNT"); i++) {
                    JSONObject imgMngJsonObject = imgMngJsonArray.getJSONObject(i);

                    // @formatter:off
                    FileDbMngSelectRequest fileDbMngSelectRequest = FileDbMngSelectRequest.builder()
                                                                                          .fileGroupKey(imgMngJsonObject.getString("FILE_GROUP_KEY"))
                                                                                          .fileKey(imgMngJsonObject.getString("FILE_KEY"))
                                                                                          .custcoId(imgMngJsonObject.getString("CUSTCO_ID"))
                                                                                          .build();
                    
                    FileDbMngSelectResponse fileDbMngSelectResponse = fileDbMngService.selectOnlyBlobAndExts(fileDbMngSelectRequest);
                    // @formatter:on

                    // byte[] --> Base64 변환하여 String으로 넘긴다.
                    String encFileString = Base64.getEncoder().encodeToString(fileDbMngSelectResponse.getFileBlob());
                    imgMngJsonObject.put("FILE_BLOB", encFileString);
                    imgMngJsonObject.put("IMAGE_TYPE", fileProperties.getTrgtTypeCd());

                    jsonArray.add(imgMngJsonObject);
                }

                objRetParams.setDataObject(TwbCmmnConst.G_DATA, jsonArray);
                break;
            }
            default:
            {
                break;
            }
        }

        /*
         * if ( "BLOB".equals(strFileUploadPath) ) { JSONArray jsonArray = new JSONArray();
         * 
         * JSONArray imgMngJsonArray = new JSONArray(); imgMngJsonArray = objRetParams.getDataObject(TwbCmmnConst.G_DATA); for(int i=0; i<objRetParams.getHeaderInt("COUNT"); i++) { JSONObject imgMngJsonObject =
         * imgMngJsonArray.getJSONObject(i); FileCmmnVO fileCmmnVO = new FileCmmnVO(); fileCmmnVO.setCustcoId(custcoId); fileCmmnVO.setFileGroupKey(imgMngJsonObject.getString("FILE_GROUP_KEY"));
         * fileCmmnVO.setFileKey(imgMngJsonObject.getString("FILE_KEY")); FileCmmnVO fileCmmnVOtmp = fileCmmnMapper.selectFileCmmnVO(fileCmmnVO); if ( fileCmmnVOtmp.getFileBlob() != null ) { // byte[] --> Base64 변환하여
         * String으로 넘긴다. String encFileString = Base64.getEncoder().encodeToString(fileCmmnVOtmp.getFileBlob()); imgMngJsonObject.put("FILE_BLOB", encFileString); imgMngJsonObject.put("IMAGE_TYPE", strFileUploadPath);
         * 
         * jsonArray.add(imgMngJsonObject); } }
         * 
         * objRetParams.setDataObject(TwbCmmnConst.G_DATA, jsonArray); }
         */

        //반환정보 세팅
        JSONObject result = new JSONObject();
        result.put("code", "0");			// 정상
        result.put("error", "");			// error
        result.put("header", objRetParams.getHeaderJSON());
        result.put("data", objRetParams.getDataObject());

        return result;
    }


    /**
     * 업무별 고유키정보를 생성하여 반환한다.
     * 
     * @return                     업무별 고유키(YYYYMMDDHHMISSMSC + 업무구분(대문자3자리) + 00000)
     * @throws TelewebApiException
     * @author                     MPC R&D Team
     */
    @NoBizLog
    @ApiOperation(value = "업무별 고유키정보를 생성",
                  notes = "업무별 고유키정보를 생성하여 반환한다.")
    @PostMapping("/chat-api/common-vue/generate-unique-no/inqry")
    public @ResponseBody Object selectBizSeq(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        String strBizCase = bodyobj.getString("BIZ_CASE");

        //고유키 정보 세팅
        objRetParams.setString("RET_VAL", innbCreatCmmnService.getSeqNo(jsonParams, strBizCase));

        //반환정보 세팅
        JSONObject result = new JSONObject();
        result.put("code", "0");			// 정상
        result.put("error", "");			// error
        result.put("data", objRetParams.getDataObject());

        return result;
    }


    /**
     * 상담설정 관련 정보
     * 
     * @return                     업무별 고유키(YYYYMMDDHHMISSMSC + 업무구분(대문자3자리) + 00000)
     * @throws TelewebApiException
     * @author                     MPC R&D Team
     */
    @NoBizLog
    @ApiOperation(value = "상담설정 관련 정보 ",
                  notes = "상담설정 관련 정보")
    @RequestMapping(value = "/chat-api/common-vue/env/inqry",
                    method = RequestMethod.POST)
    public @ResponseBody Object selectTalkEnv(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        String custcoId = bodyobj.getString("ASP_NEWCUST_KEY");

        int retryTimeOut = HcTeletalkDbEnvironment.getInstance().getString("CUSTCO_ID", "RETRY_READY_MAX_TIMEOUT") != null ? Integer
            .valueOf(HcTeletalkDbEnvironment.getInstance().getString(custcoId, "RETRY_READY_MAX_TIMEOUT")) : 0;

        objRetParams.setInt("RETRY_READY_MAX_TIMEOUT", retryTimeOut);

        // [파일o] 채팅메인-상담설정 정보 중..BLOB_YN(Vue): 채팅-이미지관리
        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지관리
        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        log.debug("fileProperties>>>{}", fileProperties);

        switch(fileProperties.getTrgtTypeCd())
        {
            case DB:
            {
                objRetParams.setString("BLOB_YN", "Y");
                break;
            }
            default:
            {
                objRetParams.setString("BLOB_YN", "N");
                break;
            }
        }

        //objRetParams.setString("BLOB_YN", "BLOB".equals(strFileUploadPath) ? "Y" : "N");

        //반환정보 세팅
        JSONObject result = new JSONObject();
        result.put("code", "0");			// 정상
        result.put("error", "");			// error
        result.put("data", objRetParams.getDataObject());

        return result;
    }

}
