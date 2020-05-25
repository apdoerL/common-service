package org.apdoer.common.service.distributionlock;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author apdoer
 * @version 1.0
 * @date 2019/12/2 15:32
 */
@Data
@Slf4j
public class RedisLock {

    private RedisUtil redisUtil;

    private String key;

    private String value;

    private int expireMillis = 60000;

    private int waitMillis = 500;

    private int tryCount = 3;

    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    public RedisLock(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public RedisLock(RedisUtil redisUtil, String key, int expire) {
        this.redisUtil = redisUtil;
        this.key = key;
        this.expireMillis = expire;
    }

    public RedisLock(RedisUtil redisUtil, String key, int expire, int waitMillis, int tryCount) {
        this.redisUtil = redisUtil;
        this.key = key;
        this.expireMillis = expire;
        this.waitMillis = waitMillis;
        this.tryCount = tryCount;
    }

    public RedisLock(RedisUtil redisUtil, String key,
                     int expire, TimeUnit timeUnit, int waitMillis, int tryCount) {
        this.redisUtil = redisUtil;
        this.key = key;
        this.expireMillis = expire;
        this.waitMillis = waitMillis;
        this.tryCount = tryCount;
        this.timeUnit = timeUnit;
    }

    public boolean getLock() {
        try {
            return getLock(tryCount);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 获取锁
     *
     * @param remainTryCount 重试次数
     * @return true if success
     * @throws Exception
     */
    private boolean getLock(int remainTryCount) throws Exception {
        this.value = System.currentTimeMillis() + "";
        // 如果成功 设置这个key的过期时间
        // setIfAbsent(K var1, V var2, long var3, TimeUnit var5)方法在spring-data-redis 2.x中支持
        boolean success = redisUtil.setIfAbsent(key, value, expireMillis, timeUnit);
        if (success) {
            return true;
        } else {
            // 失败  获取值 判断key是否超时未移除
            String value = redisUtil.getRedisTemplate().opsForValue().get(key);
            value = trimQuot(value);
            if (StringUtils.isNotEmpty(value)) {
                if (System.currentTimeMillis() - Long.parseLong(value) > timeUnit.toMillis(expireMillis)) {
                    // 超时移除
                    redisUtil.getRedisTemplate().delete(key);
                }
            }
            // 重试、等待
            if (remainTryCount > 0 && waitMillis > 0) {
                Thread.sleep(waitMillis);
                return getLock(remainTryCount - 1);
            } else {
                return false;
            }
        }
    }

    /**
     * 获取等待时间
     *
     * @return 等待时间
     */
    public long getWaitSecond() {
        long currentTime = System.currentTimeMillis();
        String value = redisUtil.getRedisTemplate().opsForValue().get(key);
        long preTime = Long.parseLong(trimQuot(value));
        return (preTime + timeUnit.toMillis(expireMillis) - currentTime) / 1000;
    }

    /**
     * 释放锁
     */
    public void release() {
        if (value == null || key == null) {
            return;
        }
        String redisValue = redisUtil.getRedisTemplate().opsForValue().get(key);
        if (value.equals(trimQuot(redisValue))) {
            redisUtil.getRedisTemplate().delete(key);
        }
    }

    private String trimQuot(String value) {
        return value.replace("\"", "").replace("\"", "");
    }
}
