package liftoff.atlas.getcultured.models.data;

import liftoff.atlas.getcultured.models.Tour;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends CrudRepository<Tour, Integer> {

}
