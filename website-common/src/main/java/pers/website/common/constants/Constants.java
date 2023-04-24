package pers.website.common.constants;

import java.awt.Color;

/**
 * 常量定义
 *
 * @author ChenetChen
 * @since 2023/3/7 14:32
 */
public interface Constants {
    /**
     * 远程调用成功
     */
    String FEIGN_STATE_SUCCESS = "1";
    /**
     * 远程调用成功，空结果
     */
    String FEIGN_STATE_EMPTY = "0";
    /**
     * 远程调用失败
     */
    String FEIGN_STATE_ERROR = "-1";
    
    /**
     * 远程调用返回默认状态值
     */
    class FeignState{
        /**
         * 远程调用成功
         */
        public static final String STATE_SUCCESS = "1";
        /**
         * 远程调用成功，空结果
         */
        public static final String STATE_EMPTY = "0";
        /**
         * 远程调用失败
         */
        public static final String STATE_ERROR = "-1";
    }

    /**
     * 邮件工具类常量
     * todo-chen 邮件配置应该放在nacos配置中心上进行存储，并且应该设置为热部署
     */
    class MailUtilConf {
        /**
         * 邮箱主机域名
         */
        public static final String MAIL_HOST = "";
        /**
         * 邮箱用户名
         */
        public static final String USERNAME = "";
        /**
         * 邮箱密码
         */
        public static final String PASSWORD = "";
        /**
         * 模板文件路径
         */
        public static final String TEMPLATES_PATH = "";
    }

    /**
     * IP工具类常量
     */
    class IpUtilConf {
        /**
         * 未知
         */
        public static final String UNKNOWN = "unknown";
        /**
         * IP长度
         */
        public static final int IP_LENGTH = 15;
    }

    /**
     * 图片工具类常量
     */
    class ImageUtilConf{
        /**
         * 旋转角度
         */
        public static final Integer MASK_DEGREE = -45;
        /**
         * 不透明度
         */
        public static final Float MASK_OPACITY = 0.5F;
        /**
         * 字体大小
         */
        public static final Integer FONT_SIZE = 20;
        /**
         * 颜色
         */
        public static final Color COLOR = Color.GRAY;
    }

    /**
     * Redis键名常量
     */
    class RedisKey {
        /**
         * 注册验证码键名
         */
        public static final String REDIS_REGISTER_VER_CODE = "REGISTER_VER_CODE";
        /**
         * 注销验证码键名
         */
        public static final String REDIS_DELETE_VER_CODE = "DELETE_VER_CODE";
    }
}
