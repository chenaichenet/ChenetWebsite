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
import pers.website.common.utils.ParamUtils;
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
        String email = ParamUtils.getHashMapValue(jsonData, "email");
        if (ObjectUtils.isNotEmpty(email)) {
            FeignDataDTO<String> verCodeDTO = userService.registeredVerCode(email);
            if (ObjectUtils.isNotEmpty(verCodeDTO) &&
                    Constants.FEIGN_STATE_SUCCESS.equals(verCodeDTO.getState())) {
                return ResultVO.ok(verCodeDTO.getResult());
            } else {
                return ResultVO.status(ResultEnum.RESULT_FEIGN_EMPTY, verCodeDTO);
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
        String userName = ParamUtils.getHashMapValue(jsonData, "user_name");
        String email = ParamUtils.getHashMapValue(jsonData, "email");
        String password = ParamUtils.getHashMapValue(jsonData,"password");
        String passwordOk = ParamUtils.getHashMapValue(jsonData,"password_ok");
        String verCode = ParamUtils.getHashMapValue(jsonData, "ver_code");
        if (ParamUtils.isNotEmpty(true, userName, password, passwordOk, verCode)) {
            FeignDataDTO<String> registeredDTO = userService.registered(userName, email, password, passwordOk, verCode);
            if (Constants.FEIGN_STATE_SUCCESS.equals(registeredDTO.getState())) {
                return ResultVO.ok();
            } else {
                return ResultVO.status(ResultEnum.RESULT_FEIGN_EMPTY, registeredDTO);
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
        String email = ParamUtils.getHashMapValue(jsonData, "email");
        if (ParamUtils.isNotEmpty(true, email)) {
            FeignDataDTO<String> verCodeDTO = userService.deleteUserVerCode(email);
            if (Constants.FEIGN_STATE_SUCCESS.equals(verCodeDTO.getState())) {
                return ResultVO.ok(verCodeDTO.getResult());
            } else {
                return ResultVO.status(ResultEnum.RESULT_FEIGN_EMPTY, verCodeDTO);
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
        String email = ParamUtils.getHashMapValue(jsonData, "email");
        String password = ParamUtils.getHashMapValue(jsonData, "password");
        String verCode = ParamUtils.getHashMapValue(jsonData, "ver_code");
        if (ParamUtils.isNotEmpty(true, email, password, verCode)) {
            FeignDataDTO<String> deleteDTO = userService.deleteUser(email, password, verCode);
            if (Constants.FEIGN_STATE_SUCCESS.equals(deleteDTO.getState())) {
                return ResultVO.ok();
            } else {
                return ResultVO.status(ResultEnum.RESULT_FEIGN_EMPTY, deleteDTO);
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
        String userName = ParamUtils.getHashMapValue(jsonData, "user_name");
        String password = ParamUtils.getHashMapValue(jsonData, "password");
        String passwordOld = ParamUtils.getHashMapValue(jsonData, "password_old");
        String passwordOk = ParamUtils.getHashMapValue(jsonData, "password_ok");
        String phone = ParamUtils.getHashMapValue(jsonData, "phone");
        String email = ParamUtils.getHashMapValue(jsonData, "email");
        String description = ParamUtils.getHashMapValue(jsonData, "description");
        if (ParamUtils.isNotEmpty(false, userName, passwordOld, password, passwordOk, phone, email, description)) {
            FeignDataDTO<String> updateDTO = userService.updateUser(userName, passwordOld, password, passwordOk, phone, email, description);
            if (Constants.FEIGN_STATE_SUCCESS.equals(updateDTO.getState())) {
                return ResultVO.ok();
            } else {
                return ResultVO.status(ResultEnum.RESULT_FEIGN_EMPTY, updateDTO);
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
        String email = ParamUtils.getHashMapValue(jsonData, "email");
        String password = ParamUtils.getHashMapValue(jsonData, "password");
        if (ParamUtils.isNotEmpty(true, email, password)) {
            FeignDataDTO<User> loginDTO = userService.login(email, password);
            if (Constants.FEIGN_STATE_SUCCESS.equals(loginDTO.getState())) {
                return ResultVO.ok(loginDTO, loginDTO.getResult());
            } else {
                return ResultVO.status(ResultEnum.RESULT_FEIGN_EMPTY, loginDTO);
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