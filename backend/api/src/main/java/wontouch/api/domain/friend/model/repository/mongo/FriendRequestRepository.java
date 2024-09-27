package wontouch.api.domain.friend.model.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wontouch.api.domain.friend.entity.FriendRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends MongoRepository<FriendRequest, Integer> {

    boolean existsByFromUserIdAndToUserId(Integer fromUserId, Integer toUserId);
    Optional<List<FriendRequest>> findByToUserId(int toUserId);
}
