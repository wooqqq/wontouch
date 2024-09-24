package wontouch.lobby.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ready")
public class ReadyController {

    @PostMapping("/change")
    public void test(@RequestBody Map<String, Object> preparationInfo) {
        System.out.println(preparationInfo);
    }
}
