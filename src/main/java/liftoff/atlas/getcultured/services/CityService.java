package liftoff.atlas.getcultured.services;

import jakarta.transaction.Transactional;
import liftoff.atlas.getcultured.dto.CityForm;
import liftoff.atlas.getcultured.dto.TourCategoryForm;
import liftoff.atlas.getcultured.models.City;
import liftoff.atlas.getcultured.models.TourCategory;
import liftoff.atlas.getcultured.models.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private static final Logger logger = LoggerFactory.getLogger(TourService.class);
    private final TourRepository tourRepository;

    private final StopRepository stopRepository;

    private final CityRepository cityRepository;


    private final TagRepository tagRepository;

    private final StopService stopService;

    public CityService(TourRepository tourRepository, StopRepository stopRepository, CityRepository cityRepository, TagRepository tagRepository, StopService stopService) {
        this.tourRepository = tourRepository;
        this.stopRepository = stopRepository;
        this.cityRepository = cityRepository;
        this.tagRepository = tagRepository;
        this.stopService = stopService;
    }

    public List<City> getAllCities() {
        return (List<City>) cityRepository.findAll();
    }

    public City getCityById(int tourId) {
        return cityRepository.findById(tourId).orElse(null);
    }

    @Transactional
    public void saveCity(City city) {
        cityRepository.save(city);
    }

    // Create or update a City from a CityForm
    public Optional<City> createCityFromForm(CityForm cityForm) {
        Optional<City> cityOptional;
        if (cityForm.getId() != 0) {
            cityOptional = cityRepository.findById(cityForm.getId());
            if(cityOptional.isPresent()) {
                City city = cityOptional.get();
                // Update the properties of the tourCategory from the form
                city.setName(cityForm.getName());
                city.setState(cityForm.getState());
                // Add other fields from the form as needed
                cityRepository.save(city);
            }
        } else {
            City city = new City();
            // Set properties from the form
            city.setName(cityForm.getName());
            city.setState(cityForm.getState());
            // Add other fields from the form as needed
            cityRepository.save(city);
            cityOptional = Optional.of(city);
        }
        return cityOptional;
    }

    @Transactional
    public void deleteCity(int cityId) {
        cityRepository.deleteById(cityId);
    }
}
