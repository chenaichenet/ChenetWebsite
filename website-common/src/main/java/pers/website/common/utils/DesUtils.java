package pers.website.common.utils;

import lombok.extern.slf4j.Slf4j;
import pers.website.common.constants.Constants;
import pers.website.common.exceptions.CustomException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 *
 * @author ChenetChen
 * @since 2023/3/7 12:47
 */
@Slf4j
public class DesUtils {
    /**
     * DES加解密
     * @param str 待加密数据
     * @param mode 模式
     * <pre>为1时，加密；为2时，解密</pre>
     * @return 加解密后的byte数组
     */
    public static String desCrypt(String str, int mode) {
        byte[] result = new byte[0];
        SecretKey secretKey = initKey();
        Cipher desCipher = getCipher(mode, secretKey);
        try {
            if (desCipher != null) {
                result = desCipher.doFinal(str.getBytes());
            }
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("DES加解密异常", e);
            throw new CustomException("DES加解密异常");
        }
        return byteArrayToHexString(result);
    }

    /**
     * byte数组转为16进制字符串
     * @param paramArrayOfByte byte数组
     * @return 16进制字符串
     */
    public static String byteArrayToHexString(byte[] paramArrayOfByte) {
        int i = paramArrayOfByte.length;
        StringBuilder stringBuilder = new StringBuilder(i * 2);
        for (int k : paramArrayOfByte) {
            int j = k;
            while (j < 0) {
                j = j + 256;
            }
            if (j < 16) {
                stringBuilder.append("0");
            }
            stringBuilder.append(Integer.toString(j, 16));
        }
        return stringBuilder.toString();
    }

    /**
     * 16进制字符串转byte数组
     * @param paramString 16进制字符串
     * @return byte数组
     */
    public static byte[] hexStringToByteArray(String paramString) {
        byte[] arrayOfByte1 = paramString.getBytes();
        int i = arrayOfByte1.length;
        byte[] arrayOfByte2 = new byte[i / 2];
        int j;
        for (j = 0; j < i; j += 2) {
            String str = new String(arrayOfByte1, j, 2);
            arrayOfByte2[j / 2] = (byte)Integer.parseInt(str, 16);
        }
        return arrayOfByte2;
    }
    
    /**
     * 创建密码类对象
     * <pre>
     *     Cipher通过getInstance方法获取实例时，其中的参数为 “加密算法/反馈模式/填充方案”
     *     其中加密算法是必须参数，其他可以使用默认值
     *     加密算法：AES、DES、DESede、RSA
     *     反馈模式：CBC（有向量模式）、ECB（无向量模式）
     *     填充方案：常用的两个——NoPadding（加密内容不足8位用0补足，需自己实现）；PKCS5Padding（加密内容不足8位用余数补足）
     * </pre>
     *
     * @param mode      模式
     * @param secretKey 密钥
     * @return 密码类对象
     */
    private static Cipher getCipher(int mode, SecretKey secretKey) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES");
            switch (mode) {
                case 1: {
                    // 加密
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                } break;
                case 2: {
                    // 解密
                    cipher.init(Cipher.DECRYPT_MODE, secretKey);
                } break;
                default:break;
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.error("创建Cipher实例失败");
            throw new RuntimeException("创建Cipher实例失败");
        } catch (InvalidKeyException e) {
            log.error("初始化Cipher失败");
            log.error(e.getMessage());
            log.error(e.toString());
            throw new CustomException("初始化Cipher失败");
        }
        return cipher;
    }

    /**
     * 初始化密钥
     * @return SecretKey
     */
    private static SecretKey initKey() {
        byte[] keyBytes = Constants.DesUtilsConf.STR_KEY.getBytes();
        byte[] tempKey = new byte[8];
        if (tempKey.length > keyBytes.length) {
            int b = tempKey.length / keyBytes.length;
            int rem = tempKey.length % keyBytes.length;
            for (int i = 0; i < b; i++) {
                System.arraycopy(keyBytes, 0, tempKey, i * keyBytes.length, keyBytes.length);
            }
            if (rem > 0) {
                System.arraycopy(keyBytes, 0, tempKey, b * keyBytes.length, rem);
            }
        } else {
            System.arraycopy(keyBytes, 0, tempKey, 0, tempKey.length);
        }
        return new SecretKeySpec(tempKey, "DES");
    }
}
