package server.config.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        // 使用自行创建的 FactoryBean
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterRuleMap = new LinkedHashMap<String, String>() {{
            put("/files", "anon");
        }};//因路由拦截认证需保证设置的先后顺序，若有多个过滤规则，此处需使用可保证顺序的对象
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return shiroFilterFactoryBean;
    }


    @Bean(name = "rememberMeManager")
    public HeaderRememberMeManager rememberMeManager() {
        HeaderRememberMeManager headerRememberMeManager = new HeaderRememberMeManager();
        //rememberMe 加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        headerRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return headerRememberMeManager;
    }

    @Bean
    public DefaultHeaderSessionManager defaultWebSessionManager(RedisSessionDao redisSessionDao) {
        DefaultHeaderSessionManager defaultHeaderSessionManager = new DefaultHeaderSessionManager();
        // 设立不使用 调取器验证 session 是否过期
        defaultHeaderSessionManager.setSessionValidationSchedulerEnabled(false);
        //自定义session存储到redis，不使用redis注释掉即可
        defaultHeaderSessionManager.setSessionDAO(redisSessionDao);
        return defaultHeaderSessionManager;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(MyRealm myRealm, DefaultHeaderSessionManager sessionManager,
                                                               RememberMeManager rememberMeManager) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //其他配置这里未列出
        defaultWebSecurityManager.setRealm(myRealm);
        //DefaultHeaderSessionManager 重写的 sessionManager
        defaultWebSecurityManager.setSessionManager(sessionManager);
        // rememberMeManager 重写的 rememberMeManager
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager);
        SecurityUtils.setSecurityManager(defaultWebSecurityManager);
        return defaultWebSecurityManager;
    }

    /**
     * 不配置，启动后shiro获取不到SecurityManager，暂不清楚原因
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
