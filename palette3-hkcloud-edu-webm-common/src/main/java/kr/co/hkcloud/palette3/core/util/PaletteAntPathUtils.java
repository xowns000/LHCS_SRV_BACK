package kr.co.hkcloud.palette3.core.util;


import org.springframework.util.AntPathMatcher;


/**
 * @author Orange
 *
 */
public class PaletteAntPathUtils
{
    /**
     * @param  pattern
     * @param  inputStr
     * @return
     */
    public static boolean matches(String pattern, String inputStr)
    {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return antPathMatcher.match(pattern, inputStr);
    }
}
