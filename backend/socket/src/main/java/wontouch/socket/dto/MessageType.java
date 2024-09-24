package wontouch.socket.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {
    CHAT("chat"),
    NOTIFY("notify"),
    READY("ready"),
    START("start"),
    KICK("kick");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}