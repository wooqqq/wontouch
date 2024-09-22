package wontouch.auth.service;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wontouch.auth.dto.response.GoogleResponseDto;
import wontouch.auth.dto.response.JwtResponseDto;
import wontouch.auth.dto.response.KakaoResponseDto;
import wontouch.auth.entity.Token;
import wontouch.auth.entity.User;
import wontouch.auth.global.exception.CustomException;
import wontouch.auth.global.exception.ExceptionResponse;
import wontouch.auth.repository.TokenRepository;
import wontouch.auth.repository.UserRepository;
import wontouch.auth.util.jwt.JwtProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    /**
     * 구글 콜백 메서드
     */
    public JwtResponseDto.TokenInfo googleCallback(String accessToken) {
        GoogleResponseDto googleResponseDto = getGoogleUserInfo(accessToken);

        if (googleResponseDto == null)
            throw new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION);

        String email = googleResponseDto.getEmail();
        String username = googleResponseDto.getUsername();
        User loginUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));
        Token token;
        JwtResponseDto.TokenInfo tokenInfo;

        // 회원가입
        if (loginUser == null) {
            User googleUser = new User();
            googleUser.createUser(username, email);
            userRepository.save(googleUser);
            tokenInfo = jwtProvider.generateToken(googleUser.getId());
            token = Token.builder()
                    .accessToken(tokenInfo.getAccessToken())
                    .refreshToken(tokenInfo.getRefreshToken())
                    .user(googleUser)
                    .build();
        } else {    // 기존 회원 로그인
            tokenInfo = jwtProvider.generateToken(loginUser.getId());
            token = Token.builder()
                    .refreshToken(tokenInfo.getRefreshToken())
                    .user(loginUser)
                    .build();
        }

        tokenRepository.save(token);
        return tokenInfo;
    }

    /**
     * 구글 유저 정보 가져오기
     */
    public GoogleResponseDto getGoogleUserInfo(String accessToken) {
        // 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap 타입 선언
        HashMap<String, Object> googleUserInfo = new HashMap<>();
        String requestUrl = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 요청에 필요한 Header 에 포함될 내용
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = connection.getResponseCode();
            System.out.println("responseCode: " + responseCode);

            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = "";
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }

                JsonElement element = JsonParser.parseString(result);
                String email = element.getAsJsonObject().get("email").getAsString();

                // 유저 존재 유무 확인
//                if (userRepository.findByEmail(email))

                String name = element.getAsJsonObject().get("name").getAsString();
                return new GoogleResponseDto(email, name);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 카카오 콜백 메서드
     */
    public JwtResponseDto.TokenInfo kakaoCallback(String accessToken) {
//        KakaoResponseDto kakaoResponseDto =


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
