package wontouch.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import wontouch.auth.global.handler.CustomSuccessHandler;
import wontouch.auth.util.jwt.JwtFilter;
import wontouch.auth.util.jwt.JwtProvider;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final CustomSuccessHandler customSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // CORS 설정 적용
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/oauth/**").permitAll() // 해당 경로는 인증 없이 접근 가능
                        .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(customSuccessHandler) // OAuth2 로그인 성공 시 핸들러 지정
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // JWT 인증 필터 추가

        return http.build();
    }

    @Bean
    public JwtFilter jwtAuthenticationFilter() {
        return new JwtFilter(jwtProvider);
    }

}
