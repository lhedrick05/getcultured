package liftoff.atlas.getcultured.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import liftoff.atlas.getcultured.dto.StopForm;

import java.util.*;

@Entity
public class Tour extends AbstractEntity {

    private int updateId;

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

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message="Category is required")
    private TourCategory tourCategory;

//    @ManyToMany
//    private final List<Tag> tags = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tour_tags",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

//    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Stop> stops = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tour_stop",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "stop_id")
    )
    private List<Stop> stops = new ArrayList<>();

    public Tour() {
    }

    public Tour(String summaryDescription, Double estimatedLength,
                Double estimatedTravelTime, Double userRating, User author, MapMarker location, City city, TourCategory tourCategory, String imagePath, int updateId, List<Tag> tags) {
        super();
        this.summaryDescription = summaryDescription;
        this.estimatedLength = estimatedLength;
        this.estimatedTravelTime = estimatedTravelTime;
        this.userRating = userRating;
        this.author = author;
        this.location = location;
        this.city = city;
        this.tourCategory = tourCategory;
        this.imagePath = imagePath;
        this.updateId = updateId;
        this.tags = tags;
    }

    public void setUpdateId(int updateId) {
        this.updateId = updateId;
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

    public TourCategory getTourCategory() {
        return tourCategory;
    }

    public void setTourCategory(TourCategory tourCategory) {
        this.tourCategory = tourCategory;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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
        stop.getTours().add(this);
    }

    // Method to remove a stop by ID
    public void removeStop(Stop stop) {
        stops.remove(stop);
        stop.getTours().remove(this);
    }

//    public void removeStop(int stopId) {
//        this.stops.removeIf(stop -> stop.getId() == stopId);
//    }

//    public void removeStop(int stopId) {
//        Optional<Stop> stopOptional = this.stops.stream()
//                .filter(stop -> stop.getId() == stopId)
//                .findFirst();
//
//        if (stopOptional.isPresent()) {
//            Stop stop = stopOptional.get();
//            this.stops.remove(stop);
//            stop.setTour(null);
//        }
//    }

    // Getters and setters for the stops field
    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }
}
