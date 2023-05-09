package pers.website.web.controller;

import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import pers.website.common.constants.Constants;
import pers.website.common.enums.ResultEnum;
import pers.website.common.pojo.dto.FeignDataDTO;
import pers.website.common.pojo.po.Book;
import pers.website.common.pojo.vo.ResultVO;
import pers.website.common.utils.ParamUtils;
import pers.website.web.service.BookService;

import java.util.HashMap;
import java.util.List;

/**
 * 书籍相关请求处理类
 *
 * @author ChenetChen
 * @since 2023/5/9 11:05
 */
@Slf4j
@RestController
@RequestMapping("/book")
@Api("书籍相关请求处理类")
public class BookController {
    @Resource
    private BookService bookService;

    /**
     * 通过id获取书籍
     * @param bookId 书籍id
     * @return 书籍
     */
    @GetMapping("/getBook/{bookId}")
    public ResultVO<Book> getBookById(@PathVariable int bookId) {
        if (ObjectUtils.isNotEmpty(bookId)) {
            FeignDataDTO<Book> bookDTO = bookService.getBookById(bookId);
            return ResultVO.ok(bookDTO, bookDTO.getResult());
        } else {
            return ResultVO.status(ResultEnum.PARAM_EMPTY);
        }
    }

    /**
     * 添加书籍
     * @param jsonData 参数
     * @return 添加结果
     */
    @PostMapping("/addBook")
    public ResultVO<Integer> addBook(@RequestBody HashMap<String, String> jsonData) {
        String bookName = ParamUtils.getHashMapValue(jsonData, "bookName");
        String bookAuthor = ParamUtils.getHashMapValue(jsonData, "bookAuthor");
        String authorCountry = ParamUtils.getHashMapValue(jsonData, "authorCountry");
        String pressName = ParamUtils.getHashMapValue(jsonData, "pressName");
        String pressDate = ParamUtils.getHashMapValue(jsonData, "pressDate");
        String bookDescription = ParamUtils.getHashMapValue(jsonData, "bookDescription");
        String typeId = ParamUtils.getHashMapValue(jsonData, "typeId");
        String userId = ParamUtils.getHashMapValue(jsonData, "userId");
        String fileId = ParamUtils.getHashMapValue(jsonData, "fileId");
        if (ParamUtils.isNotEmpty(false, bookName, bookAuthor, authorCountry, pressName, pressDate, bookDescription, fileId) && 
                ParamUtils.isNotEmpty(true, typeId, userId)) {
            FeignDataDTO<Integer> addDTO = bookService.addBook(bookName, bookAuthor, authorCountry,
                    pressName, pressDate, bookDescription,
                    Integer.parseInt(typeId), Integer.parseInt(userId), Integer.parseInt(fileId));
            if (Constants.FEIGN_STATE_SUCCESS.equals(addDTO.getState())) {
                return ResultVO.ok();
            } else {
                return ResultVO.ok(addDTO);
            }
        } else {
            return ResultVO.status(ResultEnum.PARAM_EMPTY);
        }
    }
    
    /**
     * 获取热门书籍
     * @param jsonData 参数
     * @return 书籍
     */
    @PostMapping("/getHotBooks")
    public ResultVO<List<Book>> getHotBooks(@RequestBody HashMap<String, String> jsonData) {
        String size = ParamUtils.getHashMapValue(jsonData, "size");
        if (ParamUtils.isNotEmpty(true, size)) {
            FeignDataDTO<List<Book>> hotBooksDTO = bookService.getHotBooks(Integer.parseInt(size));
            return ResultVO.ok(hotBooksDTO, hotBooksDTO.getResult());
        } else {
            return ResultVO.status(ResultEnum.PARAM_EMPTY);
        }
    }
}
