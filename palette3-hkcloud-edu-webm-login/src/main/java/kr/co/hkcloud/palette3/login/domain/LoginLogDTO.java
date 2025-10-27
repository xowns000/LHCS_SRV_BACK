package kr.co.hkcloud.palette3.login.domain;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;


/**
 * @author Orange
 *
 */
@Setter
@Getter
public class LoginLogDTO implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 5460064871063754634L;

    private String userLogId;
    private int userId;
    private String taskSeCd;
    private String cntnIp;
    private String etcInfo01;
    private int wrtrId;

    private String prstLgnYn;
    private String pswdFailCnt;
    private int custcoId;
//    private String custcoId;
}
