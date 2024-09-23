package wontouch.lobby.domain;

<<<<<<< HEAD
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Room {
    private String roomId;
    private String roomName;
    private boolean isPrivate;
    private String password;
    private Set<String> participants;
    private int currentPlayersCount;
=======
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Room {

>>>>>>> 1208bf72810ddd7108d6a2a56d1a3b9e95685dc7
}
