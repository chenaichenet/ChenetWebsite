package pers.website.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import pers.website.common.constants.Constants;
import pers.website.common.dao.BookDao;
import pers.website.common.enums.CountryEnum;
import pers.website.common.pojo.po.Book;
import pers.website.common.utils.ParamUtils;
import pers.website.common.utils.RedisUtils;
import pers.website.common.utils.WordFilterUtils;
import pers.website.server.service.BookService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenetChen
 * @since 2023/5/9 14:47
 */
@Slf4j
@Service
public class BookServiceImpl implements BookService {
    @Resource
    private BookDao bookDao;
    
    @Resource
    private RedisUtils redisUtils;
    
    @Resource
    private WordFilterUtils wordFilterUtils;
    
    /**
     * 通过id获取书籍
     * @param bookId 书籍id
     * @return 书籍
     */
    @Override
    public Book getBookById(int bookId) {
        return bookDao.selectById(bookId);
    }

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
    @Override
    public Integer addBook(String bookName, String bookAuthor, String authorCountry,
                           String pressName, String pressDate, String bookDescription,
                           int typeId, int userId, int fileId) {
        // 对简介中的敏感词进行过滤
        if (ObjectUtils.isNotEmpty(bookDescription)) {
            bookDescription = wordFilterUtils.filterWord(bookDescription);
        }
        Book book = Book.builder()
                .bookName(bookName)
                .bookAuthor(bookAuthor)
                .authorCountry(authorCountry)
                .pressName(pressName)
                .pressDate(pressDate)
                .bookDescription(bookDescription)
                .typeId(typeId)
                .userId(userId)
                .fileId(fileId)
                .build();
        return bookDao.insert(book);
    }

    /**
     * 获取热门书籍
     * @param size 列表大小
     * @return 书籍
     */
    @Override
    public List<Book> getHotBooks(int size) {
        List<Book> hotBooks = new ArrayList<>();
        Long redisSize = redisUtils.listSize(Constants.RedisKey.HOT_BOOKS);
        if (redisSize > 0) {
            List<String> redisHotBooks = redisUtils.listRange(Constants.RedisKey.HOT_BOOKS, 0, -1);
            for (String strHotBook : redisHotBooks) {
                hotBooks.add(JSON.parseObject(strHotBook, Book.class));
            }
        } else {
            hotBooks = bookDao.selectList(new QueryWrapper<Book>().lambda()
                    .orderByDesc(Book::getViews)
                    .orderByDesc(Book::getDownloadVolumes)); 
            if (hotBooks.size() != 0) {
                for (Book hotBook : hotBooks) {
                    Boolean setStatus = redisUtils.lSet(Constants.RedisKey.HOT_BOOKS, Constants.RedisKey.REDIS_EXPIRE_TIME, JSON.toJSONString(hotBook));
                    if (!setStatus) {
                        log.error("热门书籍缓存设置失败");
                    }
                }
            }
        }
        return hotBooks;
    }
}
