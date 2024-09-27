package wontouch.api.domain.friend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.api.domain.friend.dto.request.SendFriendRequestDto;
import wontouch.api.domain.friend.model.service.FriendService;
import wontouch.api.global.dto.ResponseDto;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // 친구 목록 조회


    // 친구 신청
    @PostMapping("/request")
    public ResponseEntity<?> sendFriendRequest(@RequestBody SendFriendRequestDto requestDto) {
        friendService.sendFriendRequest(requestDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("친구 신청 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 친구 신청 승인


    // 친구 신청 거절


    // 친구 끊기


}
