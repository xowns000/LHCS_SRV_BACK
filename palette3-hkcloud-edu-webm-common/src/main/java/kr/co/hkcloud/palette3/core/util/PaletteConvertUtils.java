package kr.co.hkcloud.palette3.core.util;


import org.springframework.stereotype.Component;


/**
 * 변환 유틸
 * 
 * @author Orange
 *
 */
@Component
public class PaletteConvertUtils
{
    /**
     * CamelCase to UderScore
     * 
     * @param  camelcaseName
     * @return               Underscore String
     */
    public String convertCamelcaseToUnderscoreName(String camelcaseName)
    {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";

        return camelcaseName.replaceAll(regex, replacement).toUpperCase();
    }


    /**
     * UderScore to CamelCase
     * 
     * @param  underscoreName
     * @return
     */
    public String convertUnderscoreToCamelcaseName(String underscoreName)
    {
        return org.springframework.jdbc.support.JdbcUtils.convertUnderscoreNameToPropertyName(underscoreName);
    }

}
