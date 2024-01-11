package liftoff.atlas.getcultured.controllers;

import jakarta.validation.Valid;
import liftoff.atlas.getcultured.dto.StopForm;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String showCreateStopForm(Model model, @RequestParam(required = false, defaultValue = "stops") String referrer) {
        model.addAttribute("stopForm", new StopForm());
        model.addAttribute("referrer", referrer); // Pass referrer to the model for form submission
        return "stops/create";
    }

    // Process the form for creating a new stop

    @PostMapping("/create")
    public String createStop(@ModelAttribute("stopForm") @Valid StopForm stopForm,
                             BindingResult bindingResult,
                             @RequestParam("image") MultipartFile imageFile,
                             @RequestParam("referrer")  String referrer,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "stops/create";
        }
        try {
            stopService.createStopFromForm(stopForm, imageFile);
            String redirectUrl = "redirect:/".concat(referrer.equals("tourCreation") ? "tours/create" : "stops");
            redirectAttributes.addFlashAttribute("message", "Stop created successfully");
            return redirectUrl;
        } catch (IOException e) {
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
