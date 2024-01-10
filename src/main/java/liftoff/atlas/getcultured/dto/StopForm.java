package liftoff.atlas.getcultured.dto;

public class StopForm {

    private int id;
    private String name;
    private String description;
    private String streetAddress;
    private String cityName;
    private String stateName;
    private int zipCode;
    private Double latitude;
    private Double longitude;
    private double cost;
    private String hoursOfOperation;
    private double rating;
    private String category;
    private boolean popular;

    // Constructors
    public StopForm() {
    }

    public StopForm(int id, String name, String description, String streetAddress, String cityName, String stateName, int zipCode, Double latitude, Double longitude, double cost, String hoursOfOperation, double rating, String category, boolean popular) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.streetAddress = streetAddress;
        this.cityName = cityName;
        this.stateName = stateName;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cost = cost;
        this.hoursOfOperation = hoursOfOperation;
        this.rating = rating;
        this.category = category;
        this.popular = popular;
    }

    // Getters and Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getHoursOfOperation() {
        return hoursOfOperation;
    }

    public void setHoursOfOperation(String hoursOfOperation) {
        this.hoursOfOperation = hoursOfOperation;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isPopular() {
        return popular;
    }

    public void setPopular(boolean popular) {
        this.popular = popular;
    }

    // Additional methods as needed
}