package kr.co.hkcloud.palette3.config.stomp.interceptors;


import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import kr.co.hkcloud.palette3.config.stomp.listeners.TalkWebSocketConstants;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class TalkStompHandshakeInterceptor implements HandshakeInterceptor
{

    /**
     * @throws IOException
     * 
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws IOException
    {
        log.trace("TalkStompHandshakeInterceptor.beforeHandshake ###");

        if(request instanceof ServletServerHttpRequest) {

            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();      //true

            if(session != null) {

                String userId = servletRequest.getServletRequest().getParameter("userId");
                String custcoId = servletRequest.getServletRequest().getParameter("custcoId");

                attributes.put(TalkWebSocketConstants.TELETALK_SESSION_ATTR, session.getId());
                attributes.put(TalkWebSocketConstants.TELETALK_ASP_USER_ID, userId);
                attributes.put(TalkWebSocketConstants.TELETALK_CUSTCO_ID, custcoId);

                //String strSessionInfo = (String) session.getAttribute("TWB_SESSION_INFO");
                ////String aspUserID = strSessionInfo.split("\\|")[0];
                ////attributes.put(TalkWebSocketConstants.TELETALK_ASP_USER_ID, aspUserID);

                ////String aspCustInfo = (String) session.getAttribute("ASP_CUST_INFO");
                ////attributes.put(TalkWebSocketConstants.TELETALK_CUSTCO_ID, aspCustInfo);

                //세션을 직접처리하고자할 때 사용할 수 있음
                //attributes.put(TalkWebSocketConstants.TELETALK_SESSION_OBJECT, session);

                //if(log.isInfoEnabled()) {
                StringBuffer sb = new StringBuffer("\n");
                sb.append("\tsession.getId()\t\t:").append(session.getId()).append("\n");
                sb.append("\tuserId\t\t:").append(userId).append("\n");
                sb.append("\tcustcoId\t\t:").append(custcoId).append("\n");
                log.debug("\n-------------------\nSTOMP TalkStompHandshakeInterceptor beforeHandshake INFORMATION\n-------------------\n{}\n", sb.toString());
                //}
            }
        }

        return true;
    }


    /**
     * 
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception)
    {
        log.trace("TalkStompHandshakeInterceptor.afterHandshake ###");
    }

}
