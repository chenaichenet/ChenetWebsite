package pers.website.web.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pers.website.common.pojo.dto.FeignDataDTO;
import pers.website.common.pojo.po.Book;
import pers.website.common.pojo.po.User;

import java.util.List;

/**
 * 远程调用服务接口
 * 
 * @author ChenetChen
 * @since 2023/3/7 15:49
 */
@FeignClient(name = "Website-Server", fallback = ServerFeignClientFallBack.class)
public interface ServerFeignClient {
    /**
     * 注册验证码
     * @param email 邮箱
     * @return 验证码
     */
    @PostMapping("/user/registeredVerCode")
    FeignDataDTO<String> registeredVerCode(@RequestParam("email") String email);

    /**
     * 用户注册
     * @param userName 用户名
     * @param email 邮箱
     * @param password 密码
     * @param verCode 验证码
     * @return 注册结果
     */
    @PostMapping("/user/registered")
    FeignDataDTO<String> registered(@RequestParam("userName") String userName,
                                    @RequestParam("email") String email,
                                    @RequestParam("password") String password,
                                    @RequestParam("verCode") String verCode);

    /**
     * 用户注销验证码
     * @param email 邮箱
     * @return 验证码
     */
    @PostMapping("/user/deleteUserVerCode")
    FeignDataDTO<String> deleteUserVerCode(@RequestParam("email") String email);

    /**
     * 用户注销
     * @param email 邮箱
     * @param password 密码
     * @param verCode 验证码
     * @return 注销结果
     */
    @PostMapping("/user/deleteUser")
    FeignDataDTO<String> deleteUser(@RequestParam("email") String email,
                                    @RequestParam("password") String password,
                                    @RequestParam("verCode") String verCode);

    /**
     * 用户修改信息
     * @param userName 用户名
     * @param passwordOld 旧密码
     * @param password 密码
     * @param passwordOk 确认密码
     * @param phone 手机号
     * @param email 邮箱
     * @param description 简介
     * @return 修改结果
     */
    @PostMapping("/user/updateUser")
    FeignDataDTO<String> updateUser(@RequestParam("userName") String userName,
                                    @RequestParam("passwordOld") String passwordOld,
                                    @RequestParam("password") String password,
                                    @RequestParam("passwordOk") String passwordOk,
                                    @RequestParam("phone") String phone,
                                    @RequestParam("email") String email,
                                    @RequestParam("description") String description);

    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @return 结果集
     */
    @PostMapping("/user/login")
    FeignDataDTO<User> login(@RequestParam("email") String email,
                             @RequestParam("password") String password);

    /**
     * SpringSecurity校验
     * @param userName 用户名
     * @return 用户
     */
    @PostMapping(value = "/user/loadUserByUsername")
    FeignDataDTO<? super User> loadUserByUsername(@RequestParam("userName") String userName);
    
    //=====Book=====

    /**
     * 通过id获取书籍
     * @param bookId 书籍id
     * @return 书籍
     */
    @GetMapping("/book/getBook/{bookId}")
    FeignDataDTO<Book> getBookById(@PathVariable int bookId);

    /**
     * 添加书籍
     * @param bookName 书籍名称
     * @param bookAuthor 书籍作者
     * @param authorCountry 作者国籍
     * @param pressName 出版社
     * @param pressDate 出版日期
     * @param bookDescription 书籍简介
     * @param typeId 类型id
     * @param userId 上传用户id
     * @param fileId 文件id
     * @return 上传结果
     */
    @PostMapping("/book/addBook")
    FeignDataDTO<Integer> addBook(@RequestParam("bookName") String bookName,
                                  @RequestParam("bookAuthor") String bookAuthor,
                                  @RequestParam("authorCountry") String authorCountry,
                                  @RequestParam("pressName") String pressName,
                                  @RequestParam("pressDate") String pressDate,
                                  @RequestParam("bookdescription") String bookDescription,
                                  @RequestParam("typeId") int typeId,
                                  @RequestParam("userId") int userId,
                                  @RequestParam("fileId") int fileId);
    
    /**
     * 获取热门书籍
     * @param size 热门列表大小
     * @return 热门书籍
     */
    @RequestMapping(value = "/book/getHotBooks")
    FeignDataDTO<List<Book>> getHotBooks(@RequestParam("size") int size);
}
