package wontouch.api.domain.friend.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wontouch.api.domain.friend.model.repository.jpa.FriendRepository;
import wontouch.api.domain.friend.model.repository.mongo.FriendRequestRepository;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;

    // 친구 목록 조회


    // 친구 신청


    // 친구 신청 승인


    // 친구 신청 거절


    // 친구 끊기
}
