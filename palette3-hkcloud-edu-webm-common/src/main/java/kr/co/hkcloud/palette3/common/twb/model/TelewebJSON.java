package kr.co.hkcloud.palette3.common.twb.model;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import kr.co.hkcloud.palette3.exception.teleweb.TelewebException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


@Slf4j
public class TelewebJSON implements Serializable, Cloneable
{
    /**
     *
     */
    private static final long serialVersionUID = 2767845304060655535L;

    private final JSONObject jsonSuper;

    /**
     * 특정값을 NULL 처리할수 있는 타입
     */
    //public final Object SQL_TYPE_NULL = null;//JSONNull.getInstance();


    /**
     * TelewebJSON Super 객체
     */
    public JSONObject getJsonSuper()
    {
        return this.jsonSuper;
    }


    /**
     * JSON 데이터를 추가 수정 할 수 있도록 JSON객체를 생성 시킨다.
     */
    public TelewebJSON()
    {
        jsonSuper = new JSONObject();
        JSONObject jsonHeader = new JSONObject();

        jsonSuper.put("HEADER", jsonHeader);
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObj = new JSONObject();
        jsonArray.add(0, jsonObj);
        jsonSuper.put("DATA", jsonArray);
    }


    /**
     * TelewebJSON을 생성하면서 이전에 있던 JSON HEADER를 복사한다.
     */
    public TelewebJSON(TelewebJSON json)
    {
        jsonSuper = new JSONObject();

        TelewebJSON copy = new TelewebJSON();
        copy.jsonSuper.put("HEADER", json.jsonSuper.get("HEADER"));

        try
        {
            copy = (TelewebJSON) copy.clone();
        } catch(Exception e)
        {
        }

        jsonSuper.put("HEADER", copy.jsonSuper.get("HEADER"));
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObj = new JSONObject();
        jsonArray.add(0, jsonObj);
        jsonSuper.put("DATA", jsonArray);
    }


    /**
     * <code>String</code> 을 JSON 형태로 변환 시킨다.
     *
     * @param jsonText json 형태의 String 값
     */
    public TelewebJSON(String jsonText)
    {
        jsonSuper = (JSONObject) JSONSerializer.toJSON(jsonText);
    }


    /**
     * 키를 포함하고 있는지 체크한다.
     *
     * @param key
     * @return
     */
    public boolean containsKey(String key)
    {
        return containsKey(key, "DATA");
    }


    /**
     * 키를 포함하고 있는지 체크한다.
     *
     * @param key
     * @return
     */
    public boolean containsKey(String key, String group)
    {
        if(jsonSuper.get(group) == null)
        {
            return false;
        }
        return ((JSONObject) ((JSONArray) jsonSuper.get(group)).get(0)).containsKey(key);
    }


    /**
     * 헤더부에 키를 포함하고 있는지 체크한다.
     *
     * @param key
     * @return
     */
    public boolean containsHeaderKey(String key)
    {
        if(jsonSuper.get("HEADER") == null)
        {
            return false;
        }

        return ((JSONObject) jsonSuper.get("HEADER")).containsKey(key);
    }


    /**
     * 해당 key의 object를 제거한다.
     *
     * @param key
     */
    public void remove(String key)
    {
        remove("DATA", key);
    }


    /**
     * 해당 그룹의 key를 제거한다.
     *
     * @param group
     * @param key
     */
    public void remove(String group, String key)
    {

        JSONArray objArray = (JSONArray) jsonSuper.get(group);
        for(int i = 0; i < objArray.size(); i++)
        {
            ((JSONObject) objArray.get(i)).remove(key);
        }

    }


    /**
     * 해당 그룹의 key를 제거한다.
     *
     * @param group
     * @param key
     */
    public void removeHeader(String key)
    {

        JSONObject jsonHeader = (JSONObject) jsonSuper.get("HEADER");
        jsonHeader.remove(key);

    }


    /**
     * JSON 헤더 정보를 저장 시킨다.
     *
     * @param key   입력 key 값
     * @param value key 의 입력 값
     */
    public void setHeader(String key, String value)
    {
        ((JSONObject) jsonSuper.get("HEADER")).put(key, value);
    }


    /**
     * JSON 헤더 정보를 저장한다
     *
     * @param key
     * @param b
     */
    public void setHeader(String key, boolean b)
    {
        ((JSONObject) jsonSuper.get("HEADER")).put(key, b);
    }


