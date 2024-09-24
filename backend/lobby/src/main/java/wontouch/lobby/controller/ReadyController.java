package wontouch.lobby.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.lobby.dto.ReadyStateDto;
import wontouch.lobby.service.ReadyService;

import java.util.Map;

@RestController
@RequestMapping("/ready")
@Slf4j
public class ReadyController {

    private final ReadyService readyService;

    @Autowired
    public ReadyController(ReadyService readyService) {
        this.readyService = readyService;
    }

    @PostMapping("/toggle")
    public ReadyStateDto ready(@RequestBody Map<String, Object> preparationInfo) {
        System.out.println(preparationInfo);
        ReadyStateDto state = readyService.updateReadyState(preparationInfo);
        log.debug("ready: {}", state);
        return state;
    }

    @PostMapping("/kick")
    public boolean kick(@RequestBody Map<String, Object> kickInfo) {
        log.debug("kick: {}", kickInfo);
        return readyService.kickUser(kickInfo);
    }
}
