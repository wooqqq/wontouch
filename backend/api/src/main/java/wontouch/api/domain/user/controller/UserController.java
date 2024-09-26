package wontouch.api.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.api.global.dto.ResponseDto;
import wontouch.api.domain.user.model.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 사용자 정보 조회 기능 구현
    @GetMapping
    public ResponseEntity<?> getUserInfo() {
        // service에서 사용자 정보 불러오기
        
        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("사용자 정보 조회 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}
