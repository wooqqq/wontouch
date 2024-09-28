package wontouch.api.domain.friend.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wontouch.api.domain.friend.dto.request.FriendRequestActionDto;
import wontouch.api.domain.friend.dto.request.FriendRequestDto;
import wontouch.api.domain.friend.dto.request.SendFriendRequestDto;
import wontouch.api.domain.friend.dto.response.ReceiveFriendRequestDto;
import wontouch.api.domain.friend.entity.Friend;
import wontouch.api.domain.friend.entity.FriendRequest;
import wontouch.api.domain.friend.model.repository.jpa.FriendRepository;
import wontouch.api.domain.friend.model.repository.mongo.FriendRequestRepository;
import wontouch.api.domain.user.entity.Avatar;
import wontouch.api.domain.user.entity.UserProfile;
import wontouch.api.domain.user.model.repository.AvatarRepository;
import wontouch.api.domain.user.model.repository.UserProfileRepository;
import wontouch.api.global.exception.CustomException;
import wontouch.api.global.exception.ExceptionResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UserProfileRepository userProfileRepository;
    private final AvatarRepository avatarRepository;

    // 친구 목록 조회


    // 친구 신청
    public FriendRequest sendFriendRequest(SendFriendRequestDto requestDto) {

        // 이미 친구 요청이 있는지 확인
        if (friendRequestRepository.existsByFromUserIdAndToUserId(requestDto.getFromUserId(), requestDto.getToUserId()))
            throw new ExceptionResponse(CustomException.ALREADY_EXIST_REQUEST_EXCEPTION);

        FriendRequest friendRequest = FriendRequest.builder()
                .fromUserId(requestDto.getFromUserId())
                .toUserId(requestDto.getToUserId())
                .createAt(LocalDateTime.now())
                .build();

        return friendRequestRepository.save(friendRequest);
    }

    // 친구 신청 목록 조회
    public List<ReceiveFriendRequestDto> getFriendRequestList(int userId) {

        List<FriendRequest> friendRequestList = friendRequestRepository.findByToUserId(userId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FRIEND_REQUEST_EXCEPTION));

        List<ReceiveFriendRequestDto> receiveRequestList = friendRequestList.stream()
                .map(friendRequest -> ReceiveFriendRequestDto.builder()
                        .fromUserId(friendRequest.getFromUserId())
                        .fromUserNickname(getNicknameByUserId(friendRequest.getFromUserId()))
                        .build())
                .toList();

        return receiveRequestList;
    }

    // 친구 신청 상세 조회
    public ReceiveFriendRequestDto getFriendRequest(FriendRequestDto requestDto) {

        FriendRequest friendRequest = friendRequestRepository.findByFromUserIdAndToUserId(requestDto.getFromUserId(), requestDto.getToUserId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FRIEND_REQUEST_EXCEPTION));

        UserProfile userProfile = userProfileRepository.findByUserId(friendRequest.getFromUserId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        Avatar equippedAvatar = avatarRepository.findByUserIdAndIsEquippedIsTrue(friendRequest.getFromUserId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AVATAR_EXCEPTION));

        ReceiveFriendRequestDto receiveFriendRequestDto = ReceiveFriendRequestDto.builder()
                .fromUserId(friendRequest.getFromUserId())
                .fromUserNickname(userProfile.getNickname())
                .fromUserCharacterName(equippedAvatar.getCharacterName())
                .fromUserTier("남작") // 임시 지정
                .build();

        return receiveFriendRequestDto;
    }

    // 친구 신청 승인
    @Transactional
    public void acceptRequest(FriendRequestActionDto requestDto) {
        
        // 존재하는 친구 신청인지 확인
        FriendRequest friendRequest = friendRequestRepository.findByFromUserIdAndToUserId(requestDto.getFromUserId(), requestDto.getUserId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FRIEND_REQUEST_EXCEPTION));

        // 현재 로그인한 유저가 toUser 인지 확인
        if (requestDto.getUserId() != friendRequest.getToUserId())
            throw new ExceptionResponse(CustomException.NOT_AUTH_ACCEPT_REQUEST_EXCEPTION);
        
        friendRequestRepository.delete(friendRequest);

        Friend friend = Friend.builder()
                .fromUserId(requestDto.getFromUserId())
                .toUserId(requestDto.getUserId())
                .build();

        friendRepository.save(friend);
    }

    // 친구 신청 거절
    public void rejectRequest(FriendRequestActionDto requestDto) {

        // 존재하는 친구 신청인지 확인
        FriendRequest friendRequest = friendRequestRepository.findByFromUserIdAndToUserId(requestDto.getFromUserId(), requestDto.getUserId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FRIEND_REQUEST_EXCEPTION));

        // 현재 로그인한 유저가 toUser 인지 확인
        if (requestDto.getUserId() != friendRequest.getToUserId())
            throw new ExceptionResponse(CustomException.NOT_AUTH_ACCEPT_REQUEST_EXCEPTION);

        friendRequestRepository.delete(friendRequest);
    }


    // 친구 끊기


    // userId를 통한 닉네임 불러오기
    private String getNicknameByUserId(int userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        return userProfile.getNickname();
    }
}
