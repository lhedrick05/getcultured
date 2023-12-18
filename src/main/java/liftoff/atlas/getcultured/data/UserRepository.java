package liftoff.atlas.getcultured.data;

import liftoff.atlas.getcultured.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);
    User findByEmailAddress(String emailAddress);

}
