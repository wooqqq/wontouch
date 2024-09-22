package wontouch.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wontouch.auth.service.CustomOAuth2UserService;
import wontouch.auth.util.ResponseDto;
import wontouch.auth.util.jwt.JwtUtil;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class LoginController {

    private final JwtUtil jwtUtil;
    private final CustomOAuth2UserService customOAuth2UserService;

    // 구글 소셜 로그인
    @GetMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestParam("token") String token) {

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("구글 소셜 로그인")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
