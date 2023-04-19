package pers.website.common.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pers.website.common.enums.ResultEnum;
import pers.website.common.pojo.dto.FeignDataDTO;

/**
 * 结果集Vo对象
 *
 * @author ChenetChen
 * @since 2023/3/7 12:14
 */
@Data
@ApiModel
@NoArgsConstructor
public class ResultVO<T> {
    @ApiModelProperty("响应码")
    private String code;
    @ApiModelProperty("响应信息")
    private String msg;
    @ApiModelProperty("响应数据")
    private T data;

    public ResultVO(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
    }

    public ResultVO(ResultEnum resultEnum,T data) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
        this.data = data;
    }

    public ResultVO(ResultEnum resultEnum, FeignDataDTO<?> feignDataDTO) {
        this.code = resultEnum.getCode();
        this.msg = feignDataDTO.getMessage();
    }

    public ResultVO(ResultEnum resultEnum, FeignDataDTO<?> feignDataDTO, T data) {
        this.code = resultEnum.getCode();
        this.msg = feignDataDTO.getMessage();
        this.data = data;
    }

    /**
     * 执行成功-msg取枚举
     * @return 结果-成功
     * @param <T> 泛型
     */
    public static <T> ResultVO<T> ok() {
        return new ResultVO<>(ResultEnum.SUCCESS);
    }

    /**
     * 执行成功并返回数据-msg取枚举
     * @param data 执行结果数据
     * @return 执行成功
     * @param <T> 泛型
     */
    public static <T> ResultVO<T> ok(@NonNull T data) {
        return new ResultVO<>(ResultEnum.SUCCESS, data);
    }

    /**
     * 执行成功-msg取DTO对象message
     * @param feignDataDTO 远程调用结果类对象
     * @return 执行成功
     * @param <T> 泛型
     */
    public static <T> ResultVO<T> ok(FeignDataDTO<?> feignDataDTO) {
        return new ResultVO<>(ResultEnum.SUCCESS, feignDataDTO);
    }
    /**
     * 执行成功并返回数据-msg取DTO对象message
     * @param feignDataDTO 远程调用结果类对象
     * @param data 执行结果数据
     * @return 执行成功
     * @param <T> 泛型
     */
    public static <T> ResultVO<T> ok(FeignDataDTO<?> feignDataDTO, @NonNull T data) {
        return new ResultVO<T>(ResultEnum.SUCCESS, feignDataDTO, data);
    }

    /**
     * 其他执行结果-msg取枚举
     * @param resultEnum 枚举
     * @return 执行结果
     * @param <T> 泛型
     */
    public static <T> ResultVO<T> status(ResultEnum resultEnum) {
        return new ResultVO<T>(resultEnum);
    }

    /**
     * 其他执行结果并返回数据-msg取枚举
     * @param resultEnum 枚举
     * @param data 执行结果数据
     * @return 执行结果
     * @param <T> 泛型
     */
    public static <T> ResultVO<T> status(ResultEnum resultEnum,@NonNull T data) {
        return new ResultVO<T>(resultEnum, data);
    }

    /**
     * 其他执行结果-msg取DTO对象message
     * @param resultEnum 枚举
     * @param feignDataDTO 远程调用结果类对象
     * @return 执行结果
     * @param <T> 泛型
     */
    public static <T> ResultVO<T> status(ResultEnum resultEnum, FeignDataDTO<?> feignDataDTO) {
        return new ResultVO<T>(resultEnum, feignDataDTO);
    }

    /**
     * 其他执行结果并返回数据-msg取DTO对象message
     * @param resultEnum 枚举
     * @param feignDataDTO 远程调用结果类对象
     * @param data 执行结果数据
     * @return 执行结果
     * @param <T> 泛型
     */
    public static <T> ResultVO<T> status(ResultEnum resultEnum, FeignDataDTO<?> feignDataDTO, @NonNull T data) {
        return new ResultVO<T>(resultEnum, feignDataDTO, data);
    }
}
