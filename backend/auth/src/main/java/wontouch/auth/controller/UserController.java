package wontouch.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.auth.dto.UserDto;
import wontouch.auth.entity.User;
import wontouch.auth.global.exception.CustomException;
import wontouch.auth.global.exception.ExceptionResponse;
import wontouch.auth.repository.UserRepository;
import wontouch.auth.util.ResponseDto;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail());

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("회원 정보 조회")
                .data(userDto)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
