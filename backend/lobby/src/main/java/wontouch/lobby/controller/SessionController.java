package wontouch.lobby.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wontouch.lobby.dto.SessionSaveDto;
import wontouch.lobby.service.RoomService;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private final RoomService roomService;

    @Autowired
    public SessionController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/save")
    public void save(@RequestBody SessionSaveDto session) {
        System.out.println(session);
        roomService.addSession(session);
    }
}
