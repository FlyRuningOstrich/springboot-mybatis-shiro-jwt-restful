package server.config.security.custom;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.db.primary.dto.login.LoginDTO;
import server.db.primary.model.basic.User;
import server.service.interf.basic.UserService;
import server.service.interf.login.LoginService;

@Component
public class MyRealm extends AuthorizingRealm {
    private final UserService userService;
    private final LoginService loginService;

    @Autowired
    public MyRealm(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    //登录认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) {
        User user = userService.selectOneById(Integer.parseInt(String.valueOf(auth.getPrincipal())));
        String s = String.valueOf(auth.getPrincipal());
        String s1 = String.valueOf(auth.getCredentials());
        return new SimpleAuthenticationInfo(user.getId(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
    }

    //身份认证
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LoginDTO loginDTO = loginService.selectUserById(Integer.parseInt(String.valueOf(principals.getPrimaryPrincipal())));
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(loginDTO.getRoleValueList());
        simpleAuthorizationInfo.addStringPermissions(loginDTO.getPermissionValueList());
        return simpleAuthorizationInfo;
    }
}
