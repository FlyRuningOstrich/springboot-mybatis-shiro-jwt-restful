package server.config.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.*;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Slf4j
public class DefaultHeaderSessionManager extends DefaultSessionManager implements WebSessionManager {

    private final String AUTHORIZATION = "Authorization";

    //修改将sessionId放置header中
    protected void onStart(Session session, SessionContext context) {
        super.onStart(session, context);
        HttpServletRequest request = WebUtils.getHttpRequest(context);
        HttpServletResponse response = WebUtils.getHttpResponse(context);
        Serializable sessionId = session.getId();
        String idString = sessionId.toString();
        response.setHeader(this.AUTHORIZATION, idString);
        log.info("Set session ID header for session with id {}", idString);
        request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
    }

    //修改读取sessionId改为从header中读取
    public Serializable getSessionId(SessionKey key) {
        Serializable id = super.getSessionId(key);
        if (id == null) {
            ServletRequest request = WebUtils.getRequest(key);
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            // 在request中读取Authorization信息，作为sessionId
            id = httpRequest.getHeader(this.AUTHORIZATION);
        }
        return id;
    }

    @Override
    public boolean isServletContainerSessions() {
        return false;
    }

}
