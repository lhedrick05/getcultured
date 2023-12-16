package liftoff.atlas.getcultured.services;

import liftoff.atlas.getcultured.models.Tour;
import liftoff.atlas.getcultured.models.data.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class TourService {

    private final TourRepository tourRepository;

    private Tour tour = new Tour();

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    // Get all tours
    public List<Tour> getAllTours() {
        return (List<Tour>) tourRepository.findAll();
    }

    // Get a tour by ID
    public Tour getTourById(Long tourId) {
        return tourRepository.findById(tour.getId()).orElse(null);
    }

    // Save or update a tour, including handling file upload
    public void saveTour(Tour tour, MultipartFile image) {
        // Handle the image file here (save it, process it, etc.)
        if (image != null && !image.isEmpty()) {
            try {
                // Example: Save the image to a specific directory
                byte[] bytes = image.getBytes();
                // Save the bytes to a file (customize this part based on your needs)
            } catch (IOException e) {
                // Handle the exception
            }
        }

        // Save the Tour entity to the database
        tourRepository.save(tour);
    }

    // Delete a tour by ID
    public void deleteTour(Long tourId) {
        tourRepository.deleteById(tour.getId());
    }

    // Other methods...

}

