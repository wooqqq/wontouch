package wontouch.mileage.domain.model.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import wontouch.mileage.domain.dto.request.MileageCreateRequestDto;
import wontouch.mileage.domain.entity.MileageLog;
import wontouch.mileage.domain.model.repository.MileageLogRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MileageService {

    private final MileageLogRepository mileageLogRepository;

    // 마일리지 생성
    @Transactional
    public MileageLog createMileage(MileageCreateRequestDto requestDto) {
        // 사용자에 대한 유효성 검사

        MileageLog mileageLog = MileageLog.builder()
                .userId(requestDto.getUserId())
                .amount(requestDto.getAmount())
                .description("게임 완료 보상")
                .totalMileage(10000) // 총 마일리지 조회 기능 구현 후 추가
                .createAt(LocalDateTime.now())
                .build();

        return mileageLogRepository.save(mileageLog);
    }

    // 마일리지 적립 목록 조회

    // 총 마일리지 조회

    // 마일리지 사용

    // 마일리지 삭제

}
