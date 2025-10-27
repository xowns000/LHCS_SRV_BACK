package kr.co.hkcloud.palette3.config.aspect.util;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.common.twb.app.TwbUserLogBizServiceImpl;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.security.properties.PaletteSecurityProperties;
import kr.co.hkcloud.palette3.core.util.PaletteAntPathUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Component
public class AspectBizLoggerUtils
{
    private final TwbUserLogBizServiceImpl twbUserLogBizServiceImpl;

    private final PaletteSecurityProperties paletteSecurityProperties;

    private static final List<String> EXCLUDE_URI_LIST = new ArrayList<String>();


    @PostConstruct
    public void loading()
    {
        //로그 제외 페이지 ( noBizLog annotation 수정 ) 
//    	EXCLUDE_URI_LIST.add("/api/TwbMain/getRtNotice");  				// 공지사항
//    	EXCLUDE_URI_LIST.add("/api/ProjectCommon/**");  	// 공통코드
//    	EXCLUDE_URI_LIST.add("/api/twb/tables/BTN/SELECT_BTN");  		// 버튼권한
//    	EXCLUDE_URI_LIST.add("/api/TwbCommon/getJsProperty");  			// 설정코드
//    	EXCLUDE_URI_LIST.add("/api/twb/TwbBas04/**");  // 메뉴권한관련    	     
//    	EXCLUDE_URI_LIST.add("/api/TwbMain/getAllMenuListWithAuth");  	// 메뉴권한
//    	EXCLUDE_URI_LIST.add("/api/TalkNewMonitoring/**");  			// 모니터링
//    	EXCLUDE_URI_LIST.add("/main/hkcdv/web/TwbMain");  				// TwbMain
//    	EXCLUDE_URI_LIST.add("/api/TwbMain/selectRtnMenu");  			// 메뉴목록
//    	EXCLUDE_URI_LIST.add("/api/projectcommon/**");  				// 공통코드(상담메인)
//    	EXCLUDE_URI_LIST.add("/api/stomp/chat/userinfo");  				// 웹소켓 토큰 발급 (상담메인)
//    	EXCLUDE_URI_LIST.add("/api/mng/aspcust/selectRtnAspCustComboData");  // 상단 ASP 고객사
//    	EXCLUDE_URI_LIST.add("/api/TwbBas03/**");  // 상단 ASP 고객사

    }


    /**
     *
     * @throws TelewebUtilException
     */
    public void insertBeforAdviceLog(TelewebJSON jsonParams, String strAccessIP) throws TelewebUtilException
    {
        twbUserLogBizServiceImpl.insertUserBizLog(jsonParams, strAccessIP);
    }

}
