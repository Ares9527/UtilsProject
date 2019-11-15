package com.selfimpr.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.selfimpr.bean.CustomBeanUtils;
import com.selfimpr.entityAndDto.Dog;
import com.selfimpr.entityAndDto.People;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class MapTest {

    private static People people = new People("小明", 6);
    private static People people1 = new People("小明1", 7);
    private static People people2 = new People("小明2", 8);

//    @Test
//    public void string2Map() {
//        System.out.println(MapUtils.string2MapByFastJson("{\"name\":\"小明\",\"age\":\"16\",\"color\":\"yellow\"}"));
//    }
//
    @Test
    public void entity2Map2() {
        Map<String, Object> map = CustomBeanUtils.entity2MapByFastJson(people);
        System.out.println(map.get("name"));
    }
//
//    @Test
//    public void map2Object() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "旺财");
//        map.put("age", "8");
//        map.put("color", "white");
//        System.out.println(MapUtils.map2EntityByFastJson(map, Dog.class));
//    }
//
//    @Test
//    public void mapList2EntityListByFastJson() {
//        List<Map<String, Object>> mapList = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "小花1");
//        map.put("age", "8");
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("name", "小花2");
//        map1.put("age", "9");
//        mapList.add(map);
//        mapList.add(map1);
//
//        List<People> peopleList = MapUtils.mapList2EntityListByFastJson(mapList, People.class);
//        System.out.println(peopleList);
//        System.out.println(peopleList.size());
//    }
//
//    @Test
//    public void entityList2MapListByFastJson() {
//        List<People> peopleList = new ArrayList<>();
//        peopleList.add(people);
//        peopleList.add(people1);
//        peopleList.add(people2);
//
//        List<Map<String, People>> mapList = MapUtils.entityList2MapListByFastJson(peopleList);
//        System.out.println(mapList);
//        System.out.println(mapList.size());
//        System.out.println(mapList.get(2).get("name"));
//    }
}
