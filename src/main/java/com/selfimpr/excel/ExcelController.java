package com.selfimpr.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel工具类接口
 */
@RestController
@RequestMapping("/excel")
public class ExcelController {

    private static final Logger log = LoggerFactory.getLogger(ExcelController.class);

    private static List<People> peopleList = new ArrayList<>();

    static {
        People people;
        for (int i = 0; i < 25; i++) {
            people = new People("小悦" + i, i);
            peopleList.add(people);
        }
    }

    /**
     * 【下载】通过浏览器下载excel
     *
     * @return
     */
    @GetMapping(value = "/browserDownload")
    public ResponseEntity<Resource> browserDownload() {
        log.debug("进入到browserDownload");
        return ExcelUtils.browserDownload(peopleList, People.class, 10, "yue.xlsx");
    }

    /**
     * 【下载】excel下载到本地
     */
    @GetMapping(value = "/localDownload")
    public void localDownload() {
        log.debug("进入到localDownload");
        ExcelUtils.localDownload(peopleList, People.class, 10, "E:\\test\\", "yue.xlsx");
    }

    /**
     * 【上传】根据路径上传excel
     */
    @PostMapping(value = "/uploadExcelByPath")
    public List<People> uploadExcelByPath() {
        log.debug("uploadExcelByPath");
        return ExcelUtils.uploadExcel("E:\\test\\yue.xlsx", People.class);
    }

    /**
     * 【上传】通过MultipartFile上传excel
     */
    @PostMapping(value = "/uploadExcelByMultipartFile")
    public List<People> uploadExcelByMultipartFile(@RequestParam(value = "uploadFile") MultipartFile file) {
        log.debug("uploadExcelByMultipartFile");
        return ExcelUtils.uploadExcel(file, People.class);
    }

}
