package wontouch.mileage.domain.controller;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.mileage.domain.dto.request.TierPointCreateRequestDto;
import wontouch.mileage.domain.model.service.TierPointService;
import wontouch.mileage.global.dto.ResponseDto;

@RestController
@RequestMapping("/tier")
@RequiredArgsConstructor
public class TierPointController {

    private final TierPointService tierPointService;
    
    // 티어 포인트 적립
    @PostMapping("/create")
    public ResponseEntity<?> createTierPoint(@Valid @RequestBody TierPointCreateRequestDto requestDto) {
        tierPointService.createTierPoint(requestDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("티어 포인트 적립 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    
    // 티어 포인트 적립 목록 조회

    // 총 티어 포인트 조회 기능

    // 티어 포인트 삭제

}
