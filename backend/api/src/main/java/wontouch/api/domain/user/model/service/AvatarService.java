package wontouch.api.domain.user.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wontouch.api.domain.user.dto.request.AvatarRequestDto;
import wontouch.api.domain.user.model.repository.AvatarRepository;

@Service
@RequiredArgsConstructor
public class AvatarService {

    private final AvatarRepository avatarRepository;

    public void setAvatar(AvatarRequestDto requestDto) {

    }

}
