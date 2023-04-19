package pers.website.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常信息枚举
 *
 * @author ChenetChen
 * @since 2023/4/8 17:03
 */
@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    /**
     * 事务异常
     */
    TRANSACTIONAL_ROLLBACK("ET001","SQL执行异常，抛出异常使事务回滚");
    
    /**
     * 异常编码
     */
    private final String code;
    /**
     * 异常信息
     */
    private final String message;
}
