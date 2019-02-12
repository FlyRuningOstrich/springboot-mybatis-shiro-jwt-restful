package server.config.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.db.primary.dto.login.LoginDTO;
import server.service.interf.login.LoginService;

@Component
public class MyRealm extends AuthorizingRealm {
    private final LoginService loginService;

    @Autowired
    public MyRealm(LoginService loginService) {
        this.loginService = loginService;
    }

    //登录认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) {
        return new SimpleAuthenticationInfo(auth.getPrincipal(), auth.getCredentials(), getName());
    }

    //身份认证
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //缓存中或Redis中获取当前用户的身份信息，因每验证每个角色或权限时都调用一次本方法，故极不推荐在此处直接访问数据库
        LoginDTO loginDTO = loginService.selectUserById((Integer) principals.getPrimaryPrincipal());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(loginDTO.getRoleValueList());
        simpleAuthorizationInfo.addStringPermissions(loginDTO.getPermissionValueList());
        return simpleAuthorizationInfo;
    }
}
