package liftoff.atlas.getcultured.controllers;

import jakarta.transaction.Transactional;
import liftoff.atlas.getcultured.dto.TourCategoryForm;
import liftoff.atlas.getcultured.models.TourCategory;
import liftoff.atlas.getcultured.services.TourCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tourCategories")
public class TourCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(TourCategoryController.class);
    private final TourCategoryService tourCategoryService;

    @Autowired
    public TourCategoryController(TourCategoryService tourCategoryService) {
        this.tourCategoryService = tourCategoryService;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("tourCategoryForm", new TourCategoryForm());
        return "tourCategories/create";
    }

    @PostMapping("/create")
    public String createTourCategory(@ModelAttribute("tourCategoryForm") TourCategoryForm tourCategoryForm,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "tourCategories/create";
        }
        try {
            Optional<TourCategory> tourCategoryOptional = tourCategoryService.createTourCategoryFromForm(tourCategoryForm);
            if (tourCategoryOptional.isPresent()) {
                redirectAttributes.addAttribute("tourCategoryId", tourCategoryOptional.get().getId());
                return "redirect:/tourCategories/view/{tourCategoryId}";
            } else {
                return "redirect:/tourCategories"; // Redirect if the category is not successfully created
            }
        } catch (Exception e) {
            logger.error("Error while creating tour category", e);
            return "tourCategories/create";
        }
    }

    @GetMapping("/view/{tourCategoryId}")
    public String viewCategory(@PathVariable("tourCategoryId") int categoryId, Model model) {
        Optional<TourCategory> tourCategoryOptional = Optional.ofNullable(tourCategoryService.getTourCategoryById(categoryId));
        if (tourCategoryOptional.isPresent()) {
            model.addAttribute("tourCategory", tourCategoryOptional.get());
            return "tourCategories/view"; // View template for an individual tour category
        } else {
            // Handle the absence of a tour category, e.g., redirect to a list or an error page
            return "redirect:/tourCategories"; // or a path to an error page
        }
    }


    @GetMapping("/update/{categoryId}")
    public String showUpdateForm(@PathVariable("categoryId") int categoryId, Model model) {
        Optional<TourCategory> tourCategoryOptional = Optional.ofNullable(tourCategoryService.getTourCategoryById(categoryId));
        if (tourCategoryOptional.isPresent()) {
            model.addAttribute("tourCategory", tourCategoryOptional.get());
            return "tourCategories/update"; // View template for updating a tour category
        } else {
            // Handle the absence of a tour category, e.g., redirect to a list or an error page
            return "redirect:/tourCategories"; // or a path to an error page
        }
    }

    @PostMapping("/update/{categoryId}")
    public String updateTourCategory(@PathVariable("categoryId") int categoryId,
                                     @ModelAttribute("tourCategory") TourCategoryForm tourCategoryForm,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "tourCategories/update";
        }
        Optional<TourCategory> tourCategoryOptional = updateTourCategoryFromForm(categoryId, tourCategoryForm);
        if (tourCategoryOptional.isPresent()) {
            redirectAttributes.addAttribute("categoryId", tourCategoryOptional.get().getId());
            return "redirect:/tourCategories/view/{categoryId}";
        } else {
            return "redirect:/tourCategories"; // Redirect if the category is not found
        }
    }

    @Transactional
    public Optional<TourCategory> updateTourCategoryFromForm(int categoryId, TourCategoryForm tourCategoryForm) {
        // Find the tour category by ID
        Optional<TourCategory> tourCategoryOptional = Optional.ofNullable(tourCategoryService.getTourCategoryById(categoryId));

        if (tourCategoryOptional.isPresent()) {
            TourCategory tourCategory = tourCategoryOptional.get();
            // Update the properties of the tourCategory from the form
            tourCategory.setName(tourCategoryForm.getName());
            // Add other fields from the form as needed, e.g., tourCategory.setDescription(tourCategoryForm.getDescription());

            // Save the updated tourCategory entity to the database
            tourCategoryService.saveTourCategory(tourCategory);
        }

        // Return the updated tourCategory (or empty if not found)
        return tourCategoryOptional;
    }

    @GetMapping
    public String listTourCategories(Model model) {
        List<TourCategory> categories = tourCategoryService.getAllTourCategories();  // Make sure you have this method in your service
        model.addAttribute("categories", categories);
        return "tourCategories/tourCategories";
    }

    @PostMapping("/{categoryId}/delete")
    public String deleteTourCategory(@PathVariable("categoryId") int categoryId,
                                     RedirectAttributes redirectAttributes) {
        try {
            tourCategoryService.deleteTourCategory(categoryId);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully.");
        } catch (Exception e) {
            logger.error("Error occurred while deleting category", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting category.");
        }
        return "redirect:/tourCategories";
    }

    // Other methods similar to StopController...
}

