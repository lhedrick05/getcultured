package liftoff.atlas.getcultured.services;

import jakarta.transaction.Transactional;
import liftoff.atlas.getcultured.dto.TourForm;
import liftoff.atlas.getcultured.models.Stop;
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
        this.rootLocation = Paths.get("C:/Users/lhedr/LaunchCode/GetCultured/images");
    }

    public List<Tour> getAllTours() {
        return (List<Tour>) tourRepository.findAll();
    }

    public Tour getTourById(int tourId) {
        return tourRepository.findById(tourId).orElse(null);
    }

    @Transactional
    public void saveTour(Tour tour, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = storeImage(imageFile);
            tour.setImagePath(filename);
        } else {
            String defaultImagePath = "defaultLogo/DefaultLogo.jpg"; // Path to default image
            tour.setImagePath(defaultImagePath);
        }
        tourRepository.save(tour);
    }

    @Transactional
    public void deleteTour(int tourId) {
        tourRepository.deleteById(tourId);
    }

    public String storeImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String filename = UUID.randomUUID().toString() + fileExtension;
        Path filePath = rootLocation.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "images/" + filename;
    }

    public Tour updateTour(int tourId, Tour updatedTourData) {
        Optional<Tour> existingTourOptional = tourRepository.findById(tourId);
        if (existingTourOptional.isPresent()) {
            Tour existingTour = existingTourOptional.get();
            existingTour.setName(updatedTourData.getName());
            existingTour.setSummaryDescription(updatedTourData.getSummaryDescription());
            // ... set other properties ...
            return tourRepository.save(existingTour);
        } else {
            logger.error("Tour not found for ID: " + tourId);
            // Additional handling for tour not found
            return null;
        }
    }

    public Tour createTourFromForm(TourForm tourForm, MultipartFile imageFile) throws IOException {
        Tour tour = new Tour();
        tour.setName(tourForm.getName());
        tour.setSummaryDescription(tourForm.getSummaryDescription());
        // ... set other properties ...

        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = storeImage(imageFile);
            tour.setImagePath(filename);
        } else {
            String defaultImagePath = "defaultLogo/DefaultLogo.jpg"; // Path to default image
            tour.setImagePath(defaultImagePath);
        }

        tourForm.getStops().forEach(stopForm -> {
            Stop stop = new Stop();
            stop.setName(stopForm.getName());
            stop.setStopDescription(stopForm.getDescription());
            // ... set other properties ...
            tour.addStop(stop);
        });

        return tourRepository.save(tour);
    }
}

