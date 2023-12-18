package liftoff.atlas.getcultured.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class User {

    @Id
    @GeneratedValue
    private int userId;

    @NotNull
    private String username;

    @NotNull
    @Email
    private String emailAddress;

    @NotNull
    private String passwordHash;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User() {}

    public User(String username, String emailAddress, String password) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.passwordHash = encoder.encode(password);
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, passwordHash);
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

}
