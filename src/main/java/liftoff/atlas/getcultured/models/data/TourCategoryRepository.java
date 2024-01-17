package liftoff.atlas.getcultured.models.data;


import liftoff.atlas.getcultured.models.TourCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourCategoryRepository extends CrudRepository<TourCategory, Integer> {

    Optional<TourCategory> findById(Integer id);

}