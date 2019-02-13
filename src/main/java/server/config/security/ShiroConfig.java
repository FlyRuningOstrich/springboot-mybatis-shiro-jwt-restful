package server.config.security;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import server.config.security.custom.DefaultHeaderSessionManager;
import server.config.security.custom.HeaderRememberMeManager;
import server.config.security.custom.MyRealm;
import server.config.security.session.RedisSessionDao;

@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        // 使用自行创建的 FactoryBean
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        Map<String, String> filterRuleMap = new LinkedHashMap<String, String>() {{
//            put("/files", "anon");
//        }};//因路由拦截认证需保证设置的先后顺序，若有多个过滤规则，此处需使用可保证顺序的对象(现全部由注解控制)
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(MyRealm realm, DefaultHeaderSessionManager sessionManager,
                                                     HeaderRememberMeManager rememberMeManager, RedisSessionDao redisSessionDao) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
//        realm
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(3);
        realm.setCredentialsMatcher(hashedCredentialsMatcher);
        defaultWebSecurityManager.setRealm(realm);//MyRealm 重写的 realm
//        sessionManager
        sessionManager.setSessionDAO(redisSessionDao); //自定义session存储到redis，不使用redis注释掉即可
        defaultWebSecurityManager.setSessionManager(sessionManager); //DefaultHeaderSessionManager 重写的 sessionManager
//        rememberMeManager
        rememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));//rememberMe 加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager); // HeaderRememberMeManager 重写的 rememberMeManager
        return defaultWebSecurityManager;
    }

    /**
     * 不配置，启动后shiro获取不到SecurityManager
     */
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
