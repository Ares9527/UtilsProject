package com.selfimpr.base.uniformException;

import com.selfimpr.base.uniformDataReturn.UniformResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 *
 * @ControllerAdvice 表示定义全局控制器异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public String defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("------------------>捕捉到全局异常Exception", e);
        return e.getMessage();
    }

    /**
     * 需要将抛出的异常返回给接口，需要加上注解@ResponseBody
     *
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = CustomException.class)
    public UniformResult customExceptionHandler(HttpServletRequest req, CustomException e) throws Exception {
        log.error("------------------>捕捉到全局异常CustomException", e);
        return UniformResult.failed(e.getCodeEnums(), e.getMessage());
    }

}
