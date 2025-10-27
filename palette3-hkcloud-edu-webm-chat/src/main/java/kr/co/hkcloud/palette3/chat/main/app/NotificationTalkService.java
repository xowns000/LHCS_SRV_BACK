package kr.co.hkcloud.palette3.chat.main.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.core.JsonProcessingException;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public interface NotificationTalkService {

    /**
     * 알림톡 데이터 등록
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON nfTalkDataRegist(TelewebJSON mjsonParams) throws TelewebAppException, UnsupportedEncodingException, JsonProcessingException;		
    
    /**
     * 알림톡 내역 리스트
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON getAlrimList(TelewebJSON jsonParams) throws IOException;               //20220830추가
  
    /**
     * 알림톡 목록 리스트
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON getAlrimCatalog(TelewebJSON jsonParams) throws IOException;

    /**
     * 알림톡 전송
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON regAlrim(TelewebJSON jsonParams) throws IOException;

    /**
     * 알림톡 사용여부 체크
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON checkAlrim(TelewebJSON jsonParams) throws IOException;    
    
    
    /**
     * 알림톡 템플릿 등록
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON regiAlrimTmpl(TelewebJSON jsonParams) throws IOException;               //20220906추가
    
    /**
     * 알림톡 템플릿 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON inqAlrimTmpl(TelewebJSON jsonParams) throws IOException;               //20220908추가
    
    /**
     * 알림톡 템플릿 상태 업데이트
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateAlrimTmplStat(TelewebJSON jsonParams) throws IOException;               //20220913추가
    
    /**
     * 알림톡 템플릿 삭제
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON delAlrimTmpl(TelewebJSON jsonParams) throws IOException;               //20220913추가
	
    /**
     * 알림톡 다건발송
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON alrimMultiSend(TelewebJSON jsonParams) throws IOException;               //20220915추가
    
    /**
     * 알림톡 템플릿 중복체크
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cntAlrimTmpl(TelewebJSON jsonParams) throws IOException;               //20220919추가
    
    
    /**
     * 테스트
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON testest(TelewebJSON jsonParams) throws IOException;               //20220919추가
    
    /**
     * 알림톡 템플릿 카카오 메인 이미지URL
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON alrimMainImgUrl(TelewebJSON jsonParams) throws IOException;               //20221025추가
    
    /**
     * 알림톡 템플릿 카카오 하이라이트 이미지URL
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON alrimHlitImgUrl(TelewebJSON jsonParams) throws IOException;               //20221025추가
    
    /**
     * 알림톡 템플릿 카카오 하이라이트 이미지URL
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON getAtalkSchema(TelewebJSON jsonParams) throws IOException;               //20221025추가


    TelewebJSON smsDataRegist(TelewebJSON jsonParams) throws IOException;

}
