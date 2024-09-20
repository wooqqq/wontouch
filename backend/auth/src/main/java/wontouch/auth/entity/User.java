package wontouch.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
    private String nickname;

    @Column
    private String email;


}
