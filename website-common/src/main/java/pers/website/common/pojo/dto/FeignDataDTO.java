package pers.website.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 远程调用结果类
 *
 * @author ChenetChen
 * @since 2023/3/7 12:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeignDataDTO<T> {
    /**
     * 执行状态：1：成功；0：空值；-1：异常
     */
    private String state;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回的记录集合
     */
    private T result;
}
