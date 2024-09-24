package wontouch.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.api.domain.UserProfile;
import wontouch.api.dto.request.UserProfileCreateRequestDto;
import wontouch.api.service.UserService;

@RestController
@RequestMapping("/user-profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    // 회원가입 시 닉네임 설정
    @PostMapping("/join")
    public ResponseEntity<?> createUserProfile(@RequestBody UserProfileCreateRequestDto createDto) {
        UserProfile createProfile = userService.createUserProfile(createDto);
        return new ResponseEntity<>(createProfile, HttpStatus.CREATED);
    }

    // 닉네임 중복 확인

    // 닉네임 수정

    // 한줄소개 수정


}
