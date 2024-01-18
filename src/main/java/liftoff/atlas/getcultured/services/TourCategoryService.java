package liftoff.atlas.getcultured.services;

import liftoff.atlas.getcultured.models.Tour;
import liftoff.atlas.getcultured.models.TourCategory;
import liftoff.atlas.getcultured.models.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(TourService.class);
    private final TourRepository tourRepository;

    private final StopRepository stopRepository;

    private final CityRepository cityRepository;

    private final TourCategoryRepository tourCategoryRepository;

    private final TagRepository tagRepository;

    private final StopService stopService;

    public TourCategoryService(TourRepository tourRepository, StopRepository stopRepository, CityRepository cityRepository, TourCategoryRepository tourCategoryRepository, TagRepository tagRepository, StopService stopService) {
        this.tourRepository = tourRepository;
        this.stopRepository = stopRepository;
        this.cityRepository = cityRepository;
        this.tourCategoryRepository = tourCategoryRepository;
        this.tagRepository = tagRepository;
        this.stopService = stopService;
    }

    public List<TourCategory> getAllTourCategories() {
        return (List<TourCategory>) tourCategoryRepository.findAll();
    }
}
