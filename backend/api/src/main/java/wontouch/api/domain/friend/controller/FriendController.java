package wontouch.api.domain.friend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.api.domain.friend.model.service.FriendService;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // 친구 목록 조회


    // 친구 프로필 조회 (상세 조회)


    // 닉네임으로 사용자 검색


    // 친구 신청


    // 친구 신청 승인


    // 친구 신청 거절


    // 친구 끊기


}
