package wontouch.game.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "crop")
@Data
public class Crop {

    @Id
    private String id;
    private String type;
    private String name;
    private int price;
    private String imgUrl;


}
