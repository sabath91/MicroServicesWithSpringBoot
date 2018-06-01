package pl.czyz.socialmultiplication.repository;

import org.springframework.data.repository.CrudRepository;
import pl.czyz.socialmultiplication.domain.Multiplication;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {

}