    /**
     * JSON 헤더 정보를 저장한다.
     *
     * @param key
     * @param i
     */
    public void setHeader(String key, int i)
    {
        ((JSONObject) jsonSuper.get("HEADER")).put(key, i);
    }


    /**
     * JSON 헤더 정보를 리턴 시킨다.
     *
     * @param key 헤더 정보 중 찾고자 하는 key 값
     */
    public String getHeaderString(String key)
    {
        String r = "";

        r = ((JSONObject) jsonSuper.get("HEADER")).getString(key);

        return r;
    }


    /**
     * JSON 헤더 정보를 리턴한다.
     *
     * @param key 헤더 정보 중 찾고자 하는 KEY 값
     * @return
     */
    public boolean getHeaderBoolean(String key)
    {
        boolean r = false;

        r = ((JSONObject) jsonSuper.get("HEADER")).getBoolean(key);

        return r;
    }


    /**
     * JSON 헤더 정보를 리턴한다.
     *
     * @param key 헤더 정보 중 찾고자 하는 KEY 값
     * @return
     */
    public int getHeaderInt(String key)
    {
        int r = 0;

        try
        {
            r = ((JSONObject) jsonSuper.get("HEADER")).getInt(key);
        } catch(Exception e)
        {
            r = 0;
        }

        return r;
    }


    /**
     * int 정보를 저장 시킨다.
     *
     * @param key   저장 하고자 하는 key 값
     * @param index 저장 하고자 하는 key 의 위치
     * @param value 저장 하고자 하는 key 의 value
     */
    public void setInt(String key, int value)
    {
        // setInt(key,0,value);
        setObject(key, 0, value);
    }


    /**
     * int 정보를 저장한다.
     *
     * @param key
     * @param index
     * @param value
     */
    public void setInt(String key, int index, int value)
    {
        setObject(key, index, value);
    }


    /**
     * boolean 데이터 정보를 저장한다.
     *
     * @param key
     * @param index
     * @param value
     */
    public void setBoolean(String key, int index, boolean value)
    {
        setObject(key, index, value);
    }


    /**
     * long 데이터 정보를 저장한다.
     *
     * @param key
     * @param index
     * @param value
     */
    public void setLong(String key, int index, Long value)
    {
        setObject(key, index, value);
    }


    /**
     * double 데이터 정보를 저장한다.
     *
     * @param key
     * @param index
     * @param value
     */
    public void setDouble(String key, int index, Double value)
    {
        setObject(key, index, value);
    }


    /**
     * string 데이터 정보를 저장한다.
     *
     * @param key
     * @param index
     * @param value
     */
    public void setString(String key, String value)
    {
        setString(key, 0, value);
    }


    /**
     * string 데이터 정보를 저장한다.
     *
     * @param key
     * @param index
     * @param value
     */
    public void setString(String key, int index, String value)
    {
        setObject(key, index, value);
    }


    /**
     * string 데이터 정보를 저장한다.
     *
     * @param key
     * @param index
     * @param value
     * @param group
     */
    public void setString(String key, int index, String value, String group)
    {
        setObject(key, index, value, group);
    }


    /**
     * object 형태로 정보를 저장한다.
     *
     * @param key
     * @param index
     * @param value
     */
    public void setObject(String key, int index, Object value)
    {
        setObject(key, index, value, "DATA");
    }


    public void setObject(String key, int index, Object value, String group)
    {

        if(jsonSuper.get(group) == null)
        {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            jsonArray.add(0, jsonObj);
            jsonSuper.put(group, jsonArray);
        }

        if(((JSONArray) jsonSuper.get(group)).size() > index)
        {
            ((JSONObject) ((JSONArray) jsonSuper.get(group)).get(index)).put(key, value);

        } else
        {
            JSONObject jsonObj = new JSONObject();
            int _size = ((JSONArray) jsonSuper.get(group)).size();

            for(int i = _size; i < index + 1; i++)
            {
                ((JSONArray) jsonSuper.get(group)).add(i, jsonObj);
            }

            ((JSONObject) ((JSONArray) jsonSuper.get(group)).get(index)).put(key, value);

        }

    }


    /**
     * String 데이터 정보를 리턴 시킨다.
     *
     * @param key 데이터 정보 중 찾고자 하는 key 값
     * @return <code>String<code> key value 을 리턴 시킨다.
     */
    public String getString(String key)
    {
        return getString(key, 0, "");
    }


