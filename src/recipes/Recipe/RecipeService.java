package recipes.Recipe;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;
import recipes.Recipe.Security.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

// Service for the recipe control.
@Service
@Transactional
public class RecipeService {

    // Reference for the recipe repository.
    @Autowired
    private final RecipeRepository recipeRepository;

    // Constructor
    @Autowired
    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepo) {
        this.recipeRepository = recipeRepository;
    }

    // For retrieving a recipe from the DB by a user.
    public Recipe getRecipe(long id) {
        // If the recipe exists...
        if (recipeRepository.existsById(id)) {
            // ...the recipe is retrieved and returned.
            return recipeRepository.findById(id);
        // If the recipe does not exist...
        } else {
            // ...a custom error response is given.
            throw new RecipeController.noRecipe("Recipe NOT found!");
        }
    }

    // For uploading a new recipe by a user.
    public String postRecipe(Recipe newRecipe, UserDetails details) {
        // The user uploading the recipe is identified.
        String email = details.getUsername();
        // A new Recipe object is saved into the repository.
        Recipe recipe = recipeRepository.save(new Recipe(
                newRecipe.getId(),
                newRecipe.getName(),
                newRecipe.getDescription(),
                newRecipe.getIngredients(),
                newRecipe.getDirections(),
                newRecipe.getCategory(),
                newRecipe.getDate(),
                email
        ));
        // The new recipe's ID is returned as an object.
        return "{\"id\":" + recipe.getId() + "}";
    }

    // For deleting a recipe if the user has the authority.
    public ResponseEntity<Void> deleteRecipe(long id, UserDetails details) {
        // If the recipe exists...
        if (recipeRepository.existsById(id)) {
            // ...a reference to that recipe is created.
            Recipe recipe = recipeRepository.findById(id);
            // If the user has the authority...
            if (recipe.getOwner().equals(details.getUsername())) {
                // ...the recipe gets deleted.
                recipeRepository.delete(recipe);
                // A NO CONTENT response is returned.
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            // If the user has NO authority...
            } else {
                // ...a FORBIDDEN response is returned.
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        // If the recipe does not exist...
        } else {
            // ...a NOT FOUND response is returned.
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // For updating a recipe if the user has the authority.
    public ResponseEntity<Void> updateRecipe(long id, Recipe newRecipe, UserDetails details) {
        // If the recipe exists...
        if (recipeRepository.existsById(id)) {
            // ...a reference to the recipe is created.
            Recipe recipe = recipeRepository.findById(id);
            // If the user has the authority...
            if (recipe.getOwner().equals(details.getUsername())) {
                // The fields of the recipe are updated...
                recipe.setName(newRecipe.getName());
                recipe.setDescription(newRecipe.getDescription());
                recipe.setDirections(newRecipe.getDirections());
                recipe.setIngredients(newRecipe.getIngredients());
                recipe.setCategory(newRecipe.getCategory());
                // ...and the recipe is saved into the repository.
                recipeRepository.save(recipe);
                // A NO CONTENT response is returned.
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            // If the user does not have the authority...
            } else {
                // ...a FORBIDDEN response is returned.
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        // If the recipe does not exist...
        } else {
            // ...a NOT FOUND response is returned.
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // For retrieving a list of recipes of a given category or name.
    public List<Recipe> searchRecipes(String category, String name) {
        // If a category was given...
        if (category != null) {
            // ...we return all recipes of the given category, sorted by descending date.
            return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
        }
        // If a name was given, we return all recipes containing the given string in their
        // name, sorted by descending date.
        return recipeRepository.findByNameIgnoreCaseContainsOrderByDateDesc(name);
    }
}
