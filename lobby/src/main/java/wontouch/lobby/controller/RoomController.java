package wontouch.lobby.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wontouch.lobby.domain.Room;
import wontouch.lobby.dto.CreateRoomRequest;
import wontouch.lobby.dto.ResponseDto;
import wontouch.lobby.dto.RoomResponseDto;
import wontouch.lobby.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // 방 목록 조회
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<List<RoomResponseDto>>> getGameRoomList(Pageable pageable) {
        System.out.println("HELLO LIST!!!");
        System.out.println(pageable);
        List<Room> roomByPage = roomService.findRoomByPage(pageable);
//        Page<RoomResponseDto> response = rooms.map(RoomResponseDto::fromEntity);
        for (Room room : roomByPage) {
            System.out.println(room);
        }
        return ResponseEntity.ok(null);
    }

    // 방 생성 요청 처리
    @PostMapping("/create")
    public ResponseEntity<RoomResponseDto> createGameRoom(@RequestBody CreateRoomRequest request) {
        System.out.println("안녕하세요");
        System.out.println(request);
        roomService.createRoom(request);
        return null;
    }
}
