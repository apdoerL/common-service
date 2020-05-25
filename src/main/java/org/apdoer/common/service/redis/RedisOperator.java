package org.apdoer.common.service.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 1.<@ConditionalOnProperty> 注解的使用</>
 * 2.InitializingBean 接口的作用
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/4/24 18:28
 */
@ConditionalOnProperty(prefix = "spring.redis", name = "host")
@Component
public class RedisOperator implements InitializingBean {
    /**
     * 存放redis命令的并发集合,用于redis的事务操作
     */
    private ConcurrentHashMap<String, RedisScript<Long>> commands = new ConcurrentHashMap<>();

    private RedisTemplate<Object, Object> redisTemplate;

    private static final String INCR_EXPIRE = "incr_expire";

    private static final String INCR_EXPIRE_AT = "incr_expireat";


    @Override
    public void afterPropertiesSet() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:org/apdoer/common/service/redis/*.lua");
        for (Resource resource : resources) {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setLocation(resource);
            script.setResultType(Long.class);
            commands.put(resource.getFilename().replace(".lua", ""), script);
        }
    }

    public RedisOperator(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置key的过期时间
     *
     * @param key
     * @param time
     * @return
     */
    public boolean expire(final String key, final Long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 在某时刻过期
     *
     * @param key
     * @param date
     * @return
     */
    public boolean expireAt(final String key, final Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 获取key
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 是否存在于redis中
     *
     * @param key
     * @return
     */
    public boolean hasKey(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 存入redis
     *
     * @param key
     * @param value
     */
    public void set(final String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public void delete(final String key) {
        redisTemplate.delete(key);
    }

    /**
     * 存入redis并设置过期时间
     *
     * @param key
     * @param value
     * @param timeOut
     * @param timeUnit
     */
    public void setExpire(final String key, Object value, long timeOut, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
    }

    /**
     * 自增并设置过期时间,事务操作
     *
     * @param key
     * @param timeout
     * @return
     */
    public Long incrExpire(final String key, Long timeout) {
        return incr(INCR_EXPIRE, key, timeout);
    }

    /**
     * 自增并设置何事过期,事务操作
     *
     * @param key
     * @param date
     * @return
     */
    public Long incrExpireAt(final String key, Long date) {
        return incr(INCR_EXPIRE_AT, key, date);
    }


    /**
     * 批量保存hashmap
     *
     * @param key
     * @param hashMap
     * @param <V>
     */
    public <V> void hmset(final String key, final Map<String, V> hashMap) {
        redisTemplate.opsForHash().putAll(key, hashMap);
    }

    /**
     * 获取map
     *
     * @param key
     * @param <K>
     * @param <V>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> hget(final String key) {
        return (Map<K, V>) redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取map中某个内容
     *
     * @param key
     * @param field
     * @param <K>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <K> K hget(final String key, final String field) {
        return ((K) redisTemplate.opsForHash().get(key, field));
    }


    /**
     * 存入并设置过期时间
     *
     * @param key
     * @param value
     * @param timeout
     * @param <V>
     */
    public <V> void setex(final String key, final Object value, final int timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * setnx ,事务操作
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setnx(final String key, final String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }


    /**
     * 自定义事务操作
     *
     * @param script
     * @param keys
     * @param values
     */
    public void execute(RedisScript<Object> script, List<Object> keys, List<Object> values) {
        redisTemplate.execute(script, keys, values);
    }


    private Long incr(final String command, final String key, Long timeout) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("key can not be null");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout can be less than zero");
        }
        Object[] args = {timeout.toString()};
        RedisScript<Long> script = commands.get(command);
        return redisTemplate.execute(script, Collections.<Object>singletonList(key), args);
    }
}
