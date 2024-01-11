package liftoff.atlas.getcultured.dto;

import liftoff.atlas.getcultured.models.Stop;
import liftoff.atlas.getcultured.models.Tour;

import java.util.ArrayList;
import java.util.List;

public class TourForm {


    private String imagePath;

    private String name;

    private String summaryDescription;
    private Tour tour;
    private List<StopForm> stops;

    private Double estimatedLength;

    public TourForm() {
        this.tour = new Tour();
        this.stops = new ArrayList<>();
    }

    // Getters and setters
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
}
