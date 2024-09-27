package wontouch.api.domain.user.model.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wontouch.api.domain.friend.model.repository.jpa.FriendRepository;
import wontouch.api.domain.user.dto.request.UserSearchRequestDto;
import wontouch.api.domain.user.dto.response.UserResponseDto;
import wontouch.api.domain.user.dto.response.UserSearchResponseDto;
import wontouch.api.domain.user.entity.Avatar;
import wontouch.api.domain.user.entity.UserProfile;
import wontouch.api.domain.user.model.repository.AvatarRepository;
import wontouch.api.domain.user.model.repository.UserProfileRepository;
import wontouch.api.global.exception.CustomException;
import wontouch.api.global.exception.ExceptionResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UserProfileRepository userProfileRepository;
    private final AvatarRepository avatarRepository;
    private final FriendRepository friendRepository;

    @Value("${auth.server.name}:${auth.server.path}")
    private String authServerUrl;

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

    /**
     * 닉네임을 통한 사용자 검색
     */
    public UserSearchResponseDto getUserByNickname(UserSearchRequestDto requestDto) {

        UserProfile userProfile = userProfileRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_PROFILE_EXCEPTION));

        // 찾은 프로필이 자신의 닉네임인 경우
        if (userProfile.getUserId() == requestDto.getUserId())
            throw new ExceptionResponse(CustomException.NOT_OTHER_USER_PROFILE_EXCEPTION);

        // 검색한 사용자가 이미 친구인지 확인
        if (friendRepository.existsByFromUserIdAndToUserId(requestDto.getUserId(), userProfile.getUserId()))
            throw new ExceptionResponse(CustomException.ALREADY_FRIEND_EXCEPTION);

        UserSearchResponseDto responseDto = UserSearchResponseDto.builder()
                .tier("아직 구현 X")
                .nickname(userProfile.getNickname())
                .build();

        return responseDto;
    }

}
