package pers.website.common.utils;

import org.apache.commons.lang3.ObjectUtils;
import pers.website.common.exceptions.CustomException;

import java.util.HashMap;
import java.util.Random;

/**
 * 参数工具类
 *
 * @author ChenetChen
 * @since 2023/3/7 14:03
 */
public class ParamUtil {
    
    /**
     * 从HashMap结构体中获取对应key的值
     * @param hashMap hashMap结构对象
     * @param key 对应key
     * @param defaultValue 默认值
     * @return 对应值
     */
    public static String getHashMapValue(HashMap<String, String> hashMap, String key, String defaultValue) {
        String result = defaultValue;
        if (!hashMap.isEmpty() && !key.isEmpty()) {
            result = hashMap.get(key);
        }
        return result;
    }
    
    /**
     * 从HashMap结构体中获取对应key的值，默认为”“
     * @param hashMap hashMap结构对象
     * @param key 对应key
     * @return 对应值
     */
    public static String getHashMapValue(HashMap<String, String> hashMap, String key) {
        return getHashMapValue(hashMap, key, "");
    }

    /**
     * 判断参数是否不为空
     * @param all 是否全部校验
     * <pre>true时，表示参数全部都不能为空</pre>
     * <pre>false时，表示只要有一个参数不为空即可</pre>
     * @param objects 参数
     * @return 判断结果
     */
    public static boolean isNotEmpty(boolean all, Object... objects) {
        boolean flag = false;
        if (objects != null) {
            for (Object object : objects) {
                flag = ObjectUtils.isEmpty(object);
                if (all && flag) {
                    break; // 参数为空跳出循环
                }
                if (!all && !flag) {
                    break; // 参数不为空时跳出循环
                }
            }
            flag = !flag;
        }
        return flag;
    }

    /**
     * 生成随机验证码
     * @param length 验证码长度
     * @return 验证码
     */
    public static String newVerCode(int length) {
        StringBuilder verCode = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(charOrNum)) {
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                verCode.append((char) (choice + random.nextInt(26)));
            } else {
                verCode.append(random.nextInt(10));
            }
        }
        return verCode.toString();
    }
}
