package wontouch.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wontouch.api.domain.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

    Boolean existsByNickname(String nickname);
}
