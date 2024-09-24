package wontouch.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wontouch.auth.dto.UserDto;
import wontouch.auth.entity.User;
import wontouch.auth.global.exception.CustomException;
import wontouch.auth.global.exception.ExceptionResponse;
import wontouch.auth.repository.UserRepository;
import wontouch.auth.util.jwt.JwtProvider;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public UserDto getUserDto(String accessToken) {
        User user = userRepository.findById(getUserId(accessToken))
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail());

        return userDto;
    }

    public int getUserId(String accessToken) {
        Integer userId = jwtProvider.getAuthentication(accessToken);

        if (userId == null)
            throw new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION);

        return userId;
    }
}
