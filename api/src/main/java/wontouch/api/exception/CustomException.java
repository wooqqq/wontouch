package wontouch.api.exception;

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

    // 잘못된 API 요청,
    BAD_SSAFY_API_REQUEST(400,"BadSsafyapiRequestException","잘못된 금융 API 요청입니다."),
    EXPIRED_AUTH_CODE(403,"ExpiredAuthCode", "인증시간이 만료되었습니다."),
    NOT_FOUND_AUTH_CODE(404,"NotFoundAuthCode","인증코드 발급 기록이 없습니다.(이미 인증한 코드입니다)"),
    MISMATCHED_AUTH_CODE_EXCEPTION(400,"MismatchedAuthCodeException","인증코드가 일치하지 않습니다."),
    INVALID_ACCOUNT_NUMBER_EXCEPTION(400, "InvalidAccountNumberException", "계좌번호가 유효하지 않습니다."),

    // 계좌
    NOT_FOUND_ACCOUNT_EXCEPTION(400, "NotFoundAccountException", "계좌가 존재하지 않습니다."),
    NOT_MATCH_ACCOUNT_PASSWORD_EXCEPTION(403, "NotMatchAccountPasswordException", "해당 계좌의 비밀번호가 맞지 않습니다."),

    // 펀딩
    NOT_FOUND_FUNDING_EXCEPTION(400, "NotFoundFundingException", "펀딩이 존재하지 않습니다"),
    NOT_PROCESSING_FUNDING_EXCEPTION(400, "NotProcessingFundingException", "현재 진행중인 펀딩이 아닙니다."),
    NOT_SUCCESS_FUNDING_EXCEPTION(400, "NotSuccessFundingException", "현재 완료된 펀딩이 아닙니다."),
    INVALID_FUNDING_JOIN_EXCEPTION(400, "InvalidFundingJoinException", "올바르지 않은 펀딩 참여 형식입니다."),
    INVALID_FUNDING_HOST_EXCEPTION(400, "InvalidFundingHostException", "해당 사용자는 펀딩 주최자가 아닙니다."),
    NO_AUTH_FUNDING_ACCEPT_EXCEPTION(400, "NoAuthFundingAcceptException", "펀딩 승인할 수 있는 권한이 없습니다."),
    NOT_ACCEPT_FUNDING_EXCEPTION(400, "NotAcceptFundingException", "승인받은 펀딩이 아닙니다."),
    NOT_FOUND_IMAGE_EXCEPTION(400, "NotFoundImageException", "이미지를 찾을 수 없습니다."),

    // 펀딩 공지사항
    NOT_FOUND_BOARD_EXCEPTION(400, "NotFoundBoardException", "펀딩 공지사항이 존재하지 않습니다."),

    // 펀딩 계좌
    TRANSFER_FAILURE_EXCEPTION(400, "TransferFailureException", "이체에 실패했습니다."),

    // 펀딩 소속사
    NOT_FOUND_FUNDING_AGENCY_EXCEPTION(400, "NotFoundFundingAgencyException", "해당 소속사와 연결된 펀딩이 아닙니다."),
    NOT_MATCH_AGENCY_EXCEPTION(400, "NotMatchAgencyException", "해당 소속사는 펀딩 소속사와 일치하지 않습니다."),
    ALREADY_ACCEPT_FUNDING_EXCEPTION(400, "AlreadyAcceptFundingException", "해당 펀딩은 이미 소속사 승인이 되었습니다."),

    // 아티스트
    NOT_FOUND_ARTIST_EXCEPTION(400, "NotFoundArtistException", "아티스트가 존재하지 않습니다."),

    // 소속사
    NOT_FOUND_AGENCY_EXCEPTION(400, "NotFoundAgencyException", "소속사가 존재하지 않습니다."),

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
