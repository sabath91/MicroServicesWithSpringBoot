package pl.czyz.multiplication.repository;

import org.springframework.data.repository.CrudRepository;
import pl.czyz.multiplication.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByAlias(final String alias);
}
