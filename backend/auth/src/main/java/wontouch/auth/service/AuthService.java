package wontouch.auth.service;

import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import wontouch.auth.dto.request.GoogleRequestDto;
import wontouch.auth.dto.response.JwtResponseDto;
import wontouch.auth.dto.response.KakaoResponseDto;
import wontouch.auth.entity.Token;
import wontouch.auth.entity.User;
import wontouch.auth.repository.TokenRepository;
import wontouch.auth.repository.UserRepository;
import wontouch.auth.util.jwt.JwtProvider;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;


    /**
     * 구글 콜백 메서드
     */
    public JwtResponseDto.TokenInfo googleCallback(GoogleRequestDto requestDto) {

        String email = requestDto.getEmail();
        String username = requestDto.getUsername();
        Optional<User> loginUser = userRepository.findByEmail(email);

        Token token;
        JwtResponseDto.TokenInfo tokenInfo;

        // 회원가입
        if (loginUser.isEmpty()) {
            User googleUser = new User();
            googleUser.createUser(username, email);
            userRepository.save(googleUser);
            tokenInfo = jwtProvider.generateToken(googleUser.getId());
            tokenInfo.updateFirstLogin();

            token = Token.builder()
                    .accessToken(tokenInfo.getAccessToken())
                    .refreshToken(tokenInfo.getRefreshToken())
                    .user(googleUser)
                    .build();

            tokenRepository.save(token);

            // refresh token은 Redis 에 저장
            redisTemplate.opsForValue().set("refresh_token:" + googleUser.getEmail(), tokenInfo.getRefreshToken());

            return tokenInfo;
        } else {    // 기존 회원 로그인
            User user = loginUser.get();
            tokenInfo = jwtProvider.generateToken(user.getId());

            // 기존 토큰 확인
            Optional<Token> existingToken = tokenRepository.findTokenByUserId(user.getId());

            if (existingToken.isPresent()) {
                // 기존 토큰이 있으면 업데이트
                token = existingToken.get();
                token.updateAccessToken(tokenInfo.getAccessToken());
                token.updateRefreshToken(tokenInfo.getRefreshToken());
            } else {
                // 새로운 토큰 생성
                token = Token.builder()
                        .accessToken(tokenInfo.getAccessToken())
                        .refreshToken(tokenInfo.getRefreshToken())
                        .user(user)
                        .build();
            }

            tokenRepository.save(token);

            // refresh token은 Redis 에 저장
            redisTemplate.opsForValue().set("refresh_token:" + user.getEmail(), tokenInfo.getRefreshToken());

            return tokenInfo;
        }
    }

    /**
     * 카카오 콜백 메서드
     */
    public JwtResponseDto.TokenInfo kakaoCallback(String kakaoCode) {
        String accessToken = getKakaoToken(kakaoCode);
        KakaoResponseDto kakaoResponseDto = getKakaoUserInfo(accessToken);
        String email = kakaoResponseDto.getEmail();
        String username = kakaoResponseDto.getUsername();
        Optional<User> loginUser = userRepository.findByEmail(email);

        // 회원가입
        if (loginUser.isEmpty()) {
            User kakaoUser = new User();
            kakaoUser.createUser(username, email);
            userRepository.save(kakaoUser);
            JwtResponseDto.TokenInfo tokenInfo = jwtProvider.generateToken(kakaoUser.getId());
            tokenInfo.updateFirstLogin();

            Token token = Token.builder()
                    .accessToken(tokenInfo.getAccessToken())
                    .refreshToken(tokenInfo.getRefreshToken())
                    .user(kakaoUser)
                    .build();
            tokenRepository.save(token);

            // refresh token은 Redis 에 저장
            redisTemplate.opsForValue().set("refresh_token:" + kakaoUser.getEmail(), tokenInfo.getRefreshToken());

            if (kakaoUser.getEmail().equals("") || kakaoUser.getUsername().equals("")) {
                String message = "마이페이지에서 본인의 정보를 알맞게 수정 후 이용해주세요.";
                // 메시지 후처리 필요
            }

            return tokenInfo;
        } else { // 기존 회원이 로그인하는 경우
            User user = loginUser.get();
            JwtResponseDto.TokenInfo tokenInfo = jwtProvider.generateToken(user.getId());

            // 기존 토큰 확인
            Optional<Token> existingToken = tokenRepository.findTokenByUserId(user.getId());
            Token token;

            if (existingToken.isPresent()) {
                // 기존 토큰이 있으면 업데이트
                token = existingToken.get();
                token.updateAccessToken(tokenInfo.getAccessToken());
                token.updateRefreshToken(tokenInfo.getRefreshToken());
            } else {
                // 새로운 토큰 생성
                token = Token.builder()
                        .accessToken(tokenInfo.getAccessToken())
                        .refreshToken(tokenInfo.getRefreshToken())
                        .user(user)
                        .build();
            }

            tokenRepository.save(token);

            // refresh token은 Redis 에 저장
            redisTemplate.opsForValue().set("refresh_token:" + user.getEmail(), tokenInfo.getRefreshToken());

            return tokenInfo;
        }
    }

    /**
     * 카카오 인가 코드를 통해 토큰 발급받기
     */
    public String getKakaoToken(String kakaoCode) {
        // HttpHeader 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        // body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("client_secret", kakaoClientSecret);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", kakaoCode);

        // header + body
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, httpHeaders);

        System.out.println("kakaoCode: " + kakaoCode);

        // http 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        // response 에서 access token 가져오기
        String responseBody = response.getBody();
        if (responseBody != null) {
            Gson gsonObj = new Gson();
            Map<?, ?> responseMap = gsonObj.fromJson(responseBody, Map.class);
            return (String) responseMap.get("access_token");
        }

        return null;
    }

    /**
     * 카카오 유저의 정보 가져오기
     */
    public KakaoResponseDto getKakaoUserInfo(String accessToken) {
        // HttpHeader 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader 와 HttpBody 를 하나의 객체에 담기 (body 정보는 생략 가능)
        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        // RestTemplate 를 이용하여 HTTP 요청 처리
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 요청을 GET 방식으로 실행하여 멤버 정보를 가져옴
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        // 카카오 인증 서버가 반환한 사용자 정보
        String userInfo = responseEntity.getBody();

        // JSON 데이터에서 필요한 정보 추출
        Gson gsonObj = new Gson();
        Map<?, ?> data = gsonObj.fromJson(userInfo, Map.class);

        // 이메일 동의 여부 확인
        boolean emailAgreement = (boolean) ((Map<?, ?>) (data.get("kakao_account"))).get("email_needs_agreement");
        String email;

        if (emailAgreement) {
            email = "";
        } else {
            email = (String) ((Map<?, ?>) (data.get("kakao_account"))).get("email");
        }

        // 이미 존재하는 경우
//        if (userRepository.findByEmail(email))

        // 닉네임 동의 여부 확인
        boolean nickNameAgreement = (boolean) ((Map<?, ?>) (data.get("kakao_account"))).get("profile_nickname_needs_agreement");
        String nickName;
        if (nickNameAgreement) {
            nickName = "";
        } else {
            nickName = (String) ((Map<?, ?>) ((Map<?, ?>) data.get("properties"))).get("nickname");
        }

        return new KakaoResponseDto(email, nickName);
    }

}
