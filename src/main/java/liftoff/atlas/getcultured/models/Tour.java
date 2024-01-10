package liftoff.atlas.getcultured.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Tour extends AbstractEntity {


    private String imagePath;
    private String summaryDescription;
    private Double estimatedLength;
    private Double estimatedTravelTime;
    private Double userRating;


    // Adding timestamp functionality, need to display it on the template still and check the permissions of editing, make only user and admin able to edit the tour.
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @ManyToOne
    private User createdBy;
    @ManyToOne
    private User updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Embedded
    private MapMarker location;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stop> stops = new ArrayList<>();

    public Tour() {
    }

    public Tour(String summaryDescription, Double estimatedLength,
                Double estimatedTravelTime, Double userRating, User author, MapMarker location, City city, String imagePath) {
        super();
        this.summaryDescription = summaryDescription;
        this.estimatedLength = estimatedLength;
        this.estimatedTravelTime = estimatedTravelTime;
        this.userRating = userRating;
        this.author = author;
        this.location = location;
        this.city = city;
        this.imagePath = imagePath;
    }

    public String getSummaryDescription() {
        return summaryDescription;
    }

    public void setSummaryDescription(String summaryDescription) {
        this.summaryDescription = summaryDescription;
    }

    public Double getEstimatedLength() {
        return estimatedLength;
    }

    public void setEstimatedLength(Double estimatedLength) {
        this.estimatedLength = estimatedLength;
    }

    public Double getEstimatedTravelTime() {
        return estimatedTravelTime;
    }

    public void setEstimatedTravelTime(Double estimatedTravelTime) {
        this.estimatedTravelTime = estimatedTravelTime;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public MapMarker getLocation() {
        return location;
    }

    public void setLocation(MapMarker location) {
        this.location = location;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Add a method to add a stop to the tour
    public void addStop(Stop stop) {
        stops.add(stop);
        stop.setTour(this);
    }

    // Remove a stop from the tour
    public void removeStop(Stop stop) {
        stops.remove(stop);
        stop.setTour(null);
    }

    // Getters and setters for the stops field
    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }
}
