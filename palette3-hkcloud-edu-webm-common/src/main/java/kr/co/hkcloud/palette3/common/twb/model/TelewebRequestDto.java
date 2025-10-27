package kr.co.hkcloud.palette3.common.twb.model;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Getter
@Setter
public class TelewebRequestDto {
    private List<Map<String, String>> DATA;
    private Map<String, String> HEADER;
    
    
    public TelewebJSON getTelewebJSON() {
        TelewebJSON retJSON = new TelewebJSON();
        JSONArray dataArr = new JSONArray();
        
        //DATA 생성
        for(Map<String, String> dataMap : DATA ) {
            JSONObject dataObj = new JSONObject();
            for( String key : dataMap.keySet() ){
                dataObj.put(key, dataMap.get(key));
            }
            dataArr.add(dataObj);
        }
        retJSON.setDataObject(dataArr);
        
        //HEADER 생성
        for( String key : HEADER.keySet() ){
            retJSON.setHeader(key, HEADER.get(key));
        }

        return retJSON;
    }
}
