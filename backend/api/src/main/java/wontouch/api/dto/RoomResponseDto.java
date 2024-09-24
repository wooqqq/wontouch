package wontouch.api.dto;


import java.util.Set;

public class RoomResponseDto {
    private String roomId;
    private String roomName;
    private boolean isPrivate;
    private String password;
    private Set<String> participants;
    private int maxPlayers;
    private int currentPlayersCount;
}
