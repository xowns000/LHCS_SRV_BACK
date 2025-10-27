package kr.co.hkcloud.palette3.core.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PaletteJsonUtils {

    /**
     *
     * @param  json
     * @param  element
     * @param  key
     * @return
     * @throws TelewebUtilException
     */
    public String getJsonElementString(JSONObject json, String element, String key) throws TelewebUtilException {
        if (json != null && json.containsKey(element) && ((JSONObject) json.get(element)).containsKey(key)) {
            return ((JSONObject) json.get(element)).getString(key);
        }
        return "";
    }


    /**
     *
     * @param  json
     * @param  key
     * @return
     * @throws TelewebUtilException
     */
    public String getJsonString(JSONObject json, String key) throws TelewebUtilException {
        if (json != null && json.containsKey(key)) {
            return json.getString(key);
        }
        return "";
    }


    /**
     * json obj or array is true / false
     *
     * @param  json
     * @param  key
     * @return
     * @throws TelewebUtilException
     */
    public boolean chkJsonObj(String key) {
        if (key != null && key.startsWith("[") && key.endsWith("]")) {
            return true;
        } else if (key != null && key.startsWith("{") && key.endsWith("}")) {
            return true;
        } else if (key != null && key.startsWith("[{") && key.endsWith("}]")) {
            return true;
        } else if (key != null && key.startsWith("{[") && key.endsWith("]}")) {
            return true;
        }

        return false;
    }


    /**
     * // [ , { , [{ , {[ string 변환 하지 않으면 오류발생함.( object 로 인지하기때문 ) , 특수문자 등은 jsonexp 이 난다.
     *
     * @param  json
     * @param  key
     * @return
     * @throws TelewebUtilException
     */
    public String valueToStringWithoutQutoes(String str) {
        String contentStr = "";

        try {
            contentStr = JSONUtils.valueToString(str);

            if (str != null && !chkJsonObj(str)) {
                contentStr = JSONUtils.stripQuotes(contentStr);
            }

        } catch (JSONException je) {
        }

        return contentStr;
    }


    /**
     * // [ , { , [{ , {[ string 변환 하지 않으면 오류발생함.( object 로 인지하기때문 ) , 특수문자 등은 jsonexp 이 난다.
     *
     * @param  json
     * @param  key
     * @return
     * @throws TelewebUtilException
     */
    public String jsonToString(String bodyJSONString) {

        JsonObject covertedObject = new Gson().fromJson(bodyJSONString.toString(), JsonObject.class);

        if (covertedObject != null && covertedObject.has("message") && chkJsonObj(covertedObject.get("message").toString())) {
            covertedObject.addProperty("message", covertedObject.get("message").toString());
        } else if (covertedObject != null && covertedObject.has("MESSAGE") && chkJsonObj(covertedObject.get("MESSAGE").toString())) {
            covertedObject.addProperty("MESSAGE", covertedObject.get("MESSAGE").toString());
        }

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(covertedObject);

        return json;
    }

    public static List<Map<String, Object>> convertorTreeMap(final List<Map<String, Object>> list, String rootId, final String idKey,
        final String pIdKey, final String titleKey) {
        return convertorTreeMap(list, rootId, idKey, pIdKey, titleKey, "");
    }

    public static List<Map<String, Object>> convertorTreeMap(final List<Map<String, Object>> list, String rootId, final String idKey,
        final String pIdKey, final String titleKey, final String orderKey) {
        return convertorTreeMap(list, rootId, idKey, pIdKey, titleKey, orderKey, "N");
    }

    public static List<Map<String, Object>> convertorTreeMap(List inList, String rootId, final String idKey, final String pIdKey,
        final String titleKey, final String orderKey, final String allView) {
        List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();   // 최종 트리

        if (inList == null || inList.size() == 0) {
            throw new RuntimeException("List<Map> 데이터가 없습니다.");
        }
        if (inList.get(0) == null) {
            throw new RuntimeException("Map 데이터가 없습니다.");
        }

        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); // 원본데이터(Bean일경우 Map으로 변환)
        Iterator iter;
        for (iter = inList.iterator(); iter.hasNext(); ) {
            try {
                Object obj = iter.next();
                if (obj instanceof Map) {
                    list.add((Map<String, Object>) obj);
                } else {
                    list.add((Map<String, Object>) obj);
                    //                    list.add((Map<String, Object>) BeanUtils.describe(obj));
                }
            } catch (Exception e) {
                throw new RuntimeException("Collection -> List<Map> 으로 변환 중 실패: " + e);
            }
        }

        int listLength = list.size();
        int loopLength = 0;
        final int[] treeLength = new int[]{0};

        while (treeLength[0] != listLength && listLength != loopLength++) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> item = list.get(i);
                if (rootId.equals((String) item.get(pIdKey))) {
                    Map<String, Object> view = new HashMap<String, Object>(item);
                    view.put("id", item.get(idKey));
                    view.put("name", item.get(titleKey) + ("Y".equals(allView) ? ("Y".equals(item.get("DEL_YN"))
                        ? " <span style='color: red;'>(삭제)</span>"
                        : "N".equals(item.get("USE_YN")) ? " <span style='color: blue;'>(사용안함)</span>" : "") : ""));
                    view.put("children", new ArrayList<Map<String, Object>>());

                    treeList.add(view);
                    list.remove(i);

                    treeLength[0]++;

                    if (orderKey != null) {
                        Collections.sort(treeList, new Comparator<Map<String, Object>>() {
                            public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
                                return ((String) arg0.get(orderKey)).compareTo((String) arg1.get(orderKey));
                            }
                        });
                    }
                    //                    view.put("isFolder", "true");

                    break;
                } else {
                    new InnerClass() {
                        public void getParentNode(List<Map<String, Object>> children, Map<String, Object> item) {
                            for (int i = 0; i < children.size(); i++) {
                                Map<String, Object> child = children.get(i);
                                if (child.get(idKey).equals(item.get(pIdKey))) {
                                    Map<String, Object> view = new HashMap<String, Object>(item);
                                    view.put("id", item.get(idKey));
                                    view.put("name", item.get(titleKey) + ("Y".equals(allView) ? ("Y".equals(item.get("DEL_YN"))
                                        ? " <span style='color: red;'>(삭제)</span>"
                                        : "N".equals(item.get("USE_YN")) ? " <span style='color: blue;'>(사용안함)</span>" : "") : ""));
                                    view.put("children", new ArrayList<Map<String, Object>>());
                                    ((List<Map<String, Object>>) child.get("children")).add(view);

                                    treeLength[0]++;

                                    list.remove(list.indexOf(item));
                                    //                                    view.put("isFolder", "true");

                                    if (orderKey != null) {
                                        Collections.sort(((List<Map<String, Object>>) child.get("children")),
                                            new Comparator<Map<String, Object>>() {
                                                public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
                                                    return ((String) arg0.get(orderKey)).compareTo((String) arg1.get(orderKey));
                                                }
                                            });
                                    }
                                    break;
                                } else {
                                    if (((List<Map<String, Object>>) child.get("children")).size() > 0) {
                                        getParentNode((List<Map<String, Object>>) child.get("children"), item);
                                    }
                                }
                            }
                        }
                    }.getParentNode(treeList, item);
                }
            }
        }

        return treeList;
    }

    public interface InnerClass {

        public void getParentNode(List<Map<String, Object>> list, Map<String, Object> item);
    }

    @SuppressWarnings("unchecked")
    public static String getJsonStringFromMap(Map<String, Object> map) {

        JSONObject jsonObj = new JSONObject();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            jsonObj.put(entry.getKey(), entry.getValue());
        }

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(jsonObj);

        return json;
    }

    @SuppressWarnings("unchecked")
    public static JSONArray getJsonArrayFromList(List<Map<String, Object>> list) {

        JSONArray jsonArray = new JSONArray();

        for (Map<String, Object> map : list) {
            jsonArray.add(getJsonStringFromMap(map));
        }

        return jsonArray;
    }

    /* JSONArray에서 값 비교하여 찾기. */
    public static JSONObject findMatchDataObj(String findStr, String matchKey, JSONArray arrayObj) {
        Predicate<JSONObject> filterCondition = jsonObj -> findStr.equals((jsonObj.getString(matchKey)).replaceAll("\\u003e", ">"));
        List<JSONObject> filteredList = (List<JSONObject>) arrayObj.stream().map(obj -> (JSONObject) obj).filter(filterCondition)
            .collect(Collectors.toList());
        if (!filteredList.isEmpty()) {
            return filteredList.get(0);
        } else {
            return (new JSONObject());
        }
    }


    /* JSONArray에서 key값 찾기. */
    public static String findMatchKeyValue(String keyName, JSONArray arrayObj) {
        if (arrayObj.size() > 0) {
            for (Object item : arrayObj) {
                JSONObject tempObj = (JSONObject) item;
                Iterator i = tempObj.keys();
                while (i.hasNext()) {
                    String key = i.next().toString();
                    if (keyName.equals(key)) {
                        return tempObj.getString(key);
                    }else {
                        Object valueObj = tempObj.get(key);
                        if ("net.sf.json.JSONArray".equals(valueObj.getClass().getName())) {
                            return findMatchKeyValue( keyName, (net.sf.json.JSONArray) valueObj );
                        } else if ("net.sf.json.JSONObject".equals(valueObj.getClass().getName())) {
                            return findMatchKeyValueObj( keyName, (net.sf.json.JSONObject) valueObj );
                        }
                    }
                }
            }
        }
        return null;
    }

    /* JSONObject에서 key값 찾기. */
    public static String findMatchKeyValueObj(String keyName, JSONObject tempObj) {
        Iterator i = tempObj.keys();
        while (i.hasNext()) {
            String key = i.next().toString();
            if (keyName.equals(key)) {
                return tempObj.getString(key);
            }else {
                Object valueObj = tempObj.get(key);
                if ("net.sf.json.JSONArray".equals(valueObj.getClass().getName())) {
                    return findMatchKeyValue( keyName, (net.sf.json.JSONArray) valueObj );
                } else if ("net.sf.json.JSONObject".equals(valueObj.getClass().getName())) {
                    return findMatchKeyValueObj( keyName, (net.sf.json.JSONObject) valueObj );
                }
            }
        }
        return null;
    }
}
