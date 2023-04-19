package pers.website.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结果枚举
 *
 * @author ChenetChen
 * @since 2021/12/12 11:41
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
    /**
     * 执行成功
     */
    SUCCESS("S0000","执行成功"),
    /**
     * 执行失败
     */
    FAIL("F0000", "执行失败"),
    /**
     * 系统异常
     */
    ERROR("E0000","系统异常"),
    /**
     * 参数为空
     */
    PARAM_EMPTY("EP001","参数为空"),
    /**
     * 参数格式错误
     */
    PARAM_FORMAT_ERROR("EP002", "参数格式错误"),
    /**
     * 结果为空
     */
    RESULT_EMPTY("ER001","结果为空"),
    /**
     * 远程调用失败，结果为空
     */
    RESULT_FEIGN_EMPTY("ER002","远程调用失败，结果为空");

    /**
     * 结果编码
     */
    private final String code;
    /**
     * 结果信息
     */
    private final String message;
}
