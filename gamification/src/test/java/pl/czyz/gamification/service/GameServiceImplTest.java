package pl.czyz.gamification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.czyz.gamification.client.MultiplicationResultAttemptClientImpl;
import pl.czyz.gamification.client.dto.MultiplicationResultAttempt;
import pl.czyz.gamification.domain.Badge;
import pl.czyz.gamification.domain.BadgeCard;
import pl.czyz.gamification.domain.GameStats;
import pl.czyz.gamification.domain.ScoreCard;
import pl.czyz.gamification.repository.BadgeCardRepository;
import pl.czyz.gamification.repository.ScoreCardRepository;

public class GameServiceImplTest {

  private GameService gameService;

  @Mock private ScoreCardRepository scoreCardRepository;

  @Mock private BadgeCardRepository badgeCardRepository;

  @Mock private MultiplicationResultAttemptClientImpl multiplicationResultAttemptClient;

  @Before
  public void setUp() {
    initMocks(this);
    gameService =
        new GameServiceImpl(
            scoreCardRepository, badgeCardRepository, multiplicationResultAttemptClient);
  }

  @Test
  public void processFirstCorrectAttemptTest() {
    // given
    final Long userId = 1L;
    final Long attemptId = 2L;
    int totalScore = 10;
    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
    MultiplicationResultAttempt attempt = new MultiplicationResultAttempt("alias", 11, 11, 121,
        true);
    given(multiplicationResultAttemptClient.retrieveMultiplicationResultAttemptById(any()))
        .willReturn(attempt);
    final ScoreCard scoreCard = new ScoreCard(userId, attemptId);
    given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
        .willReturn(Collections.singletonList(scoreCard));
    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(Collections.emptyList());
    // when
    final GameStats gameStats = gameService.newAttemptForUser(userId, attemptId, true);
    // then - should score one card and win the badge first won
    assertThat(gameStats.getScore()).isEqualTo(scoreCard.getScore());
    assertThat(gameStats.getBadges()).containsOnly(Badge.FIRST_WON);
  }

  @Test
  public void shouldGiveLuckyNumberBadgeWhenLuckyNumberOccurredInMultiplication() {
    final Long userId = 2L;
    final Long attemptId = 21L;
    final int luckyNumber = 42;
    MultiplicationResultAttempt attempt =
        new MultiplicationResultAttempt("alias", luckyNumber, 12, 504, true);
    given(multiplicationResultAttemptClient.retrieveMultiplicationResultAttemptById(any()))
        .willReturn(attempt);

    GameStats interaction = gameService.newAttemptForUser(userId, attemptId, true);

    assertThat(interaction.getBadges()).containsOnly(Badge.LUCKY_NUMBER);
  }

  @Test
  public void processCorrectAttemptForScoreBadgeTest() {
    final Long userId = 1L;
    final Long attemptId = 21L;
    int totalScore = 100;
    BadgeCard firstWonBadge = new BadgeCard(userId, Badge.FIRST_WON);
    MultiplicationResultAttempt attempt = new MultiplicationResultAttempt("alias", 11, 11, 121,
        true);
    given(multiplicationResultAttemptClient.retrieveMultiplicationResultAttemptById(any()))
        .willReturn(attempt);
    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
    given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
        .willReturn(createNScoreCards(10, userId));
    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(Collections.singletonList(firstWonBadge));

    final GameStats gameStats = gameService.newAttemptForUser(userId, attemptId, true);

    assertThat(gameStats.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
    assertThat(gameStats.getBadges()).containsOnly(Badge.BRONZE_MULTIPLICATOR);
  }

  @Test
  public void processWrongAttemptTest() {
    final Long userId = 1L;
    final Long attemptId = 12L;
    final int totalScore = 10;
    final ScoreCard scoreCard = new ScoreCard(userId, attemptId);
    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
    given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
        .willReturn(Collections.singletonList(scoreCard));
    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(Collections.emptyList());

    final GameStats gameStats = gameService.newAttemptForUser(userId, attemptId, false);

    assertThat(gameStats.getScore()).isEqualTo(0);
    assertThat(gameStats.getBadges()).isEmpty();
  }

  @Test
  public void retrieveStatsForUser() {
    final Long userId = 2L;
    int totalScore = 10;
    final BadgeCard badgeCard = new BadgeCard(userId, Badge.SILVER_MULTIPLICATOR);
    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(Collections.singletonList(badgeCard));

    GameStats stats = gameService.retrieveStatsForUser(userId);

    assertThat(stats.getScore()).isEqualTo(totalScore);
    assertThat(stats.getBadges()).containsOnly(Badge.SILVER_MULTIPLICATOR);
  }

  private List<ScoreCard> createNScoreCards(final int n, final long userId) {
    return IntStream.range(0, n)
        .mapToObj(i -> new ScoreCard(userId, (long) i))
        .collect(Collectors.toList());
  }
}
