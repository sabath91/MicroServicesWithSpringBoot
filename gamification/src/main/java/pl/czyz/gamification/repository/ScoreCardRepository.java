package pl.czyz.gamification.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.czyz.gamification.domain.LeaderBoardRow;
import pl.czyz.gamification.domain.ScoreCard;

import java.util.List;

public interface ScoreCardRepository extends CrudRepository<ScoreCard, Long> {

    @Query("SELECT SUM(s.score) " +
            "FROM pl.czyz.gamification.domain.ScoreCard s " +
            "WHERE s.userId = :userId " +
            "GROUP BY s.userId")
    int getTotalScoreForUser(@Param("userId") final Long userId);

    @Query("SELECT NEW pl.czyz.gamification.domain.LeaderBoardRow(s.userId, SUM(s.score)) " +
            "FROM pl.czyz.gamification.domain.ScoreCard s " +
            "GROUP BY s.userId " +
            "ORDER BY SUM(s.score) DESC")
    List<LeaderBoardRow> findFirst10();

    List<ScoreCard> findByUserIdOrderByScoreTimestampDesc(final Long userId);

}
