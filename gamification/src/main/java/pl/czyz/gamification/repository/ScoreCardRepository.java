package pl.czyz.gamification.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.czyz.gamification.domain.LeaderBoardRow;
import pl.czyz.gamification.domain.ScoreCard;

/**
 * Handles CRUD operations with ScoreCards
 */
public interface ScoreCardRepository extends CrudRepository<ScoreCard, Long> {

  /**
   * Gets the total score for a given user, being the sum of the scores of all his ScoreCards.
   *
   * @param userId the id of the user for which the total score should be retrieved
   * @return the total score for the given user
   */
  @Query(
      "SELECT SUM(s.score) "
          + "FROM pl.czyz.gamification.domain.ScoreCard s "
          + "WHERE s.userId = :userId "
          + "GROUP BY s.userId")
  int getTotalScoreForUser(@Param("userId") final Long userId);

  /**
   * Retrieves a list of {@link LeaderBoardRow}s representing the Leader Board of users and their
   * total score.
   *
   * @return the leader board, sorted by highest score first.
   */
  @Query(
      "SELECT NEW pl.czyz.gamification.domain.LeaderBoardRow(s.userId, SUM(s.score)) "
          + "FROM pl.czyz.gamification.domain.ScoreCard s "
          + "GROUP BY s.userId "
          + "ORDER BY SUM(s.score) DESC")
  List<LeaderBoardRow> findFirst10();

  List<ScoreCard> findByUserIdOrderByScoreTimestampDesc(final Long userId);
}
