package kr.co.hkcloud.palette3.core.security.authentication.domain;


import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * @author Orange
 *
 */
@Setter
@Getter
@Builder
public class TalkAuthenticationVO implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -5857539323200557850L;

    private String userId;
    private String menuId;
    private int    hasCnt;
}
