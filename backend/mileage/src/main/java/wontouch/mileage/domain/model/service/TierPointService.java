package wontouch.mileage.domain.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wontouch.mileage.domain.dto.request.TierPointCreateRequestDto;
import wontouch.mileage.domain.entity.TierPointLog;
import wontouch.mileage.domain.model.repository.TierPointLogRepository;
import wontouch.mileage.global.exception.CustomException;
import wontouch.mileage.global.exception.ExceptionResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TierPointService {

    private final TierPointLogRepository tierPointLogRepository;

    // 티어 포인트 적립
    @Transactional
    public void createTierPoint(TierPointCreateRequestDto requestDto) {

        TierPointLog tierPointLog = TierPointLog.builder()
                .userId(requestDto.getUserId())
                .amount(requestDto.getAmount())
                .description("게임 완료 보상")
                .tier("미구현")
                .createAt(LocalDateTime.now())
                .build();

        tierPointLogRepository.save(tierPointLog);
    }

    // 티어 포인트 적립 목록 조회

    // 총 티어 포인트 조회 기능
    public int getTotalTierPoint(int userId) {
        List<TierPointLog> tierPointLogList = tierPointLogRepository.findByUserId(userId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_TIER_POINT_LOG_EXCEPTION));

        return tierPointLogList.stream()
                .mapToInt(log -> log.getAmount())
                .sum();
    }

    // 티어 포인트 삭제

}
