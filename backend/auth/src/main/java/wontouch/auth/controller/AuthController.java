package wontouch.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<?> googleLogin(@RequestHeader("Authorization") String authorizationHeader) {
        // Authorization 헤더에서 Bearer 토큰 추출
        String token = authorizationHeader.substring(7); // "Bearer " 제거

        JwtResponseDto.TokenInfo tokenInfo = authService.googleCallback(token);

        String profileSetupUrl = tokenInfo.isFirstLogin() ? "/api/user-profile" : null; // 프로필 설정 URL

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("구글 소셜 로그인")
                .data(Map.of(
                        "tokenInfo", tokenInfo,
                        "profileSetupUrl", profileSetupUrl
                ))
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
