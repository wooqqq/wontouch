package wontouch.api.domain.user.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wontouch.api.domain.user.dto.request.AvatarUpdateRequestDto;
import wontouch.api.domain.user.dto.request.AvatarRequestDto;
import wontouch.api.domain.user.dto.response.AvatarDetailResponseDto;
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

    // 보유한 아바타 리스트 조회
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

    // 아바타 상세 조회
    public AvatarDetailResponseDto getAvatarDetail(AvatarRequestDto requestDto) {
        AvatarDetailResponseDto responseDto = AvatarDetailResponseDto.builder()
                .userId(requestDto.getUserId())
                .characterName(requestDto.getCharacterName())
                .build();

        // 만약 avatar 엔티티에 없다면 아직 구매하지 않은 것
        if (!avatarRepository.existsByUserIdAndCharacterName(requestDto.getUserId(), requestDto.getCharacterName())) {
            responseDto.updateOwned(false);
        } else {
            responseDto.updateOwned(true);

            // 보유하고 있다면 avatar 엔티티에서 찾은 후 장착 유무 확인
            Avatar avatar = avatarRepository.findByUserIdAndCharacterName(requestDto.getUserId(), requestDto.getCharacterName())
                    .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AVATAR_EXCEPTION));

            if (avatar.isEquipped())
                responseDto.updateEquipped(true);
        }

        return responseDto;
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

    // 아바타 구매
    @Transactional
    public void purchaseAvatar(AvatarRequestDto requestDto) {
        // 마일리지 조회 및 사용 구현 필요 => requestDto에 아바타 가격 포함되어야 함 (추후 구현사항)

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

    // 아바타 변경 (장착)
    @Transactional
    public void updateAvatar(AvatarUpdateRequestDto requestDto) {
        List<Avatar> avatars = avatarRepository.findByUserId(requestDto.getUserId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AVATAR_EXCEPTION));

        // 모든 아바타의 장착 상태를 false 로 설정
        avatars.forEach(avatar -> avatar.setEquipped(false));

        Avatar equipAvatar = avatarRepository.findById(requestDto.getAvatarId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AVATAR_EXCEPTION));
        
        // 해당 아바타의 장착 상태를 true 로 설정
        equipAvatar.setEquipped(true);
    }

}
