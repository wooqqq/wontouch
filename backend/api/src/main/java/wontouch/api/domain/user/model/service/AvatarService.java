package wontouch.api.domain.user.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wontouch.api.domain.user.dto.request.AvatarRequestDto;
import wontouch.api.domain.user.dto.response.AvatarResponseDto;
import wontouch.api.domain.user.entity.Avatar;
import wontouch.api.domain.user.model.repository.AvatarRepository;
import wontouch.api.global.exception.CustomException;
import wontouch.api.global.exception.ExceptionResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvatarService {

    private final AvatarRepository avatarRepository;

    public List<AvatarResponseDto> getOwnedAvatar(int userId) {
        List<Avatar> ownedList = avatarRepository.findByUserId(userId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AVATAR_EXCEPTION));

        // ownedList 를 AvatarResponseDto로 변환하는 과정 필요
        List<AvatarResponseDto> avatarResponseList = ownedList.stream()
                .map(avatar -> AvatarResponseDto.builder()
                        .id(avatar.getId())
                        .characterName(avatar.getCharacterName())
                        .isEquipped(avatar.isEquipped())
                        .build())
                .toList();

        return avatarResponseList;
    }

    // 회원가입 시 초기 아바타 설정
    @Transactional
    public void setInitialAvatar(int userId) {
        Avatar initialAvatar = Avatar.builder()
                .userId(userId)
                .characterName("boy")
                .isEquipped(true)
                .build();

        avatarRepository.save(initialAvatar);
    }

    // 아바타 조회

    // 아바타 구매
    @Transactional
    public void purchaseAvatar(AvatarRequestDto requestDto) {
        // 마일리지 조회 및 사용 구현 필요

        boolean isExistAvatar = avatarRepository.existsByUserIdAndCharacterName(requestDto.getUserId(), requestDto.getCharacterName());

        if (isExistAvatar)
            throw new ExceptionResponse(CustomException.ALREADY_PURCHASED_AVATAR_EXCEPTION);

        Avatar purchaseAvatar = Avatar.builder()
                .userId(requestDto.getUserId())
                .characterName(requestDto.getCharacterName())
                .isEquipped(false)
                .build();

        avatarRepository.save(purchaseAvatar);
    }

    // 아바타 설정


}