    /**
     * String 데이터 정보를 리턴 시킨다.
     *
     * @param key   데이터 정보 중 찾고자 하는 key 값
     * @param index 데이터 정보 중 찾고자 하는 key index 값
     * @return <code>String<code> key value 을 리턴 시킨다.
     */
    public String getString(String key, int index)
    {
        return getString(key, index, "");
    }


    /**
     * String 데이터 정보를 리턴 시킨다.
     *
     * @param key   데이터 정보 중 찾고자 하는 key 값
     * @param index 데이터 정보 중 찾고자 하는 key index 값
     * @param value 데이터 정보 중 찾고자 하는 key의 값이 없을 경우 리턴 값
     * @return <code>String<code> key value 을 리턴 시킨다.
     */
    public String getString(String key, int index, String value)
    {

        try
        {
            if(((JSONArray) jsonSuper.get("DATA")).get(index) != null && ((JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index)).getString(key) != null)
            {
                value = ((JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index)).getString(key);
            }
        } catch(net.sf.json.JSONException e)
        {
            value = "";
        }

        return value;
    }

    public Object getObject(String key)
    {
        return getObject(key, 0);
    }

    public Object getObject(String key, int index)
    {
        Object retObj = null;
        try
        {
            if(((JSONArray) jsonSuper.get("DATA")).get(index) != null && ((JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index)).getString(key) != null)
            {
                retObj = ((JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index)).get(key);
            }
        } catch(net.sf.json.JSONException e)
        {
            return null;
        }

        return retObj;
    }


    /**
     * string 데이터 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @param index
     * @return
     */
    public String getString(String group, String key, int index)
    {
        return getString(group, key, index, "");
    }


    /**
     * string 데이터 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @param index
     * @param value
     * @return
     */
    public String getString(String group, String key, int index, String value)
    {

        if(((JSONArray) jsonSuper.get(group)).get(index) != null && ((JSONObject) ((JSONArray) jsonSuper.get(group)).get(index)).getString(key) != null)
        {
            value = ((JSONObject) ((JSONArray) jsonSuper.get(group)).get(index)).getString(key);
        }

        return value;
    }


    /**
     * int 데이터 정보를 리턴 시킨다.
     *
     * @param key 데이터 정보 중 찾고자 하는 key 값
     * @return <code>int<code> key value 을 리턴 시킨다.
     */
    public int getInt(String key)
    {
        return getInt(key, 0, 0);
    }


    /**
     * int 데이터 정보를 리턴 시킨다.
     *
     * @param key   데이터 정보 중 찾고자 하는 key 값
     * @param index 데이터 정보 중 찾고자 하는 key index 값
     * @return <code>int<code> key value 을 리턴 시킨다.
     */
    public int getInt(String key, int index)
    {
        return getInt(key, index, 0);
    }


    /**
     * int 데이터 정보를 리턴 시킨다.
     *
     * @param key   데이터 정보 중 찾고자 하는 key 값
     * @param index 데이터 정보 중 찾고자 하는 key index 값
     * @param value 데이터 정보 중 찾고자 하는 key 의 값이 없을 경우 리턴 값
     * @return <code>int<code> key value 을 리턴 시킨다.
     */
    public int getInt(String key, int index, int value)
    {

        if(((JSONArray) jsonSuper.get("DATA")).get(index) != null)
        {
            value = ((JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index)).getInt(key);
        }

        return value;
    }


    /**
     * int 데이터 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @return
     */
    public int getInt(String group, String key)
    {
        return getInt(group, key, 0, 0);
    }


    /**
     * int 데이터 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @param index
     * @return
     */
    public int getInt(String group, String key, int index)
    {
        return getInt(group, key, index, 0);
    }


    /**
     * int 데이터 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @param index
     * @param value
     * @return
     */
    public int getInt(String group, String key, int index, int value)
    {

        if(((JSONArray) jsonSuper.get(group)).get(index) != null)
        {
            value = ((JSONObject) ((JSONArray) jsonSuper.get(group)).get(index)).getInt(key);
        }

        return value;
    }


    /**
     * Long 데이터 정보를 리턴 시킨다.
     *
     * @param key 데이터 정보 중 찾고자 하는 key 값
     * @return <code>long<code> key value 을 리턴 시킨다.
     */
    public long getLong(String key)
    {
        return getLong(key, 0, 0);
    }


