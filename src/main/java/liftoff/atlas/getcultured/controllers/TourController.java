package liftoff.atlas.getcultured.controllers;

import jakarta.validation.Valid;
import liftoff.atlas.getcultured.models.Tour;
import liftoff.atlas.getcultured.services.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLOutput;
import java.util.List;

@Controller
@RequestMapping("/tours")
public class TourController {

    private final TourService tourService;

    @Autowired
    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/index")
    public String index (Model model){
        List tour = (List<Tour>) tourService.getAllTours();
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
                             @RequestPart("image") MultipartFile image,
                             Errors errors, Model model) {
        try {
            // Validate the form inputs
            if (errors.hasErrors()) {
                model.addAttribute("tour", new Tour());
                // Log or handle validation errors
                return "tours/create"; // Return to the form page
            }

            model.addAttribute("tour", new Tour());

            tourService.saveTour(tour, image);
            return "redirect:/tours";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "tours/create";
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
        return "viewTour";
    }

    // Mapping for deleting a tour
    @PostMapping("/{tourId}/delete")
    public String deleteTour(@PathVariable int tourId) {
        tourService.deleteTour(tourId);
        return "redirect:/tours";
    }

    // Other mappings for updating tours, handling reviews, etc.

}



