package optiheat.storage.repository;

import optiheat.storage.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>
{
    User findById(String id);
}
