package server.config.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.config.security.session.HttpSessionIdHolder;


@Component
public class ShiroLoginLogout {
    private final HttpSessionIdHolder httpSessionIdHolder;

    @Autowired
    public ShiroLoginLogout(HttpSessionIdHolder httpSessionIdHolder) {
        this.httpSessionIdHolder = httpSessionIdHolder;
    }

    public void login(Object userId, Object password, boolean rememberMe) {
        httpSessionIdHolder.setSessionId(String.valueOf(userId));
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(String.valueOf(userId), String.valueOf(password));
        usernamePasswordToken.setRememberMe(rememberMe);
        SecurityUtils.getSubject().login(usernamePasswordToken);
    }

    public void login(Object userId, Object password) {
        login(userId, password, false);
    }

    public void logout()
    {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            SecurityUtils.getSubject().logout();
        }
    }
}
