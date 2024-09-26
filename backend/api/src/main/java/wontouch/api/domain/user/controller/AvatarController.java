package wontouch.api.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wontouch.api.domain.user.dto.request.AvatarRequestDto;
import wontouch.api.domain.user.model.service.AvatarService;
import wontouch.api.global.dto.ResponseDto;

@RestController
@RequestMapping("/avatar")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;

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

}
