package wontouch.api.domain.user.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import wontouch.api.domain.user.dto.request.AvatarPurchaseRequestDto;
import wontouch.api.domain.user.dto.request.AvatarUpdateRequestDto;
import wontouch.api.domain.user.dto.request.AvatarRequestDto;
import wontouch.api.domain.user.dto.request.MileageSpendRequestDto;
import wontouch.api.domain.user.dto.response.AvatarDetailResponseDto;
import wontouch.api.domain.user.dto.response.AvatarListResponseDto;
import wontouch.api.domain.user.dto.response.AvatarResponseDto;
import wontouch.api.domain.user.entity.Avatar;
import wontouch.api.domain.user.entity.AvatarType;
import wontouch.api.domain.user.model.repository.AvatarRepository;
import wontouch.api.global.exception.CustomException;
import wontouch.api.global.exception.ExceptionResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvatarService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AvatarRepository avatarRepository;

    @Value("${mileage.server.name}:${mileage.server.path}")
    private String mileageServerUrl;

    // 아바타 리스트 조회
    public List<AvatarListResponseDto> getAllAvatars(int userId) {
        List<AvatarListResponseDto> avatarList = new ArrayList<>();

        AvatarType[] avatarTypes = AvatarType.values();
        for (int i = 0; i < avatarTypes.length; i++) {
            AvatarType avatarType = avatarTypes[i];

            boolean owned = avatarRepository.existsByUserIdAndCharacterName(userId, avatarType.getName());
            boolean equipped = false;

            if (owned) {
                Avatar avatar = avatarRepository.findByUserIdAndCharacterName(userId, avatarType.getName())
                        .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AVATAR_EXCEPTION));
                equipped = avatar.isEquipped();
            }

            AvatarListResponseDto avatarDto = AvatarListResponseDto.builder()
                    .characterName(avatarType.getName())
                    .description(avatarType.getDescription())
                    .price(avatarType.getPrice())
                    .isOwned(owned)
                    .isEquipped(equipped)
                    .build();

            avatarList.add(avatarDto);
        }

        return avatarList;
    }

    // 보유한 아바타 리스트 조회
    public List<AvatarResponseDto> getOwnedAvatar(int userId) {
        List<Avatar> ownedList = avatarRepository.findByUserId(userId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AVATAR_EXCEPTION));

        // ownedList 를 AvatarResponseDto로 변환하는 과정 필요
        List<AvatarResponseDto> avatarResponseList = ownedList.stream()
                .map(avatar -> AvatarResponseDto.builder()
                        .avatarId(avatar.getId())
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
            responseDto.updateOwned(null,false);
        } else {
            // 보유하고 있다면 avatar 엔티티에서 찾은 후 장착 유무 확인
            Avatar avatar = avatarRepository.findByUserIdAndCharacterName(requestDto.getUserId(), requestDto.getCharacterName())
                    .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AVATAR_EXCEPTION));

            responseDto.updateOwned(avatar.getId(), true);

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
    public void purchaseAvatar(AvatarPurchaseRequestDto requestDto) {
        // 마일리지 조회 및 사용 구현 필요 => requestDto에 아바타 가격 포함되어야 함 (추후 구현사항)
        purchaseByMileage(requestDto.getUserId(), requestDto.getPrice(), "아바타 구매");

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

    // 마일리지 사용 로직
    private void purchaseByMileage(int userId, int amount, String description) {
        String spendMileageUrl = String.format("%s/mileage/log/spend", mileageServerUrl);

        MileageSpendRequestDto requestDto = MileageSpendRequestDto.builder()
                .userId(userId)
                .amount(amount)
                .description(description)
                .build();

        try {
            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // dto를 JSON으로 변환
            String requestJson = objectMapper.writeValueAsString(requestDto);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

            // post 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(spendMileageUrl, HttpMethod.POST, requestEntity, String.class);

            // 응답 처리
            if (response.getStatusCode() == HttpStatus.CREATED) {
                // 마일리지 사용 성공 로직
                System.out.println("마일리지 사용 성공");
            } else {
                // 실패 로직
                System.out.println("마일리지 사용 실패");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionResponse(CustomException.UNHANDLED_ERROR_EXCEPTION);
        }
    }

}
