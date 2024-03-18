package liftoff.atlas.getcultured.services;

import jakarta.transaction.Transactional;
import liftoff.atlas.getcultured.dto.TagForm;
import liftoff.atlas.getcultured.models.Tag;
import liftoff.atlas.getcultured.models.TourCategory;
import liftoff.atlas.getcultured.models.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private static final Logger logger = LoggerFactory.getLogger(TourService.class);
    private final TourRepository tourRepository;

    private final StopRepository stopRepository;

    private final CityRepository cityRepository;

    private final TagRepository tagRepository;

    private final StopService stopService;

    private final TourCategoryRepository tourCategoryRepository;

    public TagService(TourRepository tourRepository, StopRepository stopRepository, CityRepository cityRepository, TagRepository tagRepository, StopService stopService, TourCategoryRepository tourCategoryRepository) {
        this.tourRepository = tourRepository;
        this.stopRepository = stopRepository;
        this.cityRepository = cityRepository;
        this.tagRepository = tagRepository;
        this.stopService = stopService;
        this.tourCategoryRepository = tourCategoryRepository;
    }

    public Tag getTagById(int tagId) {
        return tagRepository.findById(tagId).orElse(null);
    }

    public List<Tag> getAllTags() {
        return (List<Tag>) tagRepository.findAll();
    }

    @Transactional
    public void saveTag(Tag tag) {
        tagRepository.save(tag);
    }

    // Create or update a Tag from a TagForm
    public Optional<Tag> createTagFromForm(TagForm tagForm) {
        Optional<Tag> tagOptional;
        if (tagForm.getId() != 0) {
            tagOptional = tagRepository.findById(tagForm.getId());
            if(tagOptional.isPresent()) {
                Tag tag = tagOptional.get();
                // Update the properties of the tourCategory from the form
                tag.setName(tagForm.getName());
                tag.setColor(tagForm.getColor());
                // Add other fields from the form as needed
                tagRepository.save(tag);
            }
        } else {
            Tag tag = new Tag();
            // Set properties from the form
            tag.setName(tagForm.getName());
            tag.setColor(tagForm.getColor());
            // Add other fields from the form as needed
            tagRepository.save(tag);
            tagOptional = Optional.of(tag);
        }
        return tagOptional;
    }

    @Transactional
    public void deleteTag(int tagId) {
        tagRepository.deleteById(tagId);
    }
}
