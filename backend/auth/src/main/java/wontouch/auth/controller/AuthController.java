package wontouch.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wontouch.auth.dto.request.GoogleRequestDto;
import wontouch.auth.dto.response.JwtResponseDto;
import wontouch.auth.service.AuthService;
import wontouch.auth.util.ResponseDto;

import java.util.Map;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 구글 소셜 로그인
    @GetMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleRequestDto requestDto) {
        JwtResponseDto.TokenInfo tokenInfo = authService.googleCallback(requestDto);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("구글 소셜 로그인")
                .data(tokenInfo)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 카카오 소셜 로그인
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam("token") String accessToken) {

        JwtResponseDto.TokenInfo tokenInfo = authService.kakaoCallback(accessToken);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("카카오 소셜 로그인")
                .data(tokenInfo)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
