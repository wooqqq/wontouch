package wontouch.game.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import wontouch.game.dto.CropTransactionDto;

@Service
public class ShopService {

    private RedisTemplate<String, Object> redisTemplate;
    private static final String ROOM_PREFIX = "room:";
    private static final String PLAYER_PREFIX = "player:";
    private static final String CROP_POSTFIX = ":crop";

    public ShopService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 플레이어가 특정 작물을 구매
    // TODO 구현
    public synchronized void buyCrop(String roomId, CropTransactionDto cropTransactionDto) {
        String cropId = cropTransactionDto.getCropId();
        String cropKey = ROOM_PREFIX + roomId + CROP_POSTFIX;
        String playerKey = PLAYER_PREFIX + Long.toString(cropTransactionDto.getPlayerId());
        // redis 트랜잭션 시작
        redisTemplate.multi();
        redisTemplate.opsForHash().get(playerKey, "gold");
        int price = (Integer) redisTemplate.opsForHash().get(cropKey, "price");
        long totalPrice = (long) price * cropTransactionDto.getQuantity();

        // redis 트랜잭션 종료
        redisTemplate.exec();
    }
}
