package wontouch.mileage.domain.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wontouch.mileage.domain.entity.MileageLog;

public interface MileageLogRepository extends MongoRepository<MileageLog, Integer> {
}
