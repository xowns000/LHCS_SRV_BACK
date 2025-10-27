package kr.co.hkcloud.palette3.core.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import kr.co.hkcloud.palette3.exception.model.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


/**
 * 팔레트 API 응답
 * 
 * @author     leeiy
 *
 * @param  <T>
 */
@Data
@AllArgsConstructor
@Builder
public class PaletteApiResponse<T>
{
    private int    statusCode;          //상태코드
    private String responseMessage;  //응답 메시지

    @JsonInclude(Include.NON_NULL)
    private T data;                  //응답 데이터

    @JsonInclude(Include.NON_EMPTY)
    private ErrorResponse error;     //오류 상세


    public PaletteApiResponse(final int statusCode, final String responseMessage) {

        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.data = null;
        this.error = null;
    }


    public static <T> PaletteApiResponse<T> res()
    {

        return res(200, "정상", null, null);
    }


    public static <T> PaletteApiResponse<T> res(final T t)
    {

        return res(200, "정상", t, null);
    }


    public static <T> PaletteApiResponse<T> res(final int statusCode, final String responseMessage)
    {

        return res(statusCode, responseMessage, null, null);
    }


    public static <T> PaletteApiResponse<T> res(final int statusCode, final String responseMessage, final T t)
    {

        return res(statusCode, responseMessage, t, null);
    }


    public static <T> PaletteApiResponse<T> res(final ErrorResponse error)
    {

        return res(500, "내부 오류.", null, error);
    }


    public static <T> PaletteApiResponse<T> res(final ErrorResponse error, final T t)
    {

        return res(500, "내부 오류", t, error);
    }


    public static <T> PaletteApiResponse<T> res(final int statusCode, final String responseMessage, final T t, final ErrorResponse error)
    {

        return PaletteApiResponse.<T>builder().data(t).statusCode(statusCode).responseMessage(responseMessage).error(error).build();
    }
}