    /**
     * Long 데이터 정보를 리턴 시킨다.
     *
     * @param key   데이터 정보 중 찾고자 하는 key 값
     * @param index 데이터 정보 중 찾고자 하는 key index 값
     * @return <code>long<code> key value 을 리턴 시킨다.
     */
    public long getLong(String key, int index)
    {
        return getInt(key, index, 0);
    }


    /**
     * Long 데이터 정보를 리턴 시킨다.
     *
     * @param key   데이터 정보 중 찾고자 하는 key 값
     * @param index 데이터 정보 중 찾고자 하는 key index 값
     * @param value 데이터 정보 중 찾고자 하는 key 의 값이 없을 경우 리턴 값
     * @return <code>long<code> key value 을 리턴 시킨다.
     */
    public long getLong(String key, int index, long value)
    {

        if(((JSONArray) jsonSuper.get("DATA")).get(index) != null)
        {
            value = ((JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index)).getLong(key);
        }

        return value;
    }


    /**
     * long 데이터 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @return
     */
    public long getLong(String group, String key)
    {
        return getLong(group, key, 0, 0);
    }


    /**
     * long 데이터 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @param index
     * @return
     */
    public long getLong(String group, String key, int index)
    {
        return getInt(group, key, index, 0);
    }


    /**
     * long 데이터 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @param index
     * @param value
     * @return
     */
    public long getLong(String group, String key, int index, long value)
    {

        if(((JSONArray) jsonSuper.get(group)).get(index) != null)
        {
            value = ((JSONObject) ((JSONArray) jsonSuper.get(group)).get(index)).getLong(key);
        }

        return value;
    }


    /**
     * float 데이터 정보를 리턴 시킨다.
     *
     * @param key 데이터 정보 중 찾고자 하는 key 값
     * @return <code>long<code> key value 을 리턴 시킨다.
     */
    public float getFloat(String key)
    {
        return getFloat(key, 0, 0);
    }


    /**
     * float 데이터 정보를 리턴 시킨다.
     *
     * @param key   데이터 정보 중 찾고자 하는 key 값
     * @param index 데이터 정보 중 찾고자 하는 key index 값
     * @return <code>long<code> key value 을 리턴 시킨다.
     */
    public float getFloat(String key, int index)
    {
        return getInt(key, index, 0);
    }


    /**
     * float 데이터 정보를 리턴 시킨다.
     *
     * @param key   데이터 정보 중 찾고자 하는 key 값
     * @param index 데이터 정보 중 찾고자 하는 key index 값
     * @param value 데이터 정보 중 찾고자 하는 key 의 값이 없을 경우 리턴 값
     * @return <code>long<code> key value 을 리턴 시킨다.
     */
    public float getFloat(String key, int index, long value)
    {

        if(((JSONArray) jsonSuper.get("DATA")).get(index) != null)
        {
            value = ((JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index)).getLong(key);
        }

        return value;
    }


    /**
     * double 데이터 정보를 리턴 시킨다.
     *
     * @param key 데이터 정보 중 찾고자 하는 key 값
     * @return <code>double<code> key value 을 리턴 시킨다.
     */
    public double getDouble(String key)
    {
        return getDouble(key, 0, 0);
    }


    /**
     * double 데이터 정보를 리턴 시킨다.
     *
     * @param key   데이터 정보 중 찾고자 하는 key 값
     * @param index 데이터 정보 중 찾고자 하는 key index 값
     * @return <code>double<code> key value 을 리턴 시킨다.
     */
    public double getDouble(String key, int index)
    {
        return getDouble(key, index, 0);
    }


    /**
     * double 데이터 정보를 리턴 시킨다.
     *
     * @param key   데이터 정보 중 찾고자 하는 key 값
     * @param index 데이터 정보 중 찾고자 하는 key index 값
     * @param value 데이터 정보 중 찾고자 하는 key 의 값이 없을 경우 리턴 값
     * @return <code>double<code> key value 을 리턴 시킨다.
     */
    public double getDouble(String key, int index, double value)
    {

        if(((JSONArray) jsonSuper.get("DATA")).get(index) != null)
        {
            value = ((JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index)).getDouble(key);
        }

        return value;
    }


    /**
     * double group의 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @return
     */
    public double getDouble(String group, String key)
    {
        return getDouble(group, key, 0, 0);
    }


