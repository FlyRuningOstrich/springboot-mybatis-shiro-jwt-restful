package server.db.primary.mapper.basic;

import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Component;
import server.config.tkmapper.common.CommonMapper;
import server.db.primary.model.basic.User;
@Component
@CacheNamespace
public interface UserMapper extends CommonMapper<User> {
    User selectUserByNickname1(String nickname);
}