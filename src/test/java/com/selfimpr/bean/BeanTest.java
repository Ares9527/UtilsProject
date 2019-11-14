package com.selfimpr.bean;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BeanTest {

    @Test
    public void copyPropertiesByBeanUtils() {
        Dog dog = new Dog("旺财", 8, null);
        DogDTO dogDTO = new DogDTO();
        dogDTO.setColor("white");
        System.out.println(dog);
        System.out.println(dogDTO);
        System.out.println("#########################");

        CustomBeanUtils.copyPropertiesByBeanUtils(dog, dogDTO);
        System.out.println(dog);
        System.out.println(dogDTO);
    }

    @Test
    public void copyPropertiesIgnoreNullValue() {
        Dog dog = new Dog("旺财", 8, null);
        DogDTO dogDTO = new DogDTO();
        dogDTO.setColor("white");
        System.out.println(dog);
        System.out.println(dogDTO);
        System.out.println("#########################");

        CustomBeanUtils.copyPropertiesIgnoreNullValue(dog, dogDTO);
        System.out.println(dog);
        System.out.println(dogDTO);
    }

    @Test
    public void copyPropertiesByBeanCopier() {
        System.out.println(CustomBeanUtils.beanCopierMap);
        Dog dog = new Dog("旺财", 8, null);
        DogDTO dogDTO = new DogDTO();
        dogDTO.setColor("white");
        System.out.println(dog);
        System.out.println(dogDTO);
        System.out.println("#########################");
        CustomBeanUtils.copyPropertiesByBeanCopier(dog, dogDTO);
        System.out.println(dog);
        System.out.println(dogDTO);
        System.out.println(CustomBeanUtils.beanCopierMap);
    }
}
