package recipes.Recipe.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// All security configuration for the application.
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // Instance of the userDetails Service.
    @Autowired
    UserDetailsService userDetailsService;

    // Configuration for authentication.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(getEncoder()); // Establishing the password encoder.
    }

    // Configuration for HTTP security.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/api/recipe/**").hasAnyRole("USER")   // Recipe manipulation needs authentication as USER.
                .anyRequest().permitAll()   // All other sites are permitted by everyone.
                .and()
                .csrf().disable()           // Disables Cross-Site Request Forgery.
                .httpBasic();
    }

    // Creates an BCrypt password encoder.
    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
