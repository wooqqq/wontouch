package wontouch.api.domain.notification.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import wontouch.api.domain.notification.controller.NotificationController;
import wontouch.api.domain.notification.entity.Notification;
import wontouch.api.domain.notification.entity.NotificationType;
import wontouch.api.domain.notification.model.repository.NotificationRepository;
import wontouch.api.domain.user.entity.UserProfile;
import wontouch.api.domain.user.model.repository.UserProfileRepository;
import wontouch.api.global.exception.CustomException;
import wontouch.api.global.exception.ExceptionResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final UserProfileRepository userProfileRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(Long userId) {

        // 1. 현재 클라이언트를 위한 sseEmitter 객체 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        // 2. 연결
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            log.error("Error during SSE connection for userId: {}", userId, e);
        }

        // 3. 저장
        NotificationController.sseEmitters.put(userId, sseEmitter);

        log.info("Notification sseEmitters has userId: {}", userId);

        // 4. 연결 종료 처리
        sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(userId));   // sseEmitter 연결이 완료될 경우
        sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(userId));      // sseEmitter 연결에 타임아웃이 발생할 겨우
        sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(userId));        // sseEmitter 연결에 오류가 발생할 경우

        log.info("Final check: Notification sseEmitters has userId: {}", userId);

        return sseEmitter;
    }

    // 수신 알람 - receiver 에게
    public void notifyFriendRequest(String receiverNickname, int senderId) {
        UserProfile sender = userProfileRepository.findByUserId(senderId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        UserProfile receiver = userProfileRepository.findByNickname(receiverNickname)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_PROFILE_EXCEPTION));

        // 디버깅 로그 추가
        log.info("Friend request notification for user: {}", receiverNickname);

        // 6. 수신자 정보로부터 ID 추출
        Long receiverId = Long.valueOf(receiver.getUserId());

        log.info("Notification sseEmitters has userId: {}", receiverId);

        // Map에서 userId로 사용자 검색
        if (NotificationController.sseEmitters.containsKey(receiverId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(receiverId);

            try {
                // 친구 요청 알림에 대한 추가 정보 설정
                Map<String, String> eventData = new HashMap<>();
                eventData.put("message", sender.getNickname() + " 님의 친구요청이 도착했습니다.");
                eventData.put("senderNickname", sender.getNickname());  // 친구 요청을 보낸 사람의 닉네임
                eventData.put("timestamp", LocalDateTime.now().toString()); // 요청 보낸 시간

                // SSE로 친구 요청 알림 전송
                sseEmitter.send(SseEmitter.event().name("addFriendRequest").data(eventData));

                // 알림 DB에 저장
                Notification notification = Notification.builder()
                        .sender(sender.getNickname())
                        .createAt(LocalDateTime.now())
                        .content(sender.getNickname() + " 님의 친구요청이 도착했습니다.")
                        .notificationType(NotificationType.FRIEND_REQUEST)
                        .build();
                notificationRepository.save(notification);

                log.info("Notification sent to userId: {}", receiverId);
            } catch (Exception e) {
                NotificationController.sseEmitters.remove(receiverId);
                log.error("Error sending notification to userId: {}", receiverId, e);
            }
        } else {
            log.warn("No active SSE connection for userId: {}", receiverId);
        }
    }

    public void notifyFriendAccept(int receiverId, int senderId) {
        UserProfile sender = userProfileRepository.findByUserId(senderId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        UserProfile receiver = userProfileRepository.findByUserId(receiverId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        Long notifyId = Long.valueOf(receiverId);

        if (NotificationController.sseEmitters.containsKey(notifyId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(notifyId);

            try {
                Map<String, String> eventData = new HashMap<>();
                eventData.put("message", sender.getNickname() + " 님과 친구가 되었습니다.");
                eventData.put("senderNickname", sender.getNickname());  // 친구 요청을 보낸 사람의 닉네임
                eventData.put("timestamp", LocalDateTime.now().toString()); // 요청 보낸 시간

                sseEmitter.send(SseEmitter.event().name("acceptFriend").data(eventData));

                // 알림 DB에 저장
                Notification notification = Notification.builder()
                        .sender(sender.getNickname())
                        .createAt(LocalDateTime.now())
                        .content(sender.getNickname() + " 님과 친구가 되었습니다.")
                        .notificationType(NotificationType.FRIEND_ACCEPT)
                        .build();
                notificationRepository.save(notification);
            } catch (Exception e) {
                NotificationController.sseEmitters.remove(notifyId);
                log.error("Error sending notification to userId: {}", notifyId, e);
            }
        }
    }
}
