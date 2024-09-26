package wontouch.game.repository.crop;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wontouch.game.entity.Crop;

import java.util.Map;
import java.util.Set;

@Repository
public class CropRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final int DEFAULT_QUANTITY = 50;

    public CropRedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 특정 상점(타입)에 작물 ID를 SET으로 저장
    public void addCropToShop(String roomId, String type, String cropId) {
        String redisKey = "game:" + roomId + ":shop:" + type;
        redisTemplate.opsForSet().add(redisKey, cropId);
    }

    // 특정 작물의 세부 정보를 HASH에 저장
    public void addCropDetails(String roomId, Crop crop) {
        String redisKey = "game:" + roomId + ":crop:" + crop.getId();
        redisTemplate.opsForHash().put(redisKey, "name", crop.getName());
        redisTemplate.opsForHash().put(redisKey, "price", crop.getPrice());
        redisTemplate.opsForHash().put(redisKey, "type", crop.getType());
        redisTemplate.opsForHash().put(redisKey, "description", crop.getDescription());
        redisTemplate.opsForHash().put(redisKey, "quantity", DEFAULT_QUANTITY);
    }

    // 특정 상점(타입)의 작물 ID 목록 조회
    public Set<Object> getCropsFromShop(String roomId, String type) {
        String redisKey = "game:" + roomId + ":shop:" + type;
        return redisTemplate.opsForSet().members(redisKey);
    }

    // 특정 작물의 세부 정보 조회
    public Map<Object, Object> getCropDetails(String roomId, String cropId) {
        String redisKey = "game:" + roomId + ":crop:" + cropId;
        return redisTemplate.opsForHash().entries(redisKey);
    }
}
