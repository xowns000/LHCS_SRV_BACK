package kr.co.hkcloud.palette3.core.util;


import org.springframework.context.ApplicationContext;

import kr.co.hkcloud.palette3.core.support.PaletteSpringContextSupport;


/**
 * Spring boot 빈(bean) 유틸
 * 
 * @author Orange
 *
 */
public class PaletteBeanUtils
{
    /**
     * Spring boot에서 bean 가져오기
     * 
     * @param  beanClass
     * @return
     */
    public static <T> T getBean(Class<T> beanClass)
    {
        ApplicationContext applicationContext = PaletteSpringContextSupport.getApplicationContext();
        return applicationContext.getBean(beanClass);
    }
}
