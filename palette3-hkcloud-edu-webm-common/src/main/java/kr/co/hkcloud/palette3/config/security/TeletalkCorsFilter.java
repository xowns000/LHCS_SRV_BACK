package kr.co.hkcloud.palette3.config.security;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;


/**
 * TeletalkCorsFilter
 */
@Slf4j
@WebFilter
public class TeletalkCorsFilter implements Filter {

    @Value("${stomp.allow.origin}")
    public String STOMP_ALLOW_ORIGIN;

    @Value("${access.control.allow.orign}")
    private String ACCESS_CONTROL_ALLOW_ORIGN;

    /**
     * Default constructor.
     */
    public TeletalkCorsFilter() {
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        ////
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest req = (HttpServletRequest) request;
        Enumeration<String> headerNames = req.getHeaderNames();

        //log.info("ACCESS_CONTROL_ALLOW_ORIGN : {}", ACCESS_CONTROL_ALLOW_ORIGN);
        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:3003");
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = req.getHeader(headerName);
            if(Objects.equals(headerName, "Origin")) {
                if( isAllowOrign(headerValue) ) {
                    response.setHeader("Access-Control-Allow-Origin", headerValue);
                    break;
                }
            } else if(Objects.equals(headerName, "Referer")) {
                if(isAllowOrign(headerValue)) {
                    response.setHeader("Access-Control-Allow-Origin", headerValue);
                    break;
                }
            }
        }
        response.setHeader("Access-Control-Allow-Credentials", "true");
        //response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Access-Control-Max-Age", "3600");


        chain.doFilter(request, res);
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
    }

    public boolean isAllowOrign(String headerVal) {
        boolean returnVal = false;
        if(!StringUtils.isEmpty(ACCESS_CONTROL_ALLOW_ORIGN)) {
            String[] allowOrgins = ACCESS_CONTROL_ALLOW_ORIGN.split(",");
            for(String allowOrgin : allowOrgins) {
                if(headerVal.contains(allowOrgin)) {
                    returnVal = true;
                    break;
                }
            }
        }
        return returnVal;
    }
}
