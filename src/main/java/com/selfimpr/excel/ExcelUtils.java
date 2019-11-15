package com.selfimpr.excel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExcelUtils {

    private final static Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    private final static String EXCEL2003 = "xls";

    private final static String EXCEL2007 = "xlsx";

    // TODO ###################################### Excel 导入 开始 ######################################

    /**
     * 根据路径导入excel
     *
     * @param path
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> uploadExcel(String path, Class<T> cls) {
        return readExcel(path, null, cls);
    }

    /**
     * 根据上传的excel文件导入excel
     *
     * @param file
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> uploadExcel(MultipartFile file, Class<T> cls) {
        return readExcel(null, file, cls);
    }

    /**
     * 读取excel文件数据
     *
     * @param path
     * @param cls
     * @param file
     * @param <T>
     * @return
     */
    private static <T> List<T> readExcel(String path, MultipartFile file, Class<T> cls) {
        log.debug("执行导入excel操作");
        String fileName;
        List<T> dataList = new ArrayList<>();
        Workbook workbook = null;
        try {
            InputStream inputStream;
            if (StringUtils.isNotEmpty(path)) {
                File localFile = new File(path);
                fileName = localFile.getName();
                inputStream = new FileInputStream(localFile);
            } else {
                fileName = file.getOriginalFilename();
                inputStream = file.getInputStream();
            }
            if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
                log.error("上传文件格式不正确");
            }
            if (fileName.endsWith(EXCEL2003)) {
                workbook = new HSSFWorkbook(inputStream);
            }
            if (fileName.endsWith(EXCEL2007)) {
                workbook = new XSSFWorkbook(inputStream);
            }
            if (workbook != null) {
                // 类映射  注解 columnName-->bean columns
                Map<String, List<Field>> classMap = new HashMap<>();
                List<Field> fieldList = Stream.of(cls.getDeclaredFields()).collect(Collectors.toList());
                fieldList.forEach(field -> {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        String columnName = annotation.columnName();
                        if (StringUtils.isBlank(columnName)) {
                            // return起到的作用和continue是相同的 语法
                            return;
                        }
                        if (!classMap.containsKey(columnName)) {
                            classMap.put(columnName, new ArrayList<>());
                        }
                        field.setAccessible(true);
                        classMap.get(columnName).add(field);
                    }
                });
                Sheet currentSheet;
                // 遍历excel表格里每个sheet的数据
                for (Iterator<Sheet> sheets = workbook.iterator(); sheets.hasNext(); ) {
                    currentSheet = sheets.next();
                    // 索引-->columns
                    Map<Integer, List<Field>> reflectionMap = new HashMap<>();
                    boolean firstRow = true;
                    for (int i = currentSheet.getFirstRowNum(); i <= currentSheet.getLastRowNum(); i++) {
                        Row row = currentSheet.getRow(i);
                        // 首行，提取注解
                        if (firstRow) {
                            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                                Cell cell = row.getCell(j);
                                String cellValue = getCellValue(cell);
                                if (classMap.containsKey(cellValue)) {
                                    reflectionMap.put(j, classMap.get(cellValue));
                                }
                            }
                            firstRow = false;
                        } else {
                            //忽略空白行
                            if (row == null) {
                                continue;
                            }
                            try {
                                T t = cls.newInstance();
                                // 判断是否为空白行
                                boolean allBlank = true;
                                for (int k = row.getFirstCellNum(); k <= row.getLastCellNum(); k++) {
                                    if (reflectionMap.containsKey(k)) {
                                        Cell cell = row.getCell(k);
                                        String cellValue = getCellValue(cell);
                                        if (StringUtils.isNotBlank(cellValue)) {
                                            allBlank = false;
                                        }
                                        List<Field> fields = reflectionMap.get(k);
                                        fields.forEach(x -> {
                                            try {
                                                handleField(t, cellValue, x);
                                            } catch (Exception e) {
                                                log.error(String.format("reflect field:%s value:%s exception!", x.getName(), cellValue), e);
                                            }
                                        });
                                    }
                                }
                                if (!allBlank) {
                                    dataList.add(t);
                                } else {
                                    log.warn(String.format("row:%s is blank ignore!", i));
                                }
                            } catch (Exception e) {
                                log.error(String.format("parse row:%s exception!", i), e);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(String.format("parse excel exception!"), e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error(String.format("parse excel exception!"), e);
                }
            }
        }
        return dataList;
    }

    /**
     * cell数据类型chcek
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        // 旧版写法：Cell.CELL_TYPE_NUMERIC
        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return DateUtil.getJavaDate(cell.getNumericCellValue()).toString();
            } else {
                return new BigDecimal(cell.getNumericCellValue()).toString();
            }
        } else if (cell.getCellType() == CellType.STRING) {
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        } else if (cell.getCellType() == CellType.FORMULA) {
            return StringUtils.trimToEmpty(cell.getCellFormula());
        } else if (cell.getCellType() == CellType.BLANK) {
            return "";
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.ERROR) {
            return "ERROR";
        } else {
            return cell.toString().trim();
        }
    }

    /**
     * 将数据映射到实体
     *
     * @param t
     * @param value
     * @param field
     * @param <T>
     * @throws Exception
     */
    private static <T> void handleField(T t, String value, Field field) throws Exception {
        Class<?> type = field.getType();
        if (type == null || type == void.class || StringUtils.isBlank(value)) {
            return;
        }
        if (type == Object.class) {
            field.set(t, value);
            // 数字类型
        } else if (type.getSuperclass() == null || type.getSuperclass() == Number.class) {
            if (type == int.class || type == Integer.class) {
                field.set(t, NumberUtils.toInt(value));
            } else if (type == long.class || type == Long.class) {
                field.set(t, NumberUtils.toLong(value));
            } else if (type == byte.class || type == Byte.class) {
                field.set(t, NumberUtils.toByte(value));
            } else if (type == short.class || type == Short.class) {
                field.set(t, NumberUtils.toShort(value));
            } else if (type == double.class || type == Double.class) {
                field.set(t, NumberUtils.toDouble(value));
            } else if (type == float.class || type == Float.class) {
                field.set(t, NumberUtils.toFloat(value));
            } else if (type == char.class || type == Character.class) {
                field.set(t, CharUtils.toChar(value));
            } else if (type == boolean.class) {
                field.set(t, BooleanUtils.toBoolean(value));
            } else if (type == BigDecimal.class) {
                field.set(t, new BigDecimal(value));
            }
        } else if (type == Boolean.class) {
            field.set(t, BooleanUtils.toBoolean(value));
        } else if (type == Date.class) {
            //
            field.set(t, value);
        } else if (type == String.class) {
            field.set(t, value);
        } else {
            Constructor<?> constructor = type.getConstructor(String.class);
            field.set(t, constructor.newInstance(value));
        }
    }


    // TODO ###################################### Excel 导出 开始 ######################################

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
        log.debug("执行导出excel操作");
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
