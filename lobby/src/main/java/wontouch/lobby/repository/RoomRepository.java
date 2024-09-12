package wontouch.lobby.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wontouch.lobby.domain.Room;
import wontouch.lobby.dto.CreateRoomRequest;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RoomRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public RoomRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 방을 만들고 레디스에 저장
    public void saveRoom(CreateRoomRequest room) {
        String key = "game_lobby:" + room.getRoomId() + ":info";
        redisTemplate.opsForHash().put(key, "roomId", room.getRoomId());
        redisTemplate.opsForHash().put(key, "roomName", room.getRoomName());
        redisTemplate.opsForHash().put(key, "roomId", room.isPrivate());
        if (room.isPrivate()) {
            redisTemplate.opsForHash().put(key, "roomId", room.getPassword());
        }

        // 추가 필드 작성

        // 참여자 목록에 방 생성자 삽입
        String participantsKey = "game_lobby:" + room.getRoomId() + ":participants";
        redisTemplate.opsForSet().add(participantsKey, Long.toString(room.getHostPlayerId()));
    }

    // Redis에서 방 정보 조회 (필요 시)
    public Room getRoomById(String roomId) {
        String roomKey = "game_lobby:" + roomId + ":info";
        Map<Object, Object> roomData = redisTemplate.opsForHash().entries(roomKey);
        return new Room(
        );
    }


    public Set<String> getParticipants(String roomId) {
        String participantsKey = "game_lobby:" + roomId + ":participants";
        Set<Object> participants = redisTemplate.opsForSet().members(participantsKey);

        // Set<Object>를 Set<String>으로 변환
        Set<String> participantNames = participants.stream()
                .map(Object::toString) // Object를 String으로 변환
                .collect(Collectors.toSet());

        return participantNames;
    }

    // 방의 현재 플레이어 수 조회
    public long getCurrentPlayersCount(String roomId) {
        String participantsKey = "game_lobby:" + roomId + ":participants";
        return redisTemplate.opsForSet().size(participantsKey);
    }
}
