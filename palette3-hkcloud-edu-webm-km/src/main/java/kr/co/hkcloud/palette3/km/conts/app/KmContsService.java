package kr.co.hkcloud.palette3.km.conts.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface KmContsService {

    TelewebJSON selectContentList(TelewebJSON mjsonParams) throws TelewebAppException;          // 컨텐츠 목록

    TelewebJSON selectContent(TelewebJSON mjsonParams) throws TelewebAppException;              // 컨텐츠 단건 조회

    TelewebJSON mergeContent(TelewebJSON mjsonParams) throws TelewebAppException;               // 컨텐츠 저장 수정
    
    TelewebJSON updateContent(TelewebJSON mjsonParams) throws TelewebAppException;              // 컨텐츠 수정

    TelewebJSON contsProcStts(TelewebJSON mjsonParams) throws TelewebAppException;              // 컨텐츠 상태 수정

    TelewebJSON kmContsApprManageList(TelewebJSON mjsonParams) throws TelewebAppException;      // 컨텐츠 승인관리 목록
    
    TelewebJSON updateContentSortOrd(TelewebJSON mjsonParams) throws TelewebAppException;       // 지식 콘텐츠 표시 순서 재정렬
    
    TelewebJSON selectReviewHistoryList(TelewebJSON mjsonParams) throws TelewebAppException;       // 지식 콘텐츠 검토 이력 목록
    
    TelewebJSON selectNewSortOrd(TelewebJSON mjsonParams) throws TelewebAppException;       // 새 표시 순서 값 조회
}
