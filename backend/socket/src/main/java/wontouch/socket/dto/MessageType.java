package wontouch.socket.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.beans.factory.support.MethodOverride;

public enum MessageType {
    CHAT("CHAT"),
    NOTIFY("NOTIFY"),
    READY("READY"),
    START("START"),
    KICK("KICK"),
    MOVE("MOVE"),
    BUY("BUY"),
    SELL("SELL"),
    PLAYER_CROP_LIST("PLAYER_CROP_LIST"),
    TOWN_CROP_LIST("TOWN_CROP_LIST");
    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}