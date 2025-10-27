package kr.co.hkcloud.palette3.km.right.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface KmRightService
{
    TelewebJSON rightContsList(TelewebJSON mjsonParams) throws TelewebAppException;             // 컨텐츠 목록 & 최신목록 & 나의 즐겨찾기 목록 & 상세조회
    TelewebJSON rightContsTreeList(TelewebJSON mjsonParams) throws TelewebAppException;         // 컨텐츠 목록 트리 형식 조회
    TelewebJSON rightContsGuideList(TelewebJSON mjsonParams) throws TelewebAppException;        // 선택 컨텐츠의 안내 목록 조회
    TelewebJSON rightContsGuideCuttList(TelewebJSON mjsonParams) throws TelewebAppException;    // 선택 안내의 상담목록 조회
    TelewebJSON rightContsRelGuideList(TelewebJSON mjsonParams) throws TelewebAppException;     // 선택 안내의 관련 컨텐츠 목록 조회
    TelewebJSON rightContsRelLinkList(TelewebJSON mjsonParams) throws TelewebAppException;      // 선택 안내의 관련 링크 목록 조회
    TelewebJSON rightContsRelFileList(TelewebJSON mjsonParams) throws TelewebAppException;      // 선택 안내의 관련 파일 목록 조회
    TelewebJSON rightRegUserConts(TelewebJSON mjsonParams) throws TelewebAppException;          // 사용자 컨텐츠 등록 (컨텐츠 북마크 저장)
    TelewebJSON rightDelUserConts(TelewebJSON mjsonParams) throws TelewebAppException;          // 사용자 컨텐츠 삭제 (컨텐츠 북마크 삭제)
    TelewebJSON rightContsBadgeConut(TelewebJSON mjsonParams) throws TelewebAppException;       // 상당 Badge Count
    TelewebJSON searchKeyword(TelewebJSON mjsonParams) throws TelewebAppException;              //상담_안내_키워드검색
    TelewebJSON rightContsInfo(TelewebJSON mjsonParams) throws TelewebAppException;             //지식 콘텐츠 정보 조회
    TelewebJSON mergeShortcutKmsScript(TelewebJSON mjsonParams) throws TelewebAppException;     //지식스크립트 단축키 저장/수정/삭제

}
