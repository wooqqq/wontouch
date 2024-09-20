package wontouch.auth.global.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import wontouch.auth.dto.CustomOAuth2User;
import wontouch.auth.util.JwtUtil;

import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        CustomOAuth2User customUserDetail = (CustomOAuth2User) authentication.getPrincipal();

        // 토큰 생성시에 사용자명과 권한이 필요하니 준비
        String username = customUserDetail.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // accessToken 과 refreshToken 생성
        String accessToken = jwtUtil.createJwt("access", username, role, 60000L);
        String refreshToken = jwtUtil.createJwt("refresh", username, role, 86400000L);

        // redis 에 insert (key = username / value = refreshToken)

        // 응답
        response.setHeader("access", "Bearer " + accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());
        // 로그인 성공 시 프론트에 알려줄 redirect 경로
//        response.sendRedirect("http://localhost:8080/");

    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60); // 쿠키가 살아있을 시간
//        cookie.setSecure();         // http에서만 동작할 것인지 (로컬은 http 환경이라 안먹음)
//        cookie.setPath("/");        // 쿠키가 전역에서 동작
        cookie.setHttpOnly(true);   // http에서만 쿠키가 동작할 수 있도록 (js와 같은 곳에서 가져갈 수 없도록)

        return cookie;
    }

}
