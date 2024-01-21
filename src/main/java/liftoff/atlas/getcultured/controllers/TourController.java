package liftoff.atlas.getcultured.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import liftoff.atlas.getcultured.dto.StopForm;
import liftoff.atlas.getcultured.dto.TourForm;
import liftoff.atlas.getcultured.models.City;
import liftoff.atlas.getcultured.models.Stop;
import liftoff.atlas.getcultured.models.Tour;
import liftoff.atlas.getcultured.models.TourCategory;
import liftoff.atlas.getcultured.services.CityService;
import liftoff.atlas.getcultured.services.StopService;
import liftoff.atlas.getcultured.services.TourCategoryService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tours")
@SessionAttributes("tourForm")
public class TourController {

    private static final Logger logger = LoggerFactory.getLogger(TourController.class);

    @Autowired
    private final TourService tourService;

    @Autowired
    private final StopService stopService;

    @Autowired
    private final TourCategoryService tourCategoryService;

    @Autowired
    private final CityService cityService;

    @ModelAttribute("tourForm")
    public TourForm getTourForm() {
        return new TourForm(); // This ensures a new form is created if not present in the session
    }

    @Autowired
    public TourController(TourService tourService, StopService stopService, TourCategoryService tourCategoryService, CityService cityService) {
        this.tourService = tourService;
        this.stopService = stopService;
        this.tourCategoryService = tourCategoryService;
        this.cityService = cityService;
    }

    @GetMapping("/index")
    public String index (Model model, SessionStatus sessionStatus){
        if (model.containsAttribute("tourForm")) {
            sessionStatus.setComplete();
            List<Tour> tour = (List<Tour>) tourService.getAllTours();
            model.addAttribute("tours", tour); // Make sure this is the correct value
            model.addAttribute("title", "All Tours");
        }
        return "tours/index";
    }

