package wontouch.auth.util;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    // @RequiredArgsConstructor 통해 생성자 주입
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더에 있는 access 라는 값 가져오기
        String accessToken = request.getHeader("access");

        // 요청 헤더에 access 가 없는 경우
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 제거 <- OAuth2를 이용했다고 명시적으로 붙여주는 타입. JWT 검증 혹은 정보 추출 시 제거해줘야 함
        String originToken = accessToken.substring(7);

        // 유효성 확인 후 클라이언트로 상태 코드 응답
        try {
            if (jwtUtil.isExpired(originToken)) {
                PrintWriter writer = response.getWriter();
                writer.println("access token expired");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (ExpiredJwtException e) {
            PrintWriter writer = response.getWriter();
            writer.println("access token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // accessToken 인지 refreshToken 인지
        String category = jwtUtil.getCategory(originToken);

        // JwtFilter 는 요청에 대해 accessToken 만 취급하므로 access 인지 확인
        if (!category.equals("access")) {
            PrintWriter writer = response.getWriter();
            writer.println("invalid access token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 사용자명과 권한을 accessToken 에서 추출
        String username = jwtUtil.getUsername(originToken);
        String role = jwtUtil.getRole(originToken);

        // dto

        // CustomOAuth2User

        // Authentication

        filterChain.doFilter(request, response);
    }

}
