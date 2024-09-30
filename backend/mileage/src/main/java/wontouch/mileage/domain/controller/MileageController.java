package wontouch.mileage.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.mileage.domain.model.service.MileageService;

@RestController
@RequestMapping("/mile-log")
@RequiredArgsConstructor
public class MileageController {

    private final MileageService mileageService;

    // 마일리지 생성

    // 마일리지 적립 목록 조회

    // 총 마일리지 조회

    // 마일리지 사용

    // 마일리지 삭제

}
