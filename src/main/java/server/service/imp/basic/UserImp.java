package server.service.imp.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.db.primary.mapper.basic.UserMapper;
import server.db.primary.model.basic.User;
import server.service.interf.basic.UserService;

@Service
public class UserImp implements UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserImp(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User selectOneById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public Boolean insertOne(User user) {
        return userMapper.insertSelective(user)>0;
    }
}
