package com.smartcity.common.servicebase.exceptionhandler;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//自定义异常类

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends RuntimeException{
    @ApiModelProperty(value = "状态码")
    private Integer code;

    private String message;

    @Override
    public String toString() {
        return "CustomException{" +
                "message=" + this.getMessage() +
                ", code=" + code +
                '}';
    }
}
