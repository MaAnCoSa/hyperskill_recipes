package recipes.Recipe.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Service to manage the user details.
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    // Instance to reference the user repository.
    @Autowired
    UserRepository userRepo;

    // To load the current user that is logged in.
    public UserDetailsImp loadUserByUsername(String email) throws UsernameNotFoundException {
        // A reference to the current user is created.
        User user = userRepo.findByEmail(email);
        // If the user is null (if it does not exist)...
        if (user == null) {
            // ...a custom USERNAME NOT FOUND exception is returned.
            throw new UsernameNotFoundException("Not found: " + email);
        }
        // If the user does exist, then its details are returned.
        return new UserDetailsImp(user);
    }
}
