package liftoff.atlas.getcultured.services;

import jakarta.transaction.Transactional;
import liftoff.atlas.getcultured.models.Tour;
import liftoff.atlas.getcultured.models.data.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TourService {

    private static final Logger logger = LoggerFactory.getLogger(TourService.class);

    private final TourRepository tourRepository;

    private final Path rootLocation; // Image storage location

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
        this.rootLocation = Paths.get("src/main/resources/static/images"); // Define your image storage path here
    }

    // Get all tours
    public List<Tour> getAllTours() {
        return (List<Tour>) tourRepository.findAll();
    }

    // Get a tour by ID
    public Tour getTourById(int tourId) {
        return tourRepository.findById(tourId).orElse(null);
    }

    @Transactional
    public void saveTour(Tour tour, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = storeImage(imageFile); // Store the image and get the filename
            tour.setImagePath(filename); // Save the file path in the Tour object
        }
        tourRepository.save(tour);
    }

    // Delete a tour by ID
    @Transactional
    public void deleteTour(int tourId) {
        tourRepository.deleteById(tourId);
    }

    private String storeImage(MultipartFile file) throws IOException {
        // Define the directory where you want to store images
        Path imageDirectory = Paths.get("C:/Users/lhedr/LaunchCode/GetCultured/images");

        // Extract the original file extension
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Generate a unique filename to avoid overwriting existing files
        String filename = UUID.randomUUID().toString() + fileExtension;

        // Resolve the file path
        Path filePath = imageDirectory.resolve(filename);

        // Save the file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative path to be saved in the database
        return "images/" + filename; // Just return the relative path
    }

    public Tour updateTour(int tourId, Tour updatedTourData) {
        Optional<Tour> existingTourOptional = tourRepository.findById(tourId);
        if (existingTourOptional.isPresent()) {
            Tour existingTour = existingTourOptional.get();

            // Update the fields of the existing tour with updatedTourData
            existingTour.setName(updatedTourData.getName());
            existingTour.setSummaryDescription(updatedTourData.getSummaryDescription());
            // Set other fields as needed

            return tourRepository.save(existingTour);
        } else {
            // Handle the case where the tour is not found
            // This could be throwing an exception or any other business logic
        }
        return null;
    }

}
