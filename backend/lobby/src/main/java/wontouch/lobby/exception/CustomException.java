package wontouch.lobby.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomException {

    INVALID_PASSWORD_EXCEPTION(401, "InvalidPasswordException", "비밀번호가 틀렸습니다.");

    private int statusNum;
    private String errorCode;
    private String errorMessage;

}
