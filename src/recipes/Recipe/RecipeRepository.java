package recipes.Recipe;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Interface for the repository that will connect to the recipe DB.
@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    // Allows to find a recipe by their ID value.
    Recipe findById(long id);
    // Allows to find all recipes with a given category, ignoring case and sorted by descending date.
    List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);
    // Allows to find all recipes whose names contain a given string, ignoring case and sorted by descending date.
    List<Recipe> findByNameIgnoreCaseContainsOrderByDateDesc(String name);
}
