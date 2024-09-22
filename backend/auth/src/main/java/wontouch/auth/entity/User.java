package wontouch.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String role;

    @Column
    private String name;

    @Column
    private String username;

    @Column
    private String email;

    public User createUser(String username, String email) {
        this.username = username;
        this.email = email;

        return this;
    }

}
