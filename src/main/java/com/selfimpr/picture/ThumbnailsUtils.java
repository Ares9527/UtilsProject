package com.selfimpr.picture;

import com.selfimpr.base.uniformDataReturn.codeenum.BaseResultCode;
import com.selfimpr.base.uniformException.CustomException;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;

/**
 * Thumbnails图像处理库工具类
 */
public class ThumbnailsUtils {

    private static final String ZOOM_PREFIX = "zoom_";

    private static final String ROTATE_PREFIX = "rotate_";

    private static final String WATER_MARK_PREFIX = "waterMark_";

    private static final String TAILOR_PREFIX = "tailor_";

    private static final String TRANSFER_FORMAT_PREFIX = "tansfer_";

    private static final String DEFAULT_WATER_MARK_PATH = "images/watermark.png";

    /**
     * 指定比例进行缩放
     *
     * @param multipartFile
     * @param scale         例：0.5f 缩小一半
     * @param directoryPath
     */
    public static void zoom4specifiedProportion(MultipartFile multipartFile, double scale, String directoryPath) {
        try {
            String targetFilePath = jointFilePath(multipartFile, directoryPath, ZOOM_PREFIX);
            Thumbnails.of(multipartFile.getInputStream()).scale(scale).toFile(targetFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定比例进行缩放(批量) 可设置输出文件夹，前缀
     *
     * * @param scale         例：0.5f 缩小一半
     * @param operateFolderPath
     */
    public static void zoom4specifiedProportionBatch(double scale, String operateFolderPath) {
        try {
            Thumbnails.of(new File(operateFolderPath).listFiles()).scale(scale).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定大小进行缩放（缩放图片不会变形） keepAspectRatio默认为(true)
     * <p>
     * size(width,height) 若图片横比200小，高比300小，不变
     * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
     * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
     *
     * @param multipartFile
     * @param width
     * @param height
     * @param directoryPath
     */
    public static void zoom4SpecifySizeKeepAspectRatio(MultipartFile multipartFile, Integer width, Integer height, String directoryPath) {
        try {
            String targetFilePath = jointFilePath(multipartFile, directoryPath, ZOOM_PREFIX);
            Thumbnails.of(multipartFile.getInputStream()).size(width, height).toFile(targetFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定大小进行缩放（缩放图片可能会变形）
     * <p>
     * size(width,height) 若图片横比200小，高比300小，不变
     * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
     * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
     *
     * @param multipartFile
     * @param width
     * @param height
     * @param directoryPath
     */
    public static void zoom4SpecifySize(MultipartFile multipartFile, Integer width, Integer height, String directoryPath) {
        try {
            String targetFilePath = jointFilePath(multipartFile, directoryPath, ZOOM_PREFIX);
            Thumbnails.of(multipartFile.getInputStream()).size(width, height).keepAspectRatio(false).toFile(targetFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 旋转图片
     *
     * @param multipartFile
     * @param width
     * @param height
     * @param angle
     * @param directoryPath
     */
    public static void rotate(MultipartFile multipartFile, Integer width, Integer height, double angle, String directoryPath) {
        try {
            String targetFilePath = jointFilePath(multipartFile, directoryPath, ROTATE_PREFIX);
            // 使用下面这句会报错：No suitable ImageReader found for source data.
//            final InputStream inputStream = multipartFile.getInputStream();
            if (width == null || height == null) {
                // 通过BufferedImage，获取原图片尺寸
                BufferedImage image = ImageIO.read(multipartFile.getInputStream());
                width = image.getWidth();
                height = image.getHeight();
            }
            // 旋转时需设置size，否则会报错。不传width或height时，保留原图尺寸
            Thumbnails.of(multipartFile.getInputStream()).size(width, height).rotate(angle).toFile(targetFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 水印 （可设置位置，水印图，透明度）
     * <p>
     * outputQuality 用于控制图片质量的
     *
     * @param multipartFile
     * @param width
     * @param height
     * @param directoryPath
     */
    public static void waterMark(MultipartFile multipartFile, Integer width, Integer height, String directoryPath) {
        try {
            String targetFilePath = jointFilePath(multipartFile, directoryPath, WATER_MARK_PREFIX);
            // 注意，需要增加 "/"
            File file = new File(ThumbnailsUtils.class.getResource("/" + DEFAULT_WATER_MARK_PATH).getPath());
            if (width == null || height == null) {
                BufferedImage image = ImageIO.read(multipartFile.getInputStream());
                width = image.getWidth();
                height = image.getHeight();
            }
            // 同旋转也需设置size，否则会报错。
            Thumbnails.of(multipartFile.getInputStream()).size(width, height).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(file), 0.5f)
                    .outputQuality(0.8f)
                    .toFile(targetFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 裁剪
     * <p>
     * sourceRegion设置例：sourceRegion(Positions.CENTER, 400, 400)，图片中心400*400的区域保留，其余裁去
     *
     * @param multipartFile
     * @param width         需要保留的width大小
     * @param height        需要保留的height大小
     * @param directoryPath
     */
    public static void tailor(MultipartFile multipartFile, Integer width, Integer height, String directoryPath) {
        try {
            if (width == null || height == null) {
                throw new CustomException(BaseResultCode.FEAILED, "裁剪时需要设置保留尺寸");
            }
            String targetFilePath = jointFilePath(multipartFile, directoryPath, TAILOR_PREFIX);
            // 保留图片中心400*400的区域
            Thumbnails.of(multipartFile.getInputStream()).sourceRegion(Positions.CENTER, width, height)
                    .size(width, height).keepAspectRatio(false).toFile(targetFilePath);
            // 指定坐标
//            Thumbnails.of(inputStream).sourceRegion(600, 500, 400, 400).size(200, 200).keepAspectRatio(false).toFile("C:/image_region_coord.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转化图像格式
     * outputFormat(图像格式: png/gif)
     */
    public static void transferFormat(MultipartFile multipartFile, Integer width, Integer height, String outputFormat, String directoryPath) {
        try {
            String targetFilePath = jointFilePath(multipartFile, directoryPath, TRANSFER_FORMAT_PREFIX);
            if (width == null || height == null) {
                BufferedImage image = ImageIO.read(multipartFile.getInputStream());
                width = image.getWidth();
                height = image.getHeight();
            }
            Thumbnails.of(multipartFile.getInputStream()).size(width, height).outputFormat(outputFormat).toFile(targetFilePath);
//            Thumbnails.of("images/test.jpg").size(1280, 1024).outputFormat("gif").toFile("C:/image_1280x1024.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出到OutputStream
     *
     * 某些应用上传的图片可能质量比较高，但是用户在列表浏览的时候，又不想原图展示，因为带宽要求较高，
     * 此时可以降低图片质量（上面提到的outputQuality），以outputstream输出流的方式response给浏览器去展示
     * outputQuality约接近1，质量越好，如同微信查看原图
     *
     * @param path
     * @param response 将toOutputStream(流对象)直接设置到response中，浏览器直接预览
     */
    public static void output2Stream(String path, HttpServletResponse response) {
        try {
//        OutputStream os = new FileOutputStream("C:/image_1280x1024_OutputStream.png");
            final File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            BufferedImage image = ImageIO.read(inputStream);
            Thumbnails.of(file).size(image.getWidth(), image.getHeight()).outputQuality(0.5f).toOutputStream(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出到BufferedImage
     *
     * @throws IOException
     */
    private void test9() throws IOException {
        /**
         * asBufferedImage() 返回BufferedImage
         */
        BufferedImage thumbnail = Thumbnails.of("images/test.jpg").size(1280, 1024).asBufferedImage();
        ImageIO.write(thumbnail, "jpg", new File("C:/image_1280x1024_BufferedImage.jpg"));
    }

    /**
     * 将原图通过裁剪，调整大小，修改为所需像素（防止变形但会截去四周一些内容）
     *
     * @throws IOException
     */
    public static void custom1() throws IOException {
        File file = null;
        InputStream inputStream1 = null;
        try {
            // 原图409/567，需要379/484像素
            // 379/484 = 409/x
//        double a = 409.0*484.0/379.0;
//        System.out.println(a); // 522.311345646438
            // 1，将原图放在了resources下的images目录下
            ClassPathResource classPathResource = new ClassPathResource("images/pic.jpg");
            InputStream in = classPathResource.getInputStream();
            InputStream inputStream = IOUtils.toBufferedInputStream(in);
            // 裁剪后文件生成在E:/test下
            Thumbnails.of(inputStream).sourceRegion(Positions.CENTER, 409, 523).size(409, 523)
                    .keepAspectRatio(false)
                    .toFile("E:/test/pic1.jpg");

            file = new File("E:/test/pic1.jpg");
            inputStream1 = new FileInputStream(file);
            // 将大小设置为所需大小
            Thumbnails.of(inputStream1).size(379, 484).keepAspectRatio(false)
                    .toFile("E:/test/final.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 先关闭inputStream1，否则流占用file无法delete
            inputStream1.close();
            file.delete();
        }
    }

    /**
     * 拼接文件路径
     *
     * @param multipartFile 需要操作的文件
     * @param directoryPath 存储的根目录
     * @param prefix        文件名前缀
     * @return
     * @throws IOException
     */
    private static String jointFilePath(MultipartFile multipartFile, String directoryPath, String prefix) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        // 存储文件的目录，格式：配置的文件夹/年/月/日/小时
        String detailPath = now.getYear() + File.separator + now.getMonthValue() + File.separator + now.getDayOfMonth() + File.separator + now.getHour();
        File storeDirPath = new File(directoryPath + detailPath);
        if (!storeDirPath.exists()) {
            FileUtils.forceMkdir(storeDirPath);
        }
        // 附件的文件名
        String originalFilename = multipartFile.getOriginalFilename();
        String targetFileName = prefix + System.currentTimeMillis() + originalFilename;
        // 生成附件文件的路径
        return storeDirPath + File.separator + targetFileName;
    }

}