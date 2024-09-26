package wontouch.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import wontouch.api.domain.UserProfile;
import wontouch.api.dto.request.DescriptionUpdateRequestDto;
import wontouch.api.dto.request.NicknameUpdateRequestDto;
import wontouch.api.dto.request.UserProfileCreateRequestDto;
import wontouch.api.exception.CustomException;
import wontouch.api.exception.ExceptionResponse;
import wontouch.api.repository.UserProfileRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final UserProfileRepository userProfileRepository;

//    public UserResponseDto getUserDto(int userId) {
//        String url = ":8080/auth/user/" + userId; // auth 서버 사용자 조회 URL
//
//        return restTemplate.getForObject(url, UserResponseDto.class);
//    }

    @Transactional
    public UserProfile createUserProfile(UserProfileCreateRequestDto profileCreateRequestDto) {
        UserProfile userProfile = UserProfile.builder()
                .userId(profileCreateRequestDto.getUserId())
                .nickname(profileCreateRequestDto.getNickname())
                .description(null)
                .build();

        return userProfileRepository.save(userProfile);
    }

    @Transactional
    public void updateNickname(NicknameUpdateRequestDto requestDto) {
        UserProfile userProfile = userProfileRepository.findByUserId(requestDto.getUserId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_PROFILE_EXCEPTION));

        userProfile.updateNickname(requestDto.getNickname());
    }

    @Transactional
    public void updateDescription(DescriptionUpdateRequestDto requestDto) {
        UserProfile userProfile = userProfileRepository.findByUserId(requestDto.getUserId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_PROFILE_EXCEPTION));

        userProfile.updateDescription(requestDto.getDescription());
    }

}
