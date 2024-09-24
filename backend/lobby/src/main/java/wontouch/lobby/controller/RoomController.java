package wontouch.lobby.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wontouch.lobby.dto.CreateRoomRequestDto;
import wontouch.lobby.dto.RoomRequestDto;
import wontouch.lobby.dto.ResponseDto;
import wontouch.lobby.dto.RoomResponseDto;
import wontouch.lobby.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@Slf4j
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
        List<RoomResponseDto> roomByPage = roomService.findRoomByPage(pageable);
//        Page<RoomResponseDto> response = rooms.map(RoomResponseDto::fromEntity);

        ResponseDto<List<RoomResponseDto>> responseDto = ResponseDto.<List<RoomResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("게임방 목록 조회 완료")
                .data(roomByPage)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 방 생성 요청 처리
    @PostMapping("/create")
    public ResponseEntity<ResponseDto<RoomResponseDto>> createGameRoom(@RequestBody CreateRoomRequestDto request) {
        System.out.println("안녕하세요");
        System.out.println(request);
        RoomResponseDto room = roomService.createRoom(request);
        ResponseDto<RoomResponseDto> responseDto = ResponseDto.<RoomResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("방 생성 완료")
                .data(room)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 방 입장
    @PostMapping("/join/{roomId}")
    public ResponseEntity<ResponseDto<RoomResponseDto>> joinGameRoom(@PathVariable String roomId, @RequestBody RoomRequestDto roomRequestDto) {
        System.out.println(roomRequestDto);
        RoomResponseDto roomResponseDto = roomService.joinRoom(roomId, roomRequestDto);
        ResponseDto<RoomResponseDto> responseDto = ResponseDto.<RoomResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("방 입장 완료")
                .data(roomResponseDto)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/exit/{roomId}")
    public ResponseEntity<ResponseDto<RoomResponseDto>> exitGameRoom(@PathVariable String roomId, @RequestBody RoomRequestDto roomRequestDto) {
        long playerId = roomRequestDto.getPlayerId();
        log.info("roomId:{} playerId:{} ", roomId, playerId);
        RoomResponseDto roomResponseDto = roomService.exitRoom(roomId, playerId);
        ResponseDto<RoomResponseDto> responseDto = ResponseDto.<RoomResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("방 퇴장 완료")
                .data(roomResponseDto)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