    /**
     * double group의 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @param index
     * @return
     */
    public double getDouble(String group, String key, int index)
    {
        return getDouble(group, key, index, 0);
    }


    /**
     * double group의 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @param index
     * @param value
     * @return
     */
    public double getDouble(String group, String key, int index, double value)
    {

        if(((JSONArray) jsonSuper.get("DATA")).get(index) != null)
        {
            value = ((JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index)).getDouble(key);
        }

        return value;
    }


    /**
     * boolean 데이터 정보를 리턴한다.
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key)
    {
        return getBoolean(key, 0, false);
    }


    /**
     * boolean 데이터 정보를 리턴한다.
     *
     * @param key
     * @param index
     * @return
     */
    public boolean getBoolean(String key, int index)
    {
        return getBoolean(key, index, false);
    }


    /**
     * boolean 데이터 정보를 리턴한다.
     *
     * @param key
     * @param index
     * @param value
     * @return
     */
    public boolean getBoolean(String key, int index, boolean value)
    {

        if(((JSONArray) jsonSuper.get("DATA")).get(index) != null)
        {
            value = ((JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index)).getBoolean(key);
        }

        return value;
    }


    /**
     * boolean group 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @return
     */
    public boolean getBoolean(String group, String key)
    {
        return getBoolean(group, key, 0, false);
    }


    /**
     * boolean group 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @param index
     * @return
     */
    public boolean getBoolean(String group, String key, int index)
    {
        return getBoolean(group, key, index, false);
    }


    /**
     * boolean group 정보를 리턴한다.
     *
     * @param group
     * @param key
     * @param index
     * @param value
     * @return
     */
    public boolean getBoolean(String group, String key, int index, boolean value)
    {

        if(((JSONArray) jsonSuper.get(group)).get(index) != null)
        {
            value = ((JSONObject) ((JSONArray) jsonSuper.get(group)).get(index)).getBoolean(key);
        }

        return value;
    }


    /**
     * JSON 형태의 String value 반환 한다.
     *
     * @return <code>String<code> JSON 형태의 String value 반환 한다.
     */
    @Override
    public String toString()
    {
        return jsonSuper.toString();
    }


    /**
     * 데이터의 사이즈를 리턴한다.
     *
     * @return
     */
    public int getSize()
    {
        return ((JSONArray) jsonSuper.get("DATA")).size();
    }


    /**
     * group의 사이즈를 리턴한다.
     *
     * @param group
     * @return
     */
    public int getSize(String group)
    {
        return ((JSONArray) jsonSuper.get(group)).size();
    }


    /**
     * Query String을 세팅한다.
     *
     * @param sql
     */
    public void setSQL(String sql)
    {
        setSQL(sql, 0);
    }


    /**
     * Query String을 세팅한다.
     *
     * @param sql
     * @param index
     */
    public void setSQL(String sql, int index)
    {
        setString("SQL", index, sql);
    }


    /**
     * Query String을 세팅한다.
     *
     * @param sql
     */
    public void setCountSQL(String sql)
    {
        setString("COUNT_SQL", 0, sql);
    }


    /**
     * Query String를 리턴한다.
     *
     * @return
     */
    public String getSQL()
    {
        return getSQL(0);
    }


    /**
     * Query String를 리턴한다.
     *
     * @param index
     * @return
     */
    public String getSQL(int index)
    {
        return getString("SQL", index);
    }


    /**
     * Query String를 리턴한다.
     *
     * @return
     */
    public String getCountSQL()
    {
        return getString("COUNT_SQL", 0);
    }


    /**
     * Json DATA 정보만 리턴한다.
     *
     * @return DATA정보의 JSONArray
     */
    public JSONArray getDataObject()
    {
        return (JSONArray) jsonSuper.get("DATA");
    }


    /**
     * Json HEADER 정보만 리턴한다.
     *
     * @return HEADER정보의 JSONArray
     */
    public String getHeaderObject()
    {
        return jsonSuper.get("HEADER").toString();
    }


    /**
     * Json DATA 정보만 리턴한다.
     *
     * @return DATA정보의 JSONArray
     */
    public String getDataJSON()
    {
        return jsonSuper.getString("DATA");
    }


    /**
     * Json HEADER 정보만 리턴한다.
     *
     * @return HEADER정보의 JSONObject
     */
//    public String getHeaderJSON()
    public JSONObject getHeaderJSON()
    {
//        return jsonSuper.getString("HEADER");
        return jsonSuper.getJSONObject("HEADER");
    }


