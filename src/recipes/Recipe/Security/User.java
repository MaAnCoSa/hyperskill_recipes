package recipes.Recipe.Security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;



@Validated              // Signals that the entity fields must be validated
@Entity                 // Marks the class as an entity for the DB
@Table(name = "users")  // Specifies that the entity is for the users table.
@Data                   // Enables getters and setters
@AllArgsConstructor     // Creates a constructor with all fields
@NoArgsConstructor      // Creates a constructor with no fields
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // Makes a JSON call to ignore certain automatic fields.
public class User implements Serializable {
    // The email will be used as the ID for a user.
    @Id
    @Column(name = "email")
    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = ".+@.+\\..+") // An email must be in the right format.
    private String email;

    // The password linked to the user.
    @NotBlank(message = "Password is mandatory")
    @Size(min=8)    // The password must be at least 8 characters long.
    private String password;

    // The role for a given user (only the USER role has been implemented).
    @JsonIgnore
    private String role;
}
