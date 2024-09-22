package wontouch.auth.service;

import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import wontouch.auth.dto.response.GoogleResponseDto;
import wontouch.auth.repository.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    private final UserRepository userRepository;

    /**
     * 구글 콜백 메서드
     */
    public ResponseEntity<?> googleCallback(String accessToken) {
        GoogleResponseDto googleResponseDto = new GoogleResponseDto();

        return null;
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

}
