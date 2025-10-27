package kr.co.hkcloud.palette3.phone.sms.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneSmsManageService
{
    /**
     * SMS트리 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    TelewebJSON selectRtnSmsTree(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * SMS리스트 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    TelewebJSON selectMainSmsList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * SMS트리 리스트 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    TelewebJSON selectRtnSmsList(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * MMS 업로드 파일 목록 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    TelewebJSON selectMmsUploadFileList(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * SMS템플릿 분류 추가 가능 여부 조회
     * 
     * @param  jsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     * @throws TelewebAppException 
     */
//    TelewebJSON isRegTmplClsf(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * SMS템플릿 분류 추가
     * 
     * @param  jsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     * @throws TelewebAppException 
     */
//    TelewebJSON insertTmplClsf(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * SMS템플릿 하위 모든 요소 조회
     * 
     * @param  jsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     * @throws TelewebAppException 
     */
//    TelewebJSON selectAllChildOfTmplClsf(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * SMS템플릿 분류 삭제
     * 
     * @param  jsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     * @throws TelewebAppException 
     */
//    TelewebJSON deleteTmplClsf(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * mms 그룹키 업데이트
     * 
     * @param  jsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     * @throws TelewebAppException 
     */
//    TelewebJSON fileKeyUnity(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * 템플릿 유형 Tree 순서 변경
     * 
     * @param  jsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     * @throws TelewebAppException 
     */
//    TelewebJSON cuttTypeOrderUpdate(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 템플릿 유형 Tree 순서 변경
     * 
     * @param  jsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     * @throws TelewebAppException 
     */
//    TelewebJSON deleteSmsTmpl(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 템플릿 첨부파일 단일 삭제
     * 
     * @param  jsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     * @throws TelewebAppException 
     */
//    TelewebJSON deleteSmsTmplFileByFileKey(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * SMS상세조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    TelewebJSON selectRtnSmsDetail(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * SMS하위리스트조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    TelewebJSON selectRtnLowSms(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * SMS 저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    TelewebJSON insertSmsTmpl(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * SMS 저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    TelewebJSON modifySmsTmpl(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * SMS 업데이트
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    TelewebJSON updateRtnSmsMng(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * SMS정보 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    TelewebJSON deleteRtnSmsMng(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 문자발송
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
//    TelewebJSON sendSMS(TelewebJSON jsonParams) throws TelewebAppException;  
    
    /**
     * 문자 다건발송
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
//    TelewebJSON multiSendSMS(TelewebJSON jsonParams) throws TelewebAppException;   
    
    /**
     * 문자 다건발송 조회
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
//    TelewebJSON multiSendSMSInq(TelewebJSON jsonParams) throws TelewebAppException;   
    
    /**
     * 문자 템플릿 조회
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
//    TelewebJSON SMSTmpInq(TelewebJSON jsonParams) throws TelewebAppException;   
    
    /**
     * 발송문자 이력조회
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
//    TelewebJSON SMSInq(TelewebJSON jsonParams) throws TelewebAppException;
    

    /**
     * sms다건발송
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
//    TelewebJSON SMSMultiSend(TelewebJSON jsonParams) throws TelewebAppException;

//	TelewebJSON selectParentPath(TelewebJSON mjsonParams) throws TelewebAppException;
}
