package server.config.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import server.db.primary.dto.login.LoginDTO;
import server.service.interf.login.LoginService;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ShiroLogin {
    public void login(Object userId, Object password, boolean rememberMe) {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(String.valueOf(userId), String.valueOf(password));
        usernamePasswordToken.setRememberMe(rememberMe);
        SecurityUtils.getSubject().login(usernamePasswordToken);
    }

    public void login(Object userId, Object password) {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(String.valueOf(userId), String.valueOf(password));
        usernamePasswordToken.setRememberMe(false);
        SecurityUtils.getSubject().login(usernamePasswordToken);
    }
}
