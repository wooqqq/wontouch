package wontouch.mileage.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wontouch.mileage.entity.MileageLog;

@Repository
public interface MileageLogRepository extends MongoRepository<MileageLog, Integer> {
}
