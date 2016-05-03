package com.feicuiedu.newsclient;

import android.test.AndroidTestCase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenyan on 2016/5/2.
 */
public class TestJson extends AndroidTestCase {

    private String strTag = "JSON_TEST";

    private String jsonStr = "[{\"name\":\"水浒\",\"anthor\":\"施耐庵\"},{\"name\":\"红楼梦\",\"anthor\":\"曹雪芹\"},{\"name\":\"西游记\",\"anthor\":\"吴承恩\"},{\"name\":\"三国演义\",\"anthor\":\"罗贯中\"}]";
    private String jsonStr1 = "{\"provinceList\":[{\"name\":\"山东\",\"cityList\":[{\"name\":\"济南\"},{\"name\":\"青岛\"},{\"name\":\"潍坊\"},{\"name\":\"东营\"}]},{\"name\":\"江苏\",\"cityList\":[{\"name\":\"南京\"},{\"name\":\"苏州\"},{\"name\":\"连云港\"},{\"name\":\"无锡\"}]}],\"country\":\"china\"}";

    public void testGenJsonStrGson() {

        List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "水浒");
        map.put("anthor", "施耐庵");
        result.add(map);

        map = new HashMap<String, String>();
        map.put("name", "红楼梦");
        map.put("anthor", "曹雪芹");
        result.add(map);

        map = new HashMap<String, String>();
        map.put("name", "西游记");
        map.put("anthor", "吴承恩");
        result.add(map);

        map = new HashMap<String, String>();
        map.put("name", "三国演义");
        map.put("anthor", "罗贯中");
        result.add(map);

        Gson gson = new Gson();
        String json = gson.toJson(result);
        Log.d(strTag, json);

        List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();

        map = new HashMap<String, String>();
        map.put("name", "济南");
        cityList.add(map);

        map = new HashMap<String, String>();
        map.put("name", "青岛");
        cityList.add(map);

        map = new HashMap<String, String>();
        map.put("name", "潍坊");
        cityList.add(map);

        map = new HashMap<String, String>();
        map.put("name", "东营");
        cityList.add(map);

        Map<String, Object> province = new HashMap<String, Object>();
        province.put("name", "山东");
        province.put("cityList", cityList);

        List<Map<String, Object>> proviceList = new ArrayList<Map<String, Object>>();
        proviceList.add(province);


        cityList = new ArrayList<Map<String, String>>();

        map = new HashMap<String, String>();
        map.put("name", "南京");
        cityList.add(map);

        map = new HashMap<String, String>();
        map.put("name", "苏州");
        cityList.add(map);

        map = new HashMap<String, String>();
        map.put("name", "连云港");
        cityList.add(map);

        map = new HashMap<String, String>();
        map.put("name", "无锡");
        cityList.add(map);

        province = new HashMap<String, Object>();
        province.put("name", "江苏");
        province.put("cityList", cityList);

        proviceList.add(province);

        Map<String, Object> world = new HashMap<String, Object>();

        world.put("country", "china");

        world.put("provinceList", proviceList);

        json = gson.toJson(world);
        Log.d(strTag, json);
    }

    public void testParseJsonStrGson() {

        Gson gson = new Gson();
        Type type = new TypeToken< Object>() {
        }.getType();
        List<Object> list = (List<Object>)gson.fromJson(jsonStr, type);

        for (Object obj : list) {

            Map<String, String> map = (Map<String, String>) obj;

            for (Map.Entry<String, String> entry : map.entrySet()) {
                Log.d("key", entry.getKey());
                Log.d("value", entry.getValue());
            }
        }

    }

    public void testParseJsonStr1Gson() {

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();

        Map<String, Object> worldMap = gson.fromJson(jsonStr1, type);

        String country = (String) worldMap.get("country");

        Log.d("country",country);
        List<Object> provinceList = (List<Object>) worldMap.get("provinceList");

        for (Object obj : provinceList) {

            Log.d("obj",obj.toString());
        }

    }

}
