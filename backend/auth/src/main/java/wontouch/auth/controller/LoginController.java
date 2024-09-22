package wontouch.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wontouch.auth.service.CustomOAuth2UserService;
import wontouch.auth.util.JwtUtil;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final JwtUtil jwtUtil;
    private final CustomOAuth2UserService customOAuth2UserService;

    // 구글 소셜 로그인 엔드포인트
    @GetMapping("/login/google")
    public ResponseEntity<String> googleLogin(@RequestParam("code") String code) {

        return null;
    }

}
