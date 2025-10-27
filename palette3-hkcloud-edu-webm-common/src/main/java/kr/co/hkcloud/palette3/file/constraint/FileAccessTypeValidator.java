package kr.co.hkcloud.palette3.file.constraint;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import kr.co.hkcloud.palette3.file.enumer.FileAccessType;
import lombok.extern.slf4j.Slf4j;


/**
 * 파일 엑세스 유형 유효성 검증
 * 
 * @author RND
 *
 */
@Slf4j
public class FileAccessTypeValidator implements ConstraintValidator<FileAccessTypeConstraint, FileAccessType>
{

    @Override
    public boolean isValid(FileAccessType value, ConstraintValidatorContext context)
    {
        //유효성 체크 : FileAccessType enum에 정의된 유형만 유효함
        if(FileAccessType.valueOf(value.name()) != null) {
            log.trace("Validation File Access Type Success! value={}", value);
            return true;
        }
        log.error("Validation File Access Type Failed! value={}", value);
        return false;
    }
}
