package pers.website.common.constants;

/**
 * 常量定义
 *
 * @author ChenetChen
 * @since 2023/3/7 14:32
 */
public interface Constants {
    String FEIGN_STATE_SUCCESS = "1";
    String FEIGN_STATE_EMPTY = "0";
    String FEIGN_STATE_ERROR = "-1";
    
    /**
     * 远程调用返回默认状态值
     */
    class FeignState{
        public static final String STATE_SUCCESS = "1";
        public static final String STATE_EMPTY = "0";
        public static final String STATE_ERROR = "-1";
    }

    /**
     * 邮件工具类常量
     * todo-chen 邮件配置应该放在nacos配置中心上进行存储，并且应该设置为热部署
     */
    class MailConf {
        public static final String MAIL_HOST = "";
        public static final String USERNAME = "";
        public static final String PASSWORD = "";
        public static final String TEMPLATES_PATH = "";
    }

    /**
     * IP工具类常量
     */
    class IpConf {
        public static final String UNKNOWN = "unknown";
        public static final int IP_LENGTH = 15;
    }
    
    class RedisKey {
        public static final String REDIS_REGISTER_VER_CODE = "REGISTER_VER_CODE";
        public static final String REDIS_DELETE_VER_CODE = "DELETE_VER_CODE";
    }
}
