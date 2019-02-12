package server.config.security;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class ShiroSessionTool {
    private final RedisSessionDao redisSessionDao;

    @Autowired
    public ShiroSessionTool(RedisSessionDao redisSessionDao) {
        this.redisSessionDao = redisSessionDao;
    }

    public Object getPrincipal(Serializable sessionId) {
        return redisSessionDao.doReadSession(sessionId).getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
    }

    public boolean checkSession(Serializable sessionId) {
        Session session = redisSessionDao.doReadSession(sessionId);
        if (session == null) {
            return false;
        }
        return session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null;
    }
}
