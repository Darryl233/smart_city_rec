package com.smartcity.common.servicebase.exceptionhandler;

import com.smartcity.common.commonutils.ExceptionUtil;
import com.smartcity.common.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/** 统一异常处理类 **/
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        log.error(ExceptionUtil.getMessage(e));
        return R.error().message("执行了全局异常处理器");
    }

    //特定异常处理，优先于全局，实际不常用
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R error(HttpMessageNotReadableException e){
        e.printStackTrace();
        log.error(ExceptionUtil.getMessage(e));
        return R.error().message("执行了指定异常处理器，当前json数据不可读");
    }

    //自定义异常类处理
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public R error(CustomException e){
        e.printStackTrace();
        log.error(ExceptionUtil.getMessage(e));
        return R.error().message(e.getMessage()).code(e.getCode());
    }
}
