package wontouch.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wontouch.api.domain.UserProfile;
import wontouch.api.dto.UserDto;
import wontouch.api.dto.UserProfileCreateRequestDto;
import wontouch.api.repository.UserProfileRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate6378;
    private final UserProfileRepository userProfileRepository;

    public UserDto getUserDto(int userId) {
        String url = "http://localhost:8080/auth/user/" + userId; // auth 서버 사용자 조회 URL

        return restTemplate.getForObject(url, UserDto.class);
    }

    public UserProfile createUserProfile(UserProfileCreateRequestDto profileCreateRequestDto) {
        UserProfile userProfile = UserProfile.builder()
                .userId(profileCreateRequestDto.getUserId())
                .nickname(profileCreateRequestDto.getNickname())
                .description(null)
                .build();

        return userProfileRepository.save(userProfile);
    }

    // refresh token으로 이메일을 찾는 메서드
    private String findEmailByRefreshToken(String refreshToken) {
        Map<Object, Object> tokensMap = redisTemplate6378.opsForHash().entries("refresh_token");

        for (Map.Entry<Object, Object> entry : tokensMap.entrySet()) {
            if (entry.getValue().equals(refreshToken)) {
                return (String) entry.getKey();
            }
        }

        return null; // 해당 refresh token 이 없는 경우
    }

}
