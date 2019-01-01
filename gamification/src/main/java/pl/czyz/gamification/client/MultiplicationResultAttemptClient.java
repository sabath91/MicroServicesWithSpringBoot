package pl.czyz.gamification.client;

import pl.czyz.gamification.client.dto.MultiplicationResultAttempt;

/**
 * This interface allows us to connect to the Multiplication microservice. Note that it's agnostic
 * to the way of communication.
 */
public interface MultiplicationResultAttemptClient {

  MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(final Long attemptId);
}
