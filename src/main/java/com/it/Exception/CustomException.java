package com.it.Exception;

/**
 * 自定义异常类
 * @author  hyj
 * @since  2022-9-18
 *
 */
public class CustomException extends RuntimeException{
    public  CustomException(String message){
        super(message);
    }
}
