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

    @PostMapping
    public ResponseEntity<?> createUserProfile(@RequestBody UserProfileCreateRequestDto createDto) {
        UserProfile createProfile = userService.createUserProfile(createDto);
        return new ResponseEntity<>(createProfile, HttpStatus.CREATED);
    }
}
