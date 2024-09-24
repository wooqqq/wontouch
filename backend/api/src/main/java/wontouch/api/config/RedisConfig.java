package wontouch.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory6378() {
        return new LettuceConnectionFactory("localhost", 6378); // port 6378
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory6379() {
        return new LettuceConnectionFactory("localhost", 6379); // port 6379
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate6378() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory6378());
        return template;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate6379() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory6379());
        return template;
    }

}