    /**
     * dataObjKey의 JSONArray를 리턴한다.
     *
     * @param dataObjKey
     * @return
     */
    public JSONArray getDataObject(String dataObjKey)
    {
        return (JSONArray) jsonSuper.get(dataObjKey);
    }


    /**
     * DATA부분을 put한다.
     *
     * @param dataObject 입력할 JSONArray
     */
    public void setDataObject(JSONArray dataObject)
    {
        jsonSuper.put("DATA", dataObject);
    }


    /**
     * SUB_DATA부분을 put한다. 20203031 hsi
     *
     * @param dataObject 입력할 JSONArray
     */
    public void setSubDataObject(JSONArray dataObject)
    {
        jsonSuper.put("SUB_DATA", dataObject);
    }


    /**
     * DATA부분을 put한다.
     *
     * @param dataObject 입력할 JSONArray
     */
    public void setDataObject(String dataKey, JSONArray dataObject)
    {
        jsonSuper.put(dataKey, dataObject);
    }


    /**
     * inJson의 데이터를 dataKey그룹으로 세팅한다.
     *
     * @param dataKey
     * @param inJson
     */
    public void setDataObject(String dataKey, TelewebJSON inJson)
    {
        jsonSuper.put(dataKey, inJson.getDataObject());
    }


    /**
     * @param dataKey
     * @param inJson
     */
    public void setDataObject(String dataKey, JSONObject inJson)
    {
        // jsonSuper.put(dataKey, inJson);
        JSONArray jsonArray = new JSONArray();
        // JSONObject jsonObj = new JSONObject();
        jsonArray.add(0, inJson);
        jsonSuper.put(dataKey, jsonArray);
    }


    /**
     * dataKey 그룹을 세팅한다.
     *
     * @param dataKey
     */
    public void setDataObject(String dataKey)
    {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj = new JSONObject();

        jsonArray.add(0, jsonObj);
        jsonSuper.put(dataKey, jsonArray);
    }


    /**
     * 파라메터중에 페이지관련 정보 PAGES_CNT, ROW_CNT가 존재할 경우 페이지를 계산하여 START_NUM과 END_NUM을 자동으로 설정해준다.
     *
     * @param strRowCntKeyName 클라이언트에서 전송된 페이지당 목록수를 가지고 있는 키명(Default ROW_CNT)
     */
    public void setPageData()
    {
        setPageData("ROW_CNT");
    }


    /**
     * 파라메터중에 페이지관련 정보 PAGES_CNT, ROW_CNT가 존재할 경우 페이지를 계산하여 START_NUM과 END_NUM을 자동으로 설정해준다.
     *
     * @param strRowCntKeyName
     */
    public void setPageData(String strRowCntKeyName)
    {
        // PAGES_CNT, ROW_CNT가 존재할 경우 START_NUM과 END_NUM을 계산하여 해당 키값을 자동으로 설정한다.
        if(containsHeaderKey("PAGES_CNT") && containsHeaderKey(strRowCntKeyName))
        {
            int intPagesCnt = getHeaderInt("PAGES_CNT"); // 현재페이지
            int intPagesListCnt = getHeaderInt(strRowCntKeyName); // 페이지당목록수
            setInt("START_NUM", (intPagesCnt - 1) * intPagesListCnt + 1);
            setInt("ROW_CNT", intPagesCnt * intPagesListCnt);
        }
    }


    /**
     * 빈값을 null로 치환한다.
     *
     * @param index
     */
    public void setBlankToNull(int index)
    {
        JSONObject objJson = (JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index);
        Iterator<String> objIterator = objJson.keys();
        while(objIterator.hasNext())
        {
            String strKeyName = objIterator.next();
            String strValue = objJson.getString(strKeyName);
            if(strValue.replaceAll(" ", "").equals("") || strValue == null)
            {
                objJson.put(strKeyName, "");
            }
        }

    }


