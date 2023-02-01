package recipes.Recipe.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

// Controller for all authentication and authorization.
@RestController
public class SecurityController {

    // Instance of the user repository.
    @Autowired
    UserRepository userRepo;

    // Instance of the password encoder.
    @Autowired
    PasswordEncoder encoder;

    // For registering new users.
    @PostMapping("/api/register")
    public void registerUser(@RequestBody @Valid User user) {
        // If the user already exists...
        if (userRepo.existsByEmail(user.getEmail())
                || user.getEmail() == null  // ...or if no email was given...
                || user.getPassword() == null) {    // ...or if no password was given...
            // ... a BAD REQUEST response is returned.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        // If the user does not exist...
        } else {
            // ...the password is encoded.
            user.setPassword(encoder.encode(user.getPassword()));
            // The role of the user is set as USER.
            user.setRole("ROLE_USER");
            // Finally, the user is saved into the user repository.
            userRepo.save(user);
        }
    }
}
