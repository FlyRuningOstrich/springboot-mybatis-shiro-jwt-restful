package server.db.primary.mapper.basic;

import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Component;
import server.config.tkmapper.cache.DBRedisCache;
import server.config.tkmapper.common.CommonMapper;
import server.db.primary.model.basic.Role;
@Component
@CacheNamespace(implementation= DBRedisCache.class)
public interface RoleMapper extends CommonMapper<Role> {

}