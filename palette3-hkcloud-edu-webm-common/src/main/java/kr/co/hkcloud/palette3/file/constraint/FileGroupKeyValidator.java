package kr.co.hkcloud.palette3.file.constraint;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import lombok.extern.slf4j.Slf4j;


/**
 * 파일 그룹키/키 유효성 검증
 * 
 * @author RND
 *
 */
@Slf4j
public class FileGroupKeyValidator implements ConstraintValidator<FileGroupKeyConstraint, String>
{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        //유효성 체크 : {숫자 13}-{영문소문자+숫자 32)
        //1621944635022-a460a8575b8d40e8a04395634a07510c
        // 있을 때의 유효성만 체크함.
        // ㄴ Null 체크는 @NotNull, @NotEmpty 등과 함께 체크할 것
        if(StringUtils.isBlank(value)) {
            log.trace("Validation File Group Key Success! value={}", value);
            return true;
        }

        String pattern = "^[0-9]{13}-[a-z0-9]{32}$";
        if(value.matches(pattern)) {
            log.trace("Validation File Group Key Success! value={}", value);
            return true;
        }
        log.error("Validation File Group Key Failed! value={}", value);
        return false;
    }
}
