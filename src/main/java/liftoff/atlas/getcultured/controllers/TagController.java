package liftoff.atlas.getcultured.controllers;


import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import liftoff.atlas.getcultured.dto.TagForm;
import liftoff.atlas.getcultured.dto.TourCategoryForm;
import liftoff.atlas.getcultured.models.Tag;
import liftoff.atlas.getcultured.models.TourCategory;
import liftoff.atlas.getcultured.models.data.TagRepository;
import liftoff.atlas.getcultured.services.TagService;
import liftoff.atlas.getcultured.services.TourCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tags")
public class TagController {

    private static final Logger logger = LoggerFactory.getLogger(TourCategoryController.class);
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("tagForm", new TagForm());
        return "tags/add";
    }

    @PostMapping("/add")
    public String createTag(@ModelAttribute("tagForm") TagForm tagForm,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "tags/add";
        }
        try {
            Optional<Tag> tagOptional = tagService.createTagFromForm(tagForm);
            if (tagOptional.isPresent()) {
                redirectAttributes.addAttribute("tagId", tagOptional.get().getId());
                return "redirect:/tags/view/{tagId}";
            } else {
                return "redirect:/tags"; // Redirect if the tag is not successfully created
            }
        } catch (Exception e) {
            logger.error("Error while creating tag", e);
            return "tags/add";
        }
    }

    @GetMapping("/view/{tagId}")
    public String tag(@PathVariable("tagId") int tagId, Model model) {
        Optional<Tag> tagOptional = Optional.ofNullable(tagService.getTagById(tagId));
        if (tagOptional.isPresent()) {
            model.addAttribute("tag", tagOptional.get());
            return "tags/view"; // View template for an individual tag
        } else {
            // Handle the absence of a tag, e.g., redirect to a list or an error page
            return "redirect:/tags"; // or a path to an error page
        }
    }


    @GetMapping("/update/{tagId}")
    public String showUpdateForm(@PathVariable("tagId") int tagId, Model model) {
        Optional<Tag> tagOptional = Optional.ofNullable(tagService.getTagById(tagId));
        if (tagOptional.isPresent()) {
            model.addAttribute("tag", tagOptional.get());
            return "tags/update"; // View template for updating a tag
        } else {
            // Handle the absence of a tag, e.g., redirect to a list or an error page
            return "redirect:/tags"; // or a path to an error page
        }
    }

//    @PostMapping("/update/{tagId}")
//    public String updateTag(@PathVariable("tagId") int tagId,
//                                     @ModelAttribute("tag") TagForm tagForm,
//                                     BindingResult bindingResult,
//                                     RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            return "tags/update";
//        }
//        Optional<Tag> tagOptional = updateTagFromForm(tagId, tagForm);
//        if (tagOptional.isPresent()) {
//            redirectAttributes.addAttribute("tagId", tagOptional.get().getId());
//            return "redirect:/tags/view/{tagId}";
//        } else {
//            return "redirect:/tags"; // Redirect if the category is not found
//        }
//    }

//    @Transactional
//    public Optional<Tag> updateTagFromForm(int tagId, TagForm tagForm) {
//        // Find the tag by ID
//        Optional<Tag> tagOptional = Optional.ofNullable(tagService.getTagById(tagId));
//
//        if (tagOptional.isPresent()) {
//            Tag tag = tagOptional.get();
//            // Update the properties of the tag from the form
//            tag.setName(tagForm.getName());
//            // Add other fields from the form as needed, e.g., tag.setColor(tagForm.getColor());
//
//            // Save the updated tourCategory entity to the database
//            tagService.saveTag(tag);
//        }
//
//        // Return the updated tourCategory (or empty if not found)
//        return tagOptional;
//    }

    @GetMapping
    public String listTags(Model model) {
        List<Tag> tags = tagService.getAllTags();  // Make sure you have this method in your service
        model.addAttribute("tags", tags);
        return "tags/tags";
    }

    @PostMapping("/{tagId}/delete")
    public String deleteTag(@PathVariable("tagId") int tagId,
                                     RedirectAttributes redirectAttributes) {
        try {
            tagService.deleteTag(tagId);
            redirectAttributes.addFlashAttribute("successMessage", "Tag deleted successfully.");
        } catch (Exception e) {
            logger.error("Error occurred while deleting category", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting tag.");
        }
        return "redirect:/tags";
    }

    // Other methods similar to StopController...
}

