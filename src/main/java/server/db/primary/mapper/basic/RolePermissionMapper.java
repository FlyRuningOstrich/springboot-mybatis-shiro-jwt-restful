package server.db.primary.mapper.basic;

import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Component;
import server.config.tkmapper.cache.RedisCache;
import server.config.tkmapper.common.CommonMapper;
import server.db.primary.model.basic.RolePermission;
@Component
@CacheNamespace(implementation= RedisCache.class)
public interface RolePermissionMapper extends CommonMapper<RolePermission> {

}