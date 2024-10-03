package wontouch.api.domain.notification.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wontouch.api.domain.notification.entity.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
}
