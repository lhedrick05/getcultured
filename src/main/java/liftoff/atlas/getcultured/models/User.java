package liftoff.atlas.getcultured.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
public class User extends AbstractEntity{


    @Email
    private String userEmail;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50, message = "First name cannot be longer than 50 characters")
    private String userFirstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50, message = "Last name cannot be longer than 50 characters")
    private String userLastName;


    @NotBlank(message = "Location cannot be blank")
    @Size(max = 50, message = "Location cannot be longer than 50 characters")
    private String location;

}
