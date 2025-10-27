package kr.co.hkcloud.palette3.common.innb.util;


import java.util.UUID;

import org.springframework.stereotype.Component;


/**
 * 고유번호 생성 유틸
 * 
 * @author leeiy
 *
 */
@Component
public class InnbCreateUtils
{
    /**
     * UUID 생성 (하이픈 없음)
     * 
     * @return
     */
    public String getUUID()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