    /**
     * strCol 컬럼에 있는 빈값데이터를 null로 치환한다.
     *
     * @param index
     * @param strCol
     */
    public void setBlankToNull(int index, String strCol)
    {
        String[] arrCol = strCol.split(",");

        JSONObject objJson = (JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index);
        for(int i = 0; i < arrCol.length; i++)
        {
            Iterator<String> objIterator = objJson.keys();
            while(objIterator.hasNext())
            {
                String strKeyName = objIterator.next();

                if(arrCol[i].replaceAll(" ", "").equals(strKeyName))
                {
                    String strValue = objJson.getString(strKeyName);
                    if(strValue.equals("") || strValue == null)
                    {
                        objJson.put(strKeyName, JSONNull.getInstance() );
                    }
                }
            }
        }

    }


    /**
     * 특정값이 존재할 경우 NULL로 변경
     *
     * @param index
     */
    public void setFindValueToNull(int index, String strFindValue)
    {

        JSONObject objJson = (JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index);

        Iterator<String> objIterator = objJson.keys();
        while(objIterator.hasNext())
        {
            String strKeyName = objIterator.next();
            String strValue = objJson.getString(strKeyName);
            if(strValue.equals(strFindValue))
            {
                objJson.put(strKeyName, JSONNull.getInstance());
            }
        }

    }


    /**
     * 특정값이 존재할 경우 NULL로 변경
     *
     * @param index
     * @param strCol
     * @param strFindValue
     */
    public void setFindValueToNull(int index, String strCol, String strFindValue)
    {

        String[] arrCol = strCol.split(",");

        JSONObject objJson = (JSONObject) ((JSONArray) jsonSuper.get("DATA")).get(index);
        for(int i = 0; i < arrCol.length; i++)
        {
            Iterator<String> objIterator = objJson.keys();
            while(objIterator.hasNext())
            {
                String strKeyName = objIterator.next();

                if(arrCol[i].replaceAll(" ", "").equals(strKeyName))
                {
                    String strValue = objJson.getString(strKeyName);
                    if(strValue.equals(strFindValue))
                    {
                        objJson.put(strKeyName, JSONNull.getInstance());
                    }
                }
            }
        }

    }


    /**
     * Request 객체의 파라메터 정보를 TelewebJSON으로 Convert 한다.
     *
     * @param objRequest
     * @return TelewebJSON으로 컨버트된 파라메터 객체
     */
    public TelewebJSON cnvtTelewebJson(HttpServletRequest objRequest) throws TelewebException
    {

        return cnvtTelewebJson(objRequest, true);

    }


