package pl.czyz.gamification.service;

import pl.czyz.gamification.domain.GameStats;

public interface GameService {

  GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct);

  GameStats retrieveStatsForUser(Long userId);
}
