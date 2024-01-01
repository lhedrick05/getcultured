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

    // Store the image in the local application directory images
//    private String storeImage(MultipartFile file) throws IOException {
//        if (file.isEmpty()) {
//            throw new IOException("Cannot store empty file");
//        }
//
//        Path imageDirectory = Paths.get("src/main/resources/static/images");
//        Files.createDirectories(imageDirectory);
//
//        // Extract the file extension
//        String originalFilename = file.getOriginalFilename();
//        String fileExtension = "";
//
//        if (originalFilename != null && originalFilename.lastIndexOf('.') > 0) {
//            fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
//        }
//
//        // Validate file extension
//        if (!fileExtension.matches("\\.(jpg|jpeg|png)$")) {
//            throw new IOException("Unsupported file type: " + fileExtension);
//        }
//
//        // Generate a unique filename
//        String filename = UUID.randomUUID().toString() + fileExtension;
//
//        // Resolve the file path and save the file
//        Path destinationFile = imageDirectory.resolve(filename).normalize().toAbsolutePath();
//        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
//
//        return filename; // Return the saved file name
//    }

    // Works to store image, but i dont think the path is being saved properly
//    private String storeImage(MultipartFile file) throws IOException {
//        // Define the directory where you want to store images
//        Path imageDirectory = Paths.get("C:/Users/lhedr/LaunchCode/GetCultured/images");
//
//        // Extract the original file extension
//        String originalFilename = file.getOriginalFilename();
//        String fileExtension = "";
//
//        if (originalFilename != null && originalFilename.contains(".")) {
//            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        }
//
//        // Generate a unique filename to avoid overwriting existing files
//        String filename = UUID.randomUUID().toString() + fileExtension;
//
//        // Resolve the file path
//        Path filePath = imageDirectory.resolve(filename);
//
//        // Save the file
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//        // Return the filename
//        return filename;
//    }

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

}
