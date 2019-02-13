package server.service.interf.basic;

import server.db.primary.model.basic.User;

public interface UserService {
    User selectOneById(Integer userId);
    Boolean insertOne(User user);
}
