//package com.selfimpr.picture;
//
//import net.coobird.thumbnailator.Thumbnails;
//import net.coobird.thumbnailator.geometry.Positions;
//import org.apache.commons.io.IOUtils;
//import org.junit.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//@SpringBootTest
//public class PictureTest {
//
//    @Test
//    public void test1() throws IOException {
//        ThumbnailsUtils.test1();
//    }
//
//    @Test
//    public void test2() throws IOException {
//        ThumbnailsUtils.test2();
//    }
//
//    @Test
//    public void test3() throws IOException {
//        ThumbnailsUtils.test3();
//    }
//
//    @Test
//    public void test4() throws IOException {
//        ThumbnailsUtils.test4();
//    }
//
//    @Test
//    public void test5() throws IOException {
//        ThumbnailsUtils.test5();
//    }
//
//    @Test
//    public void test10() throws IOException {
//        File file = null;
//        InputStream inputStream1 = null;
//        try {
//            // 原图409/567，需要379/484像素
//            // 379/484 = 409/x
////        double a = 409.0*484.0/379.0;
////        System.out.println(a); // 522.311345646438
//            // 1，将原图放在了resources下的images目录下
//            ClassPathResource classPathResource = new ClassPathResource("images/pic.jpg");
//            InputStream in = classPathResource.getInputStream();
//            InputStream inputStream = IOUtils.toBufferedInputStream(in);
//            // 裁剪后文件生成在E:/test下
//            Thumbnails.of(inputStream).sourceRegion(Positions.CENTER, 409, 523).size(409, 523)
//                    .keepAspectRatio(false)
//                    .toFile("E:/test/pic1.jpg");
//
//            file = new File("E:/test/pic1.jpg");
//            inputStream1 = new FileInputStream(file);
//            // 将大小设置为所需大小
//            Thumbnails.of(inputStream1).size(379, 484).keepAspectRatio(false)
//                    .toFile("E:/test/final.jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // 先关闭inputStream1，否则流占用file无法delete
//            inputStream1.close();
//            file.delete();
//        }
//    }
//}
