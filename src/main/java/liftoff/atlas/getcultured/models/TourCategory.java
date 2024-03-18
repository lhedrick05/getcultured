package liftoff.atlas.getcultured.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class TourCategory extends AbstractEntity {


    private String label;

    @OneToMany(mappedBy = "category")
    private Set<Tour> tours = new HashSet<>();

    @Column(name = "description")
    private String description;

    public TourCategory() {
    }

    public TourCategory(String label, Set<Tour> tours, String description) {
        super();
        this.label = label;
        this.tours = tours;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<Tour> getTours() {
        return tours;
    }

    public void setTours(Set<Tour> tours) {
        this.tours = tours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
