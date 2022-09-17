package com.it.Exception;

import com.it.utli.SystemJsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 * @since 2022-9-14
 * @author hyj
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public SystemJsonResponse ex(SQLIntegrityConstraintViolationException s){
        log.error(s.getMessage());
        if(s.getMessage().contains("Duplicate entry")){
            String[]split=s.getMessage().split(" ");
            String ms=split[2];
            return SystemJsonResponse.fail(0,ms+"已经存在");
        }
        return SystemJsonResponse.fail(0,"未知错误");
    }

    /**
     * 业务异常处理
     * @param exception
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public SystemJsonResponse ex2(CustomException exception){
        return SystemJsonResponse.fail(0,exception.getMessage());
    }


}
