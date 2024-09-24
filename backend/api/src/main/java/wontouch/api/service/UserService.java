package wontouch.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wontouch.api.domain.UserProfile;
import wontouch.api.dto.UserDto;
import wontouch.api.dto.UserProfileCreateRequestDto;
import wontouch.api.repository.UserProfileRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final UserProfileRepository userProfileRepository;

    public UserDto getUserDto(int userId) {
        String url = "http://localhost:8080/auth/user/" + userId; // auth 서버 사용자 조회 URL

        return restTemplate.getForObject(url, UserDto.class);
    }

    public UserProfile createUserProfile(UserProfileCreateRequestDto profileCreateRequestDto) {
        UserProfile userProfile = UserProfile.builder()
                .userId(profileCreateRequestDto.getUserId())
                .nickname(profileCreateRequestDto.getNickname())
                .description(profileCreateRequestDto.getDescription())
                .build();

        return userProfileRepository.save(userProfile);
    }
}
