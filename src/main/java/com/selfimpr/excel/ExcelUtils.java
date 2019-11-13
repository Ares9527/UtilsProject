package com.selfimpr.excel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ExcelUtils {

    /**
     * Workbook构建
     *
     * @param dataList   数据
     * @param cls        类型
     * @param splitCount 拆分sheet值
     * @return
     * @throws IOException
     */
    private static <T> Workbook writeExcel(List<T> dataList, Class<T> cls, Integer splitCount) {
        if (CollectionUtils.isEmpty(dataList)) {
            throw new RuntimeException("导出excel的数据不能为空");
        }
        if (splitCount == null) {
            splitCount = 100;
        }
        // 实体字段为private类型
        Field[] fields = cls.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields)
                .filter(field -> {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null && annotation.order() > 0) {
                        // 对于private修饰的成员，需要设置为setAccessible(true)：即赋予反射对象超级权限，绕过语言权限检查，才能访问和修改
                        field.setAccessible(true);
                        return true;
                    }
                    return false;
                }).sorted(Comparator.comparing(field -> {
                    int col = 0;
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        // 根据注解的col大小进行排序
                        col = annotation.order();
                    }
                    return col;
                })).collect(Collectors.toList());
        // HSSFWorkbook：是操作Excel2003以前（包括2003）的版本，扩展名是.xls
        // XSSFWorkbook：是操作Excel2007的版本，扩展名是.xlsx
        // 对于不同版本的EXCEL文档要使用不同的工具类
        Workbook wb = new XSSFWorkbook();

        // 拆分数据到不同的表格sheet
        int dataSize = dataList.size();
        boolean isOver = dataSize % splitCount == 0;
        int pageCount = isOver ? (dataSize / splitCount) : (dataSize / splitCount + 1);
        List<T> currentPageList;
        for (int i = 0; i < pageCount; i++) {
            if ((i == pageCount - 1) && !isOver) {
                currentPageList = dataList.subList(i * splitCount, (i + 1) * splitCount - ((i + 1) * splitCount - dataSize));
            } else {
                currentPageList = dataList.subList(i * splitCount, (i + 1) * splitCount);
            }
            Sheet sheet = wb.createSheet("sheet" + (i + 1));
            // 行的切换标识
            AtomicInteger ai = new AtomicInteger();
            {
                Row row = sheet.createRow(ai.getAndIncrement());
                // 列的切换标识
                AtomicInteger aj = new AtomicInteger();
                // 写入头部
                fieldList.forEach(field -> {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    String columnName = "";
                    if (annotation != null) {
                        columnName = annotation.columnName();
                    }
                    Cell cell = row.createCell(aj.getAndIncrement());
                    cell.setCellValue(columnName);

                    CellStyle cellStyle = wb.createCellStyle();
                    // 填充前景色,旧版写法：cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    cellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
                    // 填充模式,旧版写法：cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    // 水平居中，旧版写法：style.setAlignment(CellStyle.ALIGN_CENTER);
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    // 上下居中，旧版写法：style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                    Font font = wb.createFont();
                    // 字体加粗,旧版写法：font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
                    font.setBold(true);
                    cellStyle.setFont(font);
                    cell.setCellStyle(cellStyle);
                });
            }

            {
                currentPageList.forEach(data -> {
                    Row row = sheet.createRow(ai.getAndIncrement());
                    // 列的切换标识
                    AtomicInteger aj = new AtomicInteger();
                    fieldList.forEach(field -> {
                        Class<?> classType = field.getType();
                        Object value = "";
                        try {
                            value = field.get(data);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        Cell cell = row.createCell(aj.getAndIncrement());
                        if (value != null) {
                            if (classType == Date.class) {
                                cell.setCellValue(value.toString());
                            } else {
                                cell.setCellValue(value.toString());
                            }
                            cell.setCellValue(value.toString());
                        }
                    });
                });
            }
        }
        return wb;
    }

    /**
     * 浏览器下载excel
     *
     * @param dataList   数据
     * @param cls        类型
     * @param splitCount 拆分sheet值
     * @param filename   文件名
     * @return
     * @throws IOException
     */
    public static <T> ResponseEntity<Resource> browserDownload(List<T> dataList, Class<T> cls, Integer splitCount, String filename) {
        try {
            Workbook wb = writeExcel(dataList, cls, splitCount);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            wb.close();
            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));
            bos.flush();
            bos.close();
            filename = new String(filename.getBytes(), StandardCharsets.ISO_8859_1);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Content-Disposition", "attachment;filename=" + filename);
            headers.add("charset", "UTF-8");
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/x-msdownload")).headers(headers).body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("生成excel出现异常");
        }
    }

    /**
     * 生成excel文件
     *
     * @param dataList   数据
     * @param cls        类型
     * @param splitCount 拆分sheet值
     * @param path       生成excel路径
     * @param fileName   文件名
     */
    public static <T> void localDownload(List<T> dataList, Class<T> cls, Integer splitCount, String path, String fileName) {
        Workbook wb = writeExcel(dataList, cls, splitCount);
        File file = new File(path + fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            wb.write(fileOutputStream);
            wb.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("生成excel出现异常");
        }
    }
}
