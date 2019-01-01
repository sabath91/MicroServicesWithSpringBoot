package pl.czyz.gamification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.czyz.gamification.client.MultiplicationResultAttemptClient;
import pl.czyz.gamification.client.dto.MultiplicationResultAttempt;
import pl.czyz.gamification.domain.Badge;
import pl.czyz.gamification.domain.BadgeCard;
import pl.czyz.gamification.domain.GameStats;
import pl.czyz.gamification.domain.ScoreCard;
import pl.czyz.gamification.repository.BadgeCardRepository;
import pl.czyz.gamification.repository.ScoreCardRepository;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

  public static final int LUCKY_NUMBER = 42;
  private final ScoreCardRepository scoreCardRepository;
  private final BadgeCardRepository badgeCardRepository;
  private final MultiplicationResultAttemptClient multiplicationResultAttemptClient;

  public GameServiceImpl(
      final ScoreCardRepository scoreCardRepository,
      final BadgeCardRepository badgeCardRepository,
      final MultiplicationResultAttemptClient multiplicationResultAttemptClient) {
    this.scoreCardRepository = scoreCardRepository;
    this.badgeCardRepository = badgeCardRepository;
    this.multiplicationResultAttemptClient = multiplicationResultAttemptClient;
  }

  @Override
  public GameStats newAttemptForUser(
      final Long userId, final Long attemptId, final boolean correct) {
    if (correct) {
      ScoreCard scoreCard = new ScoreCard(userId, attemptId);
      scoreCardRepository.save(scoreCard);
      log.info(
          "User with id {} scored {} points for attempt id {}",
          userId,
          scoreCard.getScore(),
          attemptId);
      List<BadgeCard> badgeCards = processForBadges(userId, attemptId);
      List<Badge> badgeList =
          badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList());
      return new GameStats(userId, scoreCard.getScore(), badgeList);
    }
    return GameStats.emptyStats(userId);
  }

  /**
   * checks the total score and the different score cards obtained to give new badges in case their
   * conditions are met.
   */
  private List<BadgeCard> processForBadges(final Long userId, final Long attemptId) {
    List<BadgeCard> badgeCards = new ArrayList<>();
    int totalScore = scoreCardRepository.getTotalScoreForUser(userId);
    log.info("New score for user {} is {}", userId, totalScore);
    List<ScoreCard> scoreCardList =
        scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId);
    List<BadgeCard> badgeCardList =
        badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);

    checkAndGiveBadgeBasedOnScore(
        badgeCardList, Badge.BRONZE_MULTIPLICATOR, totalScore, 100, userId)
        .ifPresent(badgeCards::add);
    checkAndGiveBadgeBasedOnScore(
        badgeCardList, Badge.SILVER_MULTIPLICATOR, totalScore, 500, userId)
        .ifPresent(badgeCards::add);
    checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.GOLD_MULTIPLICATOR, totalScore, 999, userId)
        .ifPresent(badgeCards::add);

    final MultiplicationResultAttempt attempt =
        multiplicationResultAttemptClient.retrieveMultiplicationResultAttemptById(attemptId);
    if (!containsBadge(badgeCardList, Badge.LUCKY_NUMBER)
        && (LUCKY_NUMBER == attempt.getMultiplicationFactorA()
        || LUCKY_NUMBER == attempt.getMultiplicationFactorB())) {
      BadgeCard luckyNumberBadge = new BadgeCard(userId, Badge.LUCKY_NUMBER);
      badgeCards.add(luckyNumberBadge);
    }

    if (scoreCardList.size() == 1 && !containsBadge(badgeCardList, Badge.FIRST_WON)) {
      BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, userId);
      badgeCards.add(firstWonBadge);
    }
    return badgeCards;
  }

  @Override
  public GameStats retrieveStatsForUser(final Long userId) {
    int score = scoreCardRepository.getTotalScoreForUser(userId);
    List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
    return new GameStats(
        userId, score, badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
  }

  /**
   * Convenience method to check the current score against the different thresholds to gain badges.
   * It also assigns badge to user if the conditions are met.
   */
  private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(
      final List<BadgeCard> badgeCardList,
      final Badge badge,
      final int totalScore,
      final int scoreThreshold,
      final Long userId) {
    if (totalScore >= scoreThreshold && !containsBadge(badgeCardList, badge)) {
      return Optional.of(giveBadgeToUser(badge, userId));
    }
    return Optional.empty();
  }

  /**
   * Checks if the passed list of badges includes the one being checked
   */
  private boolean containsBadge(final List<BadgeCard> badgeCardList, final Badge badge) {
    return badgeCardList.stream().anyMatch(badgeCard -> badgeCard.getBadge().equals(badge));
  }

  /**
   * Checks if the passed list of badges includes the one being checked
   */
  private BadgeCard giveBadgeToUser(final Badge badge, final Long userId) {
    BadgeCard badgeCard = new BadgeCard(userId, badge);
    badgeCardRepository.save(badgeCard);
    log.info("User with id {} won a new badge: {}", userId, badge);
    return badgeCard;
  }
}
