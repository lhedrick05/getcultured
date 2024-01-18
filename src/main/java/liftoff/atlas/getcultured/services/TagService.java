package liftoff.atlas.getcultured.services;

import liftoff.atlas.getcultured.models.Tag;
import liftoff.atlas.getcultured.models.Tour;
import liftoff.atlas.getcultured.models.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private static final Logger logger = LoggerFactory.getLogger(TourService.class);
    private final TourRepository tourRepository;

    private final StopRepository stopRepository;

    private final CityRepository cityRepository;

    private final TagRepository tagRepository;

    private final StopService stopService;

    @Autowired
    public TagService(TourRepository tourRepository, StopRepository stopRepository, CityRepository cityRepository, TagRepository tagRepository, StopService stopService) {
        this.tourRepository = tourRepository;
        this.stopRepository = stopRepository;
        this.cityRepository = cityRepository;
        this.tagRepository = tagRepository;
        this.stopService = stopService;
    }

    public List<Tag> getAllTags() {
        return (List<Tag>) tagRepository.findAll();
    }
}
