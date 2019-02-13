package server.config.security;

import lombok.Data;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RedisSessionDao extends AbstractSessionDAO {
    // Session超时时间
    private long expireTime = 30;
    @Resource(name = "ShiroRedisTemplate")
    private RedisTemplate<Object, Object> redisTemplate;
    private final HttpSessionIdHolder httpSessionIdHolder;

    @Autowired
    public RedisSessionDao(HttpSessionIdHolder httpSessionIdHolder) {
        this.httpSessionIdHolder = httpSessionIdHolder;
    }

    @Override   // 加入session
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        String mySessionId = httpSessionIdHolder.getAndRemoveSessionId();
        String newSession = "[" + mySessionId + "]" + sessionId;
        this.assignSessionId(session, newSession);
        redisTemplate.opsForValue().set(getSessionId(session), session, expireTime, TimeUnit.MINUTES);
        return newSession;
    }

    @Override   // 读取session
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }
        return (Session) redisTemplate.opsForValue().get(getSessionId(sessionId));
    }

    @Override   // 更新session
    public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            return;
        }
        redisTemplate.opsForValue().set(getSessionId(session), session, expireTime, TimeUnit.MINUTES);
    }

    @Override   // 删除session
    public void delete(Session session) {
        if (null == session) {
            return;
        }
        redisTemplate.opsForValue().getOperations().delete(getSessionId(session));
    }

    @Override   // 获取活跃的session，可以用来统计在线人数，如果要实现这个功能，可以在将session加入redis时指定一个session前缀，统计的时候则使用keys("session-prefix*")的方式来模糊查找redis中所有的session集合
    public Collection<Session> getActiveSessions() {
        return Objects.requireNonNull(redisTemplate.keys("*")).stream().map(e -> (Session) e).collect(Collectors.toList());
    }

    private Serializable getSessionId(Session session) {
        return session.getId();
    }

    private Serializable getSessionId(Serializable sessionId) {
        return sessionId;
    }


}