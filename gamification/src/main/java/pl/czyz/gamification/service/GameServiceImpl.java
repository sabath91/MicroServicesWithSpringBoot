package pl.czyz.gamification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.czyz.gamification.domain.Badge;
import pl.czyz.gamification.domain.BadgeCard;
import pl.czyz.gamification.domain.GameStats;
import pl.czyz.gamification.domain.ScoreCard;
import pl.czyz.gamification.repository.BadgeCardRepository;
import pl.czyz.gamification.repository.ScoreCardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    private final ScoreCardRepository scoreCardRepository;
    private final BadgeCardRepository badgeCardRepository;

    public GameServiceImpl(final ScoreCardRepository scoreCardRepository, final BadgeCardRepository badgeCardRepository) {
        this.scoreCardRepository = scoreCardRepository;
        this.badgeCardRepository = badgeCardRepository;
    }

    @Override
    public GameStats newAttemptForUser(final Long userId, final Long attemptId, final boolean correct) {
        if(correct){
            ScoreCard scoreCard = new ScoreCard(userId, attemptId);
            scoreCardRepository.save(scoreCard);
            log.info("User with id {} scored {} points for attempt id {}", userId, scoreCard.getScore(), attemptId);
            List<BadgeCard> badgeCards = processForBadges(userId);
            List<Badge> badgeList = badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList());
            return new GameStats(userId, scoreCard.getScore(), badgeList);
        }
        return GameStats.epmtyStats(userId);
    }

    private List<BadgeCard> processForBadges(final Long userId) {
        List<BadgeCard> badgeCards = new ArrayList<>();
        int totalScore = scoreCardRepository.getTotalScoreForUser(userId);
        log.info("New score for user {} is {}", userId, totalScore);
        List<ScoreCard> scoreCardList = scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId);
        List<BadgeCard> badgeCardList = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);

        checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.BRONZE_MULTIPLICATOR, totalScore, 100, userId)
                .ifPresent(badgeCards::add);
        checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.SILVER_MULTIPLICATOR, totalScore, 500, userId)
                .ifPresent(badgeCards::add);
        checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.GOLD_MULTIPLICATOR, totalScore, 999, userId)
                .ifPresent(badgeCards::add);

        if(scoreCardList.size()==1 && !containsBadge(badgeCardList, Badge.FIRST_WON)){
            BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, userId);
            badgeCards.add(firstWonBadge);
        }
        return badgeCards;
    }

    @Override
    public GameStats retrieveStatsForUser(final Long userId) {
        int score = scoreCardRepository.getTotalScoreForUser(userId);
        List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
        return new GameStats(userId, score, badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
    }

    private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(final List<BadgeCard> badgeCardList, final Badge badge, final int totalScore, final int scoreThreshold, final Long userId) {
        if(totalScore >= scoreThreshold && !containsBadge(badgeCardList,badge)){
            return Optional.of(giveBadgeToUser(badge, userId));
        }
        return Optional.empty();
    }

    private boolean containsBadge(final List<BadgeCard> badgeCardList, final Badge badge) {
        return badgeCardList.stream().anyMatch(badgeCard -> badgeCard.getBadge().equals(badge));
    }

    private BadgeCard giveBadgeToUser(final Badge badge, final Long userId) {
        BadgeCard badgeCard = new BadgeCard(userId, badge);
        badgeCardRepository.save(badgeCard);
        log.info("User with id {} won a new badge: {}", userId, badge);
        return badgeCard;
    }
}
