package kr.co.hkcloud.palette3.config.environment;


import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


/**
 * @author leeiy
 *
 */
@Slf4j
@Component
public class HcTeletalkEnvironment
{
    @Autowired
    Environment environment;


    /**
     * @param  key
     * @return
     */
    public String getString(String key)
    {
        String value = environment.getProperty(key, String.class);
        log.trace("{}:{}", key, value);
        return value;
    }


    /**
     * @param  key
     * @param  defaultValue
     * @return
     */
    public String getString(String key, String defaultValue)
    {
        String value = getString(key);
        if(StringUtils.isBlank(value)) {
            value = defaultValue;
            log.trace("Value is blank!! Replace defaultValue = {}:{}", key, value);
        }
        return value;
    }


    /**
     * 
     * @param  key
     * @return
     */
    public int getInt(String key)
    {
        Integer i = getInteger(key, null);
        if(i != null) {
            return i.intValue();
        }
        else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }


    /**
     * 
     * @param  key
     * @param  defaultValue
     * @return
     */
    public int getInt(String key, int defaultValue)
    {
        Integer i = getInteger(key, null);
        if(i == null) { return defaultValue; }
        return i.intValue();
    }


    /**
     * 
     * @param  key
     * @param  defaultValue
     * @return
     */
    public Integer getInteger(String key, Integer defaultValue)
    {
        Integer value = environment.getProperty(key, Integer.class);
        if(value == null) { return defaultValue; }
        return value;
    }


    /**
     * 
     * @param  key
     * @return
     */
    public long getLong(String key)
    {
        Long i = getLong(key, null);
        if(i != null) {
            return i.longValue();
        }
        else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }


    /**
     * 
     * @param  key
     * @param  defaultValue
     * @return
     */
    public long getLong(String key, long defaultValue)
    {
        Long i = getLong(key, null);
        if(i == null) { return defaultValue; }
        return i.longValue();
    }


    /**
     * 
     * @param  key
     * @param  defaultValue
     * @return
     */
    public Long getLong(String key, Long defaultValue)
    {
        Long value = environment.getProperty(key, Long.class);
        if(value == null) { return defaultValue; }
        return value;
    }
}
