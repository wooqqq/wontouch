package wontouch.api.domain.friend.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wontouch.api.domain.friend.dto.request.SendFriendRequestDto;
import wontouch.api.domain.friend.entity.FriendRequest;
import wontouch.api.domain.friend.model.repository.jpa.FriendRepository;
import wontouch.api.domain.friend.model.repository.mongo.FriendRequestRepository;
import wontouch.api.global.exception.CustomException;
import wontouch.api.global.exception.ExceptionResponse;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;

    // 친구 목록 조회


    // 친구 신청
    public FriendRequest sendFriendRequest(SendFriendRequestDto requestDto) {

        // 이미 친구 요청이 있는지 확인
        if (friendRequestRepository.existsByFromUserIdAndToUserId(requestDto.getFromUserId(), requestDto.getToUserId()))
            throw new ExceptionResponse(CustomException.ALREADY_EXIST_REQUEST_EXCEPTION);

        FriendRequest friendRequest = FriendRequest.builder()
                .fromUserId(requestDto.getFromUserId())
                .toUserId(requestDto.getToUserId())
                .build();

        return friendRequestRepository.save(friendRequest);
    }


    // 친구 신청 승인


    // 친구 신청 거절


    // 친구 끊기
}
