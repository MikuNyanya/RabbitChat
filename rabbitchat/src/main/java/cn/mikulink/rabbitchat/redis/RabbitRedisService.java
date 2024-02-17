package cn.mikulink.rabbitchat.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * MikuLink created in 2024/2/18 1:24
 * For the Reisen
 * <p>
 * 对redis进行封装，目的是简化操作
 */
@Service
public class RabbitRedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 写入redis
     *
     * @param key   redis key
     * @param value 写入内容
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 使用key获取redis内容
     *
     * @param key redis key
     * @return key对应的内容
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
}
