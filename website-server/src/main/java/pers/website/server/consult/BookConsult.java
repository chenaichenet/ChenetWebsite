package pers.website.server.consult;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import pers.website.common.constants.Constants;
import pers.website.common.pojo.dto.FeignDataDTO;
import pers.website.common.pojo.po.Book;
import pers.website.server.service.BookService;

import java.util.List;

/**
 * 书籍相关远程请求
 *
 * @author ChenetChen
 * @since 2023/5/9 10:49
 */
@RestController
@RequestMapping("/book")
public class BookConsult {
    @Resource
    private BookService bookService;

    /**
     * 通过id获取书籍
     * @param bookId 书籍id
     * @return 书籍
     */
    @GetMapping("/getBook/{bookId}")
    public FeignDataDTO<Book> getBookById(@PathVariable int bookId) {
        Book book = bookService.getBookById(bookId);
        if (book != null) {
            return FeignDataDTO.<Book>builder().state(Constants.FEIGN_STATE_SUCCESS).message("获取成功").result(book).build();
        } else {
            return FeignDataDTO.<Book>builder().state(Constants.FEIGN_STATE_ERROR).message("没有该书籍 ").build();
        }
    }
    
    /**
     * 添加书籍
     * @param bookName  书籍名称
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
    @PostMapping("/addBook")
    public FeignDataDTO<Integer> addBook(String bookName, String bookAuthor, String authorCountry,
                                            String pressName, String pressDate, String bookDescription,
                                            int typeId, int userId, int fileId) {
        Integer insertBook = bookService.addBook(bookName, bookAuthor, authorCountry,
                pressName, pressDate, bookDescription,
                typeId, userId, fileId);
        if (insertBook != 0) {
            return FeignDataDTO.<Integer>builder().state(Constants.FEIGN_STATE_SUCCESS).message("添加书籍成功").build();
        } else {
            return FeignDataDTO.<Integer>builder().state(Constants.FEIGN_STATE_ERROR).message("添加书籍失败").build();
        }
    }

    /**
     * 获取热门书籍
     * @param size 列表大小
     * @return 书籍
     */
    @PostMapping("/getHotBooks")
    public FeignDataDTO<List<Book>> getHotBooks(int size) {
        List<Book> hotBooks = bookService.getHotBooks(size);
        if (hotBooks.size() != 0) {
            return FeignDataDTO.<List<Book>>builder().state(Constants.FEIGN_STATE_SUCCESS).message("获取热门书籍成功").result(hotBooks).build();
        } else {
            return FeignDataDTO.<List<Book>>builder().state(Constants.FEIGN_STATE_EMPTY).message("热门书籍列表为空").build();
        }
    }
}
