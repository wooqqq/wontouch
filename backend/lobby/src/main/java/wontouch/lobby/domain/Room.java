package wontouch.lobby.domain;

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
    private long hostId;
    private Set<String> participants;
    private int currentPlayersCount;
}
