package com.selfimpr.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping(value = "/browserDownload")
    public ResponseEntity<Resource> browserDownload() {
        log.debug("进入到browserDownload");
        return ExcelUtils.browserDownload(peopleList, People.class, 10, "yue.xlsx");
    }

    @GetMapping(value = "/localDownload")
    public void localDownload() {
        log.debug("进入到localDownload");
        ExcelUtils.localDownload(peopleList, People.class, 10, "E:\\test\\", "yue.xlsx");
    }

}
