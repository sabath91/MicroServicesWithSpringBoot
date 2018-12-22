package pl.czyz.multiplication.repository;

import org.springframework.data.repository.CrudRepository;
import pl.czyz.multiplication.domain.Multiplication;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {}
