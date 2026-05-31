package tacos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Taco {

    @Id
    private String id;

    private Date createdAt = new Date();

    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    // 💡 IngredientRef 객체 대신 단순 String 리스트로 지정!
    // 이렇게 하면 컨버터 파일이 아예 필요 없어집니다.
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    private List<String> ingredients = new ArrayList<>();

    public void addIngredient(String ingredientId) {
        this.ingredients.add(ingredientId);
    }
}