package liftoff.atlas.getcultured.controllers;

import jakarta.validation.Valid;
import liftoff.atlas.getcultured.models.Tour;
import liftoff.atlas.getcultured.services.TourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/tours")
public class TourController {

    private static final Logger logger = LoggerFactory.getLogger(TourController.class);
    private final TourService tourService;

    @Autowired
    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/index")
    public String index (Model model){
        List<Tour> tour = (List<Tour>) tourService.getAllTours();
        model.addAttribute("tours", tour); // Make sure this is the correct value
        model.addAttribute("title", "All Tours");
        return "tours/index";
    }

    // Mapping for the tour creation form
    @GetMapping("/create")
    public String showCreateTourForm(Model model) {
        model.addAttribute("tour", new Tour());
        return "tours/create";
    }

    // Handling the tour creation form submission
    @PostMapping("/create")
    public String createTour(@ModelAttribute("tour") @Valid Tour tour,
                             BindingResult bindingResult,
                             @RequestParam("image") MultipartFile imageFile,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "tours/create";
        }

        try {
            if (!imageFile.isEmpty()) {
                tourService.saveTour(tour, imageFile);
            } else {
                // Handle the case where no image is uploaded
                // Maybe set a default imagePath or leave it as null
            }
            return "redirect:/tours";
        } catch (IOException e) {
            logger.error("Error while saving tour and image", e);
            model.addAttribute("errorMessage", "Error processing image upload.");
            return "tours/create";
        }
    }


    // Mapping for listing all tours
    @GetMapping
    public String listTours(Model model) {
        List<Tour> tours = tourService.getAllTours();
        model.addAttribute("tours", tours);
        return "tours/tours";
    }

    // Mapping for viewing a specific tour
    @GetMapping("/{tourId}")
    public String viewTour(@PathVariable int tourId, Model model) {
        Tour tour = tourService.getTourById(tourId);
        model.addAttribute("tour", tour);
        return "tours/view";
    }

    // Mapping for deleting a tour
    @PostMapping("/{tourId}/delete")
    public String deleteTour(@PathVariable int tourId) {
        tourService.deleteTour(tourId);
        return "redirect:/tours";
    }

    // Other mappings for updating tours, handling reviews, etc.

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) throws MalformedURLException {
        Path file = Paths.get("src/main/resources/static/images").resolve(filename);
        if (!Files.exists(file)) {
            return ResponseEntity.notFound().build();
        }

        Resource fileResource = new UrlResource(file.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileResource.getFilename() + "\"")
                .body(fileResource);
    }

    @GetMapping("/update/{id}")
    public String showUpdateTourForm(@PathVariable("id") int id, Model model) {
        Tour tour = tourService.getTourById(id);
        if (tour == null) {
            // Handle the case where the tour does not exist (optional)
            return "redirect:/tours";
        }
        model.addAttribute("tour", tour);
        return "tours/update";  // Name of the Thymeleaf template for updating a tour
    }

    // Mapping to handle the submission of the update form
    @PostMapping("/update/{id}")
    public String updateTour(@PathVariable("id") int id,
                             @ModelAttribute("tour") Tour tour,
                             BindingResult result,
                             @RequestParam("image") MultipartFile imageFile) {
        if (result.hasErrors()) {
            return "tours/update";
        }

        try {
            // Update the tour with the provided data
            if (imageFile != null && !imageFile.isEmpty()) {
                tourService.saveTour(tour, imageFile);
            } else {
                tourService.saveTour(tour, null);  // Call with null if no new image
            }
            return "redirect:/tours/view/" + id;  // Redirect to the view page of the updated tour
        } catch (Exception e) {
            // Handle exceptions (e.g., image processing error)
            return "tours/update";
        }
    }

}



