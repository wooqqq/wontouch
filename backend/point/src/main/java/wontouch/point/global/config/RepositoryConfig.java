package wontouch.point.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {
        "wontouch.point.domain.model.repository"
})
public class RepositoryConfig {
}