    // Display the form for creating a new tour
    @GetMapping("/create")
    public String showCreateTourForm(Model model, SessionStatus sessionStatus) {
        if (!model.containsAttribute("tourForm")) {
            sessionStatus.setComplete();
            model.addAttribute("tourForm", new TourForm());
        }

        model.addAttribute("categories", tourCategoryService.getAllTourCategories());
        model.addAttribute("cities", cityService.getAllCities());
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

            // Handle category
            if (tourForm.getCategoryId() != null) {
                Optional<TourCategory> categoryOptional = Optional.ofNullable(tourCategoryService.getTourCategoryById(tourForm.getCategoryId()));
                if (categoryOptional.isPresent()) {
                    tour.setCategory(categoryOptional.get()); // Set the category
                } else {
                    // Optionally handle the case where the category is not found
                    logger.error("Category with id {} not found", tourForm.getCategoryId());
                    // Throw an exception or handle this case accordingly
                }
            }

            // Handle city
            if (tourForm.getCityId() != null) {
                Optional<City> cityOptional = Optional.ofNullable(cityService.getCityById(tourForm.getCityId()));
                if (cityOptional.isPresent()) {
                    tour.setCity(cityOptional.get()); // Set the city
                } else {
                    // Optionally handle the case where the category is not found
                    logger.error("City with id {} not found", tourForm.getCityId());
                    // Throw an exception or handle this case accordingly
                }
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
    @GetMapping("/update/cancel")
    public String cancelTourUpdate(SessionStatus sessionStatus) {
        // Mark the current session as complete to clear any session attributes
        sessionStatus.setComplete();
        // Redirect to the desired page, such as the tours list
        return "redirect:/tours";
    }


    // Mapping for listing all tours
    @GetMapping
    public String listTours(Model model, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        List<Tour> tours = tourService.getAllTours();
        model.addAttribute("tours", tours);
        return "tours/tours";
    }

    // Mapping for viewing a specific tour
    @GetMapping("/view/{tourId}")
    public String viewTour(@PathVariable int tourId, Model model) {
        Tour tour = tourService.getTourById(tourId);
        if (tour != null) {
            TourCategory category = tourCategoryService.getTourCategoryById(tour.getId());
            tour.setCategory(category);
            City city = cityService.getCityById(tour.getId());
            tour.setCity(city);
            model.addAttribute("tour", tour);
//            model.addAttribute("tour", tour);
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
        stopForm.setStopName(stop.getName());
        stopForm.setDescription(stop.getStopDescription());
        stopForm.setStreetAddress(stop.getStreetAddress());
        stopForm.setCityName(stop.getCityName());
        stopForm.setStateName(stop.getStateName());
        stopForm.setZipCode(stop.getZipCode());
        // Copy other relevant fields from Stop to StopForm
        return stopForm;
    }
    private TourForm convertTourToForm(Tour tour) {
        TourForm form = new TourForm();
        form.setId(tour.getId());
        form.setName(tour.getName());
        form.setSummaryDescription(tour.getSummaryDescription());
        form.setEstimatedLength(tour.getEstimatedLength());
        form.setImagePath(tour.getImagePath());
        List<StopForm> stopForms = tour.getStops().stream()
                .map(this::convertToStopForm)
                .collect(Collectors.toList());
        form.setStops(stopForms);
        // ... set other fields ...
        return form;
    }

    @GetMapping("/create/selectStop")
    public String selectStopsForTour(@RequestParam(required = false) String context,
                                     @ModelAttribute("tourForm") TourForm tourForm,
                                     Model model, RedirectAttributes redirectAttributes) {
        List<Stop> stops = stopService.findAll();
        model.addAttribute("stops", stops);
        model.addAttribute("tourForm", tourForm);
        model.addAttribute("context", context); // context for creating a tour
        return "stops/stops";
    }

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

    @GetMapping("/stops/select")
    @ResponseBody
    public ResponseEntity<List<Stop>> getStopsForSelection() {
        List<Stop> stops = stopService.findAll();
        return ResponseEntity.ok(stops); // Sends the list of stops as JSON
    }
    @GetMapping("/update/{tourId}")
    public String showUpdateTourForm(@PathVariable int tourId, Model model) {
        Tour tour = tourService.getTourById(tourId);
        if (tour != null) {
            // Populate the model with tour data
            TourForm tourForm = convertTourToForm(tour);
            model.addAttribute("tourForm", tourForm);
            return "tours/update";
        } else {
            model.addAttribute("errorMessage", "Tour not found");
            return "redirect:/tours";
        }
    }


    // Was for session managment with dynamic stop manipulation
//    @PostMapping("/update/{tourId}")
//    public String updateTour(@ModelAttribute("tourForm") @Valid TourForm tourForm,
//                             BindingResult bindingResult,
//                             Model model,
//                             RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            return "tours/update";
//        }
//
//        try {
//            tourService.updateTour(tourForm);  // Implement this method in your service
//            redirectAttributes.addFlashAttribute("successMessage", "Tour updated successfully!");
//            return "redirect:/tours/view/" + tourForm.getId();
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Error updating the tour.");
//            return "redirect:/tours/update";
//        }
//    }

    @PostMapping("/update/{tourId}")
    public String updateTour(@PathVariable("tourId") Integer tourId,
                             @ModelAttribute("tourForm") TourForm tourForm,
                             RedirectAttributes redirectAttributes, SessionStatus sessionStatus) {
        try {
            Tour tour = tourService.getTourById(tourId);
            if (tour != null) {
                tour.setName(tourForm.getName());
                tour.setSummaryDescription(tourForm.getSummaryDescription());
                tour.setEstimatedLength(tourForm.getEstimatedLength());
                tourService.saveTour(tour, null); // Adjust saveTour method as necessary
                sessionStatus.setComplete();
                redirectAttributes.addFlashAttribute("successMessage", "Tour updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Tour not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating the tour: " + e.getMessage());
        }
        return "redirect:/tours/view/" + tourId;
    }

    // Another method for update stops in a tour
//    @GetMapping("/update/selectStop/{tourId}")
//    public String selectStopsForTourUpdate(@PathVariable int tourId, Model model) {
//        List<Stop> stops = stopService.findAll();
//        model.addAttribute("stops", stops);
//        model.addAttribute("context", "update");
//        model.addAttribute("tourId", tourId);
//        return "stops/stops";  // Create this Thymeleaf template
//    }

    // Methods for updating stops in the tour, incomplete functionality
//    @PostMapping("/update/{tourId}/addStop/{stopId}")
//    public String addStopToTourUpdate(@PathVariable("tourId") Integer tourId,
//                                      @PathVariable("stopId") Integer stopId,
//                                      @ModelAttribute("tourForm") TourForm tourForm,
//                                      RedirectAttributes redirectAttributes,
//                                      HttpSession session) {
//        Stop stop = stopService.findById(stopId);
//        if (stop != null) {
//            // Convert stop to stopForm
//            StopForm stopForm = convertToStopForm(stop);
//
//            // Check if the stop is already in the tourForm
//            if (tourForm.getStops().stream().noneMatch(s -> s.getId().equals(stopForm.getId()))) {
//                // Add the stopForm to the tourForm if it's not already present
//                logger.info("Stop with ID {} added to tour with ID {}", stopId, tourId); // Log statement
//                tourForm.getStops().add(stopForm);
//            } else {
//                redirectAttributes.addFlashAttribute("errorMessage", "Stop is already added to the tour.");
//            }
//        } else {
//            redirectAttributes.addFlashAttribute("errorMessage", "Error adding stop to tour: Stop not found.");
//        }
//
//        // Update the session attribute with the modified tourForm
//        session.setAttribute("tourForm", tourForm);
//
//        // Redirect back to the update page, preserving the current state in the session
//        return "redirect:/tours/update/" + tourId;
//    }

//    @GetMapping("/update/{tourId}/addStop/{stopId}")
//    public String addStopToTourUpdate(@PathVariable("tourId") Integer tourId,
//                                      @PathVariable("stopId") Integer stopId,
//                                      @ModelAttribute("tourForm") TourForm tourForm,
//                                      RedirectAttributes redirectAttributes,
//                                      HttpSession session) {
//        Stop stop = stopService.findById(stopId);
//        if (stop != null && !tourForm.getStops().contains(stop)) { // Check to prevent duplicates
//            StopForm stopForm = convertToStopForm(stop);
//            tourForm.getStops().add(stopForm);
//
//            // Update the tourForm object with the new stop information
//            TourForm updatedTourForm = convertTourToForm(tourService.getTourById(tourId));
//            updatedTourForm.setStops(tourForm.getStops());
//            session.setAttribute("tourForm", updatedTourForm); // Update session
//        } else {
//            redirectAttributes.addFlashAttribute("errorMessage", "Error adding stop to tour.");
//        }
//        return "redirect:/tours/update/" + tourId;
//    }



//    @PostMapping("/update/{tourId}/finalize")
//    public String finalizeTourUpdate(@PathVariable("tourId") Integer tourId,
//                                     HttpSession session,
//                                     RedirectAttributes redirectAttributes) {
//        TourForm tourForm = (TourForm) session.getAttribute("tourForm");
//        if (tourForm != null) {
//            try {
//                Tour updatedTour = tourService.updateTourFromForm(tourId, tourForm, null);
//                session.removeAttribute("tourForm"); // Remove from session
//                redirectAttributes.addFlashAttribute("successMessage", "Tour updated successfully!");
//            } catch (Exception e) {
//                redirectAttributes.addFlashAttribute("errorMessage", "Error updating the tour: " + e.getMessage());
//            }
//        }
//        return "redirect:/tours/view/" + tourId;
//    }

    // Method to add a stop to the tour in the session
    @PostMapping("/update/{tourId}/session/addStop")
    public ResponseEntity<?> addStopToSessionTour(@PathVariable("tourId") Integer tourId,
                                                  @RequestParam("stopId") Integer stopId,
                                                  HttpSession session) {
        try {
            TourForm tourForm = (TourForm) session.getAttribute("tourForm");
            if (tourForm == null) {
                return ResponseEntity.badRequest().body("Session tour form not found");
            }

            // Assuming stopService can find a Stop by ID
            Stop stop = stopService.findById(stopId);
            if (stop == null) {
                return ResponseEntity.notFound().build();
            }

            // Convert Stop to StopForm and add to the TourForm stops
            StopForm stopForm = convertToStopForm(stop);
//            tourForm.getStops().add(stopForm);
            tourForm.addStop(stopForm);

            // Update the session attribute
            session.setAttribute("tourForm", tourForm);

            return ResponseEntity.ok("Stop added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding stop: " + e.getMessage());
        }
    }

    // Method to remove a stop from the tour in the session
    @PostMapping("/update/{tourId}/session/removeStop")
    public ResponseEntity<?> removeStopFromSessionTour(@PathVariable("tourId") Integer tourId,
                                                       @RequestParam("stopId") Integer stopId,
                                                       HttpSession session) {
        try {
            TourForm tourForm = (TourForm) session.getAttribute("tourForm");
            if (tourForm == null) {
                return ResponseEntity.badRequest().body("Session tour form not found");
            }

            // Remove the stop from the TourForm
            tourForm.getStops().removeIf(stopForm -> stopForm.getId().equals(stopId));

            // Update the session attribute
            session.setAttribute("tourForm", tourForm);

            return ResponseEntity.ok("Stop removed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing stop: " + e.getMessage());
        }
    }

    @PostMapping("/updateForm")
    public ResponseEntity<?> updateTourForm(@RequestBody String formDataJson,
                                            BindingResult result,
                                            HttpSession session) {
        if (result.hasErrors()) {
            // If there are validation errors, return a bad request response.
            return ResponseEntity.badRequest().body("Invalid form data");
        }

        try {
            // Parse the JSON data received from the request
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode formData = objectMapper.readTree(formDataJson);

            // Retrieve the tourForm from the session
            TourForm tourForm = (TourForm) session.getAttribute("tourForm");
            if (tourForm == null) {
                // If tourForm doesn't exist in the session, create a new one
                tourForm = new TourForm();
                session.setAttribute("tourForm", tourForm);
            }

            // Update the tourForm object with the new data
            if (formData.has("tourName")) {
                tourForm.setName(formData.get("tourName").asText());
            }
            if (formData.has("summaryDescription")) {
                tourForm.setSummaryDescription(formData.get("summaryDescription").asText());
            }
            if (formData.has("estimatedLength") && formData.get("estimatedLength").isNumber()) {
                // Parse the estimatedLength as double
                tourForm.setEstimatedLength(formData.get("estimatedLength").asDouble());
            }
            // Handle category ID
            if (formData.has("categoryId") && formData.get("categoryId").canConvertToInt()) {
                int categoryId = formData.get("categoryId").asInt();
                // Optional: Validate the category ID (e.g., check if such a category exists)
                tourForm.setCategoryId(categoryId);
            }

            // Handle city ID
            if (formData.has("cityId") && formData.get("cityId").canConvertToInt()) {
                int cityId = formData.get("cityId").asInt();
                // Optional: Validate the city ID (e.g., check if such a city exists)
                tourForm.setCityId(cityId);
            }

            // ... handle other fields similarly

            // Save the updated tourForm back to the session
            session.setAttribute("tourForm", tourForm);

            // Return a success response
            return ResponseEntity.ok().body("Form data saved successfully");
        } catch (JsonProcessingException e) {
            // Handle JSON parsing errors
            return ResponseEntity.badRequest().body("Invalid form data format");
        }
    }



}


