package wontouch.api.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomException {

    // 400
    NOT_EMPTY_ROLE_EXCEPTION(400,"NotEmptyRoleException","권한이 존재하지 않습니다."),
    NOT_FOUND_USER_EXCEPTION(400,"NotFoundUserException","유저가 존재하지 않습니다."),
    PASSWORD_INPUT_EXCEPTION(400,"PasswordInputException","비밀번호 입력이 잘못 되었습니다."),
    ID_PASSWORD_INPUT_EXCEPTION(400,"IdPasswordInputException", "아이디 패스워드 입력이 잘못 되었습니다."),
    NOT_AUTH_NUMBER_EXCEPTION(400,"NotAuthNumberException","인증되지 않은 번호입니다."),
    DUPLICATED_NUMBER_EXCEPTION(400,"DuplicatedNumberException","가입된 전화번호가 존재합니다."),
    NOT_SAME_USER_EXCEPTION(400,"NotSameUserException","로그인 유저와 수정 회원이 일지하지 않습니다."),
    DUPLICATED_ID_EXCEPTION(400,"DuplicatedIDException","가입된 아이디가 존재합니다."),
    DUPLICATED_NAME_EXCEPTION(400,"DuplicateNameException","가입된 닉네임 또는 이름이 존재합니다."),

    // 사용자 프로필
    NOT_FOUND_PROFILE_EXCEPTION(400, "NotFoundProfileException", "사용자 프로필이 존재하지 않습니다"),
    NOT_FOUND_EMAIL_EXCEPTION(400, "NotFoundEmailException", "사용자 이메일을 찾을 수 없습니다."),

    // 사용자 아바타
    ALREADY_PURCHASED_AVATAR_EXCEPTION(400, "AlreadyPurchasedAvatarException", "이미 구매한 아바타가 존재합니다."),
    ALREADY_EQUIPPED_AVATAR_EXCEPTION(400, "AlreadyEquippedAvatarException", "이미 장착된 아바타가 있습니다."),
    NOT_FOUND_AVATAR_EXCEPTION(400, "NotFoundAvatarException", "아바타가 존재하지 않습니다."),

    // 친구
    NOT_OTHER_USER_PROFILE_EXCEPTION(400, "NotOtherUserProfileException", "타인의 프로필이 아닙니다."),
    ALREADY_FRIEND_EXCEPTION(400, "AlreadyFriendException", "이미 친구인 사용자입니다."),

    // 아직 처리하지 않은 예외사항
    UNHANDLED_ERROR_EXCEPTION(400, "UnhandledErrorException", "처리되지 않은 에러가 발생했습니다."),

    // 인증 에러 401
    EXPIRED_JWT_EXCEPTION(401,"ExpiredJwtException","토큰이 만료했습니다."),
    NOT_VALID_JWT_EXCEPTION(401,"NotValidJwtException","토큰이 유효하지 않습니다."),

    // 403
    ACCESS_DENIEND_EXCEPTION(403,"AccessDeniendException","권한이 없습니다")
    ;

    private int statusNum;
    private String errorCode;
    private String errorMessage;

}
