package liftoff.atlas.getcultured.models;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;

@Entity
public class Stop extends AbstractEntity {

    private boolean stopStatus;
    @Size(max = 300, message = "Description can be no longer than 300 characters")
    private String stopDescription;

    private String streetAddress;

    private String cityName;

    private String stateName;

    private int zipCode;

    private int latitude;

    private int longitude;

    private MapMarker mapMarker;

    private int cost;

    private String hoursOfOperation;

    private int stopRating;

    private String category;

    private boolean popularStopDesignation;


}
