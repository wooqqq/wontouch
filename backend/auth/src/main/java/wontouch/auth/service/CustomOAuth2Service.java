package wontouch.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import wontouch.auth.dto.CustomOAuth2User;
import wontouch.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomOAuth2Service extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        // 부모 클래스의 메서드를 사용하여 객체 생성
        OAuth2User oAuth2User = super.loadUser(request);

        // 제공자
        String registration = request.getClientRegistration().getRegistrationId();

        // 제공자별로 객체를 구현하여 OAuth2Response 타입으로 반환

        // 제공자별 분기 처리
        if (registration.equals("kakao")) {

        } else if (registration.equals("google")) {

        } else return null;

        // 사용자명을 제공자_회원아이디 형식으로 만들어 저장
        // redis 에서 key 로도 사용
        String username = "";

        // 넘어온 회원정보가 이미 우리의 테이블에 존재하는지 확인

        // 존재하지 않는다면 회원정보를 저장하고 CustomOAuth2User 반환

        // 회원정보가 존재한다면 조회된 데이터로 반환

//        return new CustomOAuth2User();
        return null;
    }

}
