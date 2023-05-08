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
    TRANSACTIONAL_ROLLBACK("E0001","SQL执行异常，抛出异常使事务回滚"),
    /**
     * redis执行异常
     */
    REDIS_EXCEPTION("E0002", "Redis处理异常");
    
    /**
     * 异常编码
     */
    private final String code;
    /**
     * 异常信息
     */
    private final String message;
}
