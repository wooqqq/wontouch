package wontouch.mileage.domain.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wontouch.mileage.domain.entity.MileageLog;

import java.util.List;

public interface MileageLogRepository extends MongoRepository<MileageLog, Integer> {

    List<MileageLog> findByUserId(int userId);
    int findTotalMileageByUserId(int userId);
}
