package recipes.Recipe.Security;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// Interface to manage the user repository.
@Repository
public interface UserRepository extends CrudRepository<User, String> {
    // This method finds a given user by its email (its main ID).
    User findByEmail(String email);
    // This method confirms if a user exists with a given email (its main ID).
    boolean existsByEmail(String email);
}