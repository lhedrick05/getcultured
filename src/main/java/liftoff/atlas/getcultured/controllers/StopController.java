package liftoff.atlas.getcultured.controllers;

import liftoff.atlas.getcultured.models.Stop;
import liftoff.atlas.getcultured.services.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stops")
public class StopController {

    @Autowired
    private StopService stopService; // Replace with your StopService

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

    // Other methods for handling stop creation, editing, etc.
}
