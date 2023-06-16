package com.ahut.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @Author : Scott Chen
 * @create 2023/6/12 16:29
 *
 * 公共响应类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;
    private String message;
    private T data;


    //成功的四种重载

    public static <T> Result<T> success(){
        return new Result<T>(20000,"success",null);
    }

    public static <T> Result<T> success(T data){
        return new Result<T>(20000,"success",data);
    }

    public static <T> Result<T> success(String message){
        return new Result<T>(20000,message,null);
    }

    public static <T> Result<T> success(String success,T data){
        return new Result<T>(20000,success,data);
    }

    //失败的四种重载

    public static <T> Result<T> fail(){
        return new Result<T>(20001,"fail",null);
    }

    public static <T> Result<T> fail(Integer code){
        return new Result<T>(code,"fail",null);
    }

    public static <T> Result<T> fail(String message){
        return new Result<T>(20001,message,null);
    }

    public static <T> Result<T> fail(Integer code,String message){
        return new Result<T>(code,message,null);
    }

}
