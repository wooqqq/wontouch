package wontouch.auth.dto;

public interface OAuth2Response {

    // 제공자
    String getProvider();

    // 제공자에게 발급해준 ID
    String getProviderId();

    // 이메일
    String getEmail();

    // 사용자 실명
    String getName();

}
