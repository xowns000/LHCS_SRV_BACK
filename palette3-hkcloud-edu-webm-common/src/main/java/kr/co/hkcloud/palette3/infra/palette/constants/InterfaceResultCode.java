package kr.co.hkcloud.palette3.infra.palette.constants;


/**
 * 인터페이스 결과 리턴코드 정의
 * 
 * @author Orange
 */
public enum InterfaceResultCode
{
    SUCCESS("0", "SUCCESS"), FAIL("1", "SERVER_ERROR"), FAIL_INVAILD_PARAMETER("2", "INVALID_PARAMETER_ERROR"), NO_DATA_FOUND("99", "NO_DATA_FOUND");


    private String cd;
    private String msg;


    private InterfaceResultCode(String cd, String msg) {
        this.cd = cd;
        this.msg = msg;
    }


    public String getCd()
    {
        return cd;
    }


    public String getMsg()
    {
        return msg;
    }
}
