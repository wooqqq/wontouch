package wontouch.lobby.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wontouch.lobby.domain.Room;
import wontouch.lobby.dto.CreateRoomRequestDto;
import wontouch.lobby.dto.RoomResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RoomRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public RoomRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 방 목록 조회
    public List<RoomResponseDto> getRooms(int pageNumber, int pageSize) {
        String roomListKey = "game_lobby:rooms";

        // 페이지 번호와 크기를 기반으로 시작과 종료 인덱스 계산
        int start = (pageNumber - 1) * pageSize;
        int end = start + pageSize - 1;

        // 최신 방부터 가져오기 위해 reverseRange 사용
        Set<Object> roomIds = redisTemplate.opsForZSet().reverseRange(roomListKey, start, end);

        List<RoomResponseDto> rooms = new ArrayList<>();
        if (roomIds != null && !roomIds.isEmpty()) {
            for (Object roomIdObj : roomIds) {
                String roomId = roomIdObj.toString();
                Room room = getRoomById(roomId);
                if (room != null) {
                    rooms.add(new RoomResponseDto(room));
                }
            }
        }
        return rooms;
    }


    // 방을 만들고 레디스에 저장
    public RoomResponseDto saveRoom(CreateRoomRequestDto room) {
        String key = "game_lobby:" + room.getRoomId() + ":info";
        redisTemplate.opsForHash().put(key, "roomId", room.getRoomId());
        redisTemplate.opsForHash().put(key, "roomName", room.getRoomName());
        redisTemplate.opsForHash().put(key, "isPrivate", room.isPrivate());
        if (room.isPrivate()) {
            redisTemplate.opsForHash().put(key, "password", room.getPassword());
        }

        // 방 목록 Sorted Set에 방 ID 추가 (생성 시간을 score로 사용)
        String roomListKey = "game_lobby:rooms";
        double score = System.currentTimeMillis(); // 현재 시간을 밀리초로 가져옴
        redisTemplate.opsForZSet().add(roomListKey, room.getRoomId(), score);

        // 참여자 목록에 방 생성자 삽입
        String participantsKey = "game_lobby:" + room.getRoomId() + ":participants";
        redisTemplate.opsForSet().add(participantsKey, Long.toString(room.getHostPlayerId()));
        return new RoomResponseDto(getRoomById(room.getRoomId()));
    }

    // 방 정보 조회
    public Room getRoomById(String roomId) {
        String roomKey = "game_lobby:" + roomId + ":info";
        Map<Object, Object> roomData = redisTemplate.opsForHash().entries(roomKey);

        if (roomData == null || roomData.isEmpty()) {
            return null;
        }

        Room room = new Room();
        room.setRoomId((String) roomData.get("roomId"));
        room.setRoomName((String) roomData.get("roomName"));
        room.setPrivate(Boolean.parseBoolean(String.valueOf(roomData.get("isPrivate"))));
        if (room.isPrivate()) {
            room.setPassword((String) roomData.get("password"));
        }

        // 참여자 정보 설정
        Set<String> participants = getParticipants(roomId);
        room.setParticipants(participants);
        room.setCurrentPlayersCount(participants.size());

        return room;
    }

    // 참가자 조회
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
