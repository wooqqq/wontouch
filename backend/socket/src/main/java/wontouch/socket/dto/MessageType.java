package wontouch.socket.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.beans.factory.support.MethodOverride;

public enum MessageType {
    CHAT("chat"),
    NOTIFY("notify"),
    READY("ready"),
    START("start"),
    KICK("kick"),
    MOVE("move");
    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}