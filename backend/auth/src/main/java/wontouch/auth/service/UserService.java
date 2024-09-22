package wontouch.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wontouch.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


}
