package liftoff.atlas.getcultured.services;

import liftoff.atlas.getcultured.models.City;
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

//    private final TourCategoryRepository tourCategoryRepository;

    private final TagRepository tagRepository;

    private final StopService stopService;

    public CityService(TourRepository tourRepository, StopRepository stopRepository, CityRepository cityRepository, TagRepository tagRepository, StopService stopService) {
        this.tourRepository = tourRepository;
        this.stopRepository = stopRepository;
        this.cityRepository = cityRepository;
//        this.tourCategoryRepository = tourCategoryRepository;
        this.tagRepository = tagRepository;
        this.stopService = stopService;
    }

    public List<City> getAllCities() {
        return (List<City>) cityRepository.findAll();
    }

    public Optional<City> getCity(int cityId) {
        return cityRepository.findById(cityId);
    }
}
