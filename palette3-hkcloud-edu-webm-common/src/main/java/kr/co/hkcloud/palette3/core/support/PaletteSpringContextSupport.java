package kr.co.hkcloud.palette3.core.support;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * 스프링 컨텍스트 및 빈 관련
 * 
 * @author leeiy
 *
 */
@Component
public class PaletteSpringContextSupport implements ApplicationContextAware
{
    private static ApplicationContext context;


    /**
     * 
     * @return
     */
    public static ApplicationContext getApplicationContext()
    {
        return PaletteSpringContextSupport.context;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        PaletteSpringContextSupport.context = applicationContext;
    }


    /**
     * Returns the Spring managed bean instance of the given class type if it exists. Returns null otherwise.
     * 
     * @param  beanClass
     * @return
     */
    public static <T extends Object> T getBean(Class<T> beanClass)
    {
        return PaletteSpringContextSupport.context.getBean(beanClass);
    }

}
