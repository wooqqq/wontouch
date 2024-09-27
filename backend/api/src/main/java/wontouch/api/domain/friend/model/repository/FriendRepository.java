package wontouch.api.domain.friend.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wontouch.api.domain.friend.entity.Friend;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
}
