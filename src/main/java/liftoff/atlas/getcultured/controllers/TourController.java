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
import org.springframework.http.HttpStatus;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tours")
@SessionAttributes("tourForm")
public class TourController {

    private static final Logger logger = LoggerFactory.getLogger(TourController.class);

    @Autowired
    private final TourService tourService;

    @Autowired
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
        model.addAttribute("context", "create"); // Set context for creating a tour
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
            List<StopForm> stops = tourForm.getStops();
            if (stops == null) {
                stops = new ArrayList<>();
            }

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
        List<Stop> allStops = stopService.findAll();
        model.addAttribute("tour", tour);
        model.addAttribute("allStops", allStops);
        model.addAttribute("context", "update"); // Set context for updating a tour
        return "tours/update";  // Name of the Thymeleaf template for updating a tour
    }

    // Mapping to handle the submission of the update form
    @PostMapping("/update/{id}")
    public String updateTour(@PathVariable("id") int id,
                             @ModelAttribute("tour") Tour tour,
                             BindingResult result,
                             @RequestParam(value = "image", required = false) MultipartFile imageFile,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "tours/update";
        }

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                tourService.saveTour(tour, imageFile);
            } else if (tour.getImagePath() == null || tour.getImagePath().isEmpty()) {
                // Set default image if no image previously set and no new image uploaded
                String defaultImagePath = "defaultLogo/DefaultLogo.jpg";
                tour.setImagePath(defaultImagePath);
            }
            // No need to update the image if it exists and no new image is uploaded
            tourService.saveTour(tour, null);

            redirectAttributes.addFlashAttribute("successMessage", "Tour updated successfully!");
            return "redirect:/tours/view/" + id;
        } catch (Exception e) {
            logger.error("Error updating tour", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating tour.");
            return "redirect:/tours/update/" + id;
        }
    }

//    @GetMapping("/create/addStop/{stopId}")
//    public String addStopToTourCreation(@PathVariable Integer stopId,
//                                        @ModelAttribute("tourForm") TourForm tourForm,
//                                        RedirectAttributes redirectAttributes) {
//        Stop stop = stopService.findById(stopId);
//        if (stop != null) {
//            StopForm stopForm = convertToStopForm(stop);
//            tourForm.getStops().add(stopForm);
//            redirectAttributes.addFlashAttribute("tourForm", tourForm);
//        }
//        return "redirect:/tours/create";
//    }

    // Method to add a stop to the tour during creation
    @GetMapping("/create/addStop/{stopId}")
    public String addStopToTourCreation(@PathVariable Integer stopId,
                                        @ModelAttribute("tourForm") TourForm tourForm,
                                        @RequestParam(required = false) String context,
                                        RedirectAttributes redirectAttributes) {
        Stop stop = stopService.findById(stopId);
        if (stop != null) {
            StopForm stopForm = convertToStopForm(stop);
            tourForm.getStops().add(stopForm);
            redirectAttributes.addFlashAttribute("tourForm", tourForm);
        }
        // Redirect back to the tour creation page with context
        return "redirect:/tours/create?context=" + (context != null ? context : "");
    }

    private StopForm convertToStopForm(Stop stop) {
        StopForm stopForm = new StopForm();
        stopForm.setId(stop.getId());
        stopForm.setName(stop.getName());
        stopForm.setDescription(stop.getStopDescription());
        stopForm.setStreetAddress(stop.getStreetAddress());
        stopForm.setCityName(stop.getCityName());
        stopForm.setStateName(stop.getStateName());
        stopForm.setZipCode(stop.getZipCode());
        // Copy other relevant fields from Stop to StopForm
        return stopForm;
    }

    @GetMapping("/create/selectStop")
    public String selectStopsForTour(@RequestParam(required = true) String context,
                                     @ModelAttribute("tourForm") TourForm tourForm,
                                     Model model, RedirectAttributes redirectAttributes) {
        List<Stop> stops = stopService.findAll();
        model.addAttribute("stops", stops);
        model.addAttribute("tourForm", tourForm);
        model.addAttribute("context", context); // context for creating a tour
        return "stops/stops";
    }

//    @GetMapping("/create/selectStop")
//    public String selectStopsForTour(Model model, @ModelAttribute("tourForm") TourForm tourForm) {
//        List<Stop> stops = stopService.findAll();
//        model.addAttribute("stops", stops);
//        return "stops/stops";
//    }

    @GetMapping("/update/selectStop/{id}")
    public String selectStopsForTourUpdate(@RequestParam(required = true) String context,
                                        @ModelAttribute("tourForm") TourForm tourForm,
                                     Model model, RedirectAttributes redirectAttributes, @PathVariable("id") int tourId) {
        List<Stop> stops = stopService.findAll();
        model.addAttribute("stops", stops);
        model.addAttribute("tourForm", tourForm);
        model.addAttribute("context", context); // context for updating a tour
        model.addAttribute("tourId", tourId); // Pass the tour ID as well
        return "stops/stops";
    }


//    @GetMapping("/{tourId}/removeStop/{stopId}")
//    public String removeStopFromTour(@PathVariable int tourId, @PathVariable int stopId, RedirectAttributes redirectAttributes) {
//        try {
//            tourService.removeStopFromTour(tourId, stopId);
//            redirectAttributes.addFlashAttribute("successMessage", "Stop removed successfully!");
//        } catch (Exception e) {
//            logger.error("Error removing stop from tour", e);
//            redirectAttributes.addFlashAttribute("errorMessage", "Error removing stop.");
//        }
//        return "redirect:/tours/update/" + tourId;
//    }

    @PostMapping("/create/removeStop")
    public ResponseEntity<?> removeStopFromTourForm(@ModelAttribute("tourForm") TourForm tourForm,
                                                    @RequestParam("stopIndex") int stopIndex) {
        try {
            if (tourForm.getStops() != null && tourForm.getStops().size() > stopIndex) {
                tourForm.getStops().remove(stopIndex);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error removing stop from tour form", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/update/{tourId}/addStop/{stopId}")
    public String addStopToTourUpdate(@PathVariable Integer tourId, @PathVariable Integer stopId,
                                      RedirectAttributes redirectAttributes) {
        try {
            tourService.addStopToTour(tourId, stopId);
            redirectAttributes.addFlashAttribute("successMessage", "Stop added successfully!");
        } catch (Exception e) {
            logger.error("Error adding stop to tour", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding stop.");
        }
        return "redirect:/tours/update/" + tourId;
    }

    @GetMapping("/stops/select")
    @ResponseBody
    public ResponseEntity<List<Stop>> getStopsForSelection() {
        List<Stop> stops = stopService.findAll();
        return ResponseEntity.ok(stops); // Sends the list of stops as JSON
    }

}



