package wontouch.api.domain.user.model.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import wontouch.api.domain.user.dto.response.UserResponseDto;
import wontouch.api.domain.user.entity.Avatar;
import wontouch.api.domain.user.entity.UserProfile;
import wontouch.api.domain.user.dto.request.DescriptionUpdateRequestDto;
import wontouch.api.domain.user.dto.request.NicknameUpdateRequestDto;
import wontouch.api.domain.user.dto.request.UserProfileCreateRequestDto;
import wontouch.api.domain.user.model.repository.AvatarRepository;
import wontouch.api.global.exception.CustomException;
import wontouch.api.global.exception.ExceptionResponse;
import wontouch.api.domain.user.model.repository.UserProfileRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final UserProfileRepository userProfileRepository;
    private final ObjectMapper objectMapper;
    private final AvatarRepository avatarRepository;

    @Value("${auth.server.name}:${auth.server.path}")
    private String authServerUrl;

//    public UserResponseDto getUserDto(int userId) {
//        String url = ":8080/auth/user/" + userId; // auth 서버 사용자 조회 URL
//
//        return restTemplate.getForObject(url, UserResponseDto.class);
//    }

    /**
     * 사용자 정보 조회
     * 추후 characterName, mileage, level 등 넣어줄 예정
     */
    public UserResponseDto getUserInfo(int userId) {
        String authUrl = String.format("%s/user/email/%d", authServerUrl, userId);

        // 이메일을 제외한 정보 불러오기
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_PROFILE_EXCEPTION));

        // 아바타 정보 가져오기
        Avatar avatar = avatarRepository.findByUserIdAndIsEquippedIsTrue(userId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AVATAR_EXCEPTION));

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .userId(userId)
                .email(null)
                .nickname(userProfile.getNickname())
                .description(userProfile.getDescription())
                .characterName(avatar.getCharacterName())
                .build();

        // Auth 서버에서 사용자 이메일 불러오기
        ResponseEntity<String> response = restTemplate.getForEntity(authUrl, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String userEmail = jsonNode.get("data").asText();

            userResponseDto.updateEmail(userEmail);
        } catch (Exception e) {
            throw new ExceptionResponse(CustomException.NOT_FOUND_EMAIL_EXCEPTION);
        }

        return userResponseDto;
    }

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
