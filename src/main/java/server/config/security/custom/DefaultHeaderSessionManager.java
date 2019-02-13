package server.config.security.custom;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.*;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Slf4j
@Component
public class DefaultHeaderSessionManager extends DefaultSessionManager implements WebSessionManager {
    private final String AUTHORIZATION = "Authorization";

    //修改将sessionId放置header中
    protected void onStart(Session session, SessionContext context) {
        super.onStart(session, context);
        HttpServletRequest request = WebUtils.getHttpRequest(context);
        HttpServletResponse response = WebUtils.getHttpResponse(context);
        Serializable sessionId = session.getId();
        response.setHeader(this.AUTHORIZATION, String.valueOf(sessionId));
        request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
    }

    //修改读取sessionId改为从header中读取
    public Serializable getSessionId(SessionKey sessionKey) {
        Serializable sessionId = super.getSessionId(sessionKey);
        if (sessionId == null) {
            ServletRequest request = WebUtils.getRequest(sessionKey);
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            // 在request中读取Authorization信息，作为sessionId
            sessionId = httpRequest.getHeader(this.AUTHORIZATION);
        }
        return sessionId;
    }

    @Override
    public boolean isServletContainerSessions() {
        return false;
    }

}
