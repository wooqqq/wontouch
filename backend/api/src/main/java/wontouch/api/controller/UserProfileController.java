package wontouch.api.controller;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.api.domain.UserProfile;
import wontouch.api.dto.request.NicknameCheckRequestDto;
import wontouch.api.dto.request.UserProfileCreateRequestDto;
import wontouch.api.repository.UserProfileRepository;
import wontouch.api.service.UserService;
import wontouch.api.util.ResponseDto;

@RestController
@RequestMapping("/user-profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;
    private final UserProfileRepository userProfileRepository;

    // 회원가입 시 닉네임 설정
    @PostMapping("/join")
    public ResponseEntity<?> createUserProfile(@RequestBody UserProfileCreateRequestDto createDto) {
        UserProfile createProfile = userService.createUserProfile(createDto);
        return new ResponseEntity<>(createProfile, HttpStatus.CREATED);
    }

    // 닉네임 중복 확인
    @PostMapping("/nickname/duplicate-check")
    public ResponseEntity<?> checkNickname(@RequestBody NicknameCheckRequestDto requestDto) {
        boolean result = userProfileRepository.existsByNickname(requestDto.getNickname());

        ResponseDto<Boolean> responseDto;
        if (result) {
            responseDto = ResponseDto.<Boolean>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("이미 사용 중인 닉네임입니다.")
                    .data(true)
                    .build();
        } else {
            responseDto = ResponseDto.<Boolean>builder()
                    .status(HttpStatus.OK.value())
                    .message("사용 가능한 닉네임입니다.")
                    .data(false)
                    .build();
        }

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 닉네임 수정

    // 한줄소개 수정


}
