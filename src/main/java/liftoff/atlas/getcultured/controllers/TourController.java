package liftoff.atlas.getcultured.controllers;

import jakarta.validation.Valid;
import liftoff.atlas.getcultured.dto.StopForm;
import liftoff.atlas.getcultured.dto.TourForm;
import liftoff.atlas.getcultured.models.Stop;
import liftoff.atlas.getcultured.models.Tour;
import liftoff.atlas.getcultured.services.StopService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/tours")
@SessionAttributes("tourForm")
public class TourController {

    private static final Logger logger = LoggerFactory.getLogger(TourController.class);
    private final TourService tourService;

    private final StopService stopService;

    @ModelAttribute("tourForm")
    public TourForm getTourForm() {
        return new TourForm(); // This ensures a new form is created if not present in the session
    }

    @Autowired
    public TourController(TourService tourService, StopService stopService) {
        this.tourService = tourService;
        this.stopService = stopService;
    }

    @GetMapping("/index")
    public String index (Model model){
        List<Tour> tour = (List<Tour>) tourService.getAllTours();
        model.addAttribute("tours", tour); // Make sure this is the correct value
        model.addAttribute("title", "All Tours");
        return "tours/index";
    }

    // Display the form for creating a new tour
    @GetMapping("/create")
    public String showCreateTourForm(Model model) {
        if (!model.containsAttribute("tourForm")) {
            model.addAttribute("tourForm", new TourForm());
        }

        // Fetch all stops
        List<Stop> stops = stopService.findAll();
        model.addAttribute("stops", stops);
        return "tours/create";
    }

    // Handling the tour creation form submission
    @PostMapping("/create")
    public String createTour(@ModelAttribute("tourForm") @Valid TourForm tourForm,
                             BindingResult bindingResult,
                             @RequestParam(value = "image", required = false) MultipartFile imageFile,
                             Model model, SessionStatus sessionStatus, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "tours/create";
        }

        try {
            Tour tour;
            if (imageFile != null && !imageFile.isEmpty()) {
                // Create tour with the provided image
                tour = tourService.createTourFromForm(tourForm, imageFile);
            } else {
                // Set default image path if no image is uploaded
                String defaultImagePath = "defaultLogo/DefaultLogo.jpg";
                tourForm.setImagePath(defaultImagePath);
                // Create tour with the default image
                tour = tourService.createTourFromForm(tourForm, null);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Tour created successfully!");
            sessionStatus.setComplete(); // Mark the session as complete
            return "redirect:/tours/view/" + tour.getId(); // Redirect to view the created tour

        } catch (Exception e) { // Catching a broader range of exceptions
            logger.error("Error while saving tour", e);
            model.addAttribute("errorMessage", "Error processing the tour.");
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
    @GetMapping("/view/{tourId}")
    public String viewTour(@PathVariable int tourId, Model model) {
        Tour tour = tourService.getTourById(tourId);
        if (tour != null) {
            model.addAttribute("tour", tour);
            return "tours/view"; // This should be the name of your Thymeleaf template for viewing a tour
        } else {
            // Handle the case where the tour with the given ID does not exist
            model.addAttribute("errorMessage", "Tour not found");
            return "redirect:/tours"; // Redirecting to the list of tours
        }
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

    // Method to add a selected stop to the tour
//    @GetMapping("/create/addStop/{stopId}")
//    public String addStopToTour(@PathVariable Integer stopId,
//                                @ModelAttribute("tourForm") TourForm tourForm,
//                                RedirectAttributes redirectAttributes) {
//        Stop stop = stopService.findById(stopId);
//        if (stop != null) {
//            // Convert Stop to StopForm and add to tourForm
//            StopForm stopForm = new StopForm();
//            stopForm.setId(stop.getId());
//            // ... set other properties ...
//            tourForm.getStops().add(stopForm);
//
//            redirectAttributes.addFlashAttribute("tourForm", tourForm);
//        }
//        return "redirect:/tours/create";
//    }

    @PostMapping("/create/addStop/{stopId}")
    public String addStopToTour(@PathVariable Integer stopId,
                                @ModelAttribute("tourForm") TourForm tourForm,
                                RedirectAttributes redirectAttributes) {
        Stop stop = stopService.findById(stopId);
        if (stop != null) {
            StopForm stopForm = convertToStopForm(stop);
            tourForm.getStops().add(stopForm); // Assuming TourForm has a list of StopForm objects
        } else {
            // Handle the case where the stop is not found
        }
        redirectAttributes.addFlashAttribute("tourForm", tourForm);
        return "redirect:/tours/create";
    }

    private StopForm convertToStopForm(Stop stop) {
        StopForm stopForm = new StopForm();
        stopForm.setId(stop.getId());
        stopForm.setName(stop.getName());
        stopForm.setDescription(stop.getStopDescription());
        // Copy other relevant fields from Stop to StopForm
        return stopForm;
    }


//    // Method to select stops for the tour
//    @GetMapping("/create/selectStop")
//    public String selectStopsForTour(@ModelAttribute("tourForm") TourForm tourForm,
//                                     Model model, RedirectAttributes redirectAttributes) {
//        List<Stop> stops = stopService.findAll();
//        model.addAttribute("stops", stops);
//        redirectAttributes.addFlashAttribute("tourForm", tourForm);
//        return "redirect:/stops/stops";
//    }

    // New method to redirect to stop list from tour creation
    @GetMapping("/create/selectStop")
    public String selectStopsForTour(@ModelAttribute("tourForm") TourForm tourForm, Model model) {
        List<Stop> stops = stopService.findAll();
        model.addAttribute("stops", stops);
        model.addAttribute("addingStopsToTour", true); // Flag to indicate this view is for adding stops to a tour
        return "stops/stops"; // Reuse the stops list view with slight modification
    }



}



