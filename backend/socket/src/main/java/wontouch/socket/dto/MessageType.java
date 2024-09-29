package wontouch.socket.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {
    CHAT("CHAT"),
    NOTIFY("NOTIFY"),
    READY("READY"),
    START("START"),
    KICK("KICK"),
    MOVE("MOVE"),
    BUY_CROP("BUY_CROP"),
    SELL_CROP("SELL_CROP"),
    BUY_RANDOM_ARTICLE("BUY_RANDOM_ARTICLE"),
    PLAYER_CROP_LIST("PLAYER_CROP_LIST"),
    TOWN_CROP_LIST("TOWN_CROP_LIST"),
    CROP_CHART("CROP_CHART"),
    ROUND_READY("ROUND_READY"),
    RESULT("RESULT"),
    ERROR("ERROR");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}