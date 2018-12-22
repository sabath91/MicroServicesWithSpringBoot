package pl.czyz.gamification.client;

import pl.czyz.gamification.client.dto.MultiplicationResultAttempt;

public interface MultiplicationResultAttemptClient {
  MultiplicationResultAttempt retriveMultiplicationResultAttemptById(final Long attemptId);
}
