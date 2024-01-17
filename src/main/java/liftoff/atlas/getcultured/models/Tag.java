package liftoff.atlas.getcultured.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.persistence.CascadeType;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Tag extends AbstractEntity {

    @NotBlank(message = "Label cannot be blank")
    private String label;
    @NotBlank(message = "Color cannot be blank")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Invalid color format. Use hex code.")
    private String color;

    @ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
    private List<Tour> tours = new ArrayList<>();


    public Tag() { }

    public Tag(String label, String color) {
        super();
        this.label = label;
        this.color = color;
        this.tours = new ArrayList<>();
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