package com.selfimpr.bean;

import com.selfimpr.entityAndDto.Dog;
import com.selfimpr.string.CustomStringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class BeanTest {

//    @Test
//    public void copyPropertiesByBeanUtils() {
//        Dog dog = new Dog("旺财", 8, null);
//        DogDTO dogDTO = new DogDTO();
//        dogDTO.setColor("white");
//        System.out.println(dog);
//        System.out.println(dogDTO);
//        System.out.println("#########################");
//
//        CustomBeanUtils.copyPropertiesByBeanUtils(dog, dogDTO);
//        System.out.println(dog);
//        System.out.println(dogDTO);
//    }
//
//    @Test
//    public void copyPropertiesIgnoreNullValue() {
//        Dog dog = new Dog("旺财", 8, null);
//        DogDTO dogDTO = new DogDTO();
//        dogDTO.setColor("white");
//        System.out.println(dog);
//        System.out.println(dogDTO);
//        System.out.println("#########################");
//
//        CustomBeanUtils.copyPropertiesIgnoreNullValue(dog, dogDTO);
//        System.out.println(dog);
//        System.out.println(dogDTO);
//    }
//
//    @Test
//    public void copyPropertiesByBeanCopier() {
//        System.out.println(CustomBeanUtils.beanCopierMap);
//        Dog dog = new Dog("旺财", 8, null);
//        DogDTO dogDTO = new DogDTO();
//        dogDTO.setColor("white");
//        System.out.println(dog);
//        System.out.println(dogDTO);
//        System.out.println("#########################");
//        CustomBeanUtils.copyPropertiesByBeanCopier(dog, dogDTO);
//        System.out.println(dog);
//        System.out.println(dogDTO);
//        System.out.println(CustomBeanUtils.beanCopierMap);
//    }

//    @Test
//    public void string2MapByObjectMapper() {
//        Map<String, Object> map = CustomStringUtils.string2MapByObjectMapper("{\"age\":6,\"color\":\"black\",\"name\":\"旺财\"}");
//        System.out.println(map);
//        Map<String, Object> map1 = CustomStringUtils.string2MapByObjectMapper("{\"age\":6,\"color\":\"black\",\"name\":\"旺财\"}");
//        System.out.println(map1);
//        Map<String, Object> map2 = CustomStringUtils.string2MapByObjectMapper("{\"age\":6,\"color\":\"black\",\"name\":\"旺财\"}");
//        System.out.println(map2);
//    }
//
//    @Test
//    public void string2MapListByObjectMapper() {
//        List<Map<String, Object>> mapList = CustomStringUtils.string2MapListByObjectMapper("[{\"age\":6,\"color\":\"black\",\"name\":\"旺财\"},{\"age\":6,\"color\":\"white\",\"name\":\"旺财1\"}]");
//        System.out.println(mapList);
//    }

//    @Test
//    public void ss() {
//        System.out.println(CustomStringUtils.string2MapListByFastJson("[{\"age\":6,\"color\":\"black\",\"name\":\"旺财\"},{\"age\":6,\"color\":\"white\",\"name\":\"旺财1\"}]"));
//    }

    @Test
    public void json2EntityByObjectMapper() {
        Dog dog = CustomStringUtils.json2EntityByObjectMapper("{\"age\":6,\"color\":\"black\",\"name\":\"旺财\",\"belong\":\"home\"}", Dog.class);
        System.out.println(dog);
        Dog dog1 = CustomStringUtils.json2EntityByObjectMapper("{\"age\":6,\"color\":\"bl%$^&ack\",\"name\":\"旺‱㊣㊎财\",\"be㊙㉿囍long\":\"home\"}", Dog.class);
        System.out.println(dog1);
    }

    @Test
    public void entityList2MapListByFastJson() {
        Map<String, Object> stringStringMap = CustomStringUtils.json2CustomFormatByObjectMapper("{\"age\":6,\"color\":\"black\",\"name\":\"旺财\"}", new TypeReference<Map<String, Object>>() {});
        System.out.println(stringStringMap);

        Dog dog = CustomStringUtils.json2CustomFormatByObjectMapper("{\"age\":6,\"color\":\"black\",\"name\":\"旺财\"}", new TypeReference<Dog>() {});
        System.out.println(dog);
    }
}
