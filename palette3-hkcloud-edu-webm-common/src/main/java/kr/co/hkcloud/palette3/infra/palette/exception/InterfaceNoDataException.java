package kr.co.hkcloud.palette3.infra.palette.exception;


import kr.co.hkcloud.palette3.infra.palette.constants.InterfaceResultCode;


/**
 * 인터페이스 NO_DATA 예외 확장
 * 
 * @author liy
 */
public class InterfaceNoDataException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 3674899944735695281L;

    private InterfaceResultCode interfaceResultCode;
    private String              errMsg;
    private Object[]            arguments;


    public InterfaceNoDataException() {
        super("조회된 데이터가 없습니다.");
    }


    public InterfaceNoDataException(Throwable arg0) {
        super(arg0);
    }


    public InterfaceNoDataException(String arg0) {
        super(arg0);
    }


    public InterfaceNoDataException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }


    public InterfaceNoDataException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }


    public InterfaceNoDataException(InterfaceResultCode interfaceResultCode) {
        super();
        this.interfaceResultCode = interfaceResultCode;
    }


    public InterfaceNoDataException(InterfaceResultCode interfaceResultCode, String errMsg) {
        super();
        this.interfaceResultCode = interfaceResultCode;
        this.errMsg = errMsg;
    }


    public InterfaceNoDataException(InterfaceResultCode interfaceResultCode, String errMsg, Object[] arguments) {
        super();
        this.interfaceResultCode = interfaceResultCode;
        this.errMsg = errMsg;
        this.arguments = arguments;
    }


    public InterfaceResultCode getInterfaceResultCode()
    {
        return interfaceResultCode;
    }


    public void setResult(InterfaceResultCode interfaceResultCode)
    {
        this.interfaceResultCode = interfaceResultCode;
    }


    public String getErrMsg()
    {
        return errMsg;
    }


    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }


    public Object[] getArguments()
    {
        return arguments;
    }


    public void setArguments(Object[] arguments)
    {
        this.arguments = arguments;
    }

}
