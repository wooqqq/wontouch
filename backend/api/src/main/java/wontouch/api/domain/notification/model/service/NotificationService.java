package wontouch.api.domain.notification.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import wontouch.api.domain.notification.controller.NotificationController;
import wontouch.api.domain.user.entity.UserProfile;
import wontouch.api.domain.user.model.repository.UserProfileRepository;
import wontouch.api.global.exception.CustomException;
import wontouch.api.global.exception.ExceptionResponse;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final UserProfileRepository userProfileRepository;

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
    public void notifyFriendRequest(String nickname) {
        // 5. 수신자 정보 조회
        UserProfile userProfile = userProfileRepository.findByNickname(nickname)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_PROFILE_EXCEPTION));

        // 디버깅 로그 추가
        log.info("Friend request notification for user: {}", nickname);

        // 6. 수신자 정보로부터 ID 추출
        Long userId = Long.valueOf(userProfile.getUserId());

        log.info("Notification sseEmitters has userId: {}", userId);

        // Map에서 userId로 사용자 검색
        if (NotificationController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);

            try {
                sseEmitter.send(SseEmitter.event().name("addFriendRequest").data("새로운 친구 요청이 도착했습니다."));
                log.info("Notification sent to userId: {}", userId);
            } catch (Exception e) {
                NotificationController.sseEmitters.remove(userId);
                log.error("Error sending notification to userId: {}", userId, e);
            }
        } else {
            log.warn("No active SSE connection for userId: {}", userId);
        }
    }
}
