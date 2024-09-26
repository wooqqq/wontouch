package wontouch.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wontouch.api.domain.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

    Optional<UserProfile> findByUserId(int userId);
    Boolean existsByNickname(String nickname);
}
