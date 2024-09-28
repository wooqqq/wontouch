package wontouch.api.domain.friend.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wontouch.api.domain.friend.dto.request.FriendRequestActionDto;
import wontouch.api.domain.friend.dto.request.FriendRequestDto;
import wontouch.api.domain.friend.dto.request.SendFriendRequestDto;
import wontouch.api.domain.friend.dto.response.FriendResponseDto;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UserProfileRepository userProfileRepository;
    private final AvatarRepository avatarRepository;

    // 친구 목록 조회
    public List<FriendResponseDto> getFriendList(int userId) {

        // 존재하는 사용자 ID인지 검사
        userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        List<Friend> friendList = friendRepository.findByFromUserIdOrToUserId(userId, userId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FRIEND_EXCEPTION));

        // Friend를 FriendResponseDto로 변환
        return friendList.stream()
                .map(friend -> {
                    // 친구의 ID가 자신인지 상대인지에 따라 처리
                    int friendId = (friend.getFromUserId() == userId) ? friend.getToUserId() : friend.getFromUserId();

                    // 친구의 UserProfile 조회
                    UserProfile friendProfile = userProfileRepository.findByUserId(friendId)
                            .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

                    // FriendResponseDto로 변환
                    return FriendResponseDto.builder()
                            .friendId(friendId)
                            .nickname(friendProfile.getNickname())
                            .description(friendProfile.getDescription())
                            .characterName(getCharacterNameByUserId(friendProfile.getUserId())) // 캐릭터 이름을 가져오는 로직
                            .tier(getTierByUserId(friendProfile.getUserId())) // 티어를 가져오는 로직
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 친구 신청
    public FriendRequest sendFriendRequest(SendFriendRequestDto requestDto) {

        int fromUserId = requestDto.getFromUserId();
        int toUserId = requestDto.getToUserId();

        // 이미 친구인지 확인
        if (isAlreadyFriend(fromUserId, toUserId))
            throw new ExceptionResponse(CustomException.ALREADY_FRIEND_EXCEPTION);

        // 이미 친구 요청이 있는지 확인
        if (friendRequestRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId))
            throw new ExceptionResponse(CustomException.ALREADY_EXIST_REQUEST_EXCEPTION);

        FriendRequest friendRequest = FriendRequest.builder()
                .fromUserId(fromUserId)
                .toUserId(toUserId)
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


    // 친구 관계 확인 유효성 검사
    private boolean isAlreadyFriend(int userId, int friendId) {

        return friendRepository.existsByFromUserIdAndToUserId(userId, friendId) ||
                friendRepository.existsByFromUserIdAndToUserId(friendId, userId);
    }


    // userId를 통한 닉네임 불러오기
    private String getNicknameByUserId(int userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        return userProfile.getNickname();
    }

    // 캐릭터 이름 불러오기
    private String getCharacterNameByUserId(int userId) {
        Avatar avatar = avatarRepository.findByUserIdAndIsEquippedIsTrue(userId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AVATAR_EXCEPTION));

        return avatar.getCharacterName();
    }

    // 티어 불러오기
    private String getTierByUserId(int userId) {

        return "티어 예시 (미구현)";
    }

}
