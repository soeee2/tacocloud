package tacos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IngredientRef {
    private String ingredient; // Ingredient의 id가 매핑되는 필드
}