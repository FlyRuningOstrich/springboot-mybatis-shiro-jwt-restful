package server.service.interf.basic;

import server.db.primary.model.basic.User;

public interface UserService {
    User selectOneById(Integer userId);

    User selectOneByUsername(String username);

    int insertOne(User user);

    User selectUserByNickname(String nickname);

    User selectUserByNickname1(String nickname);
}
