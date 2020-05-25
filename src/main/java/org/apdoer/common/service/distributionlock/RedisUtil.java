package org.apdoer.common.service.distributionlock;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author apdoer
 * @version 1.0
 * @date 2019/12/2 15:41
 */
@Component
@Getter
@Slf4j
public class RedisUtil {
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    RedisSerializer keySerializer() {
        return this.redisTemplate.getKeySerializer();
    }

    RedisSerializer valueSerializer() {
        return this.redisTemplate.getValueSerializer();
    }

    @SuppressWarnings("unchecked")
    byte[] rawKey(Object key) {
        Assert.notNull(key, "non null key required");
        return this.keySerializer() == null && key instanceof byte[] ? (byte[]) key : this.keySerializer().serialize(key);
    }

    @SuppressWarnings("unchecked")
    byte[] rawValue(Object value) {
        return this.valueSerializer() == null && value instanceof byte[] ? (byte[]) ((byte[]) value) : this.valueSerializer().serialize(value);
    }


    /**
     * 这里也可以使用redisOperator中的自定义事务操作配合lua脚本来实现.
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit) {
        // 使用sessionCallBack处理
        SessionCallback<Boolean> sessionCallback = new SessionCallback<Boolean>() {
            List<Object> exec = null;

            @Override
            @SuppressWarnings("unchecked")
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                redisTemplate.opsForValue().setIfAbsent(key, JSON.toJSONString(value));
                redisTemplate.expire(key, timeout, unit);
                exec = operations.exec();
                if (exec.size() > 0) {
                    return (Boolean) exec.get(0);
                }
                return false;
            }
        };
        return redisTemplate.execute(sessionCallback);
    }

}
