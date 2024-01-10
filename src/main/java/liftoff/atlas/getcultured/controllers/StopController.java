package liftoff.atlas.getcultured.controllers;

import jakarta.validation.Valid;
import liftoff.atlas.getcultured.models.Stop;
import liftoff.atlas.getcultured.services.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/stops")
public class StopController {

    private static final Logger logger = LoggerFactory.getLogger(StopController.class);


    private final StopService stopService; // Replace with your StopService

    @Autowired
    public StopController(StopService stopService) {
        this.stopService = stopService;
    }

    // Display form to create a new stop
    @GetMapping("/create")
    public String showCreateStopForm(Model model) {
        model.addAttribute("stop", new Stop());
        return "stops/create";
    }

    // Process the form for creating a new stop

    @PostMapping("/create")
    public String createStop(@ModelAttribute("stop") @Valid Stop stop,
                             BindingResult bindingResult,
                             @RequestParam("image") MultipartFile imageFile,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "stops/create";
        }

        try {
            if (!imageFile.isEmpty()) {
                stopService.saveStop(stop, imageFile);
            } else {
                // Handle the case where no image is uploaded
                // Maybe set a default imagePath or leave it as null
            }
            return "redirect:/stops"; // Redirect to a relevant page after creating the stop
        } catch (IOException e) {
            // Log the error and show an error message
            logger.error("Error while saving stop and image", e);
            model.addAttribute("errorMessage", "Error processing image upload.");
            return "stops/create";
        }
    }

    // Method to display a specific stop
    @GetMapping("/{id}")
    public String viewStop(@PathVariable Integer id, Model model) {
        Stop stop = stopService.findById(id); // Replace with method to find a stop by id
        if (stop != null) {
            model.addAttribute("stop", stop);
            return "stopDetails";
        }
        return "redirect:/tours"; // Redirect if stop not found
    }

    @GetMapping
    public String listStops(Model model) {
        List<Stop> stops = stopService.findAll(); // Assuming a method to find all stops
        model.addAttribute("stops", stops);
        return "stops/stops";
    }


    // Other methods for handling stop creation, editing, etc.
}
