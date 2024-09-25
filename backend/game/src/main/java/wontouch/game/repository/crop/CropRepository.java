package wontouch.game.repository.crop;

import org.springframework.data.mongodb.repository.MongoRepository;
import wontouch.game.entity.Crop;

import java.util.List;
import java.util.Optional;

public interface CropRepository extends MongoRepository<Crop, String>, CropCustomRepository {
    Optional<Crop> findByName(String name);

    List<Crop> findByType(String type);
}
