package wontouch.game.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wontouch.game.entity.Crop;

import java.util.Optional;

public interface CropRepository extends MongoRepository<Crop, String> {
    Optional<Crop> findByName(String name);
}
