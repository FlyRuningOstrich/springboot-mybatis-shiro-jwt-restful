package server.config.security.session;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;


@Configuration
public class ShiroRedisTemplateConfig {
    @Bean()
    RedisTemplate<Object, Object> ShiroRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(Object.class);
            serializer.setObjectMapper(mapper);
            redisTemplate.setKeySerializer(serializer);
        }
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}