package pl.czyz.gamification.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.czyz.gamification.client.dto.MultiplicationResultAttempt;

@Component
public class MultiplicationResultAttemptClientImpl implements MultiplicationResultAttemptClient {
  private final RestTemplate restTemplate;
  private final String multiplicationHost;

  public MultiplicationResultAttemptClientImpl(
      final RestTemplate restTemplate,
      @Value("${multiplicationHost}") final String multiplicationHost) {
    this.restTemplate = restTemplate;
    this.multiplicationHost = multiplicationHost;
  }

  @Override
  public MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(final Long attemptId) {
    return restTemplate.getForObject(
        multiplicationHost + "/results/" + attemptId, MultiplicationResultAttempt.class);
  }
}
