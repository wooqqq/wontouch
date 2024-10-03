package wontouch.api.domain.rank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.api.domain.user.model.service.UserService;

@RestController
@RequestMapping("/rank")
@RequiredArgsConstructor
public class RankController {

    private final UserService userService;


}
