package pers.website.common.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import pers.website.common.enums.ExceptionEnum;
import pers.website.common.exceptions.CustomException;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author ChenetChen
 * @since 2023/3/7 14:15
 */
@Slf4j
@Component
public class RedisUtils {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public RedisUtils() {
    }

    public RedisUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public void expire(String key, long time) {
        try {
            if (time > 0) {
                stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            throw new CustomException(ExceptionEnum.REDIS_EXCEPTION);
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 0：代表为永久有效
     */
    public Long getExpire(String key) {
        if (ObjectUtils.isNotEmpty(key) && Boolean.TRUE.equals(hasKey(key))) {
            return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        } else {
            return null;
        }
    }

    /**
     * 判断field是否存在
     *
     * @param key   键
     * @param field hash中的键
     * @return 判断结果
     */
    public Boolean hasField(String key, String field) {
        if (Boolean.TRUE.equals(hasKey(key))) {
            return stringRedisTemplate.opsForHash().hasKey(key, field);
        } else {
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param strArrayKey 可以传一个值 或多个
     */
    public void delete(String... strArrayKey) {
        try {
            if (strArrayKey != null && strArrayKey.length > 0) {
                if (strArrayKey.length == 1) {
                    stringRedisTemplate.delete(strArrayKey[0]);
                } else {
                    stringRedisTemplate.delete(Arrays.asList(strArrayKey));
                }
            }
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            throw new CustomException(ExceptionEnum.REDIS_EXCEPTION);
        }
    }

    // 游标查询

    /**
     * 创建游标
     *
     * @param pattern 匹配
     * @param limit   步长
     * @return 游标对象
     */
    @SuppressWarnings({"unchecked","deprecation"})
    public Cursor<String> createScan(String pattern, int limit) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(limit).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
        return stringRedisTemplate.executeWithStickyConnection(redisConnection ->
                new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize));
    }

    /**
     * 通过关键字匹配所有的key和value
     *
     * @param pattern 关键字
     * @param limit   长度
     * @return map
     */
    public Map<Object, Object> scanByPattern(String pattern, int limit) {
        // 调用scan查出所有key
        Cursor<String> cursor = createScan(pattern, limit);
        if (null != cursor) {
            Map<Object, Object> result = new HashMap<>();
            while (cursor.hasNext()) {
                String key = cursor.next();
                DataType type = stringRedisTemplate.type(key);
                switch (Objects.requireNonNull(type)) {
                    case STRING:
                        String value = stringRedisTemplate.opsForValue().get(key);
                        result.put(key, value);
                        break;
                    case HASH:
                        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
                        result.put(key, entries);
                        break;
                    case LIST:
                        List<String> range = stringRedisTemplate.opsForList().range(key, 0, -1);
                        result.put(key, range);
                        break;
                    case SET:
                        Set<String> members = stringRedisTemplate.opsForSet().members(key);
                        result.put(key, members);
                        break;
                    case ZSET:
                        Set<String> zrange = stringRedisTemplate.opsForZSet().range(key, 0, -1);
                        result.put(key, zrange);
                        break;
                    default:
                        log.error("目前不支持该格式");
                }
            }
            cursor.close();
            return result;
        } else {
            log.info("创建游标异常，无法匹配到对应的Redis值");
            return Collections.emptyMap();
        }
    }

    //=====String=====

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public Boolean set(String key, Object value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value.toString());
            return true;
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public Boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                stringRedisTemplate.opsForValue().set(key, value.toString(), time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return false;
        }
    }

    /**
     * 通过关键字匹配key
     *
     * @param pattern 关键字
     * @param limit   步长
     * @return key列表
     */
    public List<String> scanKey(String pattern, int limit) {
        List<String> result = new ArrayList<>();
        try (Cursor<String> cursor = createScan(pattern, limit)) {
            while (cursor.hasNext()) {
                result.add(cursor.next());
            }
        }
        return result;
    }

    //=====HashMap=====

    /**
     * 获取HashMap缓存
     *
     * @param key     键
     * @param hashKey hash键
     * @return 获取结果
     */
    public Object hashGet(String key, String hashKey) {
        return stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取HashMap中的所有值
     *
     * @param key 键
     * @return map对象
     */
    public Map<Object, Object> hashEntries(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    /**
     * HashMap缓存存入
     *
     * @param key 键
     * @param map 对象
     * @return 执行结果
     */
    public Boolean hashPutAll(String key, Map<String, Object> map) {
        try {
            stringRedisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return false;
        }
    }

    /**
     * HashMap缓存存入并设置过期时间
     *
     * @param key  键
     * @param map  map对象
     * @param time 过期时间
     * @return 执行结果
     */
    public Boolean hashPutAll(String key, Map<String, Object> map, long time) {
        try {
            stringRedisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return false;
        }
    }

    /**
     * 向对应hash表中存入数据，不存在则创建
     *
     * @param key     键
     * @param hashKey hash键
     * @param value   值
     * @return 执行结果
     */
    public Boolean hashPut(String key, String hashKey, Object value) {
        try {
            stringRedisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return false;
        }
    }

    /**
     * 向对应hash表中存入数据，不存在则创建
     *
     * @param key     键
     * @param hashKey hash键
     * @param value   值
     * @param time    过期时间
     * @return 执行结果
     */
    public Boolean hashPut(String key, String hashKey, Object value, long time) {
        try {
            stringRedisTemplate.opsForHash().put(key, hashKey, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hashDelete(String key, Object... item) {
        stringRedisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key     键 不能为null
     * @param hashKey 项 不能为null
     * @return true 存在 false不存在
     */
    public Boolean hashHasKey(String key, String hashKey) {
        return stringRedisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key     键
     * @param hashKey 项
     * @param delta   要增加几(大于0)
     * @return 结果
     */
    public double hashIncrement(String key, String hashKey, double delta) {
        return stringRedisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * hash递减
     *
     * @param key     键
     * @param hashKey 项
     * @param delta   要减少记(小于0)
     * @return 结果
     */
    public double hashDecrement(String key, String hashKey, double delta) {
        return stringRedisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    /**
     * 查找指定Hash的指定字段
     *
     * @param key     键
     * @param pattern 匹配字符
     * @param count   游标步长
     * @return 结果集
     */
    public Object hashScan(String key, String pattern, int count) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(count).build();
        return stringRedisTemplate.opsForHash().scan(key, options);
    }

    //=====Set=====

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return 结果集
     */
    public Set<String> setMembers(String key) {
        try {
            return stringRedisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return Collections.emptySet();
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public Boolean setHasKey(String key, Object value) {
        try {
            return stringRedisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long setAdd(String key, String... values) {
        try {
            return stringRedisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return 0L;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long setAdd(String key, long time, String... values) {
        try {
            Long count = stringRedisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return 0L;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public Long setSize(String key) {
        try {
            return stringRedisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return 0L;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setRemove(String key, Object... values) {
        try {
            return stringRedisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return 0L;
        }
    }

    //=====List=====

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return 结果
     */
    public List<String> listRange(String key, long start, long end) {
        try {
            return stringRedisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public Long listSize(String key) {
        try {
            return stringRedisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return 0L;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 结果
     */
    public Object listIndex(String key, long index) {
        try {
            return stringRedisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 执行结果
     */
    public Boolean listLeftPush(String key, String value) {
        try {
            stringRedisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 执行结果
     */
    public Boolean listLeftPush(String key, String value, long time) {
        try {
            stringRedisTemplate.opsForList().leftPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 执行结果
     */
    public Boolean listLeftPushAll(String key, String... value) {
        try {
            stringRedisTemplate.opsForList().leftPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 执行结果
     */
    public Boolean lSet(String key, long time, String... value) {
        try {
            stringRedisTemplate.opsForList().leftPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 执行结果
     */
    public Boolean listSetByIndex(String key, long index, String value) {
        try {
            stringRedisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long listRemove(String key, long count, Object value) {
        try {
            return stringRedisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
            return 0L;
        }
    }

    /**
     * 移除列表索引范围之外的值
     *
     * @param key 键
     */
    public void listTrim(String key, int start, int stop) {
        try {
            stringRedisTemplate.opsForList().trim(key, start, stop);
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
        }
    }

    /**
     * 移除列表索引范围之外的值
     *
     * @param key 键
     */
    public void listRightPop(String key) {
        try {
            stringRedisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            log.error(ExceptionEnum.REDIS_EXCEPTION.getMessage());
        }
    }
}
