package liftoff.atlas.getcultured.models;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Tour extends AbstractEntity{

    @NotBlank(message = "Summary cannot be blank")
    @Size(max = 200, message = "Summary cannot be longer than 200 characters")
    private String summaryDescription;
}
