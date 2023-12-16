package liftoff.atlas.getcultured.controllers;

import liftoff.atlas.getcultured.models.Tour;
import liftoff.atlas.getcultured.services.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/tours")
public class TourController {

    private final TourService tourService;

    @Autowired
    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    // Mapping for the tour creation form
    @GetMapping("/create")
    public String showCreateTourForm(Model model) {
        model.addAttribute("tour", new Tour());
        return "createTour"; // Assuming you have a Thymeleaf template named createTour.html
    }

    // Handling the tour creation form submission
    @PostMapping("/create")
    public String createTour(@ModelAttribute("tour") Tour tour,
                             @RequestParam("image") MultipartFile image) {
        tourService.saveTour(tour, image);
        return "redirect:/tours";
    }

    // Mapping for listing all tours
    @GetMapping
    public String listTours(Model model) {
        List<Tour> tours = tourService.getAllTours();
        model.addAttribute("tours", tours);
        return "tourList"; // Assuming you have a Thymeleaf template named tourList.html
    }

    // Mapping for viewing a specific tour
    @GetMapping("/{tourId}")
    public String viewTour(@PathVariable Long tourId, Model model) {
        Tour tour = tourService.getTourById(tourId);
        model.addAttribute("tour", tour);
        return "viewTour"; // Assuming you have a Thymeleaf template named viewTour.html
    }

    // Mapping for deleting a tour
    @PostMapping("/{tourId}/delete")
    public String deleteTour(@PathVariable Long tourId) {
        tourService.deleteTour(tourId);
        return "redirect:/tours";
    }

    // Other mappings for updating tours, handling reviews, etc.

}

