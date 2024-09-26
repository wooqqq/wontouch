package wontouch.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    @Size(min = 2, max = 6, message = "닉네임은 한글 기준 2자 이상 6자 이하로 설정해주세요.")
    private String nickname;

    @Column
    @Size(max = 15, message = "한줄소개는 한글 기준 15자 이하로 설정해주세요.")
    private String description;

    @Builder
    public UserProfile(int userId, String nickname, String description) {
        this.userId = userId;
        this.nickname = nickname;
        this.description = description;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}
