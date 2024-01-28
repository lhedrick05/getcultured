package liftoff.atlas.getcultured.dto;

import liftoff.atlas.getcultured.models.City;

import java.util.List;
import java.util.stream.Collectors;

public class TagForm {

    private int id;

    private String name;
    private String label;
    private String color;

    // Default constructor, getters, and setters


    public TagForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
