package kr.co.hkcloud.palette3.common.chat.web;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.config.security.properties.ChatSecurityProperties;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
@Api(value = "ChatViewrCmmnController",
     description = "채팅공통뷰어 컨트롤러")
public class ChatCmmnViewrController
{
    private final ChatSecurityProperties chatSecurityProperties;


    /**
     * 채팅뷰어(텍스트/이미지) 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "채팅뷰어 페이지",
                  notes = "채팅뷰어(텍스트/이미지) 페이지로 이동한다")
    @GetMapping("/chat/common/web/chat-cmmn-viewr")
    public String moveChatViewr(HttpServletRequest request, @RequestParam(required = false,
                                                                          name = "IMG_URL") final String imgUrl) throws TelewebWebException
    {
        log.debug("moveChatViewr");
        if(StringUtils.isNotBlank(imgUrl)) {

            //WEB-SER-016: 리다이렉트 기능을 이용한 피싱 공격 (화이트리스트 체크)
            boolean pass = false;
            try {
                URL urlIimg = new URL(request.getRequestURL() + imgUrl);

                log.debug("\n==========================>{}\n==========================>{}", imgUrl, urlIimg.toURI()
                    .getHost());
                for(URI whiteDomain : chatSecurityProperties.getViewerWhiteListDomain()) {
                    log.debug("\n==========================>{}\n==========================>{}", urlIimg.toURI()
                        .getHost(), whiteDomain.toString());
                    if(whiteDomain.toString().equals(urlIimg.toURI().getHost())) {
                        log.debug("\n<=========================={}\n<=========================={}", urlIimg.toURI()
                            .getHost(), whiteDomain.toString());
                        pass = true;
                        break;
                    }
                }
            }
            catch(MalformedURLException | URISyntaxException e) {

                throw new TelewebWebException(e.getLocalizedMessage());
            }
            if(!pass) { throw new TelewebWebException("접근이 거부되었습니다."); }
        }
        return "chat/common/chat-cmmn-viewr";
    }

// 뷰어 관련 사용 안하면 아래 삭제할 것
//    /**
//     * 이미지보기 페이지로 이동한다
//     * @return
//     */
//    @ApiOperation(value = "이미지보기 페이지", notes="이미지보기 페이지로 이동한다")
//    @GetMapping("/common/web/img-viewer01")
//    public String moveCommonImgViewer01() throws TelewebWebException
//    {
//        log.debug("moveCommonImgViewer01");
//        return "common/img-viewer01";
//    }
//    
//    /**
//     * 원본이미지보기 페이지로 이동한다
//     * @return
//     */
//    @ApiOperation(value = "원본이미지보기 페이지", notes="원본이미지보기 페이지로 이동한다")
//    @GetMapping("/common/web/img-viewer02")
//    public String moveCommonImgViewer02() throws TelewebWebException
//    {
//        log.debug("moveCommonImgViewer02");
//        return "common/img-viewer02";
//    }
//    
//    /**
//     * HTML보기 페이지로 이동한다
//     * @return
//     */
//    @ApiOperation(value = "HTML보기 페이지", notes="HTML보기 페이지로 이동한다")
//    @GetMapping("/common/web/html-viewer-page")
//    public String moveCommonHTMLViewerPage() throws TelewebWebException
//    {
//        log.debug("moveCommonHTMLViewerPage");
//        return "common/html-viewer-page";
//    }
}
