package wontouch.game.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import wontouch.game.dto.shop.CropTransactionRequestDto;
import wontouch.game.dto.shop.CropTransactionResponseDto;
import wontouch.game.dto.shop.CropTransactionResult;
import wontouch.game.dto.shop.TransactionStatusType;

@Service
@Slf4j
public class ShopService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ROOM_PREFIX = "game:";
    private static final String PLAYER_PREFIX = "player:";
    private static final String CROP_SUFFIX = ":crop:";
    private static final String CROP_POSTFIX = ":crop";

    public ShopService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 플레이어가 특정 작물을 구매
    public synchronized CropTransactionResult buyCrop(String roomId, CropTransactionRequestDto cropTransactionRequestDto) {
        String cropId = cropTransactionRequestDto.getCropId();
        long playerId = cropTransactionRequestDto.getPlayerId();
        String cropKey = ROOM_PREFIX + roomId + CROP_SUFFIX + cropId;
        String playerKey = PLAYER_PREFIX + Long.toString(playerId);
        String playerCropKey = PLAYER_PREFIX + Long.toString(playerId) + CROP_POSTFIX;

        log.debug("cropId: {}, cropKey: {}, playerKey: {}", cropId, cropKey, playerKey);
        // 1. 플레이어의 보유 골드와 작물의 가격 및 상점에 남은 수량 가져오기
        Integer gold = (Integer) redisTemplate.opsForHash().get(playerKey, "gold");
        Integer price = (Integer) redisTemplate.opsForHash().get(cropKey, "price");
        Integer availableQuantity = (Integer) redisTemplate.opsForHash().get(cropKey, "quantity");

        log.debug("gold: {}, price: {}, availableQuantity: {}", gold, price, availableQuantity);
        // 2. 총 거래 금액 계산
        long totalPrice = (long) price * cropTransactionRequestDto.getQuantity();
        int purchaseQuantity = cropTransactionRequestDto.getQuantity();

        // 거래 응답 객체 초기화
        CropTransactionResponseDto responseDto = new CropTransactionResponseDto();
        responseDto.setCropId(cropId);

        // 3. 골드와 작물 수량이 충분한지 확인
        if (gold != null && gold >= totalPrice) {
            if (availableQuantity != null && availableQuantity >= purchaseQuantity) {

                // 3.1 충분하다면 골드 차감 및 작물 수량 감소
                // TODO 트랜잭션 처리
//                redisTemplate.multi(); // 새로운 트랜잭션 시작
                redisTemplate.opsForHash().increment(playerKey, "gold", -totalPrice); // 골드 차감
                redisTemplate.opsForHash().increment(cropKey, "quantity", -purchaseQuantity); // 작물 수량 감소
                redisTemplate.opsForHash().increment(playerCropKey, cropId, purchaseQuantity); // 작물 수량 감소
//                redisTemplate.exec(); // 트랜잭션 커밋
                System.out.println("Transaction successful! Player bought " + purchaseQuantity + " crops.");
                Integer playerGold = (Integer) redisTemplate.opsForHash().get(playerKey, "gold");
                Integer playerQuantity = (Integer) redisTemplate.opsForHash().get(playerCropKey, cropId);
                Integer townQuantity = (Integer) redisTemplate.opsForHash().get(cropKey, "quantity");

                // TODO 플레이어에게 남은 수량으로 변경
                responseDto.setPlayerQuantity(playerQuantity);  // 플레이어가 구매한 수량
                responseDto.setTownQuantity(townQuantity);        // 마을에 남은 작물 수량
                responseDto.setPlayerGold(playerGold);            // 플레이어의 남은 골드
                // 성공 반환
                return new CropTransactionResult(TransactionStatusType.SUCCESS, responseDto);
            } else {
                // 3.2 상점에 충분한 수량이 없는 경우
                return new CropTransactionResult(TransactionStatusType.INSUFFICIENT_STOCK, null);
            }
        } else {
            // 3.3 골드가 부족한 경우
            return new CropTransactionResult(TransactionStatusType.INSUFFICIENT_FUNDS, null);
        }
    }

    // 플레이어가 특정 작물을 판매
    public synchronized CropTransactionResult sellCrop(String roomId, CropTransactionRequestDto cropTransactionRequestDto) {
        String cropId = cropTransactionRequestDto.getCropId();
        long playerId = cropTransactionRequestDto.getPlayerId();
        String cropKey = ROOM_PREFIX + roomId + CROP_SUFFIX + cropId;  // 상점의 작물 키
        String playerKey = PLAYER_PREFIX + Long.toString(playerId);    // 플레이어의 기본 정보 (골드)
        String playerCropKey = PLAYER_PREFIX + Long.toString(playerId) + CROP_POSTFIX;  // 플레이어의 작물 인벤토리 키

        log.debug("cropId: {}, cropKey: {}, playerKey: {}", cropId, cropKey, playerKey);

        // 1. 플레이어의 작물 수량과 상점의 가격 가져오기
        Integer playerCropQuantity = (Integer) redisTemplate.opsForHash().get(playerCropKey, cropId);  // 플레이어의 작물 수량
        Integer price = (Integer) redisTemplate.opsForHash().get(cropKey, "price");                    // 상점의 작물 가격

        log.debug("playerCropQuantity: {}, price: {}", playerCropQuantity, price);

        int sellQuantity = cropTransactionRequestDto.getQuantity();
        long totalPrice = (long) price * sellQuantity;  // 판매 후 받을 총 금액

        // 거래 응답 객체 초기화
        CropTransactionResponseDto responseDto = new CropTransactionResponseDto();
        responseDto.setCropId(cropId);

        // 2. 플레이어가 판매할 수 있는 작물 수량이 충분한지 확인
        if (playerCropQuantity != null && playerCropQuantity >= sellQuantity) {

            // 2.1 충분하다면 작물 수량 감소 및 골드 증가
            redisTemplate.opsForHash().increment(playerCropKey, cropId, -sellQuantity);  // 플레이어의 작물 수량 감소
            redisTemplate.opsForHash().increment(playerKey, "gold", totalPrice);         // 골드 증가
            redisTemplate.opsForHash().increment(cropKey, "quantity", sellQuantity);     // 상점의 작물 수량 증가

            log.debug("Player sold {} crops for {} gold.", sellQuantity, totalPrice);

            // 3. 갱신된 정보 가져오기
            Integer playerGold = (Integer) redisTemplate.opsForHash().get(playerKey, "gold");  // 갱신된 플레이어의 골드
            Integer playerUpdatedQuantity = (Integer) redisTemplate.opsForHash().get(playerCropKey, cropId);  // 갱신된 플레이어의 작물 수량
            Integer townQuantity = (Integer) redisTemplate.opsForHash().get(cropKey, "quantity");  // 갱신된 상점의 작물 수량

            // 4. 응답 DTO에 갱신된 정보 설정
            responseDto.setPlayerQuantity(playerUpdatedQuantity);  // 플레이어가 남은 작물 수량
            responseDto.setTownQuantity(townQuantity);             // 상점에 남은 작물 수량
            responseDto.setPlayerGold(playerGold);                 // 플레이어의 남은 골드

            // 성공 반환
            return new CropTransactionResult(TransactionStatusType.SUCCESS, responseDto);
        } else {
            // 2.2 플레이어가 충분한 작물을 가지고 있지 않으면
            return new CropTransactionResult(TransactionStatusType.INSUFFICIENT_STOCK, null);
        }
    }

}
