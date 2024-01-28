package liftoff.atlas.getcultured.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;


@Entity
public class Tag extends AbstractEntity {

//    @NotBlank(message = "Label cannot be blank")
    private String label;
    @NotBlank(message = "Color cannot be blank")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Invalid color format. Use hex code.")
    private String color;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<Tour> tours;

    public Tag() {

    }

    public Tag(String color, List<Tour> tours) {
        super();
        this.color = color;
        this.tours = tours;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }
}