package kr.co.hkcloud.palette3.chat.setting.app;


import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
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
@Service("chatSettingImageManageService")
public class ChatSettingImageManageServiceImpl implements ChatSettingImageManageService
{
    private final TwbComDAO               mobjDao;
    private final FileRulePropertiesUtils fileRulePropertiesUtils;
    private final FileDbMngService        fileDbMngService;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnImgMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        //DAO검색 메서드 호출
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingImageManageMapper", "selectRtnImgMng", jsonParams);

        // [파일o] 상담원-이미지관리-리스트: 채팅-이미지
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

                for(int i = 0; i < objRetParams.getHeaderInt("COUNT"); i++) {
                    JSONObject imgMngJsonObject = imgMngJsonArray.getJSONObject(i);

                    // @formatter:off
                    FileDbMngSelectRequest fileDbMngSelectRequest = FileDbMngSelectRequest.builder()
                                                                                          .fileGroupKey(imgMngJsonObject.getString("FILE_GROUP_KEY"))
                                                                                          .fileKey(imgMngJsonObject.getString("FILE_KEY"))
                                                                                          .build();
                    
                    FileDbMngSelectResponse fileDbMngSelectResponse = fileDbMngService.selectOnlyBlobAndExts(fileDbMngSelectRequest);
                    // @formatter:on

                    // byte[] --> Base64 변환하여 String으로 넘긴다.
                    String encFileString = Base64.getEncoder().encodeToString(fileDbMngSelectResponse.getFileBlob());
                    imgMngJsonObject.put("FILE_BLOB", encFileString);

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

        //최종결과값 반환
        return objRetParams;
    }

}
