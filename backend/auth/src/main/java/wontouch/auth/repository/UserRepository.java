package wontouch.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wontouch.auth.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
