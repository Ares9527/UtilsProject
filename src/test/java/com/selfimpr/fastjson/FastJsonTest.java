//package com.selfimpr.fastjson;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.selfimpr.entityAndDto.Dog;
//import com.selfimpr.entityAndDto.People;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//public class FastJsonTest {
//
//    /**
//     * 【Entity or List<Entity> 转为 String】
//     *      JSON.toJSONString
//     *
//     *         System.out.println(JSON.toJSONString(dog));
//     *         System.out.println(JSON.toJSONString(dogList));
//     */
//
//    /**
//     * 【String 转为 Entity or List<Entity>】
//     *     JSON.parseObject
//     *     JSON.parseArray
//     *
//     * Dog dog = JSON.parseObject("{\"age\":6,\"color\":\"black\",\"name\":\"旺财\"}", Dog.class);
//     * List<Dog> dogList = JSON.parseArray("[{\"age\":6,\"color\":\"black\",\"name\":\"旺财\"},{\"age\":6,\"color\":\"white\",\"name\":\"旺财1\"}]", Dog.class);
//     */
//
//    /**
//     * 【entity to jsonObject】
//     *      (JSONObject)JSON.toJSON
//     *
//     *         JSONObject jsonObject = (JSONObject)JSON.toJSON(dog);
//     *         System.out.println(jsonObject.get("name"));
//     */
//
//    /**
//     * 【List<entity> to jsonArray】
//     *
//     *         JSONArray jsonArrays = (JSONArray)JSON.toJSON(dogList);
//     *         System.out.println(jsonArrays);
//     */
//
//    /**
//     * 【jsonObject to entity】
//     *
//     *         JSONObject jsonObject = (JSONObject)JSON.toJSON(dog);
//     *         Dog dog1 = JSON.toJavaObject(jsonObject, Dog.class);
//     */
//
//    /**
//     * 【jsonArry to List<entity>】
//     *
//     *         JSONArray jsonArrays = (JSONArray)JSON.toJSON(dogList);
//     *         System.out.println(jsonArrays);
//     */
//
//    Dog dog;
//    Dog dog1;
//
//    List<Dog> dogList = new ArrayList<>();
//
//    @Before
//    public void bbb() {
//        dog = new Dog("旺财", 6, "black");
//        dog1 = new Dog("旺财1", 8, "white");
//        dogList.add(dog);
//        dogList.add(dog1);
//    }
//
//    @Test
//    public void object2String() {
//        System.out.println(JSON.toJSONString(dog));// {"age":6,"color":"black","name":"旺财"}
//        System.out.println(JSON.toJSONString(dogList));// [{"age":6,"color":"black","name":"旺财"},{"age":6,"color":"white","name":"旺财1"}]
//    }
//
//    @Test
//    public void string2Object() {
//        Dog dog = JSON.parseObject("{\"age\":6,\"color\":\"black\",\"name\":\"旺财\"}", Dog.class);
//        System.out.println(dog);
//
//        List<Dog> dogList = JSON.parseArray("[{\"age\":6,\"color\":\"black\",\"name\":\"旺财\"},{\"age\":6,\"color\":\"white\",\"name\":\"旺财1\"}]", Dog.class);
//        System.out.println(dogList);
//        System.out.println(dogList.size());
//    }
//
//    @Test
//    public void javaBeans2JsonObject() {
//        JSONObject jsonObject =(JSONObject)JSON.toJSON(dog);
//        System.out.println(jsonObject);
//        System.out.println(jsonObject.get("name"));
//    }
//
//    @Test
//    public void javaBeansList2JsonArray() {
//        JSONArray jsonArrays = (JSONArray)JSON.toJSON(dogList);
//        System.out.println(jsonArrays);
//    }
//
//    @Test
//    public void jsonObject2JavaBeans() {
//        JSONObject jsonObject =(JSONObject)JSON.toJSON(dog);
//        System.out.println("################");
//        Dog dog1 = JSON.toJavaObject(jsonObject, Dog.class);
//        System.out.println(dog1);
//    }
//
//    @Test
//    public void JSONArray2JavaBeansList() {
//        JSONArray jsonArrays = (JSONArray)JSON.toJSON(dogList);
//        List<People> peopleList = new ArrayList<>();
//        for(int i=0;i<jsonArrays.size();i++){
//            People people = JSON.toJavaObject(jsonArrays.getJSONObject(i), People.class);
//            peopleList.add(people);
//        }
//        System.out.println(peopleList.size());
//    }
//
//
//}
