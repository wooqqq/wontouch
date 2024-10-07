package wontouch.auth.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wontouch.auth.domain.dto.request.GoogleRequestDto;
import wontouch.auth.domain.dto.request.KakaoLogoutRequestDto;
import wontouch.auth.domain.dto.request.KakaoRequestDto;
import wontouch.auth.domain.dto.response.LoginTokenResponseDto;
import wontouch.auth.global.util.dto.JwtResponseDto;
import wontouch.auth.domain.model.service.AuthService;
import wontouch.auth.global.util.dto.ResponseDto;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping("/login/oauth2/code/kakao")
    public String kakaoCallback() {
        // 처리가 끝나면 프론트엔드로 리디렉션
        return "redirect:http://localhost:3000/auth/kakao";
    }

    // 카카오 소셜 로그인
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoRequestDto requestDto) {

        LoginTokenResponseDto tokenInfo = authService.kakaoCallback(requestDto.getCode());

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("카카오 소셜 로그인")
                .data(tokenInfo)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 카카오 로그아웃
    @PostMapping("/kakao/logout")
    public ResponseEntity<?> kakaoLogout(@RequestBody KakaoLogoutRequestDto requestDto) {
        authService.kakaoLogout(requestDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("카카오 로그아웃 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
