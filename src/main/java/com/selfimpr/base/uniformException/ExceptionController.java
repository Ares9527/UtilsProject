package com.selfimpr.base.uniformException;

import com.selfimpr.base.uniformDataReturn.codeenum.BaseResultCode;
import com.selfimpr.entityAndDto.People;
import com.selfimpr.entityAndDto.UserDTO;
import com.selfimpr.excel.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一异常处理接口
 */
@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping(value = "/test")
    public ResponseEntity<Resource> testException() {
        throw new CustomException(BaseResultCode.PARAMETERS_ERROR, "用户userId不能为空");
    }

}
