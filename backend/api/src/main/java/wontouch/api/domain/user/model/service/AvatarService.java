package wontouch.api.domain.user.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wontouch.api.domain.user.entity.Avatar;
import wontouch.api.domain.user.model.repository.AvatarRepository;

@Service
@RequiredArgsConstructor
public class AvatarService {

    private final AvatarRepository avatarRepository;

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

}
