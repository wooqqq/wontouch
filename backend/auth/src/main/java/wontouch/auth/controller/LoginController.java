package wontouch.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wontouch.auth.dto.response.JwtResponseDto;
import wontouch.auth.service.CustomOAuth2UserService;
import wontouch.auth.util.ResponseDto;

import java.util.Map;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class LoginController {

    private final CustomOAuth2UserService customOAuth2UserService;

    // 구글 소셜 로그인
    @GetMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestParam("token") String token) {
        JwtResponseDto.TokenInfo tokenInfo = customOAuth2UserService.googleCallback(token);

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

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("카카오 소셜 로그인")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
