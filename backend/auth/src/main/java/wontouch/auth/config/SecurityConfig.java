package wontouch.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import wontouch.auth.global.handler.CustomSuccessHandler;
import wontouch.auth.service.CustomOAuth2UserService;
import wontouch.auth.util.jwt.JwtUtil;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())

                // 권한 설정
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/reissue").permitAll()
                        .anyRequest().authenticated())

                // 세션 설정
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                // oauth2
//                .oauth2Login(oauth2 -> oauth2
//                        .successHandler(customSuccessHandler)
//                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
//                                .userService(customOAuth2UserService)));

        return http.build();
    }

}
