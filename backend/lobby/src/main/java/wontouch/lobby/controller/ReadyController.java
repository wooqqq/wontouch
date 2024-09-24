package wontouch.lobby.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.lobby.service.ReadyService;

import java.util.Map;

@RestController
@RequestMapping("/api/ready")
@Slf4j
public class ReadyController {

    private final ReadyService readyService;

    @Autowired
    public ReadyController(ReadyService readyService) {
        this.readyService = readyService;
    }

    @PostMapping("/change")
    public Boolean ready(@RequestBody Map<String, Object> preparationInfo) {
        System.out.println(preparationInfo);
        boolean state = readyService.updateReadyState(preparationInfo);
        log.debug("ready: {}", state);
        return state;
    }
}
