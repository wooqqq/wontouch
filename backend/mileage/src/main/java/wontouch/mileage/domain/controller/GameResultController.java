package wontouch.mileage.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.mileage.domain.dto.request.GameResultRequestDto;
import wontouch.mileage.domain.dto.request.MileageCreateRequestDto;
import wontouch.mileage.domain.dto.request.TierPointCreateRequestDto;
import wontouch.mileage.domain.model.service.MileageService;
import wontouch.mileage.domain.model.service.TierPointService;
import wontouch.mileage.global.dto.ResponseDto;

@RestController
@RequestMapping("/result")
@RequiredArgsConstructor
public class GameResultController {

    private final MileageService mileageService;
    private final TierPointService tierPointService;

    /**
     * 게임 종료 시 마일리지 및 티어 포인트 한번에 적립
     */
    @PostMapping
    public ResponseEntity<?> earnPoint(@RequestBody GameResultRequestDto requestDto) {

        MileageCreateRequestDto mileageDto = MileageCreateRequestDto.builder()
                .userId(requestDto.getUserId())
                .amount(requestDto.getMileage())
                .build();
        mileageService.createMileage(mileageDto);

        TierPointCreateRequestDto tierPointDto = TierPointCreateRequestDto.builder()
                .userId(requestDto.getUserId())
                .amount(requestDto.getTierPoint())
                .build();
        tierPointService.createTierPoint(tierPointDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("마일리지 및 티어 포인트 적립 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
