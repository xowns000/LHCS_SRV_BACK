package kr.co.hkcloud.palette3.excel.args;


import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import kr.co.hkcloud.palette3.excel.domain.ExcelResponse.ExcelPropertiesResponse;
import kr.co.hkcloud.palette3.excel.enumer.ExcelBusiType;
import kr.co.hkcloud.palette3.excel.enumer.ExcelPathType;
import kr.co.hkcloud.palette3.excel.util.ExcelRulePropertiesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 파일 경로구분에 따른 프로퍼티
 * 
 * @author RND
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ExcelRulePropertiesArgumentResolver implements HandlerMethodArgumentResolver
{
    private final ExcelRulePropertiesUtils excelRulePropertiesUtils;


    /**
     * Method parameter에 대한 Argument Resovler로직 처리 ㄴ 파일 경로구분에 따른 프로퍼티를 함수의 인수에 자동으로 설정해 줌
     * 
     * @param  parameter
     * @param  mavContainer
     * @param  webRequest
     * @param  binderFactory
     * @return
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
    {
        @SuppressWarnings("unchecked")
        Map<String, Object> uriTemplateVars = (Map<String, Object>) webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        final ExcelBusiType taskTypeCd = ExcelBusiType.valueOf(uriTemplateVars.get("taskTypeCd").toString());
        final ExcelPathType pathTypeCd = ExcelPathType.valueOf(uriTemplateVars.get("pathTypeCd").toString());
        log.trace("taskTypeCd={}", taskTypeCd);
        log.trace("pathTypeCd={}", pathTypeCd);

        ExcelPropertiesResponse excelPropertiesResponse = excelRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        log.trace("excelPropertiesResponse={}", excelPropertiesResponse);

        return excelPropertiesResponse;
    }


    /**
     * resolveArgument를 실행 할 수 있는 method인지 판별
     * 
     * @param  methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter)
    {
        //@FileRuleProperties 어노테이션이 붙은 파라미터에 대해 적용
        //ㄴ Controller에서만 사용해야 함
        return parameter.hasParameterAnnotation(ExcelRuleProperties.class);

    }
}
