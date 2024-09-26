package wontouch.api.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wontouch.api.domain.user.dto.request.AvatarUpdateRequestDto;
import wontouch.api.domain.user.dto.request.AvatarRequestDto;
import wontouch.api.domain.user.dto.response.AvatarResponseDto;
import wontouch.api.domain.user.model.service.AvatarService;
import wontouch.api.global.dto.ResponseDto;

import java.util.List;

@RestController
@RequestMapping("/avatar")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;

    // 보유 아바타 리스트 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getOwnedAvatar(@PathVariable int userId) {
        List<AvatarResponseDto> avatarList = avatarService.getOwnedAvatar(userId);

        ResponseDto<Object> responseDto = ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("보유한 아바타 리스트 조회 성공")
                .data(avatarList)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 보유 아바타 상세 조회


    @PatchMapping
    public ResponseEntity<?> updateAvatar(@RequestBody AvatarRequestDto requestDto) {
        // 서비스 내 메서드 호출

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("아바타 수정 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 아바타 구매
    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseAvatar(@RequestBody AvatarRequestDto requestDto) {
        avatarService.purchaseAvatar(requestDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("아바타 구매 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 아바타 변경
    @PatchMapping("/update")
    public ResponseEntity<?> updateAvatar(@RequestBody AvatarUpdateRequestDto requestDto) {
        avatarService.updateAvatar(requestDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("아바타 변경 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
