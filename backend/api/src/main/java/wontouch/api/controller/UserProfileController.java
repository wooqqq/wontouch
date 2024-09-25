package wontouch.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wontouch.api.domain.UserProfile;
import wontouch.api.dto.request.DescriptionUpdateRequestDto;
import wontouch.api.dto.request.NicknameCheckRequestDto;
import wontouch.api.dto.request.NicknameUpdateRequestDto;
import wontouch.api.dto.request.UserProfileCreateRequestDto;
import wontouch.api.repository.UserProfileRepository;
import wontouch.api.service.UserService;
import wontouch.api.util.ResponseDto;

@RestController
@RequestMapping("/user-profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
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
    @PatchMapping("/nickname/update")
    public ResponseEntity<?> updateNickname(@RequestBody NicknameUpdateRequestDto requestDto) {
        userService.updateNickname(requestDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("닉네임 수정 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    // 한줄소개 수정
    @PatchMapping("/description")
    public ResponseEntity<?> updateDescription(@RequestBody DescriptionUpdateRequestDto requestDto) {
        userService.updateDescription(requestDto);
        
        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("한줄소개 수정 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
