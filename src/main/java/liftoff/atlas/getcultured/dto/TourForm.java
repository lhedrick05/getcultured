//package liftoff.atlas.getcultured.dto;
//
//import jakarta.annotation.ManagedBean;
//import jakarta.validation.constraints.NotNull;
//import liftoff.atlas.getcultured.models.*;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class TourForm {
//
//    private int id;
//
//    private String imagePath;
//
//    private String name;
//
//    private String summaryDescription;
//    private Tour tour;
//
//    private City city;
//
//    private TourCategory tourCategory;
//
//    private Tag tag;
//
////    @NotNull(message = "City is required")
////    private Integer cityId;
////
////    @NotNull(message = "Category is required")
////    private Integer categoryId;
////
////    private Set<Integer> tagIds = new HashSet<>();
//    private List<StopForm> stops;
//
//    private Double estimatedLength;
//
//    private List<StopForm> newStops; // List to hold new stops to be added
//    private List<Integer> stopsToRemove; // List of stop IDs to be removed
//
//    public TourForm() {
//        // Default constructor
//        this.stops = new ArrayList<>();
//    }
//
//    public TourForm(Integer cityId, Tag categoryId, Set<Integer> tagIds, List<StopForm> stops) {
//        this.tour = new Tour();
////        this.cityId = cityId;
////        this.categoryId = categoryId;
////        this.tagIds = tagIds;
//        this.tag = new Tag();
//        this.city = new City();
//        this.tourCategory = new TourCategory();
//        this.stops = new ArrayList<>();
//        this.newStops = new ArrayList<>();
//        this.stopsToRemove = new ArrayList<>();
//    }
//
//    // Getters and setters
//
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public Tour getTour() {
//        return tour;
//    }
//
//    public void setTour(Tour tour) {
//        this.tour = tour;
//    }
//
////    public Tag getTag() {
////        return tag;
////    }
////
////    public void setTag(Tag tag) {
////        this.tag = tag;
////    }
////
////    public City getCity() {
////        return city;
////    }
////
////    public void setCity(City city) {
////        this.city = city;
////    }
////
////    public TourCategory getTourCategory() {
////        return tourCategory;
////    }
////
////    public void setTourCategory(TourCategory tourCategory) {
////        this.tourCategory = tourCategory;
////    }
//
//
//    public Integer getCityId() {
//        return cityId;
//    }
//
//    public void setCityId(Integer cityId) {
//        this.cityId = cityId;
//    }
//
//    public Integer getCategoryId() {
//        return categoryId;
//    }
//
//    public void setCategoryId(Integer categoryId) {
//        this.categoryId = categoryId;
//    }
//
//    public Set<Integer> getTagIds() {
//        return tagIds;
//    }
//
//    public void setTagIds(Set<Integer> tagIds) {
//        this.tagIds = tagIds;
//    }
//
//    public List<StopForm> getStops() {
//        return stops;
//    }
//
//    public void setStops(List<StopForm> stops) {
//        this.stops = stops;
//    }
//
//    // Method to add a stop
//    public void addStop(StopForm stop) {
//        this.stops.add(stop);
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getSummaryDescription() {
//        return summaryDescription;
//    }
//
//    public void setSummaryDescription(String summaryDescription) {
//        this.summaryDescription = summaryDescription;
//    }
//
//    public Double getEstimatedLength() {
//        return estimatedLength;
//    }
//
//    public void setEstimatedLength(Double estimatedLength) {
//        this.estimatedLength = estimatedLength;
//    }
//
//    public String getImagePath() {
//        return imagePath;
//    }
//
//    public void setImagePath(String imagePath) {
//        this.imagePath = imagePath;
//    }
//
//    public List<StopForm> getNewStops() {
//        return newStops;
//    }
//
//    public void setNewStops(List<StopForm> newStops) {
//        this.newStops = newStops;
//    }
//
//    public List<Integer> getStopsToRemove() {
//        return stopsToRemove;
//    }
//
//    public void setStopsToRemove(List<Integer> stopsToRemove) {
//        this.stopsToRemove = stopsToRemove;
//    }
//}

package liftoff.atlas.getcultured.dto;

import jakarta.annotation.ManagedBean;
import liftoff.atlas.getcultured.models.*;

import java.util.ArrayList;
import java.util.List;

public class TourForm {

    private int id;

    private String imagePath;

    private String name;

    private String summaryDescription;
    private Tour tour;
    private List<StopForm> stops;

    private Double estimatedLength;

    private List<StopForm> newStops; // List to hold new stops to be added
    private List<Integer> stopsToRemove; // List of stop IDs to be removed

    private City city;

    private List<Tag> tags;

    private TourCategory tourCategory;

    public TourForm() {
        this.tour = new Tour();
        this.stops = new ArrayList<>();
        this.newStops = new ArrayList<>();
        this.stopsToRemove = new ArrayList<>();
        this.city = new City();
        this.tags = new ArrayList<>();
        this.tourCategory = new TourCategory();
    }

    // Getters and setters


    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public TourCategory getTourCategory() {
        return tourCategory;
    }

    public void setTourCategory(TourCategory tourCategory) {
        this.tourCategory = tourCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public List<StopForm> getStops() {
        return stops;
    }

    public void setStops(List<StopForm> stops) {
        this.stops = stops;
    }

    // Method to add a stop
    public void addStop(StopForm stop) {
        this.stops.add(stop);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<StopForm> getNewStops() {
        return newStops;
    }

    public void setNewStops(List<StopForm> newStops) {
        this.newStops = newStops;
    }

    public List<Integer> getStopsToRemove() {
        return stopsToRemove;
    }

    public void setStopsToRemove(List<Integer> stopsToRemove) {
        this.stopsToRemove = stopsToRemove;
    }
}