    /**
     * Request 객체의 파라메터 정보를 TelewebJSON으로 Convert 한다.
     *
     * @param objRequest
     * @param blnCnvtXSS XSS방지를 위해 데이터 컨버전을 할지 여부
     * @return TelewebJSON으로 컨버트된 파라메터 객체
     */
    public  TelewebJSON cnvtTelewebJson(HttpServletRequest objRequest, boolean blnCnvtXSS)throws TelewebException
    {

        TelewebJSON jsonObject = new TelewebJSON();
        String strParams = objRequest.getParameter("PARAMS");
        if(strParams == null)
        {
            for(Enumeration<?> e = objRequest.getParameterNames(); e.hasMoreElements(); )
            {
                String strKey = (String) e.nextElement();
                String strVal = "";
                if(objRequest.getParameterValues(strKey) != null)
                {
                    for(int i = 0; i < objRequest.getParameterValues(strKey).length; i++)
                    {
                        if(strKey.startsWith("HEADER"))
                        {
                            try
                            {
                                jsonObject.setHeader(strKey.substring(7, strKey.length() - 1), URLDecoder.decode(objRequest.getParameterValues(strKey)[i], StandardCharsets.UTF_8.name()));
                            } catch(UnsupportedEncodingException e1)
                            {
                                throw new TelewebException(e1);
                            }
                        } else
                        {
                            int _index = strKey.indexOf("]");
                            int _indexFirst = strKey.indexOf("[");
                            strVal = objRequest.getParameterValues(strKey)[i];
                            if("[!NULL!]".equals(strVal))
                            {
                                jsonObject.setObject(strKey.substring(_index + 2, strKey.length() - 1), Integer.parseInt(strKey.substring(_indexFirst + 1, _index)), JSONNull.getInstance(), strKey.substring(0, _indexFirst));
                            } else
                            {
                                if(strVal.length() > 1)
                                {
                                    if("[".equals(strVal.substring(0, 1)) && "]".equals(strVal.substring(strVal.length() - 1)))
                                    {
                                        strVal = "&#91;" + strVal.substring(1, strVal.length() - 1) + "&#93;";
                                        jsonObject.setString(strKey.substring(_index + 2, strKey.length() - 1), Integer.parseInt(strKey.substring(_indexFirst + 1, _index)), strVal, strKey.substring(0, _indexFirst));
                                        continue;
                                    }
                                }
                                try {
                                    jsonObject.setString(strKey.substring(_index + 2, strKey.length() - 1), Integer.parseInt(strKey.substring(_indexFirst + 1, _index)), objRequest.getParameterValues(strKey)[i], strKey.substring(0, _indexFirst));
                                }catch(Exception eee){
                                    jsonObject.setString(strKey, objRequest.getParameter(strKey) );
                                }
                            }
                        }
                    }
                }
            }
        } else
        {
            try
            {
                jsonObject = new TelewebJSON(URLDecoder.decode(URLEncoder.encode(strParams, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name()));
            } catch(UnsupportedEncodingException e)
            {
                throw new TelewebException(e);
            }
        }

        // 데이터에 Cross-Site Script 존재 유무를 체크해서 변환시킨다.
        if(blnCnvtXSS)
        {
            cnvtXSS(jsonObject);
        }

        return jsonObject;

    }


    /**
     * 파라메터의 데이터그룹에 있는 모든 데이터 중 <, > 문자가 있을 경우 특수문자로 변환한다. 모든 데이터를 대상으로 해당 문자열 치환을 적용할 경우 업무관점에서 문제가 발생할 수 있으므로 DATA 그룹 파라메터만 대상으로 처리한다.
     *
     * @param objJsonParams 파라메터
     */
    public String cnvtXSS(TelewebJSON objJsonParams)
    {
        String strRetValue = "OK";
        try
        {
            JSONArray jsonArrayData = objJsonParams.getDataObject();
            for(int i = 0; i < jsonArrayData.size(); i++)
            {
                JSONObject objJson = jsonArrayData.getJSONObject(i);
                Iterator<String> objIterator = objJson.keys();
                while(objIterator.hasNext())
                {
                    String strKeyName = objIterator.next();
                    String strValue = objJson.getString(strKeyName);
                    objJson.put(strKeyName, chkXSSConstraints2(strValue));
                }
            }
        } catch(Exception e)
        {
            strRetValue = "{\"HEADER\":{\"ERROR_FLAG\":true,\"ERROR_MSG\":\"" + e.getMessage() + "\"},\"DATA\":[{}]}";
            throw e;
        }
        return strRetValue;
    }


    public String getKeyListData(TelewebJSON objJsonParams, String strKeyList) throws TelewebException
    {

        String[] objKeyList = strKeyList.split("\\|\\|");
        String strKeyListData = "";
        for(int i = 0; objKeyList.length > i; i++)
        {
            strKeyListData += objJsonParams.getString(objKeyList[i]) + "||";
        }

        /* 엔터값 암호화 공백처리 2023-05-18 he */
        strKeyListData = strKeyListData.replace("\r\n", "");
        /* 엔터값 암호화 공백처리 끝 */

        return strKeyListData.substring(0, strKeyListData.length() - 2);

    }


    /**
     * 그룹키가 존재하는지 체크
     *
     * @param dataObjKey 그룹키이름
     * @return
     */
    public boolean isExistGroupKey(String dataObjKey)
    {
        return jsonSuper.get(dataObjKey) != null;
    }


    /**
     * XSS을 방지하기 위해서 문자열에 있는 위험 특수문자를 치환한다. JSP에서 직접 파라메터를 찍을 때 해당 메서드를 이용하여 필터처리한다.
     *
     * @param key
     * @return
     */
    public String chkXSSConstraints(String strData)
    {
        strData = strData.replaceAll("&", "&amp;");
        strData = strData.replaceAll("<", "&lt;");
        strData = strData.replaceAll(">", "&gt;");
        strData = strData.replaceAll("%00", null);
        strData = strData.replaceAll("\"", "&#34;");
        strData = strData.replaceAll("'", "&#39;");
        strData = strData.replaceAll("%", "&#37;");
        strData = strData.replaceAll("../", "");
        strData = strData.replaceAll("..\\\\", "");
        strData = strData.replaceAll("./", "");
        strData = strData.replaceAll("%2F", "");
        return strData;
    }


    public String chkXSSConstraints2(String strData)
    {
        strData = strData.replaceAll("<", "&lt;");
        strData = strData.replaceAll(">", "&gt;");
        return strData;
    }
}
