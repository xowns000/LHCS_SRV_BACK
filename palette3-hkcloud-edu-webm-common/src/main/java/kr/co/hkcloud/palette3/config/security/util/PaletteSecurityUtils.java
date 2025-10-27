package kr.co.hkcloud.palette3.config.security.util;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.config.security.properties.PaletteSecurityProperties;
import kr.co.hkcloud.palette3.core.support.PaletteServletRequestSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Component
public class PaletteSecurityUtils
{
    private final PaletteSecurityProperties paletteSecurityProperties;


    /**
     * 개인정보영향평가 망분리 IP 체크
     * 
     * @param  strRequestUserIP
     * @return                  true:허용, false:불가
     */

    /**
     * 개인정보영향평가 망분리 IP 체크
     * 
     * @param  strRequestUserIP
     * @return                  true:허용, false:불가
     */
    public boolean checkVDI(HttpServletRequest req)
    {
        //접속자 IP
        final String clientIp = PaletteServletRequestSupport.getClientIp(req);
        if(StringUtils.isEmpty(clientIp)) {
            log.info("checkVDI ip null = {} >>> BLOCK", clientIp);
            return false;
        }
        //비활성
        if(!paletteSecurityProperties.getVdiIp().isEnabled()) {
            log.info("checkVDI enabled = {}, clientIp = {}  >>> PASS", paletteSecurityProperties.getVdiIp().isEnabled(), clientIp);
            return true;
        }

        //접속자 IP long 변환
        String[] clientIpDotList = clientIp.split("\\.");
        long lngClientIp = (Long.parseLong(clientIpDotList[0]) << 24) + (Long.parseLong(clientIpDotList[1]) << 16) + (Long.parseLong(clientIpDotList[2]) << 8) + (Long.parseLong(clientIpDotList[3]));

        //체크할 목록
        final List<String> vdiIpList = paletteSecurityProperties.getVdiIp().getList();
        for(String vdiIp : vdiIpList) {

            if(StringUtils.isEmpty(vdiIp)) {
                log.info("checkVDI vdiIp null = {}, clientIp = {} >>> BLOCK", vdiIp, clientIp);
                return false;
            }

            //IP 구간 체크
            //아이피 구간은 ~로 표현함
            //vdi-ip:
            //  enabled: true
            //  list:
            //    - "127.0.0.1~127.0.0.10"
            //    - "192.168.0.1~192.168.0.100"
            String[] ipSectionList = vdiIp.split("~");
            if(ipSectionList != null && ipSectionList.length == 2) {

                String[] ipSectionStartDotList = ipSectionList[0].split("\\.");
                String[] ipSectionEndDotList = ipSectionList[1].split("\\.");

                long lngIpSectionStart = (Long.parseLong(ipSectionStartDotList[0]) << 24) + (Long.parseLong(ipSectionStartDotList[1]) << 16) + (Long.parseLong(ipSectionStartDotList[2]) << 8) + (Long
                    .parseLong(ipSectionStartDotList[3]));
                long lngIpSectionEnd = (Long.parseLong(ipSectionEndDotList[0]) << 24) + (Long.parseLong(ipSectionEndDotList[1]) << 16) + (Long.parseLong(ipSectionEndDotList[2]) << 8) + (Long
                    .parseLong(ipSectionEndDotList[3]));

                if(lngClientIp >= lngIpSectionStart && lngClientIp <= lngIpSectionEnd) {
                    log.info("checkVDI section vdiIp = {}, clientIp = {} >>> PASS", vdiIp, clientIp);
                    return true;
                }
            }
            //특정 IP 체크
            else {

                String[] vdiIpDotList = vdiIp.split("\\.");
                long lngVdiIp = (Long.parseLong(vdiIpDotList[0]) << 24) + (Long.parseLong(vdiIpDotList[1]) << 16) + (Long.parseLong(vdiIpDotList[2]) << 8) + (Long.parseLong(vdiIpDotList[3]));

                if(lngClientIp == lngVdiIp) {
                    log.info("checkVDI only vdiIp = {}, clientIp = {} >>> PASS", vdiIp, clientIp);
                    return true;
                }
            }
        }
        log.info("checkVDI clientIp = {} >>> BLOCK!!!", clientIp);
        return false;
    }
}
