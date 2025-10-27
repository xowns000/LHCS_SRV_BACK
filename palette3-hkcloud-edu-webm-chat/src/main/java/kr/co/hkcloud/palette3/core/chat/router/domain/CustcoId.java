package kr.co.hkcloud.palette3.core.chat.router.domain;

import java.io.Serializable;

import lombok.Getter;

/**
 * 
 * @author Orange
 *
 */
@Getter
public class CustcoId implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String custKey;
    private String key;
    
    public CustcoId(String custKey, String key)
    {
        this.custKey = custKey;
        this.key = key;
    }
    
    public int hashCode()
    {
        //고객사키와 설정키가 같으면
        //항상 동일한 hashCode값을 발생시키도록 메서드들 재정의 하면
        //HashTable에 값을 저장하거나 검색할때 내부적을 사용한다.
        return custKey.hashCode() + key.hashCode();
    }
       
    //저장된 위치를 hashcode를 통해서 파악한 후
    //equals 메서드가 호출되어 진다.
    public boolean equals(Object obj)
    {
        if(!(obj instanceof CustcoId))
        {
            return false;
        }
        CustcoId custcoId = (CustcoId)obj;
        return (custKey.equals(custcoId.custKey) && key.equals(custcoId.key));
    }
}
