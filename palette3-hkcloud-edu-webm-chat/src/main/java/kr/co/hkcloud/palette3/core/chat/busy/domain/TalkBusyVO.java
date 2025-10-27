package kr.co.hkcloud.palette3.core.chat.busy.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * BUSY(상담불가) VO
 * @author User
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class TalkBusyVO implements Serializable
{
	/**
     * 
     */
    private static final long serialVersionUID = -394346562839717519L;
    
    private String systemMsgId = ""; //시스템메시지ID
}
