package wontouch.mileage.domain.model.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import wontouch.mileage.domain.dto.request.MileageCreateRequestDto;
import wontouch.mileage.domain.dto.response.MileageResponseDto;
import wontouch.mileage.domain.entity.MileageLog;
import wontouch.mileage.domain.entity.MileageLogType;
import wontouch.mileage.domain.model.repository.MileageLogRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MileageService {

    private final MileageLogRepository mileageLogRepository;

    // 마일리지 적립
    @Transactional
    public MileageLog createMileage(MileageCreateRequestDto requestDto) {
        // 사용자에 대한 유효성 검사

        MileageLog mileageLog = MileageLog.builder()
                .userId(requestDto.getUserId())
                .amount(requestDto.getAmount())
                .description("게임 완료 보상")
                .totalMileage(getTotalMileageByUserId(requestDto.getUserId()))
                .createAt(LocalDateTime.now())
                .mileageLogType(MileageLogType.EARN)
                .build();

        return mileageLogRepository.save(mileageLog);
    }

    // 마일리지 적립 목록 조회
    public List<MileageResponseDto> getMileageLogList(int userId) {
        List<MileageLog> mileageLogList = mileageLogRepository.findByUserId(userId);

        List<MileageResponseDto> responseDtoList = mileageLogList.stream()
                .map(mileageLog -> MileageResponseDto.builder()
                        .amount(mileageLog.getAmount())
                        .description(mileageLog.getDescription())
                        .totalMileage(mileageLog.getTotalMileage())
                        .createAt(mileageLog.getCreateAt().toString())
                        .mileageLogType(mileageLog.getMileageLogType().toString())
                        .build())
                .toList();

        return responseDtoList;
    }

    // 총 마일리지 조회
    public int getTotalMileageByUserId(int userId) {
        List<MileageLog> mileageLogs = mileageLogRepository.findByUserId(userId);

        // 적립일 경우 더하고, 사용일 경우 빼는 방식으로 총 마일리지 계산
        return mileageLogs.stream()
                .mapToInt(log -> log.getMileageLogType() == MileageLogType.EARN ? log.getAmount() : -log.getAmount())
                .sum();
    }

    // 마일리지 사용

    // 마일리지 삭제

}
