package wontouch.api.domain.friend.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wontouch.api.domain.friend.entity.Friend;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Integer> {

    Optional<List<Friend>> findByFromUserId(int fromUserId);
}
