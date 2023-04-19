package pers.website.common.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
public class WordFilterUtil {
    @Resource
    private RedisUtil redisUtil;

    private static final String BAD_WORD_KEY = "BadWord";
    private static final int MIN_MATCH_TYPE = 1;
    private static final int MAX_MATCH_TYPE = 2;

    private HashMap<String, String> badWordMap;

    /**
     * 字符串过滤
     *
     * @param str 原始字符串
     * @return 过滤后字符串
     */
    public String filterWord(String str) {
        Set<String> badWordSet = getBadWordSet();
        toHashMap(badWordSet);
        return replaceBadWord(str, MIN_MATCH_TYPE, "*");
    }

    /**
     * 从Redis中获取敏感词列表
     *
     * @return 敏感词列表
     */
    private Set<String> getBadWordSet() {
        Set<String> bakWordSet = redisUtil.setMembers(BAD_WORD_KEY);
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
        Long setSize = redisUtil.setAdd(BAD_WORD_KEY, badWords);
        if (setSize.intValue() == badWords.length) {
            log.info("Redis中敏感词库新增成功");
            return true;
        } else {
            log.error("Redis中敏感词库新增失败");
            return false;
        }
    }

    /**
     * 讲敏感词库构建成类似树的结果，减少检索的匹配范围
     *
     * @param badWordSet 敏感词集合
     */
    private void toHashMap(Set<String> badWordSet) {
        HashMap<String, String> badWordMap = new HashMap<>(badWordSet.size());
        String key;
        Map nowMap;
        HashMap<String, String> newWorMap;
        for (String badWord : badWordSet) {
            key = badWord;
            nowMap = badWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);
                Object map = nowMap.get(keyChar);

                if (map != null) {
                    nowMap = (Map) map;
                } else {
                    newWorMap = new HashMap<>();
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");
                }
            }
        }
        this.badWordMap = badWordMap;
    }

    /**
     * 替换敏感词
     *
     * @param str         原始字符串
     * @param matchType   匹配规则
     * @param replaceChar 替换字符串
     * @return 替换结果字符串
     */
    private String replaceBadWord(String str, int matchType, String replaceChar) {
        String result = str;
        Set<String> badWordSet = getBadWord(str, matchType);
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
     * @param str        原始字符
     * @param beginIndex 开始标记
     * @param matchType  匹配规则
     * @return 敏感词字符长度，不存在返回0
     */
    private int checkBadWord(String str, int beginIndex, int matchType) {
        boolean flag = false;
        // 匹配标识数默认为0
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
