package wontouch.api.domain.user.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wontouch.api.domain.user.entity.UserProfile;
import wontouch.api.domain.user.dto.request.DescriptionUpdateRequestDto;
import wontouch.api.domain.user.dto.request.NicknameUpdateRequestDto;
import wontouch.api.domain.user.dto.request.UserProfileCreateRequestDto;
import wontouch.api.global.exception.CustomException;
import wontouch.api.global.exception.ExceptionResponse;
import wontouch.api.domain.user.model.repository.UserProfileRepository;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

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
