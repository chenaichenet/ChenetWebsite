package pers.website.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.website.common.constants.Constants;
import pers.website.common.enums.ResultEnum;
import pers.website.common.pojo.dto.FeignDataDTO;
import pers.website.common.pojo.po.User;
import pers.website.common.pojo.vo.ResultVO;
import pers.website.common.utils.ParamUtil;
import pers.website.web.service.UserService;

import java.util.HashMap;

/**
 * 用户相关请求处理类
 * 
 * @author ChenetChen
 * @since 2023/3/7 15:58
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api("用户相关请求处理类")
public class UserController {
    @Resource
    private UserService userService;
    
    /**
     * 获取验证码
     * @param jsonData 参数
     * @return 结果集
     */
    @PostMapping("/registeredVerCode")
    @ApiOperation("获取验证码")
    public ResultVO<Object> registeredVerCode(@RequestBody HashMap<String, String> jsonData){
        String email = ParamUtil.getHashMapValue(jsonData, "email");
        if (ObjectUtils.isNotEmpty(email)) {
            FeignDataDTO<String> stringFeignDataDTO = userService.registeredVerCode(email);
            if (ObjectUtils.isNotEmpty(stringFeignDataDTO) &&
                    Constants.FEIGN_STATE_SUCCESS.equals(stringFeignDataDTO.getState())) {
                return ResultVO.ok(stringFeignDataDTO.getResult());
            } else {
                return ResultVO.status(ResultEnum.RESULT_FEIGN_EMPTY, stringFeignDataDTO);
            }
        } else {
            return ResultVO.status(ResultEnum.PARAM_EMPTY);
        }
    }

    /**
     * 注册
     * @param jsonData 参数
     * @return 结果集
     */
    @PostMapping("/registered")
    @ApiOperation("用户注册")
    public ResultVO<Object> registered(@RequestBody HashMap<String, String> jsonData) {
        String userName = ParamUtil.getHashMapValue(jsonData, "user_name");
        String email = ParamUtil.getHashMapValue(jsonData, "email");
        String password = ParamUtil.getHashMapValue(jsonData,"password");
        String passwordOk = ParamUtil.getHashMapValue(jsonData,"password_ok");
        String verCode = ParamUtil.getHashMapValue(jsonData, "ver_code");
        if (ParamUtil.isNotEmpty(true, userName, password, passwordOk, verCode)) {
            FeignDataDTO<String> feignDataDTO = userService.registered(userName, email, password, passwordOk, verCode);
            if (Constants.FeignState.STATE_SUCCESS.equals(feignDataDTO.getState())) {
                return ResultVO.ok();
            } else {
                return ResultVO.status(ResultEnum.RESULT_FEIGN_EMPTY, feignDataDTO);
            }
        } else {
            return ResultVO.status(ResultEnum.PARAM_EMPTY);
        }
    }

    /**
     * 用户注销验证码
     * @param jsonData 参数
     * @return 结果集
     */
    @RequestMapping("/deleteUserVerCode")
    @ApiOperation("用户注销获取验证码")
    public ResultVO<String> deleteUserVerCode(@RequestBody HashMap<String, String> jsonData) {
        String email = ParamUtil.getHashMapValue(jsonData, "email");
        if (ParamUtil.isNotEmpty(true, email)) {
            FeignDataDTO<String> stringFeignDataDTO = userService.deleteUserVerCode(email);
            if (Constants.FEIGN_STATE_SUCCESS.equals(stringFeignDataDTO.getState())) {
                return ResultVO.ok(stringFeignDataDTO.getResult());
            } else {
                return ResultVO.status(ResultEnum.RESULT_FEIGN_EMPTY, stringFeignDataDTO);
            }
        } else {
            return ResultVO.status(ResultEnum.PARAM_EMPTY);
        }
    }

    /**
     * 用户注销
     * @param jsonData 参数
     * @return 结果集
     */
    @PostMapping("/deleteUser")
    @ApiOperation("用户注销")
    public ResultVO<String> deleteUser(@RequestBody HashMap<String, String> jsonData) {
        String email = ParamUtil.getHashMapValue(jsonData, "email");
        String password = ParamUtil.getHashMapValue(jsonData, "password");
        String verCode = ParamUtil.getHashMapValue(jsonData, "ver_code");
        if (ParamUtil.isNotEmpty(true, email, password, verCode)) {
            FeignDataDTO<String> stringFeignDataDTO = userService.deleteUser(email, password, verCode);
            if (Constants.FEIGN_STATE_SUCCESS.equals(stringFeignDataDTO.getState())) {
                return ResultVO.ok();
            } else {
                return ResultVO.status(ResultEnum.RESULT_FEIGN_EMPTY, stringFeignDataDTO);
            }
        } else {
            return ResultVO.status(ResultEnum.PARAM_EMPTY);
        }
    }

    /**
     * 更新用户信息
     * @param jsonData 参数
     * @return 结果集
     */
    @RequestMapping("/updateUser")
    @ApiOperation("更新用户信息")
    public ResultVO<String> updateUser(@RequestBody HashMap<String, String> jsonData) {
        String userName = ParamUtil.getHashMapValue(jsonData, "user_name");
        String password = ParamUtil.getHashMapValue(jsonData, "password");
        String passwordOld = ParamUtil.getHashMapValue(jsonData, "password_old");
        String passwordOk = ParamUtil.getHashMapValue(jsonData, "password_ok");
        String phone = ParamUtil.getHashMapValue(jsonData, "phone");
        String email = ParamUtil.getHashMapValue(jsonData, "email");
        String description = ParamUtil.getHashMapValue(jsonData, "description");
        if (ParamUtil.isNotEmpty(false, userName, passwordOld, password, passwordOk, phone, email, description)) {
            FeignDataDTO<String> stringFeignDataDTO = userService.updateUser(userName, passwordOld, password, passwordOk, phone, email, description);
            if (Constants.FeignState.STATE_SUCCESS.equals(stringFeignDataDTO.getState())) {
                return ResultVO.ok();
            } else {
                return ResultVO.status(ResultEnum.RESULT_FEIGN_EMPTY, stringFeignDataDTO);
            }
        } else {
            return ResultVO.status(ResultEnum.PARAM_EMPTY);
        }
    }

    /**
     * 用户登录
     * @param jsonData 参数
     * @return 结果集
     */
    @RequestMapping("login")
    @ApiOperation("用户登录")
    public ResultVO<User> login(@RequestBody HashMap<String, String> jsonData) {
        String email = ParamUtil.getHashMapValue(jsonData, "email");
        String password = ParamUtil.getHashMapValue(jsonData, "password");
        if (ParamUtil.isNotEmpty(true, email, password)) {
            FeignDataDTO<User> userFeignDataDTO = userService.login(email, password);
            if (Constants.FEIGN_STATE_SUCCESS.equals(userFeignDataDTO.getState())) {
                return ResultVO.ok(userFeignDataDTO, userFeignDataDTO.getResult());
            } else {
                return ResultVO.status(ResultEnum.RESULT_FEIGN_EMPTY, userFeignDataDTO);
            }
        } else {
            return ResultVO.status(ResultEnum.PARAM_EMPTY);
        }
    }

    /**
     * 用户退出登录
     * @param jsonData 参数
     * @return 结果集
     */
    @RequestMapping("logout")
    @ApiOperation("退出登录")
    public ResultVO<String> logout(@RequestBody HashMap<String, String> jsonData) {
        // todo-chen 23/4/1更新，不需要写，通过springSecurity直接从session中删除登录状态然后重定向到首页即可
        return null;
    }
}