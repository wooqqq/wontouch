package wontouch.api.domain;

import jakarta.persistence.*;
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
    private String nickname;

    @Column
    private String description;

    @Builder
    public UserProfile(int userId, String nickname, String description) {
        this.userId = userId;
        this.nickname = nickname;
        this.description = description;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}
