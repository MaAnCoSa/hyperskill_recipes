package recipes.Recipe;

// Necessary for all annotations
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.validation.annotation.Validated;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Validated              // Signals that the entity fields must be validated
@Entity                 // Marks the class as an entity for the DB
@Data                   // Enables getters and setters
@AllArgsConstructor     // Creates a constructor with all fields
@NoArgsConstructor      // Creates a constructor with no fields
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // Makes a JSON call to ignore certain automatic fields.
public class Recipe {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;    // Id generated automatically to distinguish recipes.

    @NotBlank
    private String name;    // Name of the recipe.

    @NotBlank
    private String description;     // General description of the dish.

    @NotEmpty
    private String[] ingredients;   // List of ingredients.

    @NotEmpty
    private String[] directions;    // List of steps for the dish.

    @NotBlank
    @NotNull
    private String category;    // Category of the recipe.

    @UpdateTimestamp
    LocalDateTime date;     // Last update (or upload) of the recipe.

    @JsonIgnore
    private String owner;   // Identifier for the user that created the recipe.
}
