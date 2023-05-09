package pers.website.common.exceptions;

import pers.website.common.enums.ExceptionEnum;

/**
 * 自定义异常类
 *
 * @author ChenetChen
 * @since 2023/3/7 12:12
 */
public class CustomException extends RuntimeException{
    /**
     * 枚举异常信息
     * @param exceptionEnum 枚举
     */
    public CustomException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage(), null, false, false);
    }

    /**
     * 自定义异常信息
     * @param message 异常信息
     */
    public CustomException(String message) {
        super(message, null, false, false);
    }

    /**
     * 原生构造方法
     * @param e 异常对象
     */
    public CustomException(Exception e) {super(e);}
}
