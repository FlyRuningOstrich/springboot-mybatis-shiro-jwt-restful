package server.config.tkmapper.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import server.tool.ApplicationContextHelper;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class DBRedisCache implements Cache {
    private static final long EXPIRE_TIME_IN_MINUTES = 30; // redis过期时间
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final String id;

    public DBRedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        getRedisTemplate().opsForValue().set(key, value, EXPIRE_TIME_IN_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public Object getObject(Object key) {
        return getRedisTemplate().opsForValue().get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return getRedisTemplate().delete(key);
    }

    @Override
    public void clear() {
        getRedisTemplate().execute((RedisCallback<Object>) connection -> {
            connection.flushDb();
            return null;
        });
    }

    @Override
    public int getSize() {
        return Objects.requireNonNull(getRedisTemplate().keys("*")).size();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    private RedisTemplate<Object, Object> getRedisTemplate() {
        return ApplicationContextHelper.getBean("DBRedisTemplate");
    }
}
