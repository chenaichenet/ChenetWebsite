package pers.website.common.utils;

import lombok.extern.slf4j.Slf4j;
import pers.website.common.exceptions.CustomException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * 加密类
 *
 * @author ChenetChen
 * @since 2023/3/7 12:47
 */
@Slf4j
public class DesUtil {
    private static final String STR_DEFAULT_KEY = "DbI9c3N2j8y6x0QxZwP1kHSm";
    private static final Cipher ENCRYPT_CIPHER;
    private static final Cipher DECRYPT_CIPHER;

    static {
        try {
            Key key = getKey(STR_DEFAULT_KEY.getBytes());
            ENCRYPT_CIPHER = Cipher.getInstance("DES");
            ENCRYPT_CIPHER.init(1, key);
            DECRYPT_CIPHER = Cipher.getInstance("DES");
            DECRYPT_CIPHER.init(2, key);
        } catch (Exception e) {
            log.error("初始化Cipher密码器失败");
            throw new CustomException("初始化Cipher密码器失败");
        }
    }

    public static String byteArray2HexStr(byte[] paramArrayOfByte) {
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

    public static byte[] hexStr2ByteArray(String paramString) {
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

    private static Key getKey(byte[] paramArrayOfByte) {
        byte[] arrayOfByte = new byte[8];
        for (byte b = 0; b < paramArrayOfByte.length && b < arrayOfByte.length; b++) {
            arrayOfByte[b] = paramArrayOfByte[b];
        }
        return new SecretKeySpec(arrayOfByte, "DES");
    }

    public static byte[] encrypt(byte[] paramArrayOfByte) {
        byte[] arrayOfByte = null;
        try {
            arrayOfByte = ENCRYPT_CIPHER.doFinal(paramArrayOfByte);
        } catch (Exception exception) {
            log.error("加密失败");
        }
        return arrayOfByte;
    }

    public static String encrypt(String paramString) {
        return byteArray2HexStr(encrypt(paramString.getBytes()));
    }

    public static byte[] decrypt(byte[] paramArrayOfByte) {
        byte[] arrayOfByte = null;
        try {
            arrayOfByte = DECRYPT_CIPHER.doFinal(paramArrayOfByte);
        } catch (Exception exception) {
            log.error("解密失败");
        }
        return arrayOfByte;
    }

    public static String decrypt(String paramString) {
        return new String(decrypt(hexStr2ByteArray(paramString)));
    }
    
}
