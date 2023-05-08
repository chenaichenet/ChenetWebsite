package pers.website.common.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static pers.website.common.constants.Constants.RedisKey.REDIS_BAD_WORD;
import static pers.website.common.constants.Constants.WordFilterUtilsConf.*;

/**
 * 词语脱敏工具类
 *  <pre>使用{@link #filterWord(String)}方法进行脱敏</pre>
 *  <pre>使用{@link #setBadWordSet(String...)}方法对redis中的敏感词库进行新增</pre>
 *
 * @author ChenetChen
 * @since 2023/3/7 14:17
 */
@Slf4j
@Component
public class WordFilterUtils {
    @Resource
    private RedisUtils redisUtil;

    private HashMap<String, String> badWordMap;

    /**
     * 字符串过滤
     *
     * @param str 原始字符串
     * @return 过滤后字符串
     */
    public String filterWord(String str) {
        if (badWordMap.isEmpty()) {
            Set<String> badWordSet = getBadWordSet();
            toHashMap(badWordSet);
        }
        return replaceBadWord(str, REPLACE_CHAR);
    }

    /**
     * 从Redis中获取敏感词列表
     *
     * @return 敏感词列表
     */
    private Set<String> getBadWordSet() {
        Set<String> bakWordSet = redisUtil.setMembers(REDIS_BAD_WORD);
        if (bakWordSet.isEmpty()) {
            log.info("Redis中敏感词库为空");
        }
        return bakWordSet;
    }

    /**
     * 向Redis中新增敏感词
     *
     * @param badWords 敏感词
     */
    private Boolean setBadWordSet(String... badWords) {
        Long setSize = redisUtil.setAdd(REDIS_BAD_WORD, badWords);
        if (setSize.intValue() == badWords.length) {
            log.info("Redis中敏感词库新增成功");
            return true;
        } else {
            log.error("Redis中敏感词库新增失败");
            return false;
        }
    }

    /**
     * 将敏感词库构建成类似树的结果，减少检索的匹配范围
     * 最终会通过单个字符来形成一个树结构，通过匹配key，以及最后的结束标识来判断是否命中
     * 
     * @param badWordSet 敏感词集合
     */
    @SuppressWarnings({"rawtypes","unchecked"})
    private void toHashMap(Set<String> badWordSet) {
        badWordMap = new HashMap<>(badWordSet.size());
        Map tempMap;
        HashMap<String, String> sonMap;
        for (String badWord : badWordSet) {
            tempMap = badWordMap;
            for (int i = 0; i < badWord.length(); i++) {
                char charKey = badWord.charAt(i);
                // 获取子树
                Object map = tempMap.get(charKey);

                if (map != null) {
                    // 如果子树存在，直接赋值
                    tempMap = (Map) map;
                } else {
                    // 如果子树不存在，则构建一个map，并将结束标志置为0
                    sonMap = new HashMap<>();
                    sonMap.put("isEnd", "0");
                    tempMap.put(charKey, sonMap);
                    tempMap = sonMap;
                }
                if (i == badWord.length() - 1) {
                    tempMap.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * 替换敏感词
     *
     * @param str         原始字符串
     * @param replaceChar 替换字符串
     * @return 替换结果字符串
     */
    private String replaceBadWord(String str, String replaceChar) {
        String result = str;
        Set<String> badWordSet = getBadWord(str, MAX_MATCH_TYPE);
        StringBuilder replaceString;
        for (String badWord : badWordSet) {
            replaceString = new StringBuilder(replaceChar);
            replaceString.append(replaceChar.repeat(Math.max(0, badWord.length() - 1)));
            result = result.replaceAll(badWord, replaceString.toString());
        }
        return result;
    }

    /**
     * 获取文字中的敏感词
     *
     * @param str       原始字符串
     * @param matchType 匹配规则
     * @return 存在的敏感词集合
     */
    private Set<String> getBadWord(String str, int matchType) {
        Set<String> wordSet = new HashSet<>();
        for (int i = 0; i < str.length(); i++) {
            int length = checkBadWord(str, i, matchType);
            if (length > 0) {
                wordSet.add(str.substring(i, i + length));
                i = i + length - 1;
            }
        }
        return wordSet;
    }

    /**
     * 检查文字中是否包含敏感词
     *
     * @param str 原始字符
     * @param beginIndex 开始标记
     * @param matchType  匹配规则
     * @return 敏感词字符长度，不存在返回0
     */
    @SuppressWarnings("rawtypes")
    private int checkBadWord(String str, int beginIndex, int matchType) {
        boolean flag = false;
        int matchFlag = 0;
        char word;
        Map nowMap = badWordMap;
        for (int i = beginIndex; i < str.length(); i++) {
            word = str.charAt(i);
            // 获取指定key
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {
                matchFlag++;
                // 存在，则判断是否为最后一个
                if ("1".equals(nowMap.get("isEnd"))) {
                    flag = true;
                    if (MIN_MATCH_TYPE == matchType) {
                        break;
                    }
                }
            } else {
                break;
            }
        }

        if (!flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }
}
