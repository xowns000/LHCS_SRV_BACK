package kr.co.hkcloud.palette3.admin.linkageInfo.app;

import java.util.Iterator;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.linkageInfo.app
 * fileName       :
 * author         : njy
 * date           : 2024-03-19
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-19           njy            최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("linkageInfoSettingService")
public class LinkageInfoSettingServiceImpl implements LinkageInfoSettingService {

    public JSONArray resArr = new JSONArray();
    public int nodeId = 1;

    public TelewebJSON responseParamDetailInfo(JSONObject obj) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();
        JSONObject defaultNode = new JSONObject();
        defaultNode.put("NODE_ID", "0");
        defaultNode.put("NODE_NM", "응답 결과");
        defaultNode.put("P_NODE_ID", "");
        defaultNode.put("P_NODE_NM", "");
        resArr.add(defaultNode);

        makeTree(obj, 0, "응답 결과");
        objRetParams.setDataObject("DATA", resArr);
        nodeId = 1;
        resArr = new JSONArray();
        return objRetParams;
    }

    public void makeTree(JSONObject jsonObject, int pId, String pNodeNm) {
        @SuppressWarnings("rawtypes") Iterator it = jsonObject.keys();
        while (it.hasNext()) {
            String strKey = (String) it.next();

            if (jsonObject.get(strKey) instanceof String || jsonObject.get(strKey) instanceof Integer) {
                //value가 String 또는 integer인 경우, telewebJson에 삽입
                resArr.add(makeRow(nodeId, pId, strKey, pNodeNm));
            } else if (jsonObject.get(strKey) instanceof JSONObject) {
                resArr.add(makeRow(nodeId, pId, strKey, pNodeNm));
                makeTree((JSONObject) jsonObject.get(strKey), nodeId, strKey);
                nodeId++;
            } else if (jsonObject.get(strKey) instanceof JSONArray) {
                for (int i = 0; i < ((JSONArray) jsonObject.get(strKey)).size(); i++) {
                    JSONObject obj = (JSONObject) ((JSONArray) jsonObject.get(strKey)).get(i);
                    makeTree(obj, nodeId, strKey);
                }
            } else {

            }
            nodeId++;
        }
    }

    public JSONObject makeRow(int id, int pId, String nodeNm, String pNodeNm) {
        JSONObject obj = new JSONObject();

        obj.put("NODE_ID", id);
        obj.put("NODE_NM", nodeNm);
        if (!StringUtils.isEmpty(pId)) {
            obj.put("P_NODE_ID", pId);
        } else if (id == pId) {
            obj.put("P_NODE_ID", "");
        } else {
            obj.put("P_NODE_ID", "");
        }
        if (!StringUtils.isEmpty(pNodeNm)) {
            obj.put("P_NODE_NM", pNodeNm);
        } else {
            obj.put("P_NODE_NM", "");
        }
        return obj;
    }
}
