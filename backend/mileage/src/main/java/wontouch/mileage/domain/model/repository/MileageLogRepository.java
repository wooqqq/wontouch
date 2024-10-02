package wontouch.mileage.domain.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wontouch.mileage.domain.entity.MileageLog;

import java.util.List;
import java.util.Optional;

public interface MileageLogRepository extends MongoRepository<MileageLog, Integer> {

    Optional<List<MileageLog>> findByUserId(int userId);
}
