package liftoff.atlas.getcultured.controllers;

import jakarta.transaction.Transactional;
import liftoff.atlas.getcultured.dto.CityForm;
import liftoff.atlas.getcultured.models.City;
import liftoff.atlas.getcultured.services.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cities")
public class CityController {

    private static final Logger logger = LoggerFactory.getLogger(TourCategoryController.class);
    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("cityForm", new CityForm());
        return "cities/add";
    }

    @PostMapping("/add")
    public String addCity(@ModelAttribute("cityForm") CityForm cityForm,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cities/add";
        }
        try {
            Optional<City> cityOptional = cityService.createCityFromForm(cityForm);
            if (cityOptional.isPresent()) {
                redirectAttributes.addAttribute("cityId", cityOptional.get().getId());
                return "redirect:/cities/view/{cityId}";
            } else {
                return "redirect:/cities"; // Redirect if the category is not successfully created
            }
        } catch (Exception e) {
            logger.error("Error while creating tour category", e);
            return "cities/add";
        }
    }

    @GetMapping("/view/{cityId}")
    public String viewCity(@PathVariable("cityId") int cityId, Model model) {
        Optional<City> cityOptional = Optional.ofNullable(cityService.getCityById(cityId));
        if (cityOptional.isPresent()) {
            model.addAttribute("city", cityOptional.get());
            return "cities/view"; // View template for an individual city
        } else {
            // Handle the absence of a city, e.g., redirect to a list or an error page
            return "redirect:/cities"; // or a path to an error page
        }
    }


    @GetMapping("/update/{cityId}")
    public String showUpdateForm(@PathVariable("cityId") int cityId, Model model) {
        Optional<City> cityOptional = Optional.ofNullable(cityService.getCityById(cityId));
        if (cityOptional.isPresent()) {
            model.addAttribute("city", cityOptional.get());
            return "cities/update"; // View template for updating a city
        } else {
            // Handle the absence of a city, e.g., redirect to a list or an error page
            return "redirect:/cities"; // or a path to an error page
        }
    }

    @PostMapping("/update/{cityId}")
    public String updateCity(@PathVariable("cityId") int cityId,
                                     @ModelAttribute("city") CityForm cityForm,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cities/update";
        }
        Optional<City> cityOptional = updateCityFromForm(cityId, cityForm);
        if (cityOptional.isPresent()) {
            redirectAttributes.addAttribute("cityId", cityOptional.get().getId());
            return "redirect:/cities/view/{cityId}";
        } else {
            return "redirect:/cities"; // Redirect if the category is not found
        }
    }

    @Transactional
    public Optional<City> updateCityFromForm(int cityId, CityForm cityForm) {
        // Find the tour category by ID
        Optional<City> cityOptional = Optional.ofNullable(cityService.getCityById(cityId));

        if (cityOptional.isPresent()) {
            City city = cityOptional.get();
            // Update the properties of the tourCategory from the form
            city.setName(cityForm.getName());
            city.setState(cityForm.getState());
            // Add other fields from the form as needed, e.g., tourCategory.setDescription(tourCategoryForm.getDescription());

            // Save the updated tourCategory entity to the database
            cityService.saveCity(city);
        }

        // Return the updated tourCategory (or empty if not found)
        return cityOptional;
    }

    @GetMapping
    public String listCities(Model model) {
        List<City> cities = cityService.getAllCities();  // Make sure you have this method in your service
        model.addAttribute("cities", cities);
        return "cities/cities";
    }

    @PostMapping("/{cityId}/delete")
    public String deleteCity(@PathVariable("cityId") int cityId,
                                     RedirectAttributes redirectAttributes) {
        try {
            cityService.deleteCity(cityId);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully.");
        } catch (Exception e) {
            logger.error("Error occurred while deleting category", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting category.");
        }
        return "redirect:/cities";
    }

}


