package pl.czyz.gamification.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import pl.czyz.gamification.domain.BadgeCard;

public interface BadgeCardRepository extends CrudRepository<BadgeCard, Long> {

  List<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(final Long userId);
}
