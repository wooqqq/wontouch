package wontouch.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wontouch.api.domain.UserProfile;
import wontouch.api.dto.response.UserResponseDto;
import wontouch.api.dto.request.UserProfileCreateRequestDto;
import wontouch.api.repository.UserProfileRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate6378;
    private final UserProfileRepository userProfileRepository;

//    public UserResponseDto getUserDto(int userId) {
//        String url = ":8080/auth/user/" + userId; // auth 서버 사용자 조회 URL
//
//        return restTemplate.getForObject(url, UserResponseDto.class);
//    }

    public UserProfile createUserProfile(UserProfileCreateRequestDto profileCreateRequestDto) {
        UserProfile userProfile = UserProfile.builder()
                .userId(profileCreateRequestDto.getUserId())
                .nickname(profileCreateRequestDto.getNickname())
                .description(null)
                .build();

        return userProfileRepository.save(userProfile);
    }

    // refresh token으로 userId를 찾는 메서드
    // auth 서버의 parseClaims를 통해 찾을 수 있어서 필요없음
//    private Integer findUserIdByRefreshToken(String refreshToken) {
//        Map<Object, Object> tokensMap = redisTemplate6378.opsForHash().entries("refresh_token");
//
//        for (Map.Entry<Object, Object> entry : tokensMap.entrySet()) {
//            if (entry.getValue().equals(refreshToken)) {
//                return (Integer) entry.getKey();
//            }
//        }
//
//        return null; // 해당 refresh token 이 없는 경우
//    }

}
