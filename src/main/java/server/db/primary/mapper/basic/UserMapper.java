package server.db.primary.mapper.basic;

import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Component;
import server.config.tkmapper.cache.RedisCache;
import server.config.tkmapper.common.CommonMapper;
import server.db.primary.model.basic.User;
@Component
@CacheNamespace(implementation= RedisCache.class)
public interface UserMapper extends CommonMapper<User> {
}