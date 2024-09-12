package wontouch.lobby.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.lobby.dto.CreateRoomRequest;
import wontouch.lobby.dto.RoomDto;
import wontouch.lobby.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // 방 생성 요청 처리
    @PostMapping("/create")
    public ResponseEntity<RoomDto> createRoom(@RequestBody CreateRoomRequest request) {
        System.out.println("안녕하세요");
        System.out.println(request);
        roomService.createRoom(request);
        return null;
    }
}
