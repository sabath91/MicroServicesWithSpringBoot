package pl.czyz.multiplication.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import pl.czyz.multiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationResultAttemptRepository
    extends CrudRepository<MultiplicationResultAttempt, Long> {
  List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias);
}
