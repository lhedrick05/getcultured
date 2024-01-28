package liftoff.atlas.getcultured.services;

import jakarta.transaction.Transactional;
import liftoff.atlas.getcultured.dto.TourCategoryForm;
import liftoff.atlas.getcultured.models.Tour;
import liftoff.atlas.getcultured.models.TourCategory;
import liftoff.atlas.getcultured.models.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(TourService.class);
    private final TourRepository tourRepository;

    private final StopRepository stopRepository;

    private final CityRepository cityRepository;

    private final TagRepository tagRepository;

    private final StopService stopService;

    private final TourCategoryRepository tourCategoryRepository;

    public TourCategoryService(TourRepository tourRepository, StopRepository stopRepository, CityRepository cityRepository, TagRepository tagRepository, StopService stopService, TourCategoryRepository tourCategory) {
        this.tourRepository = tourRepository;
        this.stopRepository = stopRepository;
        this.cityRepository = cityRepository;
        this.tagRepository = tagRepository;
        this.stopService = stopService;
        this.tourCategoryRepository = tourCategory;
    }

    public TourCategory getTourCategoryById(int tourId) {
        return tourCategoryRepository.findById(tourId).orElse(null);
    }

    public List<TourCategory> getAllTourCategories() {
        return (List<TourCategory>) tourCategoryRepository.findAll();
    }

    @Transactional
    public void saveTourCategory(TourCategory tourCategory) {
        tourCategoryRepository.save(tourCategory);
    }

    // Create or update a TourCategory from a TourCategoryForm
    public Optional<TourCategory> createTourCategoryFromForm(TourCategoryForm tourCategoryForm) {
        Optional<TourCategory> tourCategoryOptional;
        if (tourCategoryForm.getId() != 0) {
            tourCategoryOptional = tourCategoryRepository.findById(tourCategoryForm.getId());
            if(tourCategoryOptional.isPresent()) {
                TourCategory tourCategory = tourCategoryOptional.get();
                // Update the properties of the tourCategory from the form
                tourCategory.setName(tourCategoryForm.getName());
                tourCategory.setDescription(tourCategoryForm.getDescription());
                // Add other fields from the form as needed
                tourCategoryRepository.save(tourCategory);
            }
        } else {
            TourCategory tourCategory = new TourCategory();
            // Set properties from the form
            tourCategory.setName(tourCategoryForm.getName());
            tourCategory.setDescription(tourCategoryForm.getDescription());
            // Add other fields from the form as needed
            tourCategoryRepository.save(tourCategory);
            tourCategoryOptional = Optional.of(tourCategory);
        }
        return tourCategoryOptional;
    }

    @Transactional
    public void deleteTourCategory(int tourCategoryId) {
        tourCategoryRepository.deleteById(tourCategoryId);
    }
}
