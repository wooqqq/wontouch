package wontouch.mileage.domain.controller;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping("/total/{userId}")
    public ResponseEntity<?> getTotalTierPoint(@PathVariable int userId) {
        int totalAmount = tierPointService.getTotalTierPoint(userId);

        ResponseDto<Integer> responseDto = ResponseDto.<Integer>builder()
                .status(HttpStatus.OK.value())
                .message("총 티어 포인트 조회 성공")
                .data(totalAmount)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 티어 포인트 삭제

}
