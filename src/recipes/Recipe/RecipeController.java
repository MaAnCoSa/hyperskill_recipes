package recipes.Recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Controller for all recipe manipulation.
@RestController
public class RecipeController {

    // Instance of the recipe Service that manages the instructions passed by any request.
    @Autowired
    private final RecipeService recipeService;

    // Constructor
    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // Reference for all recipes available in the DB.
    public static Map<Integer, Recipe> recipes = new HashMap<>();

    // For retrieval of a recipe by a user.
    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable long id) {
        return recipeService.getRecipe(id);
    }

    // For uploading of new recipe by a user.
    @PostMapping("/api/recipe/new")
    public String postRecipe(@RequestBody @Valid Recipe newRecipe, @AuthenticationPrincipal UserDetails details) {
        return recipeService.postRecipe(newRecipe, details);
    }

    // Custom response for when no recipe is found.
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    static
    class noRecipe extends RuntimeException {
        public noRecipe(String cause) {
            super(cause);
        }
    }

    // For deleting a recipe if the user has the authority to do so.
    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable long id, @AuthenticationPrincipal UserDetails details) {
        return recipeService.deleteRecipe(id, details);
    }

    // For updating an existing recipe if the user has the authority.
    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<Void> updateRecipe(@PathVariable long id, @RequestBody @Valid Recipe newRecipe, @AuthenticationPrincipal UserDetails details) {
        return recipeService.updateRecipe(id, newRecipe, details);
    }

    // For retrieving a list of recipes by their category or their names.
    @GetMapping("/api/recipe/search")
    public List<Recipe> searchRecipes(@RequestParam(name = "category", required = false) @Valid String category, @RequestParam(name = "name", required = false) @Valid String name) {
        // If there is more than 1 parameter passed, or if there are none...
        if ((category != null && name != null) || (category == null && name == null)) {
            // ...then a custom error is passed.
            throw new wrongParam("0 or more than 1 parameters passed. Must be only 1.");
        // If everything is alright...
        } else {
            // ...the corresponding function is called from the recipe service.
            return recipeService.searchRecipes(category, name);
        }
    }

    // Custom response for when there is not only 1 parameter.
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    static
    class wrongParam extends RuntimeException {
        public wrongParam(String cause) {
            super(cause);
        }
    }
}
