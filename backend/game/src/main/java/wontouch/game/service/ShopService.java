package wontouch.game.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShopService {

    private final RedisTemplate<String, Object> redisTemplate;

    public ShopService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 플레이어가 특정 작물을 구매
}
