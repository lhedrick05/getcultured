package liftoff.atlas.getcultured.models.data;

import liftoff.atlas.getcultured.models.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City, Integer> {

    Optional<City> findById(Integer id);

}
