package wontouch.api.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import wontouch.api.domain.notification.model.service.NotificationService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    public static Map<Long, SseEmitter> sseEmitters = new HashMap<>();

    // 클라이언트가 SSE 연결을 요청할 때 사용하는 엔드포인트
    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subsribe(@PathVariable Long userId) {
        SseEmitter sseEmitter = notificationService.subscribe(userId);

        System.out.println("User " + userId + " is now subscribed.");

        return sseEmitter;
    }

    // 친구 신청 알림 전송
    @PostMapping("/friend-request")
    public void sendFriendRequestNofication(@RequestParam String nickname) {
        notificationService.notifyFriendRequest(nickname);
    }

}
