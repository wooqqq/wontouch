package wontouch.mileage.domain.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wontouch.mileage.domain.dto.request.MileageCreateRequestDto;
import wontouch.mileage.domain.model.service.MileageService;
import wontouch.mileage.global.dto.ResponseDto;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class MileageController {

    private final MileageService mileageService;

    // 마일리지 적립
    @PostMapping("/create")
    public ResponseEntity<?> createMileage(@Valid @RequestBody MileageCreateRequestDto requestDto) {
        mileageService.createMileage(requestDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("마일리지 적립 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 마일리지 적립 목록 조회

    // 총 마일리지 조회
    @GetMapping("/total/{userId}")
    public ResponseEntity<?> getTotalAmount(@PathVariable int userId) {

        int totalAmount = mileageService.getTotalMileageByUserId(userId);

        ResponseDto<Integer> responseDto = ResponseDto.<Integer>builder()
                .status(HttpStatus.OK.value())
                .message("총 마일리지 조회 성공")
                .data(totalAmount)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 마일리지 사용

    // 마일리지 삭제

}
